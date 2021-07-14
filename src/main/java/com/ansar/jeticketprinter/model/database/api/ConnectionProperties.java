package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.security.PasswordDecoder;

import javax.xml.bind.JAXB;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ConnectionProperties {

    private static final Logger logger = Logger.getLogger(ConnectionProperties.class.getName());

    private String address;
    private String port;
    private String databaseName;
    private String userName;
    private String password;
    private String anbar;

    public static class Builder{

        private String address;
        private String port;
        private String databaseName;
        private String userName;
        private String password;
        private String anbar;

        public Builder(){

        }

        public Builder address(String address){
            this.address = address;
            return this;
        }

        public Builder port(String port){
            this.port = port;
            return this;
        }
        public Builder databaseName(String databaseName){
            this.databaseName = databaseName;
            return this;
        }
        public Builder userName(String userName){
            this.userName = userName;
            return this;
        }
        public Builder password(String password){
            this.password = password;
            return this;
        }

        public Builder anbar(String anbar){
            this.anbar = anbar;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public String getPort() {
            return port;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public ConnectionProperties build(){
            return new ConnectionProperties(this);
        }

        public String getAnbar() {
            return anbar;
        }
    }


    private ConnectionProperties(Builder builder) {
        this.address = builder.getAddress();
        this.port = builder.getPort();
        this.databaseName = builder.getDatabaseName();
        this.userName = builder.getUserName();
        this.password = builder.getPassword();
        this.anbar = builder.getAnbar();
    }

    public ConnectionProperties() {
    }

    public static void serializeToXml(ConnectionProperties properties){

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("connection.xml"))) {

            properties.setPassword(PasswordDecoder.encode(properties.getPassword(), 5));

            JAXB.marshal(properties, writer);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static ConnectionProperties deserializeFromXml(){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("connection.xml"))){
            ConnectionProperties properties = JAXB.unmarshal(reader, ConnectionProperties.class);

            properties.setPassword(PasswordDecoder.decode(properties.getPassword(), 5));

            return properties;
        }catch (NoSuchFileException exception){

            logger.info("Connection file not found");
        } catch (IOException exception){
            exception.printStackTrace();
        }
        return new ConnectionProperties.Builder().address("").port("").databaseName("").userName("").password("").
                build();
    }

    public String getAddress() {
        if (address == null || address.equals(""))
            return "localhost";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        if (port == null || port.equals(""))
            return "1433";
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAnbar() {
        return anbar;
    }

    public void setAnbar(String anbar) {
        this.anbar = anbar;
    }


    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "address='" + address + '\'' +
                ", port='" + port + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}