package com.ansar.jeticketprinter.model.database.config;

import com.ansar.jeticketprinter.model.database.api.ConnectionProperties;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionConfig {

    private final ConnectionProperties properties = new ConnectionProperties.Builder().
            address("localhost").
            port("1433").
            userName("sepehr").
            password("s134s134").
            databaseName("1399").
            anbar("120").build();

    @Test
    public void testConnection() throws SQLException {

        Connection connection = ConnectionFactory.openConnection(properties);
        connection.close();

    }

}
