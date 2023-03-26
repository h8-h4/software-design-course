package exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import exchange.dto.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class IntegrationTest {
    private final static File JAR_DIR = new File(System.getProperty("distribution.dir"));
    private final static String ARCHIVE_NAME = System.getProperty("archive.name");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;

    @Container
    private GenericContainer container = new GenericContainer(buildImage())
            .withExposedPorts(7075)
            .waitingFor(Wait.forListeningPort()
                    .withStartupTimeout(Duration.ofMinutes(10)));

    private static ImageFromDockerfile buildImage() {
        return new ImageFromDockerfile()
                .withFileFromFile(ARCHIVE_NAME, new File(JAR_DIR, ARCHIVE_NAME))
                .withDockerfileFromBuilder(builder -> builder
                        .from("openjdk:17-alpine")
                        .copy(ARCHIVE_NAME, "/app/" + ARCHIVE_NAME)
                        .entryPoint("java", "-jar", "/app/" + ARCHIVE_NAME)
                        .expose(7075)
                        .build());
    }

    @BeforeEach
    public void init() {
        url = "http://%s:%d".formatted(container.getHost(), container.getFirstMappedPort());
    }


    @Test
    public void testCompany() {
        final CompanyResponse companyResponse = addCompany(
                AddCompanyRequest.builder()
                        .name("comp")
                        .stockCount(1000)
                        .stockPrice(3.0)
                        .build()
        );
        assertEquals("comp", companyResponse.getName());
        assertEquals(1000, companyResponse.getStockCount());
        assertEquals(3.0, companyResponse.getStockPrice(), 0.0001);


        final CompanyResponse company = getCompany(companyResponse.getId());

        assertEquals(companyResponse, company);
    }

    @Test
    public void testUpdatePrice() {
        final CompanyResponse company = addCompany(
                AddCompanyRequest.builder()
                        .name("comp")
                        .stockCount(1000)
                        .stockPrice(3.0)
                        .build()
        );


        final CompanyResponse updated = changeStockPrice(company.getId(), 1000.5);
        assertEquals(1000.5, updated.getStockPrice(), 0.001);
        assertEquals("comp", updated.getName());
        assertEquals(1000, updated.getStockCount());

        final CompanyResponse getUpdated = getCompany(updated.getId());

        assertEquals(updated, getUpdated);
    }


    @Test
    public void testRegisterUser() {
        final UserResponse user = registerUser(new RegisterUserRequest(500));

        assertEquals(500, user.getBalance());
        assertEquals(500, getBalance(user.getId()));

        assertTrue(getUserStocks(user.getId()).getStocks().isEmpty());
    }

    @Test
    public void testAddBalance() {
        final UserResponse user = registerUser(new RegisterUserRequest(500));
        assertEquals(500, user.getBalance());

        final UserResponse updated = addBalance(user.getId(), 2000);

        assertEquals(2500, updated.getBalance());
        assertEquals(2500, getBalance(user.getId()));
    }

    @Test
    public void testBuyStocks() {
        final UserResponse user = registerUser(new RegisterUserRequest(1000));
        final CompanyResponse company = addCompany(
                AddCompanyRequest.builder()
                        .name("yandex")
                        .stockCount(1000)
                        .stockPrice(5.0)
                        .build()
        );

        buyStocks(new BuyStocksRequest(user.getId(), company.getId(), 100));

        assertEquals(500, getBalance(user.getId()));
        assertEquals(900, getCompany(company.getId()).getStockCount());
        assertEquals(
                List.of(new StockResponse(company.getId(), 100, 5.0)),
                getUserStocks(user.getId()).getStocks()
        );
    }


    @SneakyThrows
    private CompanyResponse addCompany(AddCompanyRequest request) {
        HttpResponse<String> response = post("/exchange/company", request);

        assertEquals(200, response.statusCode());

        return objectMapper.readValue(response.body(), CompanyResponse.class);
    }

    @SneakyThrows
    private CompanyResponse getCompany(String companyId) {
        HttpResponse<String> response = get("/exchange/company?company_id=%s".formatted(companyId));

        assertEquals(200, response.statusCode());

        return objectMapper.readValue(response.body(), CompanyResponse.class);
    }

    @SneakyThrows
    private CompanyResponse changeStockPrice(String companyId, double price) {
        HttpResponse<String> response = put("/exchange/company/%s/stocks?price=%f".formatted(companyId, price));

        assertEquals(200, response.statusCode());

        return objectMapper.readValue(response.body(), CompanyResponse.class);
    }

    @SneakyThrows
    private UserResponse registerUser(RegisterUserRequest request) {
        HttpResponse<String> response = post("/users/register", request);

        assertEquals(200, response.statusCode());

        return objectMapper.readValue(response.body(), UserResponse.class);
    }

    @SneakyThrows
    private UserResponse addBalance(String userId, double balance) {
        HttpResponse<String> response = post("/users/%s/balance?balance=%f".formatted(userId, balance));

        assertEquals(200, response.statusCode());

        return objectMapper.readValue(response.body(), UserResponse.class);
    }

    @SneakyThrows
    private double getBalance(String userId) {
        HttpResponse<String> response = get("/users/%s/balance".formatted(userId));

        assertEquals(200, response.statusCode());

        return Double.parseDouble(response.body());
    }

    @SneakyThrows
    private UserStocksResponse getUserStocks(String userId) {
        HttpResponse<String> response = get("/stocks/%s".formatted(userId));

        assertEquals(200, response.statusCode());

        return objectMapper.readValue(response.body(), UserStocksResponse.class);
    }

    @SneakyThrows
    private void buyStocks(BuyStocksRequest request) {
        HttpResponse<String> response = post("/stocks/buy", request);
        assertEquals(200, response.statusCode());
    }

    @SneakyThrows
    private HttpResponse<String> post(String endpoint, Object body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url + endpoint))
                .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(body)))
                .header("Content-type", "application/json")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @SneakyThrows
    private HttpResponse<String> post(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url + endpoint))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    @SneakyThrows
    private HttpResponse<String> put(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url + endpoint))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @SneakyThrows
    private HttpResponse<String> get(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url + endpoint))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
