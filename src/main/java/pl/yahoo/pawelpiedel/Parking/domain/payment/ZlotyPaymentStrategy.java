package pl.yahoo.pawelpiedel.Parking.domain.payment;

import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;

public class ZlotyPaymentStrategy implements PaymentStrategy {

    @Override
    public Money calculateMoney(Parking parking) {
        return null;
    }
}
