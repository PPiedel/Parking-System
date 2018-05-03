package pl.yahoo.pawelpiedel.Parking.domain.payment.pln;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PlnDriverFeeStrategy {
    public BigDecimal calculateMoney(LocalDateTime startTime, LocalDateTime stopTime);
}
