package pl.yahoo.pawelpiedel.Parking.service.payment;

import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.payment.CurrencyType;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;
import pl.yahoo.pawelpiedel.Parking.domain.payment.PaymentFactory;

@Component
public class PaymentServiceImpl implements PaymentService {

    @Override
    public Parking assignPaymentToParking(Parking parking, CurrencyType currencyType) {
        Payment payment = PaymentFactory.getPayment(parking, currencyType);
        parking.setPayment(payment);
        return parking;
    }
}
