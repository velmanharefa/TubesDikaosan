<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="session" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="en">
  <head>
    <title>Description</title>
    <%@ include file = "../../includes/fe_includes/_header.jsp" %>
  </head>

  <body>
    <!-- Navbar Section Start -->
    <%@ include file = "../../includes/fe_includes/_navbar.jsp" %>
    <!-- Navbar Section End -->

    <!-- Activity Information Start-->
    <section class="activity">
      <div class="activity_page">
        <div class="container activity-cont offset mx-1">
          <div class="row">
            <div class="col-sm-12">
              <div class="activity-nav offset-mx-1">
                <h1>${data.name_product}</h1>
                <a class="nav-link" href="${pageContext.request.contextPath}/home" style="word-spacing: 2px;">Home 
                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="12" fill="currentColor" class="bi bi-chevron-right" viewBox="0 0 16 16">
                      <path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
                    </svg> 
                    <a class="nav-link" href="${pageContext.request.contextPath}/shop" style="word-spacing: 2px;">Shop
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="12" fill="currentColor" class="bi bi-chevron-right" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
                      </svg>
                    </a>
                    <a href="${pageContext.request.contextPath}/description" style="text-decoration: none;">
                      <span>${data.name_product}</span>
                    </a>
                  </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- Activity Information End-->

    <!-- Description Section Start -->
    <section class="description_content">

      <div class="container description my-5">
        <div class="row">
          <c:if test="${not empty message}">
                          <div class="alert alert-danger my-2" role="message">
                            ${message}
                          </div>
                        </c:if>
          <div class="col-6 d-flex justify-content-center align-items-center">
            <img style="object-fit: cover;" src="${pageContext.request.contextPath}/resources/uploads/images/products/${data.images[0].image}" alt="">
          </div>

          <div class="col-6">
            <div class="row title_product">
              <div class="col-8">
                <h1>${data.name_product}<br><fmt:formatNumber value="${data.price}" type="currency" currencyCode="IDR" /></h1>
              </div>
            </div>

            <div class="d-flex rating-desc">
              <div class="col-2">
                <a style="text-decoration: none;">
                  <span>
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="var(--YN300, #FFC400)" class="bi bi-star-fill" viewBox="0 0 16 16">
                      <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
                    </svg>
                    <a>${total_rating} (${total_riviews})</a>
                  </span>
                </a>
              </div>
            </div>

            <div class="row">
              <div class="d-flex justify-content-left align-items-center">
                <c:forEach items="${data.images}" var="item">
                  <div class="product_img">
                    <img src="${pageContext.request.contextPath}/resources/uploads/images/products/${item.image}" style="max-width: 20em; max-height: 20em; width: 100%; height: 100%; object-fit: cover;">
                  </div>
                </c:forEach>
              </div>

              
              <form action="${pageContext.request.contextPath}/addTocart" method="post">
                <input type="hidden" name="product_id" value="${data.product_id}">
            
                <div class="d-flex color my-2 gap-1">
                    <div class="justify-content-center align-items-center text_color">
                        <a>Color: </a>
                    </div>
                    <div id="myBtnColor" class="d-flex">
                        <c:forEach items="${data_stock}" var="item" varStatus="colorIndex">
                            <div class="color_product d-flex px-1">
                                <input type="radio" class="btn-check mx-5" name="colors" id="colors-${colorIndex.index}" value="${item.key}" autocomplete="off">
                                <label class="btn btn-secondary" for="colors-${colorIndex.index}" data-color="${item.key}">${item.key}</label>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                
                <div class="d-flex size my-2" id="sizeContainer">
                  
                </div>
            
                <div class="d-flex button_description my-2">
                    <div class="button_buy">
                        <!-- <a class="btn btn-add-cart d-flex justify-content-center align-items-center" href="${pageContext.request.contextPath}/shoping_cart">ADD TO CART</a> -->
                        <!-- alert color text red -->
                        
                        <button type="submit" class="btn btn-add-cart d-flex justify-content-center align-items-center">ADD TO CART</button>
                        
                      </div>
                </div>
            </form>

              <div class="d-flex text_description my-2">
                <div class="description_product">
                  <h1>Description :</h1>
                  <p>
                    ${data.description}
                  </p> 
                </div>
              </div>

            </div>
          </div>

        </div>
      </div>
    </section>
    <!-- Description Section End -->

    <!-- section riview -->
   <!-- Review Section Start -->
   <section class="review" style="height: 300px; overflow-y: auto;">
    <div class="container review-cont">
      <!-- if condition -->
      <c:if test="${not empty alert}">
        <div class="alert alert-primary" role="alert">
          ${alert}
        </div>
      </c:if>
  
      <div class="row">
        <!-- Add review content here -->
        <!-- header review -->
        <div class="col-sm-12">
          <div class="review-header">
            <b>Reviews: </b>
          </div>
        </div>
        
        <div class="col-sm-12 my-1">
          <div class="user-review">
            <!-- Review 1 -->
            <c:choose>
              <c:when test="${total_riviews == 0}">
                <div class="review-item">
                  <b>There are no reviews yet.</b>
                </div>
              </c:when>
              <c:otherwise>
                <c:forEach items="${riviews}" var="item">
                  <hr>
                  <div class="review-item">
                    <b>From : ${item.user.first_name} ${item.user.last_name}</b>
                    <div class="rating">
                      Rating: ${item.rate} (${total_riviews})
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="var(--YN300, #FFC400)" class="bi bi-star-fill" viewBox="0 0 16 16">
                        <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
                      </svg>
                      <!-- Add more stars if needed -->
                    </div>
                    <p>Comment : ${item.comment}</p>
                  </div>
                  <hr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </div>
  </section>

  <!-- get attribute model.addAttribute("review", true); -->
  <c:if test="${feedback == true and sessionScope.user != null}">
    <section class="feedback">
      <div class="container">
        <div class="row">
          <!-- Form for adding a new review -->
      <div class="col-sm-12 my-5">
        <form action="${pageContext.request.contextPath}/feedback" method="post">
          <input type="hidden" name="product" value="${data.product_id}">
          <input type="hidden" name="user_id" value="${sessionScope.user.user_id}">
          <h2 style="overflow-y: hidden;">Add Your Review</h2>
          <div class="form-group">
            <label for="userRating">Rating</label>
            <input type="number" class="form-control" id="userRating" name="rate" min="1" max="5" step="0.1" required>
          </div>
          <div class="form-group">
            <label for="userComment">Your Comment</label>
            <textarea class="form-control" id="userComment" name="comment" rows="4" required></textarea>
          </div>
          <br>
          <button type="submit" class="btn btn-primary">Submit Review</button>
        </form>
      </div>
        </div>
      </div>
    </section>
  </c:if>
