package pl.yahoo.pawelpiedel.Parking.domain.payment.pln;

import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Money;
import pl.yahoo.pawelpiedel.Parking.domain.payment.pln.regular.PlnRegularDriverStrategy;
import pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip.PLNVipDriverStrategy;

public class PlnPaymentStrategy implements pl.yahoo.pawelpiedel.Parking.domain.payment.PaymentStrategy {
    protected final static int HOUR_MINUTES = 60;
    protected final static int SECOND_HOUR_MINUTES = 2 * HOUR_MINUTES;

    @Override
    public Money calculateMoney(Parking parking) {
        DriverType driverType = parking.getCar().getDriver().getDriverType();

        PlnDriverFeeStrategy strategy;
        switch (driverType) {
            case REGULAR:
                strategy = new PlnRegularDriverStrategy();
                return new Money(strategy.calculateMoney(parking.getStartTime(), parking.getStopTime()));
            case VIP:
                strategy = new PLNVipDriverStrategy();
                return new Money(strategy.calculateMoney(parking.getStartTime(), parking.getStopTime()));
            default:
                strategy = new PLNVipDriverStrategy();
                return new Money(strategy.calculateMoney(parking.getStartTime(), parking.getStopTime()));
        }

    }


}
