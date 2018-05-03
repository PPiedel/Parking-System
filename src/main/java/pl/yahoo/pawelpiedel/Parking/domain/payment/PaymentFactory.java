package pl.yahoo.pawelpiedel.Parking.domain.payment;

import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;

public abstract class PaymentFactory {

    public static Payment getPayment(Parking parking, CurrencyType currencyType) {
        Payment payment;
        switch (currencyType) {
            case PLN: {
                ZlotyPaymentStrategy paymentStrategy = new ZlotyPaymentStrategy();
                payment = new Payment(parking, paymentStrategy.calculateMoney(parking));
                break;
            }
            default: {
                ZlotyPaymentStrategy paymentStrategy = new ZlotyPaymentStrategy();
                payment = new Payment(parking, paymentStrategy.calculateMoney(parking));
                break;
            }
        }
        return payment;
    }
}
