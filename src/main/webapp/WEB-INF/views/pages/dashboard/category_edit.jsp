<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">${title} ${data.category_name}</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/categories">Categories</a></li>
              <li class="breadcrumb-item active">${title}</li>
            </ol>
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <div class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <form action="${pageContext.request.contextPath}/dashboard/categories/update?categoryID=${data.category_id}" method="POST">
              <div class="card card-warning">
                <div class="card-header">
                  <h3 class="card-title">${title}</h3>
                </div>
                <!-- /.card-header -->
                <div class="card-body">
                  <div class="form-group">
                    <label for="exampleInputBorder">Category</label>
                    <input type="hidden" name="category_id" value="${data.category_id}">
                    <input type="text" class="form-control form-control-border" id="category_name" name="category_name" placeholder="Enter category" value="${data.category_name}">
                  </div>
                  
                  <div class="form-group">
                    <label for="exampleSelectBorder">Status visible</label>
                    <select class="custom-select form-control-border" id="visible" name="visible">
                      <option value="${data.visible}" selected>${data.visible == 1 ? "Yes" : "No"}</option>
                      ${data.visible == 1 ? "<option value='0'>No</option>" : "<option value='1'>Yes</option>"}
                    </select>
                  </div>
                  <button type="submit" class="btn btn-warning" name="submit">Update</button>
                  <a href="${pageContext.request.contextPath}/dashboard/categories" class="btn btn-secondary">Cancel</a>
                </div>
                <!-- /.card-body -->
              </div>
            </form>
          </div>
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
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