<!-- Review Section End -->

    <!-- Chat Section Start -->
    <!-- <%@ include file = "../../includes/fe_includes/_chat.jsp" %> -->
    <!-- Chat Section End -->

    <!-- Footer Section Start -->
    <%@ include file = "../../includes/fe_includes/_footer.jsp" %>
    <!-- Footer Section End-->
    
    <!-- script Section Start -->
    <%@ include file = "../../includes/fe_includes/_scripts.jsp" %>
    <!-- script Section End -->

    <script>
      document.addEventListener('DOMContentLoaded', function () {
          var rawDataStock = '${data_stock}';
          var formattedDataStock = rawDataStock.replace(/(\w+)=/g, '"$1":').replace(/=/g, ':');
          var dataStock = JSON.parse(formattedDataStock);
  
          var colorRadios = document.querySelectorAll('input[name="colors"]');
          var sizeRadios = document.querySelectorAll('input[name="sizes"]');
          var sizeContainer = document.getElementById('sizeContainer');
  
          console.log(dataStock);
  
          colorRadios.forEach(function (colorRadio) {
              colorRadio.addEventListener('change', function () {
                  var selectedColor = this.value;
                  var sizes = dataStock[selectedColor];
                  console.log('Selected Color:', selectedColor);
                  sizeContainer.innerHTML = '';
  
                  for (var size in sizes) {
                      if (sizes.hasOwnProperty(size) && sizes[size] > 0) {
                          var sizeDiv = document.createElement('div');
                          sizeDiv.classList.add('size_product');
  
                          var sizeRadio = document.createElement('input');
                          sizeRadio.type = 'radio';
                          sizeRadio.classList.add('btn-check');
                          sizeRadio.name = 'sizes';
                          sizeRadio.id = 'sizes-' + selectedColor + '-' + size;
                          sizeRadio.value = size;
                          sizeRadio.autocomplete = 'off';
  
                          sizeRadio.addEventListener('change', function () {
                              console.log('Selected Size:', this.value);
                          });
  
                          var sizeLabel = document.createElement('label');
                          sizeLabel.classList.add('btn', 'btn-secondary');
                          sizeLabel.htmlFor = 'sizes-' + selectedColor + '-' + size;
                          sizeLabel.dataset.size = size;
                          sizeLabel.innerText = size;
  
                          sizeDiv.appendChild(sizeRadio);
                          sizeDiv.appendChild(sizeLabel);
                          sizeContainer.appendChild(sizeDiv);
                      }
                  }
              });
          });
      });
  </script>
    </body>
  </html>