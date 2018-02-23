package ru.mail.system.configs.listener.impl;

import ru.mail.system.configs.connectionPoolClasses.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Denis Monich
 * creating and setting updates to MySQL DB
 */
public class ScriptDAO {


    private static ScriptDAO instance = null;

    private ScriptDAO() {
        // Exists only to defeat instantiation.
    }

    static ScriptDAO getInstance() {
        if (instance == null) {
            instance = new ScriptDAO();
        }
        return instance;
    }

    /**
     * launch commands for prepare MySQL DB
     * @param commands to MySQL DB
     */
    void executeUserDBScript(List<String> commands) throws SQLException {
        try {
            Connection cn = DataSource.getConnection();
            Statement statement = cn.createStatement();
            for (String command : commands) {
                statement.executeUpdate(command);
            }
            DataSource.returnConnection(DataSource.getConnection());
            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
