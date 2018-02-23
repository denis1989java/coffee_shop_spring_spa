package ru.mail.coffee.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.mail.coffee.service.CoffeeDAO;
import ru.mail.system.configs.connectionPoolClasses.DataSource;
import ru.mail.coffee.model.Coffee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Monich
 * this class realise all logic to get list of existing coffee fromOracle DB
 */

@Service("coffeeDAOOracleImpl")
public class CoffeeDAOOracleImpl implements CoffeeDAO {

    private static final Logger logger = Logger.getLogger(CoffeeDAOOracleImpl.class);
    private static final String GET_ALL_COFFEES_QUERY = "SELECT * FROM Coffee";


    /**
     * getting all coffees
     *
     * @return list of coffees
     */
    @Override
    public List<Coffee> list() {
        logger.debug("Get all coffees");
        logger.debug("Query: " + GET_ALL_COFFEES_QUERY);
        List<Coffee> coffees = new ArrayList<>();

        try {
            Connection connection = DataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_COFFEES_QUERY);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                //create new coffee
                Coffee coffee = new Coffee();
                coffee.setId(rs.getLong(1));
                coffee.setName(rs.getString(2));
                coffee.setDescription(rs.getString(3));
                coffee.setPrice(rs.getBigDecimal(4));
                //add coffee to list
                coffees.add(coffee);
            }

            DataSource.returnConnection(connection);
            preparedStatement.close();

        } catch (SQLException e) {
            logger.debug("DB connection error: " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return coffees;
    }

}
