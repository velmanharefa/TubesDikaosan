package com.TubesDiKaosan.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.TubesDiKaosan.ecommerce.models.OrdersItem;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrdersItem, Integer> {

    // GET ITEM IN CART AND STATUS ORDERS DRAFT
    @Query(value = "SELECT * FROM orders_item WHERE order_id = ?1", nativeQuery = true)
    List<OrdersItem> getAllItemInCart(Integer order_id);

    // GET ORDER DETAILS BY ORDER ID
    @Query(value = "SELECT * FROM orders_item WHERE order_id = ?1", nativeQuery = true)
    List<OrdersItem> getOrderDetail(Integer order_id);

    @Query(value = "SELECT SUM(price) FROM orders_item WHERE product_id = ?1 AND order_id = ?2", nativeQuery = true)
    Integer TotalPrice(Integer product_id, Integer order_id);

    @Query(value = "SELECT orders_item.* FROM orders_item JOIN orders ON orders_item.order_id = orders.order_id WHERE orders.user_id = ?1 AND orders_item.product_id = ?2 AND orders.status = 'delivered'", nativeQuery = true)
    List<OrdersItem> getItemDelivered(String user_id, Integer product_id);
}
