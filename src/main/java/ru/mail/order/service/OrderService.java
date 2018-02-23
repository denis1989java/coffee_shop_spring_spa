package ru.mail.order.service;

import ru.mail.order.model.Order;

/**
 * @author Denis Monich
 */
public interface OrderService {

    Order updateOrder(Order order, String sendQuantity, String sendCoffeeId);

    Order putToBasket(Order order, String sendQuantity, String sendCoffeeId);

    Order deleteFromOrder(Order order, Long coffeeId);
}
