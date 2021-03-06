package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.database.config.ConnectionFactory;
import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.pojo.IntervalProduct;
import com.ansar.jeticketprinter.model.pojo.SearchingType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class OpenedDatabaseApi implements IReadable {

    private static final Logger logger = Logger.getLogger(OpenedDatabaseApi.class.getName());

    private Connection connection;

    private PreparedStatement selectStatement;
    private PreparedStatement intervalSelectStatement;
    private PreparedStatement searchByNameStartingStatement;
    private PreparedStatement searchByNameMiddleStatement;
    private PreparedStatement searchByNameEndingStatement;

    private String anbar;

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

            intervalSelectStatement = connection.prepareStatement("select T.K_Code, T.Name1, T.Price_Consumer, T.Price_Forosh, T.A_Code, F.tarikh from \n" +
                    "(select k.name1 , A.A_Code , k.k_code, A.Price_Consumer, A.Price_Forosh from KalaId k join Anbar A on k.K_Code = A.K_Code) T join \n" +
                    "(select distinct k_code, A_Code , tarikh, ChangePrice from History_Price where tarikh between ? and ?) F on T.K_Code = F.K_Code and T.A_Code = ? and F.A_Code = T.A_Code and (F.ChangePrice = 1 or F.ChangePrice = 5);");
            intervalSelectStatement.setString(3, properties.getAnbar().trim());

            searchByNameStartingStatement = connection.prepareStatement("select ID1, K_Code_B, A_Code, Name1, Price_Consumer, Price_Forosh,Barcode, K_Qty1 from(\n" +
                    "select kalaid.K_Code as ID1 ,A_Code, Name1, Barcode, Price_Consumer, price_forosh from kalaid join Anbar on KalaId.K_Code = Anbar.K_Code\n" +
                    ") P\n" +
                    "join\n" +
                    "(select k_Code, '0' as K_Code_B, 1 as K_Qty1 from KalaId left join (select K_Code as ID ,'0' as k_code_B, 1 as K_Qty1 from TblBasket_Kala) P on K_Code = P.ID\n" +
                    "union all\n" +
                    "select K_Code as ID2,  K_Code_B, K_Qty1 from TblBasket_Kala) T\n" +
                    "on P.ID1 = T.K_Code where Name1 like ? and A_Code = ?");

            searchByNameStartingStatement.setString(2, properties.getAnbar().trim());

            searchByNameMiddleStatement = connection.prepareStatement("select ID1, K_Code_B, A_Code, Name1, Price_Consumer, Price_Forosh,Barcode, K_Qty1 from(\n" +
                    "select kalaid.K_Code as ID1 ,A_Code, Name1, Barcode, Price_Consumer, price_forosh from kalaid join Anbar on KalaId.K_Code = Anbar.K_Code\n" +
                    ") P\n" +
                    "join\n" +
                    "(select k_Code, '0' as K_Code_B, 1 as K_Qty1 from KalaId left join (select K_Code as ID ,'0' as k_code_B, 1 as K_Qty1 from TblBasket_Kala) P on K_Code = P.ID\n" +
                    "union all\n" +
                    "select K_Code as ID2,  K_Code_B, K_Qty1 from TblBasket_Kala) T\n" +
                    "on P.ID1 = T.K_Code where Name1 like ? and A_Code = ?; ");
            searchByNameMiddleStatement.setString(2, properties.getAnbar().trim());

            anbar = properties.getAnbar().trim();

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

    public List<IntervalProduct> getProductsManager(String fromDate, String toDate) throws SQLException {
        List<IntervalProduct> managers = new ArrayList<>();
        if (isOpened){

            intervalSelectStatement.setString(1, fromDate);
            intervalSelectStatement.setString(2, toDate);

            ResultSet resultSet = intervalSelectStatement.executeQuery();

            while (resultSet.next()){
                String name = (resultSet.getString("Name1") != null) ? resultSet.getString("Name1"): "null";
                String highPrice = (resultSet.getString("Price_Consumer") != null)? resultSet.getString("Price_Consumer"): "0";
                String lowPrice = resultSet.getString("Price_Forosh") != null ? resultSet.getString("Price_Forosh"): "0";
                String id = resultSet.getString("K_Code") != null ? resultSet.getString("K_Code"): "0";
                String date = resultSet.getString("tarikh");

                //String name, String priceConsumer, String priceForosh, String id, String date
                IntervalProduct product = new IntervalProduct(name, highPrice, lowPrice, id, date);
                managers.remove(product);
                managers.add(product);
            }

        }
        return managers;
    }

    public Set<ProductsManager> searchProductsByName(String name, SearchingType searchingType) throws SQLException {
        Set<ProductsManager> productsManagers = new HashSet<>();
        ResultSet resultSet = null;
        if (isOpened){
            String inserting = "";
            switch (searchingType){
                case START:
                    inserting = name + "%";
                    System.out.println(inserting);
                    searchByNameStartingStatement.setString(1, inserting);
                    resultSet = searchByNameStartingStatement.executeQuery();
                    break;
                case MIDDLE:
                    inserting = "%" + name + "%";
                    System.out.println(inserting);
                    searchByNameMiddleStatement.setString(1, inserting);
                    resultSet = searchByNameMiddleStatement.executeQuery();
                    break;
                case ALL:
                    resultSet = searchByNames(name);
                    break;
            }

            while (resultSet.next()){
                String productName = resultSet.getString("name1") == null ? "" : resultSet.getString("name1");
                String barcode = resultSet.getString("ID1") == null ? "" : resultSet.getString("ID1");
                String priceForosh = resultSet.getString("Price_Forosh") == null ? "0": resultSet.getString("Price_Forosh");
                String priceConsumer = resultSet.getString("Price_Consumer") == null ? "0":  resultSet.getString("Price_Consumer");
                String count = resultSet.getString("K_qty1") == null ? "1" : resultSet.getString("K_qty1");

                ProductsManager manager = new ProductsManager(barcode, productName, priceConsumer, priceForosh, count);
                productsManagers.add(manager);
            }

        }
        return productsManagers;
    }

    private ResultSet searchByNames(String names) throws SQLException {
        StringBuilder searchingQuery = new StringBuilder("select ID1, K_Code_B, A_Code, Name1, Price_Consumer, Price_Forosh,Barcode, K_Qty1 from(\n" +
                "select kalaid.K_Code as ID1 ,A_Code, Name1, Barcode, Price_Consumer, price_forosh from kalaid join Anbar on KalaId.K_Code = Anbar.K_Code\n" +
                ") P\n" +
                "join\n" +
                "(select k_Code, '0' as K_Code_B, 1 as K_Qty1 from KalaId left join (select K_Code as ID ,'0' as k_code_B, 1 as K_Qty1 from TblBasket_Kala) P on K_Code = P.ID\n" +
                "union all\n" +
                "select K_Code as ID2,  K_Code_B, K_Qty1 from TblBasket_Kala) T\n" +
                "on P.ID1 = T.K_Code where Name1 like '%");
        String[] arrNames = names.split(" ");
        for (String name: arrNames){
            searchingQuery.append(name).append("%");
        }
        searchingQuery.append("'").append("and A_code = ").append(anbar);

        return connection.prepareStatement(searchingQuery.toString()).executeQuery();
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

}
