package exchange.admin;

import exchange.dto.BuyStocksRequest;
import exchange.dto.CompanyResponse;
import exchange.dto.StockResponse;
import exchange.dto.UserStocksResponse;
import exchange.model.User;
import exchange.model.UserStock;
import exchange.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {
    private final UserRepository userRepository;

    private final ExchangeClient exchangeClient;


    public void buyStocks(BuyStocksRequest request) {
        final User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        final CompanyResponse company = exchangeClient.getCompany(request.getCompanyId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (user.getBalance() < request.getAmount() * company.getStockPrice()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (company.getStockCount() < request.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        exchangeClient.buyStocks(company.getId(), request.getAmount());

        userRepository.save(
                user
                        .withBalance(user.getBalance() - request.getAmount() * company.getStockCount())
                        .withStocks(
                                List.of(
                                        new UserStock(request.getCompanyId(),
                                                user,
                                                request.getAmount(),
                                                company.getStockPrice())
                                )
                        )
        );
    }

    public UserStocksResponse getUserStocks(String userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );


        return new UserStocksResponse(
                userId,
                user.getStocks()
                        .stream()
                        .map(s -> new StockResponse(s.getCompanyId(), s.getAmount(), s.getPrice()))
                        .toList()
        );
    }
}
