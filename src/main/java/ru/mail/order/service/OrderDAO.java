package ru.mail.order.service;

import ru.mail.order.model.Order;

import java.util.List;

/**
 * @author Denis Monich
 */
public interface OrderDAO {

    void update(Long id);

    List<Order> list();

    void save(Order order);

    Order getOrder(Long id);
}
