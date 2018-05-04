package pl.yahoo.pawelpiedel.Parking.domain.payment.pln.regular;

import pl.yahoo.pawelpiedel.Parking.domain.payment.pln.PlnDriverFeeStrategy;
import pl.yahoo.pawelpiedel.Parking.domain.payment.pln.PlnPaymentStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class PlnRegularDriverStrategy extends PlnPaymentStrategy implements PlnDriverFeeStrategy {
    final static double FIRST_HOUR_RATE = 1;
    final static double SECOND_HOUR_RATE = 2;
    private final static double EACH_NEXT_HOUR_RATE = 1.5;

    @Override
    public BigDecimal calculateFee(LocalDateTime startTime, LocalDateTime stopTime) {
        long minutesBetween = ChronoUnit.MINUTES.between(startTime, stopTime);
        return new BigDecimal(getAmount(minutesBetween));
    }

    public double getAmount(long minutesBetween) {
        if (minutesBetween <= HOUR_MINUTES) {
            return (FIRST_HOUR_RATE / HOUR_MINUTES) * minutesBetween;
        } else if (minutesBetween <= SECOND_HOUR_MINUTES) {
            return FIRST_HOUR_RATE + (SECOND_HOUR_RATE / HOUR_MINUTES) * (minutesBetween - HOUR_MINUTES);
        } else {
            return FIRST_HOUR_RATE + SECOND_HOUR_RATE + getRateForEachNextHour(minutesBetween - SECOND_HOUR_MINUTES, EACH_NEXT_HOUR_RATE * SECOND_HOUR_RATE);
        }
    }

    private double getRateForEachNextHour(long minutesLeft, double hourRate) {
        if (minutesLeft <= HOUR_MINUTES) {
            return (hourRate / HOUR_MINUTES) * minutesLeft;
        } else {
            return (hourRate / HOUR_MINUTES) * minutesLeft + getRateForEachNextHour(minutesLeft - HOUR_MINUTES, EACH_NEXT_HOUR_RATE * hourRate);
        }
    }


}
