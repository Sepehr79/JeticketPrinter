package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.pojo.IntervalProduct;
import com.ansar.jeticketprinter.model.pojo.SearchingType;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
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

    @Test
    public void testGetIntervalTimeProducts() throws SQLException {

        ConnectionProperties properties = ConnectionProperties.deserializeFromXml();
        properties.setAnbar("110");

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        api.openConnection(properties);

        List<IntervalProduct> managers = api.getProductsManager("2011-8-20 20:59", "2021-8-20 00:59");

        System.out.println(managers.size());
    }

    @Test
    public void testSearchByName() throws SQLException {
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        api.openConnection(properties);

        Set<ProductsManager> managers = api.searchProductsByName("?? ??", SearchingType.ALL);

        System.out.println(managers.size());
        api.closeConnection();
    }
}
