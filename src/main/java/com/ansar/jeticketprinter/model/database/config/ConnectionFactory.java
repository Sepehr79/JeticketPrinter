package com.ansar.jeticketprinter.model.database.config;

import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionFactory  {

    // Logger for see results and debugging
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());

    private final static BasicDataSource basicDataSource = new BasicDataSource();

    /**
     * Constructor
     */
    private ConnectionFactory() {

    }

    public static Connection openConnection(ConnectionProperties properties) throws SQLException {
        if (isAvailable(properties.getAddress(), Integer.parseInt(properties.getPort().trim()))){
            // Create suitable input string
            String connectionString =  "jdbc:sqlserver://" + properties.getAddress().trim() + ":"
                    + properties.getPort().trim() + "; databaseName=" + properties.getDatabaseName().trim() + ";";

            logger.info("Database url: " + connectionString);

            basicDataSource.setUrl(connectionString);
            basicDataSource.setUsername(properties.getUserName());
            basicDataSource.setPassword(properties.getPassword());
            basicDataSource.setMinIdle(2);
            basicDataSource.setMaxIdle(4);
            basicDataSource.setMaxOpenPreparedStatements(10);

            return basicDataSource.getConnection();
        }
        throw new SQLException("Connection is unavailable");
    }

    private static boolean isAvailable(String host ,int port) {
        try (Socket ignored = new Socket(host, port)) {
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

}
