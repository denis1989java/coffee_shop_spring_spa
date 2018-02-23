package ru.mail.system.service.model;

import ru.mail.coffee.model.Coffee;
import ru.mail.order.model.Order;

import java.util.List;

/**
 * @author Denis Monich
 */
public class CustomResponseEntity {

    private List<Coffee> coffees;
    private Order order;
    private Integer totalQuantity;
    private List<Order> orders;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Coffee> getCoffees() {
        return coffees;
    }

    public void setCoffees(List<Coffee> coffees) {
        this.coffees = coffees;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "CustomResponseEntity{" +
                "coffees=" + coffees +
                ", order=" + order +
                ", totalQuantity=" + totalQuantity +
                ", orders=" + orders +
                ", message='" + message + '\'' +
                '}';
    }
}
