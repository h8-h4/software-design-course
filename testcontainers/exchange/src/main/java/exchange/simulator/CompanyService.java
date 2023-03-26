package exchange.simulator;

import exchange.dto.AddCompanyRequest;
import exchange.model.Company;
import exchange.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company addCompany(AddCompanyRequest company) {
        return companyRepository.save(
                Company.builder()
                        .id(UUID.randomUUID().toString())
                        .name(company.getName())
                        .stockCount(company.getStockCount())
                        .stockPrice(company.getStockPrice())
                        .build()
        );
    }

    public Optional<Company> getCompany(String id) {
        return companyRepository.findById(id);
    }

    public Optional<Company> changeStocksPrice(String companyId, double newPrice) {
        return companyRepository.findById(companyId)
                .map(company -> companyRepository.save(company.withStockPrice(newPrice)));
    }


    public Optional<Company> buyStocks(String companyId, int count) {
        return companyRepository.findById(companyId)
                .map(company -> {
                    final int stockCount = company.getStockCount();
                    if (stockCount < count) {
                        return null;
                    }
                    return companyRepository.save(company.withStockCount(stockCount - count));
                });
    }
}
