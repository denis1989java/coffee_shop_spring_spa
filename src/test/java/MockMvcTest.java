import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mail.coffee.controller.CoffeController;
import ru.mail.coffee.model.Coffee;
import ru.mail.order.controller.OrderController;
import ru.mail.order.model.Order;
import ru.mail.order.model.OrderItem;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.RequestEntity.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
@WebAppConfiguration
public class MockMvcTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;


    @Autowired
    private CoffeController coffeController;

    @Autowired
    private OrderController orderController;


    @Before
    public void setUp() {
        coffeController = Mockito.mock(CoffeController.class);
        Mockito.reset(coffeController);

        orderController = Mockito.mock(OrderController.class);
        Mockito.reset(orderController);

        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    /**
     * test to get all coffees from DB - work with file and MySQL? no Oracle
     */
    @Test
    public void getAllCoffees() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coffees")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Americano")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Cappuccino")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Espresso")))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].name", is("Latte")))
                .andExpect(jsonPath("$[4].id", is(5)))
                .andExpect(jsonPath("$[4].name", is("Lungo")))
                .andExpect(jsonPath("$[5].id", is(6)))
                .andExpect(jsonPath("$[5].name", is("Doppiio")))
                .andExpect(jsonPath("$[6].id", is(7)))
                .andExpect(jsonPath("$[6].name", is("Glace")))
                .andExpect(jsonPath("$[7].id", is(8)))
                .andExpect(jsonPath("$[7].name", is("Raf")));

        verifyZeroInteractions(coffeController);
    }

    /**
     * test to add coffee to basket
     */
    @Test
    public void addToBasket() throws Exception {

        mockMvc.perform(post("/api/v1/order")
                .param("id", "1")
                .param("quantity", "2")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.order.orderItems[0].coffee.id", is(1)))
                .andExpect(jsonPath("$.order.orderItems[0].quantity", is(2)))
                .andExpect(jsonPath("$.order.price", is(17.12)));

        verifyZeroInteractions(orderController);

    }

    /**
     * test to change coffee quantity in basket
     */
    @Test
    public void changeCoffeeQuantity() throws Exception {

        Order order=new Order();

        Coffee coffee=new Coffee(1L,"Black","Bit", BigDecimal.valueOf(26.36));

        OrderItem orderItem=new OrderItem();
        orderItem.setCoffee(coffee);
        orderItem.setId(1L);
        orderItem.setQuantity(10);
        orderItem.setOrder(order);

        List<OrderItem> orderItemList=new ArrayList<>();
        orderItemList.add(orderItem);

        order.setOrderItems(orderItemList);

        BigDecimal price=BigDecimal.ZERO;
        for (OrderItem anOrderItemList : orderItemList) {
            price = price.add(anOrderItemList.getCoffee().getPrice().multiply(BigDecimal.valueOf(anOrderItemList.getQuantity())));
        }
        order.setPrice(price);
        HashMap<String, Object> sessionAttr = new HashMap<>();
        sessionAttr.put("order", order);


        mockMvc.perform(post("/api/v1/order/price")
                .sessionAttrs(sessionAttr)
                .param("id", "1")
                .param("quantity", "1")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.price", is(26.36)));

        verifyZeroInteractions(orderController);
    }

    /**
     * test to create and save Order
     */
    @Test
    public void createOrder() throws Exception {

        Order order=new Order();

        Coffee coffee=new Coffee(1L,"White","Sweet", BigDecimal.valueOf(12.12));

        OrderItem orderItem=new OrderItem();
        orderItem.setCoffee(coffee);
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setOrder(order);

        List<OrderItem> orderItemList=new ArrayList<>();
        orderItemList.add(orderItem);

        order.setOrderItems(orderItemList);

        BigDecimal price=BigDecimal.ZERO;
        for (OrderItem anOrderItemList : orderItemList) {
            price = price.add(anOrderItemList.getCoffee().getPrice().multiply(BigDecimal.valueOf(anOrderItemList.getQuantity())));
        }

        order.setPrice(price);

        HashMap<String, Object> sessionAttr = new HashMap<>();
        sessionAttr.put("order", order);


                mockMvc.perform(put("/api/v1/order")
                .sessionAttrs(sessionAttr)
                .param("name", "Denis")
                .param("address", "Sharangovica 33-92")
                .param("phone", "+375296522540")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        verifyZeroInteractions(orderController);
    }
}