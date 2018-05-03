package pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip.PLNVipDriverStrategy.FIRST_HOUR_RATE;
import static pl.yahoo.pawelpiedel.Parking.domain.payment.pln.vip.PLNVipDriverStrategy.SECOND_HOUR_RATE;

public class PLNVipDriverStrategyTest {

    @Test
    public void calculateMoney() {
    }

    @Test
    public void getAmount_ZeroMinutes_ZeroReturned() {
        //given
        long minutes = 0;

        //when
        PLNVipDriverStrategy strategy = new PLNVipDriverStrategy();
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
        PLNVipDriverStrategy strategy = new PLNVipDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(0, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesGreaterThanHour_HourRateReturned() {
        //given
        long minutes = 90;

        //when
        PLNVipDriverStrategy strategy = new PLNVipDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(1, amount, 0.0);
    }


    @Test
    public void getAmount_120Minutes_First_CorrectAmountReturned() {
        //given
        long minutes = 120;

        //when
        PLNVipDriverStrategy strategy = new PLNVipDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(FIRST_HOUR_RATE + SECOND_HOUR_RATE, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesGreatherThanTwoHours_CorrectAmountReturned() {
        //given
        long minutes = 150;

        //when
        PLNVipDriverStrategy strategy = new PLNVipDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(3.2, amount, 0.0);
    }
}