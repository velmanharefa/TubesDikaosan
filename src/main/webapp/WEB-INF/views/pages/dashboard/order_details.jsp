<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="session" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <%@ include file = "../../includes/dashboard/_styles.jsp" %>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">

  <!-- Navbar -->
  <%@ include file = "../../includes/dashboard/_navbar.jsp" %>
  <!-- /.navbar -->

  <!-- Main Sidebar Container -->
  <%@ include file = "../../includes/dashboard/_sidebar.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Order Details</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">Order Details</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <section class="content">
      <!-- alert -->
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <c:if test="${not empty alert}">
              <div class="alert alert-${alert.type} alert-dismissible">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                ${alert.message}
              </div>
            </c:if>
          </div>
        </div>
      </div>
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="invoice p-3 mb-3">
              <div class="row">
                <div class="col-12">
                  <h4>
                    <i class="fas fa-globe"></i> DiKaosan.
                    <small class="float-right">Status : ${data[0].order_id.status}</small>
                  </h4>
                </div>
                <!-- /.col -->
              </div>
              <!-- info row -->
              <div class="row invoice-info">
                <div class="col-sm-6 invoice-col">
                  Order By
                  <address>
                    <strong>${data[0].order_id.user_id.first_name} ${data[0].order_id.user_id.last_name}</strong><br>
                    ${data[0].order_id.address_id.address}, ${data[0].order_id.address_id.city}, ${data[0].order_id.address_id.province}, ${data[0].order_id.address_id.postal_code}<br>
                    Phone: ${data[0].order_id.address_id.phone_number}<br>
                    Email: ${data[0].order_id.user_id.email}
                  </address>
                </div>
                <!-- /.col -->
                <div class="col-sm-6 invoice-col">
                  <b>Order ID #${data[0].order_id.order_id}</b><br>
                    <c:if test="${data[0].order_id.payment_id.payment_status == 'UNPAID'}">
                      <b>
                        (Cash on Delivery)
                      </b>
                    </c:if>
                  <br>
                </div>
                <!-- /.col -->
              </div>
              <!-- /.row -->

              <!-- Table row -->
              <!-- var jsp -->
              <c:set var="total" value="0" />
              <div class="row">
                <div class="col-12 table-responsive">
                  <table class="table table-striped">
                    <thead>
                    <tr>
                      <th>Qty</th>
                      <th>Product</th>
                      <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${data}" var="item">
                      <tr>
                        <td>${item.quantity}</td>
                        <td>${item.product_id.name_product}</td>
                        <td>${item.product_id.price}</td>
                        <c:set var="total" value="${total + item.product_id.price}" />
                      </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
                <!-- /.col -->
              </div>
              <!-- /.row -->

              <div class="row">
                <!-- /.col -->
                <div class="col-12">
                  <div class="table-responsive">
                    <table class="table text-center">
                      <tr>
                        <th>Total:</th>
                        <td></td>
                        <td>${total}</td>
                      </tr>
                    </table>

                    <b>Payment Details : </b>
                    <table class="table text-center">
                      <tr>
                        <th>Payment ID</th>
                        <th>Payment Method</th>
                        <th>Total</th>
                        <th>Payment Date</th>
                      </tr>
                      <tr>
                        <td>${data[0].order_id.payment_id.payment_id}</td>
                        <td>
                          <c:choose>
                            <c:when test="${empty data[0].order_id.payment_id.payment_method}">
                              Cash on Delivery
                            </c:when>
                            <c:otherwise>
                              Transer to ${data[0].order_id.payment_id.payment_method.payment_method_name}
                            </c:otherwise>
                          </c:choose>                          
                        </td>
                        <td>${data[0].order_id.payment_id.payment_total}</td>
                        <td>
                          <c:choose>
                            <c:when test="${empty data[0].order_id.payment_id.createdAt}">
                              -
                            </c:when>
                            <c:otherwise>
                              ${data[0].order_id.payment_id.createdAt}
                            </c:otherwise>
                          </c:choose>
                        </td>
                      </tr>
                      <tr>
                      </tr>
                    </table>
                  </div>
                </div>
                <!-- /.col -->
              </div>
              <!-- /.row -->

              <!-- this row will not appear when printing -->
              <div class="row no-print">
                <div class="col-6">
                  <p>Note From Customer: ${data[0].order_id.notes}</p>
                </div>
                <div class="col-6">
                  <!-- a tag process, delivered, cancel -->
                  <a href="${pageContext.request.contextPath}/dashboard/orders/detail_update?OrderID=${data[0].order_id.order_id}&userID=${data[0].order_id.user_id.user_id}&status=process" class="btn btn-success float-right" onclick="print()"><i class="far fa-credit-card"></i> Process</a>
                  <a href="${pageContext.request.contextPath}/dashboard/orders/detail_update?OrderID=${data[0].order_id.order_id}&userID=${data[0].order_id.user_id.user_id}&status=delivered" class="btn btn-primary float-right" style="margin-right: 5px;">
                    <i class="fas fa-download"></i> Delivered
                  </a>
                  <!-- sending -->
                  <a href="${pageContext.request.contextPath}/dashboard/orders/detail_update?OrderID=${data[0].order_id.order_id}&userID=${data[0].order_id.user_id.user_id}&status=sending" class="btn btn-info float-right" style="margin-right: 5px;">
                    <i class="fas fa-download"></i> Sending
                  </a>
                  <a href="${pageContext.request.contextPath}/dashboard/orders/detail_update?OrderID=${data[0].order_id.order_id}&userID=${data[0].order_id.user_id.user_id}&status=cancel" class="btn btn-danger float-right" style="margin-right: 5px;">
                    <i class="fas fa-download"></i> Cancel
                  </a>
                </div>
              </div>
            </div>
            <!-- /.invoice -->
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

  <!-- Main Footer -->
  <%@ include file = "../../includes/dashboard/_footer.jsp" %>
</div>
<!-- ./wrapper -->

<!-- REQUIRED SCRIPTS -->
<%@ include file = "../../includes/dashboard/_scripts.jsp" %>
</body>
</html>
