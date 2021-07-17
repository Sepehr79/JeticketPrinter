package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.entity.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Set;

public class OpenedDatabaseApiTest {

    private final ConnectionProperties properties = new ConnectionProperties.Builder().
            address("localhost").
            port("1433").
            userName("sepehr").
            password("s134s134").
            databaseName("1399").
            anbar("110").build();

    @Test
    public void testGetProduct() throws SQLException {

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        api.openConnection(properties);

        Set<ProductsManager> managers = api.getProductsById("6260001601000");

        Assert.assertEquals(managers.size(), 1);

        System.out.println(managers);

        api.closeConnection();

    }

    @Test
    public void testGetProducts() throws SQLException {
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        api.openConnection(properties);

        Set<ProductsManager> managers = api.getProductsById("6260009100185",
                "6260009100314",
                "6260009100321",
                "6260009100482",
                "6260009100505",
                "6260009100512"
        );

        Assert.assertEquals(managers.size(), 6);

        for (ProductsManager manager:managers){
            System.out.println(manager);
        }

        api.closeConnection();
    }

    @Test
    public void testId() throws SQLException {
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        api.openConnection(properties);

        Set<ProductsManager> products = api.getProductsById("6261715203252");

        System.out.println(products);

        Assert.assertEquals(products.size(), 1);

        api.closeConnection();
    }

    /*
    @Test
    public void testChangeName() throws SQLException {
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        api.openConnection(properties);

        System.out.println(api.update(OpenedDatabaseApi.UPDATE_NAME ,"اسکاچ سوپر", "1122339003022"));

        api.closeConnection();
    }

    @Test
    public void testUpdatePriceForosh() throws SQLException {
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        api.openConnection(properties);

        System.out.println(api.update(OpenedDatabaseApi.UPDATE_PRICE_FOROSH ,"44000", "1122339003022"));

        api.closeConnection();
    }

    @Test
    public void testUpdatePriceConsumer() throws SQLException {
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        api.openConnection(properties);

        System.out.println(api.update(OpenedDatabaseApi.UPDATE_PRICE_CONSUMER ,"50000", "1122339003022"));

        api.closeConnection();
    }
    */
}
