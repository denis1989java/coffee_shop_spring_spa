package ru.mail.system.configs.connectionPoolClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Denis Monich
 * class for work with connection pool
 */
public class JdbcConnectionPool {
    private List<Connection> availableConnections = new ArrayList<Connection>();

    JdbcConnectionPool() {
        initializeConnectionPool();
    }

    private void initializeConnectionPool() {
        while (!checkIfConnectionPoolIsFull()) {
            availableConnections.add(createNewConnectionForPool());
        }
    }

    private synchronized boolean checkIfConnectionPoolIsFull() {
        final int MAX_POOL_SIZE = Configuration.getInstance().DB_MAX_CONNECTIONS;

        if (availableConnections.size() < MAX_POOL_SIZE) {
            return false;
        }

        return true;
    }

    //Creating a connection
    private Connection createNewConnectionForPool() {
        Configuration config = Configuration.getInstance();

        try {
            Class.forName(config.DB_DRIVER);
            return (Connection) DriverManager.getConnection(
                    config.DB_URL, config.PROPERTIES);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    synchronized Connection getConnectionFromPool() {

        Connection connection = null;
        if (availableConnections.size() > 0) {
            connection = (Connection) availableConnections.get(0);
            availableConnections.remove(0);
        }
        return connection;
    }

    synchronized void returnConnectionToPool(Connection connection) {
        availableConnections.add(connection);
    }
}

