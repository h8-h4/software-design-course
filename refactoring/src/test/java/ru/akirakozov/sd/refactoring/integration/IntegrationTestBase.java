package ru.akirakozov.sd.refactoring.integration;

import org.eclipse.jetty.server.Response;
import org.junit.jupiter.api.Assertions;
import ru.akirakozov.sd.refactoring.config.ConfigProvider;
import ru.akirakozov.sd.refactoring.model.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class IntegrationTestBase extends ServerTestBase {
    private static final String SERVER_URL = "http://localhost:%d".formatted(ConfigProvider.serverConfig().port());
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    protected String executeRequest(String path, Map<String, String> params) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(buildUri(path, params))
                    .timeout(TIMEOUT)
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(Response.SC_OK, response.statusCode());
            Assertions.assertFalse(response.body().isBlank());

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String addProduct(Product product) {
        return executeRequest(ENDPOINTS.addProduct(), Map.of(
                "name", product.getName(),
                "price", String.valueOf(product.getPrice())
        ));
    }

    protected String getProducts() {
        return executeRequest(ENDPOINTS.getProducts(), Collections.emptyMap());
    }

    protected String query(String op) {
        return executeRequest(ENDPOINTS.query(), Map.of("command", op));
    }

    protected static List<Product> extractProducts(String html, Pattern pattern) {
        ArrayList<Product> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            result.add(new Product(matcher.group(1), Integer.parseInt(matcher.group(2))));
        }

        return result;
    }

    protected static List<Long> extractPrices(String html) {
        ArrayList<Long> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+").matcher(html);

        while (matcher.find()) {
            result.add(Long.parseLong(matcher.group(0)));
        }

        return result;
    }

    private static URI buildUri(String path, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(SERVER_URL).append(path);

        String paramsString = params.entrySet()
                .stream()
                .map(entry -> "%s=%s".formatted(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&", "?", ""));

        return URI.create(builder.append(paramsString).toString());
    }
}
