import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.mail.coffee.model.Coffee;
import ru.mail.system.service.DaoFactory;
import ru.mail.order.model.Order;
import ru.mail.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class InMemoryDBTest {

    @Autowired
    private DaoFactory daoFactory;

//    @Qualifier("coffeeDAOMySQLImpl")
//    @Autowired
//    private CoffeeDAO coffeeDAO;
//
//    @Qualifier("orderDAOMySQLImpl")
//    @Autowired
//    private OrderDAO orderDAO;

    /**
     * getting all coffees
     * assertEquals for coffees quantity
     */
    @Test
    public void gettingAllCoffees() {
        List<Coffee> coffees = daoFactory.getCoffeeDao().list();
        assertEquals(8, coffees.size());
    }

    /**
     * saving 2 orders with orderItems
     * getting them with orderItems
     * assertEquals order price and order items quantity
     */
    @Test
    public void savingOrder() {

        List<Coffee> coffees = daoFactory.getCoffeeDao().list();

        Order order = new Order();
        Order order2 = new Order();

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItem> orderItems2 = new ArrayList<>();

        BigDecimal price = BigDecimal.ZERO;
        BigDecimal price2 = BigDecimal.ZERO;

        for (long i = 0; i < 3; i++) {

            OrderItem orderItem = new OrderItem();
            orderItem.setId(i);
            orderItem.setQuantity(2);
            orderItem.setCoffee(coffees.get((int) i));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            price = price.add(coffees.get((int) i).getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            OrderItem orderItem2 = new OrderItem();
            orderItem2.setId(i + 1);
            orderItem2.setQuantity(3);
            orderItem2.setCoffee(coffees.get((int) i + 2));
            orderItem2.setOrder(order);
            orderItems2.add(orderItem2);
            price2 = price2.add(coffees.get((int) i + 2).getPrice().multiply(BigDecimal.valueOf(orderItem2.getQuantity())));
        }
        order.setId((long) 1);
        order.setPrice(price);
        order.setOrderItems(orderItems);
        order.setPhoneNumber("+375296522540");
        order.setDelivery(0);
        order.setUserName("Denis");
        order.setAddress("Sharangovicha 33-92");

        order2.setId((long) 2);
        order2.setPrice(price2);
        order2.setOrderItems(orderItems2);
        order2.setPhoneNumber("+375296522540");
        order2.setDelivery(0);
        order2.setUserName("Denis");
        order2.setAddress("Sharangovicha 33-92");

        daoFactory.getOrderDao().save(order);
        daoFactory.getOrderDao().save(order2);

        Order order1 = daoFactory.getOrderDao().getOrder(Long.valueOf(1));
        assertEquals(3, order1.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(57.98), order1.getPrice());

        Order order3 = daoFactory.getOrderDao().getOrder(Long.valueOf(2));
        assertEquals(3, order1.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(90.33), order3.getPrice());
    }

    /**
     * test to getting all Orders
     */
    @Test
    public void gettingAllOrders() {
        List<Coffee> coffees = daoFactory.getCoffeeDao().list();

        Order order = new Order();
        Order order2 = new Order();

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItem> orderItems2 = new ArrayList<>();

        BigDecimal price = BigDecimal.ZERO;
        BigDecimal price2 = BigDecimal.ZERO;

        for (long i = 0; i < 3; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(i);
            orderItem.setQuantity(2);
            orderItem.setCoffee(coffees.get((int) i));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            price = price.add(coffees.get((int) i).getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            OrderItem orderItem2 = new OrderItem();
            orderItem2.setId(i + 1);
            orderItem2.setQuantity(3);
            orderItem2.setCoffee(coffees.get((int) i + 2));
            orderItem2.setOrder(order);
            orderItems2.add(orderItem2);
            price2 = price2.add(coffees.get((int) i + 2).getPrice().multiply(BigDecimal.valueOf(orderItem2.getQuantity())));
        }
        order.setId((long) 3);
        order.setPrice(price);
        order.setOrderItems(orderItems);
        order.setPhoneNumber("+375296522540");
        order.setDelivery(0);
        order.setUserName("Denis");
        order.setAddress("Sharangovicha 33-92");

        order2.setId((long) 4);
        order2.setPrice(price2);
        order2.setOrderItems(orderItems2);
        order2.setPhoneNumber("+375296522540");
        order2.setDelivery(0);
        order2.setUserName("Denis");
        order2.setAddress("Sharangovicha 33-92");

        daoFactory.getOrderDao().save(order);
        daoFactory.getOrderDao().save(order2);

        List<Order> orders = daoFactory.getOrderDao().list();
        assertEquals(4, orders.size());
    }

    /**
     * test to update Delivery in order
     */
    @Test
    public void updateDelivery() {
        List<Coffee> coffees = daoFactory.getCoffeeDao().list();

        Order order = new Order();

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal price = BigDecimal.ZERO;

        for (long i = 0; i < 3; i++) {

            OrderItem orderItem = new OrderItem();
            orderItem.setId(i);
            orderItem.setQuantity(2);
            orderItem.setCoffee(coffees.get((int) i));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            price = price.add(coffees.get((int) i).getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

        }
        order.setId((long) 5);
        order.setPrice(price);
        order.setOrderItems(orderItems);
        order.setPhoneNumber("+375296522540");
        order.setDelivery(0);
        order.setUserName("Denis");
        order.setAddress("Sharangovicha 33-92");

        daoFactory.getOrderDao().save(order);
        Order order1 = daoFactory.getOrderDao().getOrder(Long.valueOf(1));
        assertEquals(3, order1.getOrderItems().size());

        daoFactory.getOrderDao().update((long) 1);
        Order order2 = daoFactory.getOrderDao().getOrder(Long.valueOf(1));
        assertEquals((long) 1, (long) order2.getDelivery());
    }
}
