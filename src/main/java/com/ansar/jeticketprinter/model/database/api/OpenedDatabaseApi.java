package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.database.config.ConnectionFactory;
import com.ansar.jeticketprinter.model.entity.ConnectionProperties;
import com.ansar.jeticketprinter.model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OpenedDatabaseApi {

    private Connection connection;
    private PreparedStatement statement;
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
            statement = connection.prepareStatement("select ID1, K_Code_B, A_Code, Name1, Price_Consumer, Price_Forosh,Barcode, K_Qty1 from(\n" +
                    "select kalaid.K_Code as ID1 ,A_Code, Name1, Barcode, Price_Consumer, price_forosh from kalaid join Anbar on KalaId.K_Code = Anbar.K_Code\n" +
                    ") P\n" +
                    "join\n" +
                    "(select k_Code, '0' as K_Code_B, 1 as K_Qty1 from KalaId left join (select K_Code as ID ,'0' as k_code_B, 1 as K_Qty1 from TblBasket_Kala) P on K_Code = P.ID\n" +
                    "union all\n" +
                    "select K_Code as ID2,  K_Code_B, K_Qty1 from TblBasket_Kala) T\n" +
                    "on P.ID1 = T.K_Code where (ID1 = ? or K_Code_B = ? or Barcode = ?) and A_Code = ?;");

            statement.setString(4, properties.getAnbar().trim());
            isOpened = true;
        }
    }

    public Product getProductById(String barcode) throws SQLException {
        // If connection is opened
        if (isOpened){
            statement.setString(1, barcode);
            statement.setString(2, barcode);
            statement.setString(3, barcode);

            return new Product("se", "", "", "", "");
        }
        throw new SQLException("Cant execute query: connection is closed");
    }

    public void closeConnection() throws SQLException {
        // If connection is opened
        if (isOpened){
            connection.close();
            isOpened = false;
        }
    }

}
