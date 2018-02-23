package ru.mail.order.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.mail.order.service.OrderDAO;
import ru.mail.system.configs.connectionPoolClasses.DataSource;
import ru.mail.coffee.model.Coffee;
import ru.mail.order.model.Order;
import ru.mail.order.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Monich
 * this class realise all logic of work with orders in Oracle DB
 */
@Service("orderDAOOracleImpl")
public class OrderDAOOracleImpl implements OrderDAO {

    private static final Logger logger = Logger.getLogger(OrderDAOOracleImpl.class);
    private static final String GET_ORDER_BY_ID = "select * from Orders WHERE seqnr=?";
    private static final String GET_ORDER_NEXT_ID = "SELECT SEQ_Orders.nextval FROM dual";
    private static final String GET_ORDER_ITEM_NEXT_ID = "SELECT SEQ_OrderItem.nextval FROM dual";
    private static final String UPDATE_ORDER_QUERY = "UPDATE Orders SET status=1 WHERE seqnr=?";
    private static final String GET_ALL_ORDERS_QUERY = "select * from Orders";
    private static final String INSERT_NEW_ORDER_QUERY = "INSERT INTO Orders (seqnr,customername,customeraddress,phone,status,price) values(?,?,?,?,?,?)";
    private static final String INSERT_NEW_ORDER_ITEM_QUERY = "INSERT INTO OrderItem (seqnr,coffee,\"order\",quantity) values(?,?,?,?)";
    private static final String GET_ALL_ORDER_ITEMS_BY_ORDER_ID = "select OrderItem.quantity, Coffee.name, Coffee.price from OrderItem inner join Coffee on OrderItem.coffee=Coffee.seqnr where OrderItem.\"order\"=?";

    /**
     * changing the status
     *
     * @param id of which status will changed
     */
    @Override
    public void update(Long id) {

        logger.debug("Update order with id: " + id);
        logger.debug("Query: " + UPDATE_ORDER_QUERY);
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_QUERY);
            preparedStatement.setInt(1, Integer.parseInt(String.valueOf(id)));
            int i = preparedStatement.executeUpdate();
            logger.debug("Update finished with code: " + i);
            DataSource.returnConnection(connection);
            preparedStatement.close();
        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * getting all orders
     *
     * @return list of orders
     */
    @Override
    public List<Order> list() {

        logger.debug("Get all orders");
        logger.debug("Query: " + GET_ALL_ORDERS_QUERY);
        List<Order> orders = new ArrayList<>();

        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDERS_QUERY);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                Order order = new Order();
                order.setId(rs.getLong(1));
                order.setUserName(rs.getString(2));
                order.setAddress(rs.getString(3));
                order.setPhoneNumber(rs.getString(4));
                order.setDelivery(rs.getInt(5));
                order.setPrice(rs.getBigDecimal(6));
                orders.add(order);
            }

            DataSource.returnConnection(connection);
            preparedStatement.close();

        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug("Get all orders items");
        logger.debug("Query: " + GET_ALL_ORDER_ITEMS_BY_ORDER_ID);

        for (Order order : orders) {

            List<OrderItem> orderItems = new ArrayList<>();

            try {
                Connection connection = DataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDER_ITEMS_BY_ORDER_ID);
                preparedStatement.setLong(1, order.getId());
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {

                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(rs.getInt(1));
                    Coffee coffee = new Coffee();
                    coffee.setName(rs.getString(2));
                    coffee.setPrice(rs.getBigDecimal(3));
                    orderItem.setCoffee(coffee);
                    orderItems.add(orderItem);

                }

                DataSource.returnConnection(connection);
                preparedStatement.close();

            } catch (SQLException e) {
                logger.debug("DB connection error: " + e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            order.setOrderItems(orderItems);
        }
        return orders;
    }

    /**
     * saving ste order
     *
     * @param order is order which will be saved
     */
    @Override
    public void save(Order order) {
        logger.debug("save order");
        logger.debug("get order future id");
        logger.debug("Query: " + GET_ORDER_NEXT_ID);

        try {
            Connection connection = DataSource.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet generatedKeys = stmt.executeQuery(GET_ORDER_NEXT_ID);

            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong(1));
            }

            DataSource.returnConnection(connection);
            stmt.close();
        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug("insert order");
        logger.debug("Query: " + INSERT_NEW_ORDER_QUERY);

        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_ORDER_QUERY);
            preparedStatement.setLong(1, order.getId());

            preparedStatement.setString(2, order.getUserName());
            preparedStatement.setString(3, order.getAddress());
            preparedStatement.setString(4, order.getPhoneNumber());
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, Integer.parseInt(String.valueOf(order.getPrice())));
            preparedStatement.executeUpdate();

            DataSource.returnConnection(connection);
            preparedStatement.close();
        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        logger.debug("get order item future id");
        logger.debug("Query: " + GET_ORDER_ITEM_NEXT_ID);
        for (OrderItem item : order.getOrderItems()) {
            try {
                Connection connection = DataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet generatedKeys = stmt.executeQuery(GET_ORDER_ITEM_NEXT_ID);
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                }

                DataSource.returnConnection(connection);
                stmt.close();
            } catch (SQLException e) {
                logger.debug("DB connection error: " + e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        logger.debug("insert order items");
        logger.debug("Query: " + INSERT_NEW_ORDER_ITEM_QUERY);
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_ORDER_ITEM_QUERY);

            for (OrderItem orderItem : order.getOrderItems()) {
                preparedStatement.setLong(1, orderItem.getId());
                preparedStatement.setLong(2, orderItem.getCoffee().getId());
                preparedStatement.setLong(3, order.getId());
                preparedStatement.setInt(4, orderItem.getQuantity());
                preparedStatement.executeUpdate();
            }

            DataSource.returnConnection(connection);
            preparedStatement.close();

        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * getting the order by id
     *
     * @param id of order
     * @return order
     */
    @Override
    public Order getOrder(Long id) {
        logger.debug("GET order with id: " + id);
        logger.debug("Query: " + GET_ORDER_BY_ID);
        Order order = new Order();
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_BY_ID);
            preparedStatement.setInt(1, Integer.parseInt(String.valueOf(id)));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                order.setId(rs.getLong(1));
                order.setUserName(rs.getString(2));
                order.setAddress(rs.getString(3));
                order.setPhoneNumber(rs.getString(4));
                order.setDelivery(rs.getInt(5));
                order.setPrice(rs.getBigDecimal(6));

            }
            DataSource.returnConnection(connection);
            preparedStatement.close();
        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDER_ITEMS_BY_ORDER_ID);
            preparedStatement.setLong(1, order.getId());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(rs.getInt(1));
                Coffee coffee = new Coffee();
                coffee.setName(rs.getString(2));
                coffee.setPrice(rs.getBigDecimal(3));
                orderItem.setCoffee(coffee);
                orderItems.add(orderItem);
            }

            DataSource.returnConnection(connection);
            preparedStatement.close();
        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        order.setOrderItems(orderItems);
        return order;
    }
}
