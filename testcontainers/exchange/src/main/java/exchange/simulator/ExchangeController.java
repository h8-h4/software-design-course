package exchange.simulator;

import exchange.dto.AddCompanyRequest;
import exchange.dto.CompanyResponse;
import exchange.model.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ExchangeController {
    private final CompanyService companyService;

    @PostMapping(value = "/company")
    public CompanyResponse addCompany(@RequestBody AddCompanyRequest company) {
        final Company saved = companyService.addCompany(company);

        return toResponse(saved);
    }

    @GetMapping(value = "/company")
    public CompanyResponse getCompany(@RequestParam("company_id") String companyId) {
        final Company company = companyService.getCompany(companyId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return toResponse(company);
    }

    @PutMapping(value = "company/{company_id}/stocks")
    public CompanyResponse changeStockPrice(
            @PathVariable("company_id") String companyId,
            @RequestParam("price") double newPrice
    ) {
        if (newPrice <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final Company company = companyService.changeStocksPrice(companyId, newPrice).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        return toResponse(company);
    }

    @PutMapping(value = "company/{company_id}/stocks/buy")
    public CompanyResponse buyStocks(
            @PathVariable("company_id") String companyId,
            @RequestParam("count") int count
    ) {
        if (count <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final Company company = companyService.buyStocks(companyId, count).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        return toResponse(company);
    }

    private CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .stockCount(company.getStockCount())
                .stockPrice(company.getStockPrice())
                .build();
    }
}
