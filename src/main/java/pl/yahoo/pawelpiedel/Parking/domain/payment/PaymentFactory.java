package pl.yahoo.pawelpiedel.Parking.domain.payment;

import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.payment.pln.PlnPaymentStrategy;

public abstract class PaymentFactory {

    public static Payment getPayment(Parking parking, CurrencyType currencyType) {
        Payment payment;

        PaymentStrategy paymentStrategy;
        switch (currencyType) {
            case PLN: {
                paymentStrategy = new PlnPaymentStrategy();
                payment = new Payment(parking, paymentStrategy.calculateMoney(parking));
                break;
            }
            default: {
                paymentStrategy = new PlnPaymentStrategy();
                payment = new Payment(parking, paymentStrategy.calculateMoney(parking));
                break;
            }
        }

        return payment;
    }
}
