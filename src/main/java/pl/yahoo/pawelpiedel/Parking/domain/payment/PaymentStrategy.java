package pl.yahoo.pawelpiedel.Parking.domain.payment;

import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;

public interface PaymentStrategy {
    Money calculateMoney(Parking parking);
}
