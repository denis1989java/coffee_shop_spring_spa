package ru.mail.system.configs.connectionPoolClasses;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author Denis Monich
 * class with methods to get jdbs connection
 */
public class DataSource {

    static JdbcConnectionPool pool = new JdbcConnectionPool();

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = pool.getConnectionFromPool();
        return connection;
    }

    public static void returnConnection(Connection connection) {
        pool.returnConnectionToPool(connection);
    }
}
