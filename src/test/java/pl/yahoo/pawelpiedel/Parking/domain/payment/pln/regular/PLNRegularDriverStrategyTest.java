package pl.yahoo.pawelpiedel.Parking.domain.payment.pln.regular;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.yahoo.pawelpiedel.Parking.domain.payment.pln.regular.PlnRegularDriverStrategy.FIRST_HOUR_RATE;
import static pl.yahoo.pawelpiedel.Parking.domain.payment.pln.regular.PlnRegularDriverStrategy.SECOND_HOUR_RATE;

public class PLNRegularDriverStrategyTest {

    @Test
    public void getFee() {
    }

    @Test
    public void getAmount_ZeroMinutes_ZeroReturned() {
        //given
        long minutes = 0;

        //when
        PlnRegularDriverStrategy strategy = new PlnRegularDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(0, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesLessThanHour_CorrectAmountRetuerned() {
        //given
        long minutes = 30;

        //when
        PlnRegularDriverStrategy strategy = new PlnRegularDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(0.5, amount, 0.0);
    }

    @Test
    public void getAmount_60Minutes_FirstHourRateReturned() {
        //given
        long minutes = 60;

        //when
        PlnRegularDriverStrategy strategy = new PlnRegularDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(FIRST_HOUR_RATE, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesGreaterThanHour_HourRateReturned() {
        //given
        long minutes = 90;

        //when
        PlnRegularDriverStrategy strategy = new PlnRegularDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(2, amount, 0.0);
    }


    @Test
    public void getAmount_120Minutes_First_CorrectAmountReturned() {
        //given
        long minutes = 120;

        //when
        PlnRegularDriverStrategy strategy = new PlnRegularDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(FIRST_HOUR_RATE + SECOND_HOUR_RATE, amount, 0.0);
    }

    @Test
    public void getAmount_MinutesGreatherThanTwoHours_CorrectAmountReturned() {
        //given
        long minutes = 150;

        //when
        PlnRegularDriverStrategy strategy = new PlnRegularDriverStrategy();
        double amount = strategy.getAmount(minutes);

        //then
        assertEquals(4.5, amount, 0.0);
    }


}