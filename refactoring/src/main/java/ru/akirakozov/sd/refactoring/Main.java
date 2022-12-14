package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.config.ConfigProvider;
import ru.akirakozov.sd.refactoring.dao.DatabaseConnector;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.ProductDaoDb;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    private static final String DDL = """
            CREATE TABLE IF NOT EXISTS PRODUCT
            (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            NAME           TEXT    NOT NULL,
            PRICE          INT     NOT NULL)""";

    public static void main(String[] args) throws Exception {
        try (
                Connection c = DatabaseConnector.newConnection();
                Statement stmt = c.createStatement()
        ) {
            stmt.executeUpdate(DDL);
        }

        Server server = new Server(ConfigProvider.serverConfig().port());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductDao productDao = new ProductDaoDb();

        context.addServlet(new ServletHolder(new AddProductServlet(productDao)),
                ConfigProvider.endpointPathConfig().addProduct());
        context.addServlet(new ServletHolder(new GetProductsServlet(productDao)),
                ConfigProvider.endpointPathConfig().getProducts());
        context.addServlet(new ServletHolder(new QueryServlet(productDao)),
                ConfigProvider.endpointPathConfig().query());

        server.start();
        server.join();
    }
}
