package exchange.admin;

import exchange.dto.BuyStocksRequest;
import exchange.dto.UserStocksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class UserStocksController {
    private final ExchangeService exchangeService;

    @PostMapping(value = "/buy")
    public void buyStocks(@RequestBody BuyStocksRequest buySharesRequest) {
        exchangeService.buyStocks(buySharesRequest);
    }

    @GetMapping(value = "/{user_id}")
    public UserStocksResponse getUserStocks(@PathVariable("user_id") String userId) {
        return exchangeService.getUserStocks(userId);
    }
}
