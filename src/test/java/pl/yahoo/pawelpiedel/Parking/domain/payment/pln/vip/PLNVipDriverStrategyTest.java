package pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;
import static pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip.PLNVipDriverStrategy.FIRST_HOUR_RATE;
import static pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip.PLNVipDriverStrategy.SECOND_HOUR_RATE;

@RunWith(SpringRunner.class)
public class PLNVipDriverStrategyTest {

    @Autowired
    private PLNVipDriverStrategy strategy;

    @Test
    public void calculateFee_ZeroMinutesParking_ZeroReturned() {
        //given
        LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime stopTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        //when
        BigDecimal fee = strategy.calculateFee(startTime, stopTime);

        //then
        assertEquals(new BigDecimal(0), fee);
    }

    @Test
    public void calculateFee_30MinutesParking_FeeReturned() {
        //given
        LocalDateTime startTime = LocalDateTime.of(2015, 10, 10, 12, 0, 1);
        LocalDateTime stopTime = LocalDateTime.of(2015, 10, 10, 12, 30, 1);

        //when
        BigDecimal fee = strategy.calculateFee(startTime, stopTime);

        //then
        assertEquals(new BigDecimal(0), fee);
    }

    @Test
    public void calculateFee_60MinutesParking_FeeReturned() {
        //given
        LocalDateTime startTime = LocalDateTime.of(2015, 10, 10, 12, 5, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, 10, 10, 13, 5, 0);

        //when
        BigDecimal fee = strategy.calculateFee(startTime, stopTime);

        //then
        assertEquals(new BigDecimal(0), fee);
    }

    @Test
    public void calculateFee_90MinutesParking_FeeReturned() {
        //given
        LocalDateTime startTime = LocalDateTime.of(2015, 10, 10, 12, 0, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, 10, 10, 13, 30, 0);

        //when
        BigDecimal fee = strategy.calculateFee(startTime, stopTime);

        //then
        assertEquals(new BigDecimal(1), fee);
    }

    @Test
    public void calculateFee_120MinutesParking_FeeReturned() {
        //given
        LocalDateTime startTime = LocalDateTime.of(2015, 10, 10, 12, 0, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, 10, 10, 14, 0, 0);

        //when
        BigDecimal fee = strategy.calculateFee(startTime, stopTime);

        //then
        assertEquals(new BigDecimal(FIRST_HOUR_RATE + SECOND_HOUR_RATE), fee);
    }

    @Test
    public void calculateFee_150MinutesParking_FeeReturned() {
        //given
        LocalDateTime startTime = LocalDateTime.of(2015, 10, 10, 12, 0, 0);
        LocalDateTime stopTime = LocalDateTime.of(2015, 10, 10, 14, 30, 0);

        //when
        BigDecimal fee = strategy.calculateFee(startTime, stopTime);

        //then
        assertEquals(new BigDecimal(3.2), fee);
    }

    @Test
    public void getAmount_ZeroMinutes_ZeroReturned() {
        //given
        long minutes = 0;

        //when
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(0, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesLessThanHour_CorrectAmountRetuerned() {
        //given
        long minutes = 30;

        //when
        PLNVipDriverStrategy strategy = new PLNVipDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(0, amount, 0.0);
    }

    @Test
    public void getAmount_60Minutes_FirstHourRateReturned() {
        //given
        long minutes = 60;

        //when
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(0, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesGreaterThanHour_HourRateReturned() {
        //given
        long minutes = 90;

        //when
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(1, amount, 0.0);
    }


    @Test
    public void getAmount_120Minutes_First_CorrectAmountReturned() {
        //given
        long minutes = 120;

        //when
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(FIRST_HOUR_RATE + SECOND_HOUR_RATE, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesGreatherThanTwoHours_CorrectAmountReturned() {
        //given
        long minutes = 150;

        //when
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(3.2, amount, 0.0);
    }

    @TestConfiguration
    static class PLNVipDriverStrategyTestConfiguration {
        @Bean
        PLNVipDriverStrategy plnVipDriverStrategy() {
            return new PLNVipDriverStrategy();
        }
    }
}