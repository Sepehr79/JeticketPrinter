package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.database.config.ConnectionFactory;
import com.ansar.jeticketprinter.model.entity.ConnectionProperties;
import com.ansar.jeticketprinter.model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OpenedDatabaseApi {

    private Connection connection;

    private PreparedStatement selectStatement;
    private PreparedStatement updateNameStatement;
    private PreparedStatement updatePriceForoshStatement;
    private PreparedStatement updatePriceConsumerStatement;

    private boolean isOpened = false;

    private static final OpenedDatabaseApi api = new OpenedDatabaseApi();

    private OpenedDatabaseApi(){

    }

    public static  OpenedDatabaseApi getInstance(){
        return api;
    }

    public void openConnection(ConnectionProperties properties) throws SQLException {
        // If connection is closed
        if (!isOpened){
            // Open connection with prepared statement
            connection = ConnectionFactory.openConnection(properties);

            selectStatement = connection.prepareStatement("select ID1, K_Code_B, A_Code, Name1, Price_Consumer, Price_Forosh,Barcode, K_Qty1 from(\n" +
                    "select kalaid.K_Code as ID1 ,A_Code, Name1, Barcode, Price_Consumer, price_forosh from kalaid join Anbar on KalaId.K_Code = Anbar.K_Code\n" +
                    ") P\n" +
                    "join\n" +
                    "(select k_Code, '0' as K_Code_B, 1 as K_Qty1 from KalaId left join (select K_Code as ID ,'0' as k_code_B, 1 as K_Qty1 from TblBasket_Kala) P on K_Code = P.ID\n" +
                    "union all\n" +
                    "select K_Code as ID2,  K_Code_B, K_Qty1 from TblBasket_Kala) T\n" +
                    "on P.ID1 = T.K_Code where (ID1 = ? or K_Code_B = ? or Barcode = ?) and A_Code = ?;");
            selectStatement.setString(4, properties.getAnbar().trim());

            updatePriceForoshStatement = connection.prepareStatement("update Anbar set Price_Forosh = ? where K_Code = ? and A_Code = ?");
            updatePriceForoshStatement.setString(3, properties.getAnbar());

            updatePriceConsumerStatement = connection.prepareStatement("update anbar set Price_Consumer = ? where K_Code = ? and A_Code = ?");
            updatePriceConsumerStatement.setString(3, properties.getAnbar());

            updateNameStatement = connection.prepareStatement("update kalaid set name1 = ? where K_Code = ?");

            isOpened = true;
        }
    }

    public Set<Product> getProductsById(String ...barcodes) throws SQLException {
        // If connection is opened
        if (isOpened){
            Set<Product> products = new HashSet<>();

            for (String barcode : barcodes) {
                selectStatement.setString(1, barcode);
                selectStatement.setString(2, barcode);
                selectStatement.setString(3, barcode);

                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()){
                    String name = resultSet.getString("Name1");
                    String highPrice = resultSet.getString("Price_Consumer");
                    String lowPrice = resultSet.getString("Price_Forosh");
                    String count = resultSet.getString("K_Qty1");
                    String id = resultSet.getString("ID1");

                    Product product = new Product(id, name, highPrice, lowPrice, count);
                    products.add(product);
                }
            }
            return products;
        }
        throw new SQLException("Cant execute query: connection is closed");
    }

    public int updateName(String newName, String id) throws SQLException {
        if (isOpened){
            updateNameStatement.setString(1, newName);
            updateNameStatement.setString(2, id);

            return updateNameStatement.executeUpdate();
        }
        return 0;
    }

    public int updatePriceForosh(String newPrice, String id) throws SQLException {
        if (isOpened){
            updatePriceForoshStatement.setString(1, newPrice);
            updatePriceForoshStatement.setString(2, id);

            return updatePriceForoshStatement.executeUpdate();
        }
        return 0;
    }

    public int updatePriceConsumer(String newPrice, String id) throws SQLException {
        if (isOpened){
            updatePriceConsumerStatement.setString(1, newPrice);
            updatePriceConsumerStatement.setString(2, id);

            return updatePriceConsumerStatement.executeUpdate();
        }
        return 0;
    }

    public void closeConnection() throws SQLException {
        // If connection is opened
        if (isOpened){
            connection.close();
            isOpened = false;
        }
    }

    public PreparedStatement getUpdateNameStatement() {
        return updateNameStatement;
    }
}
