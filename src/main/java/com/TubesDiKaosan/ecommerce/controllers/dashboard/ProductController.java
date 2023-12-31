package com.TubesDiKaosan.ecommerce.controllers.dashboard;

import java.util.ArrayList;

import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Iterator;

import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.TubesDiKaosan.ecommerce.models.Category;
import com.TubesDiKaosan.ecommerce.models.Product;
import com.TubesDiKaosan.ecommerce.models.Riviews;
import com.TubesDiKaosan.ecommerce.models.Stock;
import com.TubesDiKaosan.ecommerce.models.Users;
import com.TubesDiKaosan.ecommerce.payloads.requests.ImagesProductRequest;
import com.TubesDiKaosan.ecommerce.payloads.requests.ProductRequest;
import com.TubesDiKaosan.ecommerce.payloads.requests.StockProductRequest;
import com.TubesDiKaosan.ecommerce.payloads.response.Response;
import com.TubesDiKaosan.ecommerce.services.ActorServices.UsersService;
import com.TubesDiKaosan.ecommerce.services.ProductServices.CategoryService;
import com.TubesDiKaosan.ecommerce.services.ProductServices.ProductService;
import com.TubesDiKaosan.ecommerce.services.ProductServices.RiviewServices;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard/products")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, maxFileSize = 1024 * 1024 * 2, maxRequestSize = 1024 * 1024 * 4)

