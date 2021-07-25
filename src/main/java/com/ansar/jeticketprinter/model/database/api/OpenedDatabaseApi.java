package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.database.config.ConnectionFactory;
import com.ansar.jeticketprinter.model.dto.DateTimeProperties;
import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.github.mfathi91.time.PersianDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class OpenedDatabaseApi implements IReadable {

    private static final Logger logger = Logger.getLogger(OpenedDatabaseApi.class.getName());

//    public static final Integer UPDATE_NAME = 1;
//    public static final Integer UPDATE_PRICE_FOROSH = 2;
//    public static final Integer UPDATE_PRICE_CONSUMER = 3;

    private Connection connection;

    private PreparedStatement selectStatement;
    private PreparedStatement intervalSelectStatement;
//    private PreparedStatement updateNameStatement;
//    private PreparedStatement updatePriceForoshStatement;
//    private PreparedStatement updatePriceConsumerStatement;

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
                    "on P.ID1 = T.K_Code where ((ID1 = ? and K_Code_B = '0') or K_Code_B = ? or Barcode = ?) and A_Code = ?;");
            selectStatement.setString(4, properties.getAnbar().trim());

            intervalSelectStatement = connection.prepareStatement("select K.Name1 ,H.A_Code, H.K_Code, H.Price_Consumer, H.Price_Forosh, H.Price_Finish, H.ChangePrice, H.tarikh from History_Price H join KalaId K on H.K_Code = K.K_Code where\n" +
                    " ((YEAR(H.tarikh) >= YEAR(?) and YEAR(H.tarikh) <= YEAR(?)) and\n" +
                    " (MONTH(H.tarikh) >= MONTH(?) and MONTH(H.tarikh) <= MONTH(?)) and\n" +
                    " (DAY(H.tarikh) >= DAY(?) and DAY(H.tarikh) <= DAY(?)) and \n" +
                    " (DATEPART(HOUR, H.tarikh) >= DATEPART(HOUR, ?) and DATEPART(HOUR, H.tarikh) <= DATEPART(HOUR, ?)) and\n" +
                    " (DATEPART(MINUTE, H.tarikh) >= DATEPART(MINUTE, ?) and DATEPART(MINUTE, H.tarikh) <= DATEPART(MINUTE, ?))) and H.A_Code = ?;");
            intervalSelectStatement.setString(11, properties.getAnbar().trim());

            //updatePriceForoshStatement = connection.prepareStatement("update Anbar set Price_Forosh = ? where K_Code = ? and A_Code = ?");
            //updatePriceForoshStatement.setString(3, properties.getAnbar());

            //updatePriceConsumerStatement = connection.prepareStatement("update anbar set Price_Consumer = ? where K_Code = ? and A_Code = ?");
            //updatePriceConsumerStatement.setString(3, properties.getAnbar());

            //updateNameStatement = connection.prepareStatement("update kalaid set name1 = ? where K_Code = ?");

            isOpened = true;
        }
    }

    @Override
    public Set<ProductsManager> getProductsById(String ...barcodes) throws SQLException {
        // If connection is opened
        if (isOpened){
            Set<ProductsManager> managers = new HashSet<>();

            for (String barcode : barcodes) {
                selectStatement.setString(1, barcode);
                selectStatement.setString(2, barcode);
                selectStatement.setString(3, barcode);

                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()){
                    String name = (resultSet.getString("Name1") == null) ? " " : (resultSet.getString("Name1"));
                    String highPrice = (resultSet.getString("Price_Consumer") == null) ? "0" :  (resultSet.getString("Price_Consumer"));
                    String lowPrice = (resultSet.getString("Price_Forosh") == null) ? "0" :  (resultSet.getString("Price_Forosh"));
                    String count = (resultSet.getString("K_Qty1") == null) ? "1" :   (resultSet.getString("K_Qty1"));

                    String id = "";
                    if (!resultSet.getString("Barcode").equals(""))
                        id = resultSet.getString("Barcode");
                    else if (!resultSet.getString("K_Code_B").equals("0"))
                        id = resultSet.getString("K_Code_B");
                    else
                        id = resultSet.getString("ID1");

                    ProductsManager manager = new ProductsManager(id, name, highPrice, lowPrice, count);
                    managers.add(manager);
                }
            }
            return managers;
        }
        throw new SQLException("Cant execute query: connection is closed");
    }

    public List<ProductsManager> getProductsManager(DateTimeProperties properties) throws SQLException {
        List<ProductsManager> managers = new ArrayList<>();
        if (isOpened){
            logger.info("From date: " + properties.getFromDate());
            logger.info("To date:" + properties.getToDate());
            logger.info("From time:" + properties.getFromTime());
            logger.info("To time:" + properties.getToTime());

            intervalSelectStatement.setString(1, properties.getFromDate());
            intervalSelectStatement.setString(2, properties.getToDate());
            intervalSelectStatement.setString(3, properties.getFromDate());
            intervalSelectStatement.setString(4, properties.getToDate());
            intervalSelectStatement.setString(5, properties.getFromDate());
            intervalSelectStatement.setString(6, properties.getToDate());
            intervalSelectStatement.setString(7, properties.getFromTime());
            intervalSelectStatement.setString(8, properties.getToTime());
            intervalSelectStatement.setString(9, properties.getFromTime());
            intervalSelectStatement.setString(10, properties.getToTime());

            logger.info(intervalSelectStatement.toString());

            ResultSet resultSet = intervalSelectStatement.executeQuery();

            while (resultSet.next()){
                String name = (resultSet.getString("Name1") != null) ? resultSet.getString("Name1"): "null";
                String highPrice = (resultSet.getString("Price_Consumer") != null)? resultSet.getString("Price_Consumer"): "0";
                String lowPrice = resultSet.getString("Price_Forosh") != null ? resultSet.getString("Price_Forosh"): "0";
                String id = resultSet.getString("K_Code") != null ? resultSet.getString("K_Code"): "0";

                ProductsManager manager = new ProductsManager(id, name, highPrice, lowPrice, "1");
                managers.add(manager);
            }

        }
        logger.info("===================>" + managers.size());
        for (ProductsManager manager: managers){
            logger.info("manager: " + manager.getName());
            logger.info("manager: " + manager.getBarcode());
        }

        return managers;
    }

    public void closeConnection(){
        // If connection is opened
        if (isOpened){
            try {
                selectStatement.close();
                connection.close();
                isOpened = false;
            } catch (SQLException exception) {
                logger.info("Exception on close connection");
                exception.printStackTrace();
            }
        }
    }


    /*
    @Deprecated
    private Integer updateName(String newName, String id) throws SQLException {
        if (isOpened){
            updateNameStatement.setString(1, newName);
            updateNameStatement.setString(2, id);

            return updateNameStatement.executeUpdate();
        }
        return 0;
    }

    @Deprecated
    private int updatePriceForosh(String newPrice, String id) throws SQLException {
        if (isOpened){
            updatePriceForoshStatement.setString(1, newPrice);
            updatePriceForoshStatement.setString(2, id);

            return updatePriceForoshStatement.executeUpdate();
        }
        return 0;
    }

    @Deprecated
    private int updatePriceConsumer(String newPrice, String id) throws SQLException {
        if (isOpened){
            updatePriceConsumerStatement.setString(1, newPrice);
            updatePriceConsumerStatement.setString(2, id);

            return updatePriceConsumerStatement.executeUpdate();
        }
        return 0;
    }

    @Override
    @Deprecated
    public Integer update(Integer updateType, String newValue, String id) throws SQLException {
        switch (updateType){
            case 1:
                return updateName(newValue, id);
            case 2:
                return updatePriceForosh(newValue, id);
            case 3:
                return updatePriceConsumer(newValue, id);
        }
        return 0;
    }


     */


}
