package com.ansar.jeticketprinter.model.gsontraining;

import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;

public class TestObject {
    private ConnectionProperties connectionProperties;

    public TestObject(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public TestObject() {
        this.connectionProperties = new ConnectionProperties.Builder().build();
    }

    public ConnectionProperties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}
