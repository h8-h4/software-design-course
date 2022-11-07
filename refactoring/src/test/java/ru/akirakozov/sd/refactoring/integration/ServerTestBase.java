package ru.akirakozov.sd.refactoring.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.akirakozov.sd.refactoring.config.ConfigProvider;
import ru.akirakozov.sd.refactoring.config.EndpointConfig;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.ProductDaoDb;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ServerTestBase {
    private static final String DATABASE_URL = ConfigProvider.dbConfig().url();
    private static final String DDL = """
            CREATE TABLE IF NOT EXISTS PRODUCT
            (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            NAME           TEXT    NOT NULL,
            PRICE          INT     NOT NULL);
            """;

    private static final String TRUNCATE = """
            DELETE FROM PRODUCT;
            """;

    protected static final EndpointConfig ENDPOINTS = ConfigProvider.endpointPathConfig();


    private Server server;

    @BeforeEach
    public void runApp() throws Exception {
        setUpDatabase();
        server = new Server(ConfigProvider.serverConfig().port());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductDao productDao = new ProductDaoDb();

        context.addServlet(new ServletHolder(new AddProductServlet(productDao)), ENDPOINTS.addProduct());
        context.addServlet(new ServletHolder(new GetProductsServlet(productDao)), ENDPOINTS.getProducts());
        context.addServlet(new ServletHolder(new QueryServlet(productDao)), ENDPOINTS.query());

        server.start();
    }

    @AfterEach
    public void stopApp() throws Exception {
        pruneDatabase();

        server.stop();
        server = null;
    }


    private void setUpDatabase() throws SQLException {
        executeUpdate(DDL);
    }

    private void pruneDatabase() throws SQLException {
        executeUpdate(TRUNCATE);
    }

    private void executeUpdate(String sqlUpdate) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(sqlUpdate);
        }
    }
}
