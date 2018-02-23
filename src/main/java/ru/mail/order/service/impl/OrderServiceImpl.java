package ru.mail.order.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.mail.order.service.OrderService;
import ru.mail.system.service.DaoFactory;
import ru.mail.coffee.model.Coffee;
import ru.mail.order.model.Order;
import ru.mail.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Denis Monich
 * this class realise all logic of work with orders while it is't confirmed
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);


    private final DaoFactory daoFactory;
    private Long ORDERID = 0L;

    public OrderServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }


    /**
     * update orders parameters
     * @param order        which will be updated
     * @param sendQuantity array of orders items quantities
     * @param sendCoffeeId array of coffees id which must be in order
     * @return updated order
     */
    @Override
    public Order updateOrder(Order order, String sendQuantity, String sendCoffeeId) {
        logger.debug("updating order");
        BigDecimal price = BigDecimal.ZERO;
        List<OrderItem> items = order != null ? order.getOrderItems() : null;
            for (OrderItem item : items) {

                if (item.getCoffee().getId().toString().equals(sendCoffeeId)) {
                    item.setQuantity(Integer.parseInt(sendQuantity));
                }
                price=price.add(item.getCoffee().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
            order.setOrderItems(items);
            order.setPrice(price);
        return order;
    }




    /**
     * @param ord          order which will be updated
     * @param sendQuantity array of orders items quantities
     * @param sendCoffeeId array of coffees id which must be in order
     * @return updated order
     */
    @Override
    public Order putToBasket(Order ord, String sendQuantity, String sendCoffeeId) {
        logger.debug("putting order to basket");
        List<Coffee> coffees = daoFactory.getCoffeeDao().list();
        List<OrderItem> items = new ArrayList<>();
        BigDecimal price = BigDecimal.ZERO;

        Order order = new Order(ORDERID, items, price);

            for (Coffee coffee : coffees) {
                if (coffee.getId().toString().equals(sendCoffeeId)) {
                    OrderItem orderItem = new OrderItem(coffee, order, Integer.parseInt(sendQuantity));
                    items.add(orderItem);
                    price = price.add(coffee.getPrice().multiply(BigDecimal.valueOf(Long.parseLong(sendQuantity))));
                }
            }


        if (ord == null) {
            ORDERID++;
            order.setPrice(price);
            order.setOrderItems(items);
            return order;
        } else {
            for (OrderItem item : items) {
                boolean notNew = true;
                for (OrderItem orderItem : ord.getOrderItems()) {
                    if (item.getCoffee().getId().equals(orderItem.getCoffee().getId())) {
                        Integer newQuant = item.getQuantity() + orderItem.getQuantity();
                        orderItem.setQuantity(newQuant);
                        notNew = false;
                        break;
                    }
                }
                if (notNew) {
                    ord.getOrderItems().add(item);
                }
            }

            BigDecimal newPrice = BigDecimal.ZERO;

            for (OrderItem orderItem : ord.getOrderItems()) {
                newPrice = newPrice.add(orderItem.getCoffee().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            }
            ord.setPrice(newPrice);

            return ord;
        }
    }

    /**
     * deleting orderItem from order
     * @param order from session
     * @param coffeeId id of coffee which should be deleted from order
     * @return current order
     */
    @Override
    public Order deleteFromOrder(Order order, Long coffeeId) {
        logger.debug("deleting from order");
        List <OrderItem> list=new ArrayList<>();
        BigDecimal price=BigDecimal.ZERO;
        for (OrderItem orderItem : order.getOrderItems()) {
            if(!Objects.equals(orderItem.getCoffee().getId(), coffeeId)){
                list.add(orderItem);
                price=price.add(orderItem.getCoffee().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            }
        }
        order.setOrderItems(list);
        order.setPrice(price);
        return order;
    }
}
