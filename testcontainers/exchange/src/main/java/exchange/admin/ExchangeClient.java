package exchange.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import exchange.config.ExchangeConfig;
import exchange.dto.CompanyResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExchangeClient {
    private final ExchangeConfig exchangeConfig;
    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @SneakyThrows
    public Optional<CompanyResponse> getCompany(String id) {
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(exchangeConfig.getExchangeServiceUri())
                        .resolve("/company")
                        .resolve("?company_id=%s".formatted(id)))
                .build();

        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.value()) {
            return Optional.of(objectMapper.readValue(response.body(), CompanyResponse.class));
        }

        return Optional.empty();
    }


    @SneakyThrows
    public Optional<CompanyResponse> buyStocks(String companyId, int count) {
        final HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(exchangeConfig.getExchangeServiceUri())
                        .resolve("/company")
                        .resolve("/%s".formatted(companyId))
                        .resolve("/buy")
                        .resolve("?count=%d".formatted(count)))
                .build();

        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.value()) {
            return Optional.of(objectMapper.readValue(response.body(), CompanyResponse.class));
        }

        return Optional.empty();
    }
}
