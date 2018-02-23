package ru.mail.order.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * @author Denis Monich
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 7527156666538399253L;
    private Long id;
    @JsonManagedReference
    private List<OrderItem> orderItems;
    private BigDecimal price;
    private String address;
    private String userName;
    private String phoneNumber;
    private Integer delivery;

    public Order() {
    }

    public Order(Long id, List<OrderItem> orderItemList, BigDecimal price) {
        this.id = id;
        this.orderItems = orderItemList;
        this.price = price;
        this.delivery = 0;
    }

    public Integer getDelivery() {
        return delivery;
    }

    public void setDelivery(Integer delivery) {
        this.delivery = delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderItems=" + orderItems +
                ", price=" + price +
                ", address='" + address + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", delivered=" + delivery +
                '}';
    }
}
