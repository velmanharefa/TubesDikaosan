package com.TubesDiKaosan.ecommerce.services.ActorServices;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.TubesDiKaosan.ecommerce.models.CustomerAddress;
import com.TubesDiKaosan.ecommerce.models.Orders;
import com.TubesDiKaosan.ecommerce.models.OrdersItem;
import com.TubesDiKaosan.ecommerce.models.Roles;
import com.TubesDiKaosan.ecommerce.models.Users;
import com.TubesDiKaosan.ecommerce.payloads.requests.UserRequest;
import com.TubesDiKaosan.ecommerce.payloads.requests.CustomerAddressRequest;
import com.TubesDiKaosan.ecommerce.payloads.response.Response;
import com.TubesDiKaosan.ecommerce.repositories.CustomerAddressRepository;
import com.TubesDiKaosan.ecommerce.repositories.OrderItemRepository;
import com.TubesDiKaosan.ecommerce.repositories.OrdersRepository;
import com.TubesDiKaosan.ecommerce.repositories.UserRepository;
import com.TubesDiKaosan.ecommerce.services.ShoppingServices.ShoppingServices;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@Service
@Primary
public class CustomerService extends UsersService {
    @Autowired
    private ShoppingServices shoppingServices;

    public CustomerService(UserRepository userRepository, RolesService rolesService) {
        super(userRepository, rolesService);
    }
    
    @Override
    public Response getAll() throws SQLException {
        return new Response(HttpStatus.BAD_REQUEST.value(), "Method not allowed", null);
    }
    
    @Override
    public Response deleteDataByID(String id) throws SQLException {
        return new Response(HttpStatus.BAD_REQUEST.value(), "Method not allowed", null);
    }
    
    @Override
    public final Response updateDataById(String id, UserRequest request) throws SQLException {
        try {
            Optional<Users> optionalUser = super.userRepository.findById(id);

            if (optionalUser.isPresent()) {
                Users data = optionalUser.get();
                data.setFirst_name(request.getFirst_name());
                data.setLast_name(request.getLast_name());
                data.setEmail(request.getEmail());
                data.setPassword(request.getPassword());

                Response roleResponse = super.rolesService.findDataByID(request.getRole());
                if (roleResponse.getStatus() != HttpStatus.OK.value()) {
                    return new Response(HttpStatus.BAD_REQUEST.value(), "Invalid role ID!", null);
                }

                Roles roleData = (Roles) roleResponse.getData();
                data.setRole(roleData);
                userRepository.save(data);

                return new Response(HttpStatus.OK.value(), "Success", data);
            } else {
                return new Response(HttpStatus.NOT_FOUND.value(), "Data not found", null);
            }
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error updating user", null);
        }
    }
    
    @Override
    public Response createData(UserRequest request) throws SQLException {
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirst_name(request.getFirst_name());
        user.setLast_name(request.getLast_name());

        Response roleResponse = rolesService.findDataByID(request.getRole());
        if (roleResponse.getStatus() != HttpStatus.OK.value()) {
            return new Response(HttpStatus.BAD_REQUEST.value(), "Invalid role ID!", null);
        }

        Roles roleData = (Roles) roleResponse.getData();
        user.setRole(roleData);

        userRepository.save(user);
        return new Response(HttpStatus.OK.value(), "success", user);
    }

    // Address
    @Autowired
    private CustomerAddressRepository addressRepository;
    public Response addAddress(String id, CustomerAddressRequest request) throws SQLException {
        Response userResponse = super.findDataByID(id);
        if (userResponse.getStatus() != HttpStatus.OK.value()) {
            return new Response(HttpStatus.BAD_REQUEST.value(), "Invalid user ID!", null);
        }

        Users userData = (Users) userResponse.getData();

        CustomerAddress addressData = new CustomerAddress();
        addressData.setAddress(request.getAddress());
        addressData.setCity(request.getCity());
        addressData.setProvince(request.getProvince());
        addressData.setPostal_code(request.getPostal_code());
        addressData.setPhone_number(request.getPhone_number());
        addressData.setCustomerAddress(userData);

        addressRepository.save(addressData);
        return new Response(HttpStatus.OK.value(), "Success", addressData);
    }

    public Response updateAddress(String id, Integer addressId, CustomerAddressRequest request) throws SQLException {
        Response userResponse = super.findDataByID(id);
        if (userResponse.getStatus() != HttpStatus.OK.value()) {
            return new Response(HttpStatus.BAD_REQUEST.value(), "Invalid user ID!", null);
        }

        Users userData = (Users) userResponse.getData();

        Optional<CustomerAddress> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isPresent()) {
            CustomerAddress addressData = optionalAddress.get();
            addressData.setAddress(request.getAddress());
            addressData.setCity(request.getCity());
            addressData.setProvince(request.getProvince());
            addressData.setPostal_code(request.getPostal_code());
            addressData.setPhone_number(request.getPhone_number());
            addressData.setCustomerAddress(userData);

            addressRepository.save(addressData);
            return new Response(HttpStatus.OK.value(), "Success", addressData);
        } else {
            return new Response(HttpStatus.NOT_FOUND.value(), "Data not found", null);
        }
    }

    public Response deleteAddress(String id, Integer addressId) throws SQLException {
        Response userResponse = super.findDataByID(id);
        if (userResponse.getStatus() != HttpStatus.OK.value()) {
            return new Response(HttpStatus.BAD_REQUEST.value(), "Invalid user ID!", null);
        }

        Optional<CustomerAddress> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isPresent()) {
            CustomerAddress addressData = optionalAddress.get();
            addressRepository.delete(addressData);
            return new Response(HttpStatus.OK.value(), "Success", null);
        } else {
            return new Response(HttpStatus.NOT_FOUND.value(), "Data not found", null);
        }
    }

    public Response getAllAddress(String id) throws SQLException { // GET ALL ADDRESS BY CUSTOMER
        if (addressRepository.findAllByCustomerAddress(id).isEmpty())
            return new Response(HttpStatus.NOT_FOUND.value(), "NO Data!", null);
        else {
            List<CustomerAddress> data = addressRepository.findAllByCustomerAddress(id);
            return new Response(HttpStatus.OK.value(), "success", data);
        }
    }
    
    public Response getRolesCustomer() throws SQLException {
        Roles role = rolesService.existsByName("CUSTOMER");
        if (role == null) {
            return new Response(HttpStatus.NOT_FOUND.value(), "Data not found", null);
        }
        return new Response(HttpStatus.OK.value(), "success", role);
    }


    // ORDERS & CARTS
    @Autowired
    private ShoppingServices shoppingService;
    public Response getDraftOrder(String id) throws SQLException { // GET DRAFT ORDER (KERANJANG)
        if (shoppingService.getDraftOrder(id) == null)
            return new Response(HttpStatus.NOT_FOUND.value(), "NO Data!", null);
        else {
            Orders data = (Orders) shoppingService.getDraftOrder(id).getData();
            return new Response(HttpStatus.OK.value(), "success", data);
        }
    }

    public Response getDataCart(Integer orderID, String UserID) throws SQLException { // GET ALL ITEM IN CART
        if (shoppingService.getDataCart(orderID, UserID).getData() == null)
            return new Response(HttpStatus.NOT_FOUND.value(), "NO Data!", null);
        else {
            List<OrdersItem> data = (List<OrdersItem>) shoppingService.getDataCart(orderID, UserID).getData();
            return new Response(HttpStatus.OK.value(), "success", data);
        }
    }

    
}