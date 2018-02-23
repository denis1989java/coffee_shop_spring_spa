package ru.mail.system.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mail.coffee.service.CoffeeDAO;
import ru.mail.order.service.OrderDAO;

import java.util.ResourceBundle;


@Service
public class DaoFactory {


    private static final Logger logger = Logger.getLogger(DaoFactory.class);
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("root");
    private String DBType = resourceBundle.getString("method.of.orders.saving");


    private static final DaoFactory instance = new DaoFactory();

    public static DaoFactory getInstance() {
        return instance;
    }

    @Autowired
    @Qualifier("coffeeDAOFileImpl")
    private CoffeeDAO coffeeDaoFile;

    @Autowired
    @Qualifier("orderDAOFileImpl")
    private OrderDAO orderDaoFile;
    @Autowired
    @Qualifier("coffeeDAOMySQLImpl")
    private CoffeeDAO coffeeDaoMySQL;

    @Autowired
    @Qualifier("orderDAOMySQLImpl")
    private OrderDAO orderDaoMySql;

    @Autowired
    @Qualifier("coffeeDAOOracleImpl")
    private CoffeeDAO coffeeDaoOracle;

    @Autowired
    @Qualifier("orderDAOOracleImpl")
    private OrderDAO orderDaoOracle;


    public CoffeeDAO getCoffeeDao() {
        if ("file".equals(DBType)) {
            logger.debug("Work with file");
            return coffeeDaoFile;
        } else if ("mysql".equals(DBType)) {
            logger.debug("Work with MySQL DB");
            return coffeeDaoMySQL;
        } else if ("oracle".equals(DBType)) {
            logger.debug("Work with Oracle DB");
            return coffeeDaoOracle;
        } else {
            logger.debug("wrong method");
            return null;
        }
    }

    public OrderDAO getOrderDao() {
        if ("file".equals(DBType)) {
            logger.debug("Work with file");
            return orderDaoFile;
        } else if ("mysql".equals(DBType)) {
            logger.debug("Work with MySQL DB");
            return orderDaoMySql;
        } else if ("oracle".equals(DBType)) {
            logger.debug("Work with Oracle DB");
            return orderDaoOracle;
        } else {
            logger.debug("wrong method");
            return null;
        }
    }
}