public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RiviewServices riviewServices;

    @RequestMapping({ "", "/" })
    public String index(Model model, HttpSession session) throws SQLException {
        model.addAttribute("title", "Product");
        if (session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                List<Product> products = (List<Product>) productService.getAll().getData();
                model.addAttribute("products", products);
                return "pages/dashboard/product";
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @RequestMapping("/create")
    public String createPage(Model model, HttpSession session) throws SQLException {
        model.addAttribute("title", "Create Product");
        if (session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                List<Category> categories = (List<Category>) categoryService.getAll().getData();
                model.addAttribute("categories", categories);

                return "pages/dashboard/product_add";
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {

                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/save")
    public String create(@RequestParam("name_product") String name_product,
            @RequestParam("category_id") Integer category_id,
            @RequestParam("description") String description,
            @RequestParam("price") Integer price,
            @RequestParam("visible") String visible,
            @RequestParam("images") List<MultipartFile> files,
            @RequestParam("color[]") List<String> color,
            @RequestParam("size[S][]") List<String> sizeS,
            @RequestParam("size[M][]") List<String> sizeM,
            @RequestParam("size[L][]") List<String> sizeL,
            @RequestParam("size[XL][]") List<String> sizeXL,
            Model model, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
        if (session.getAttribute("user") != null) {
            ProductRequest request = new ProductRequest();
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                List<ImagesProductRequest> images = new ArrayList<>();
                String path = "src/main/resources/static/uploads/images/products";
                File dirPath = new File(path);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                for (MultipartFile file : files) {
                    try {
                        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                        if (fileName.equals(""))
                            continue;
                        fileName = System.currentTimeMillis() + fileName;
                        Path filePath = Path.of(path + "/" + fileName);
                        System.out.println(filePath);
                        ImagesProductRequest image = new ImagesProductRequest();
                        image.setImage(fileName);
                        images.add(image);
                        Files.copy(file.getInputStream(), filePath);
                    } catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }

                Object[][] stock = new Object[][] {
                        { "S", sizeS },
                        { "M", sizeM },
                        { "L", sizeL },
                        { "XL", sizeXL }
                };

                List<StockProductRequest> stocks = new ArrayList<>();
                for (int i = 0; i < color.size(); i++) {
                    for (int j = 0; j < stock.length; j++) {
                        StockProductRequest stockProductRequest = new StockProductRequest();
                        if (color.get(i).equals("") || ((List<String>) stock[j][1]).get(i).equals(""))
                            continue;
                        stockProductRequest.setColor(color.get(i));
                        if (Integer.parseInt(((List<String>) stock[j][1]).get(i)) < 1)
                            continue;
                        stockProductRequest.setQuantity(Integer.parseInt(((List<String>) stock[j][1]).get(i)));
                        stockProductRequest.setSize((String) stock[j][0]);
                        stocks.add(stockProductRequest);
                    }
                }
                request.setName_product(name_product);
                request.setCategory_id(category_id);
                request.setDescription(description);
                request.setPrice(price);
                // parset to int
                request.setVisible(visible.equals("visible") ? 1 : 0);
                request.setImages(images);
                request.setStock(stocks);

                productService.createData(request);
                redirectAttributes.addFlashAttribute("alert", "produk berhasil ditambahkan");
                return "redirect:/dashboard/products";
            }
        }
        redirectAttributes.addFlashAttribute("alert", "produk gagal ditambahkan");
        return "redirect:/";
    }

    @RequestMapping("/edit")
    public String pageUpdate(@RequestParam Integer productID, ProductRequest productRequest, Model model,
            HttpSession session) throws SQLException {
        model.addAttribute("title", "Update Product");
        if (session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                Product product = (Product) productService.findDataByID(productID).getData();
                List<Category> categories = (List<Category>) categoryService.getAll().getData();

                Map<String, Object> data = new HashMap<>();
                for (Stock stock : product.getStock()) {
                    // check color value is exist or not
                    if (data.containsKey(stock.getColor())) {
                        Map<String, Object> size = (Map<String, Object>) data.get(stock.getColor());
                        size.put(stock.getSize(), stock.getQuantity());
                        data.put(stock.getColor(), size);
                    } else {
                        data.put(stock.getColor(), new HashMap<String, Object>() {
                            {
                                put("id", stock.getStock_id().toString());
                                put("S", stock.getSize().equals("S") ? stock.getQuantity() : "0");
                                put("M", stock.getSize().equals("M") ? stock.getQuantity() : "0");
                                put("L", stock.getSize().equals("L") ? stock.getQuantity() : "0");
                                put("XL", stock.getSize().equals("XL") ? stock.getQuantity() : "0");
                            }
                        });
                    }
                }

                model.addAttribute("product", product);
                model.addAttribute("categories", categories);
                model.addAttribute("stocks", data);
                return "pages/dashboard/product_edit";
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam("productID") Integer productID,
            @RequestParam("name_product") String name_product,
            @RequestParam("category_id") Integer category_id,
            @RequestParam("description") String description,
            @RequestParam("price") Integer price,
            @RequestParam("visible") Integer visible,
            @RequestParam("image_id") List<Integer> image_id,
            @RequestParam("images") List<MultipartFile> files,
            @RequestParam("color[]") List<String> color,
            @RequestParam("stock_id") List<Integer> stock_id,
            @RequestParam("size[S][]") List<String> sizeS,
            @RequestParam("size[M][]") List<String> sizeM,
            @RequestParam("size[L][]") List<String> sizeL,
            @RequestParam("size[XL][]") List<String> sizeXL,
            Model model, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
        if (session.getAttribute("user") != null) {
            ProductRequest request = new ProductRequest();
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                List<ImagesProductRequest> images = new ArrayList<>();
                String path = "src/main/resources/static/uploads/images/products";
                
                ExecutorService executor = Executors.newFixedThreadPool(5);

                int[] idx_imageID = { 0 };

                for (MultipartFile file : files) {
                    executor.submit(() -> {
                        try {
                            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                            if (fileName.equals("")) {
                                idx_imageID[0]++;
                                return; // Skip file kosong
                            }

                            fileName = System.currentTimeMillis() + fileName;
                            Path filePath = Paths.get(path, fileName);

                            ImagesProductRequest image = new ImagesProductRequest();
                            image.setImage(fileName);
                            image.setimageid_request(image_id.get(idx_imageID[0]));
                            images.add(image);

                            byte[] buffer = new byte[8192];
                            try (InputStream inputStream = file.getInputStream();
                                    OutputStream outputStream = Files.newOutputStream(filePath,
                                            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }

                            } catch (IOException e) {
                                System.out.println(e);
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            System.out.println(e);
                            e.printStackTrace();
                        }
                        idx_imageID[0]++;
                    });
                }

                executor.shutdown();

                Object[][] stock = new Object[][] {
                        { "S", sizeS },
                        { "M", sizeM },
                        { "L", sizeL },
                        { "XL", sizeXL }
                };

                // getstock
                List<StockProductRequest> stocks = new ArrayList<>();
                for (int i = 0; i < color.size(); i++) {
                    for (int j = 0; j < stock.length; j++) {
                        StockProductRequest stockProductRequest = new StockProductRequest();
                        if (color.get(i).equals("") || ((List<String>) stock[j][1]).get(i).equals(""))
                            continue;
                        stockProductRequest.setColor(color.get(i));
                        if (Integer.parseInt(((List<String>) stock[j][1]).get(i)) < 0)
                            continue;
                        stockProductRequest.setQuantity(Integer.parseInt(((List<String>) stock[j][1]).get(i)));
                        stockProductRequest.setSize((String) stock[j][0]);
                        if (stock_id.get(i) != null && stock.length > i) {
                            Stock stockData = productService.getStockById(stock_id.get(i));
                            stockProductRequest.setId(productService.findStockByProductIdAndSizeAndColor(productID,
                                    (String) stock[j][0], stockData.getColor()));
                        } else {
                            stockProductRequest.setId(null);
                        }
                        stocks.add(stockProductRequest);
                    }
                }
                request.setName_product(name_product);
                request.setCategory_id(category_id);
                request.setDescription(description);
                request.setPrice(price);
                // parset to int
                request.setVisible(visible);
                request.setImages(images);
                request.setStock(stocks);

                productService.updateDataById(productID, request);
                redirectAttributes.addFlashAttribute("alert", "produk berhasil diupdate");
                return "redirect:/dashboard/products";
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {
                redirectAttributes.addFlashAttribute("alert", "produk gagal diupdate");
                return "redirect:/";
            }
        }
        return "redirect:/";

    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer productID, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) throws SQLException {
        if (session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                productService.hideProduct(productID);
                redirectAttributes.addFlashAttribute("alert", "produk berhasil didelete");
                return "redirect:/dashboard/products";
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {
                redirectAttributes.addFlashAttribute("alert", "produk gagal didelete");
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @RequestMapping("/search")
    public String search(@RequestParam String keyword, Model model, HttpSession session) throws SQLException {
        model.addAttribute("title", "Product");
        List<Product> products = (List<Product>) productService.searchProduct(keyword).getData();
        model.addAttribute("products", products);
        return "redirect:/";
    }

    @RequestMapping("/delete_stock")
    public String deleteStock(@RequestParam Integer ProductID, @RequestParam Integer stockID,
            @RequestParam String color, Model model, HttpSession session, RedirectAttributes redirectAttributes)
            throws SQLException {
        if (session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                productService.removeStock(ProductID, color);
                redirectAttributes.addFlashAttribute("alert", "produk stock berhasil didelete");
                return "redirect:/dashboard/products/edit?productID=" + ProductID;
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {
                redirectAttributes.addFlashAttribute("alert", "produk stock gagal didelete");
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @RequestMapping("/delete_image")
    public String deleteImage(@RequestParam Integer imageID, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) throws SQLException {
        if (session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            if (user.getRole().getRole_name().equals("ADMIN")) {
                productService.removeImages(imageID);
                redirectAttributes.addFlashAttribute("alert", "produk gambar berhasil didelete");
                return "redirect:/dashboard/products";
            } else if (user.getRole().getRole_name().equals("CUSTOMER")) {
                redirectAttributes.addFlashAttribute("alert", "produk gambar gagal didelete");
                return "redirect:/";
            }
        }
        return "redirect:/";
    }
}