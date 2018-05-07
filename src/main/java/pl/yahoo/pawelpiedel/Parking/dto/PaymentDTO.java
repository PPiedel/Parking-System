package pl.yahoo.pawelpiedel.Parking.dto;

import pl.yahoo.pawelpiedel.Parking.domain.payment.Money;

public class PaymentDTO {

    private ParkingDTO parking;

    private Money money;

    public ParkingDTO getParking() {
        return parking;
    }

    public void setParking(ParkingDTO parking) {
        this.parking = parking;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }
}
