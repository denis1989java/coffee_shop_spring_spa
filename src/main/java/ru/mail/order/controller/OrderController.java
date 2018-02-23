package ru.mail.order.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.system.service.model.CustomResponseEntity;
import ru.mail.order.service.OrderService;
import ru.mail.system.service.DaoFactory;
import ru.mail.order.model.Order;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = {"api/v1"})
public class OrderController {
    private static final Logger logger = Logger.getLogger(OrderController.class);

    private final DaoFactory daoFactory;
    private final OrderService orderService;

    @Autowired
    public OrderController(DaoFactory daoFactory, OrderService orderService) {
        this.daoFactory = daoFactory;
        this.orderService = orderService;
    }



    /**
     * getting the order from session
     *
     * @param session httpSession to get valid order
     * @return response entity with current order and totalQuantity
     */
    @RequestMapping(value = {"/order"}, method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object> order(HttpSession session) {
        logger.debug("getting the order");
        Order curOrder = (Order) session.getAttribute("order");

        CustomResponseEntity customResponseEntity = new CustomResponseEntity();

        Integer totalQuantity = 0;
        if (curOrder != null) {
            for (int i = 0; i < curOrder.getOrderItems().size(); i++) {
                totalQuantity = totalQuantity + curOrder.getOrderItems().get(i).getQuantity();
            }
        }

        customResponseEntity.setOrder(curOrder);
        customResponseEntity.setTotalQuantity(totalQuantity);

        return new ResponseEntity<Object>(customResponseEntity, HttpStatus.OK);
    }

    /**
     * adding the orderItem to basket
     *
     * @param quantity of coffee
     * @param id       of coffee
     * @param session  httpSession to get valid order
     * @return response entity with current order and totalQuantity
     */
    @RequestMapping(value = {"/order"}, method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object>  addToBasket(@RequestParam(value = "quantity", required = false) String quantity,
                              @RequestParam(value = "id", required = false) String id,
                              HttpSession session) {
        logger.debug("adding to basket");

        Order curOrder = (Order) session.getAttribute("order");

        //putting order to basket and getting Id of this order
        Order order = orderService.putToBasket(curOrder, quantity, id);

        CustomResponseEntity customResponseEntity = new CustomResponseEntity();

        Integer totalQuantity = 0;
        if (order != null) {
            for (int i = 0; i < order.getOrderItems().size(); i++) {
                totalQuantity = totalQuantity + order.getOrderItems().get(i).getQuantity();
            }
        }

        customResponseEntity.setOrder(order);
        customResponseEntity.setTotalQuantity(totalQuantity);

        //putting order id to session
        session.setAttribute("order", order);

        return  new ResponseEntity<Object>(customResponseEntity, HttpStatus.OK);
    }


    /**
     * deleting orderItem from basket
     *
     * @param id      of coffee in order
     * @param session httpSession to get valid order
     * @return response entity with current order
     */
    @RequestMapping(value = {"/orderItem/{id}"}, method = RequestMethod.DELETE, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object>  delete(@PathVariable Long id,
                 HttpSession session) {

        logger.debug("deleting orderItem from order");

        Order curOrder = (Order) session.getAttribute("order");

        //putting order to basket and getting Id of this order
        Order order = orderService.deleteFromOrder(curOrder, id);

        //putting order id to session
        session.setAttribute("order", order);

        Order orderToSend = new Order();
        orderToSend.setPrice(order.getPrice());

        return  new ResponseEntity<Object>(orderToSend, HttpStatus.OK);
    }

    /**
     * updating quantity in basket
     *
     * @param quantity new quantity
     * @param id       of coffee in order
     * @param session  httpSession to get valid order
     * @return response entity with current order
     */
    @RequestMapping(value = {"/order/price"}, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object> update(@RequestParam(value = "quantity", required = false) String quantity,
                 @RequestParam(value = "id", required = false) String id,
                 HttpSession session) {

        logger.debug("updating order");

        Order curOrder = (Order) session.getAttribute("order");

        //putting order to basket and getting Id of this order
        Order order = orderService.updateOrder(curOrder, quantity, id);

        //putting order id to session
        session.setAttribute("order", order);

        Order orderToSend = new Order();
        orderToSend.setPrice(order.getPrice());

        return new ResponseEntity<Object>(orderToSend, HttpStatus.OK);
    }

    /**
     * saving the order and cleaning the session
     * @param name    of customer from UI
     * @param address of customer from UI
     * @param phone   of customer from UI
     * @param session httpSession to get valid order
     * @return empty response entity
     */
    @RequestMapping(value = {"/order"}, method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object> confirm(@RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "address", required = false) String address,
                          @RequestParam(value = "phone", required = false) String phone,
                          HttpSession session) {

        Order order = (Order) session.getAttribute("order");

        if (name != null && phone != null && address != null) {

            logger.debug("confirming order");
            //confirming order
            order.setUserName(name);
            order.setAddress(address);
            order.setPhoneNumber(phone);
            order.setDelivery(0);

            daoFactory.getOrderDao().save(order);
            //cleaning of session

            session.setAttribute("order", null);
        }

        return new ResponseEntity<Object>(new Order(), HttpStatus.OK);
    }

    /**
     * getting all orders from DB
     *
     * @return response entity with all orders
     */
    @RequestMapping(value = {"/admin/orders"}, method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object> orders() {

        logger.debug("getting list of all orders");

        List<Order> orders = daoFactory.getOrderDao().list();

        return new ResponseEntity<Object>(orders, HttpStatus.OK);
    }

    /**
     * changing delivery status of order
     *
     * @param id of order where status must be changed
     * @return empty response entity
     */
    @RequestMapping(value = {"/admin/order/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<Object> changeDeliveriStatus(@PathVariable Long id) {

        logger.debug("updating delivery status");

        daoFactory.getOrderDao().update(id);

        return new ResponseEntity<Object>(new Order(), HttpStatus.OK);
    }

    /**
     * getting order's details by order id
     *
     * @param id of order which will be shown
     * @return response entity with required order
     */
    @RequestMapping(value = {"/admin/order/{id}"}, method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object> getOrderDetail(@PathVariable Long id) {

        logger.debug("getting required order");

        Order order = daoFactory.getOrderDao().getOrder(id);

        return  new ResponseEntity<Object>(order, HttpStatus.OK);
    }

}
