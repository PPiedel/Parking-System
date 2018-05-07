package pl.yahoo.pawelpiedel.Parking.service.payment;

import org.springframework.stereotype.Service;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.payment.CurrencyType;

@Service
public interface PaymentService {
    Parking assignPaymentInGivenCurrency(Parking parking, CurrencyType currencyType);
}
