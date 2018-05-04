package pl.yahoo.pawelpiedel.Parking.domain.payment.pln;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.payment.CurrencyType;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Money;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PlnPaymentStrategyTest {

    @Autowired
    private PlnPaymentStrategy plnPaymentStrategy;

    @Test
    public void calculateMoney_RegularDriver_PLN_30Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 15, 30, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(0.5), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_RegularDriver_PLN_60Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 16, 0, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(1), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_RegularDriver_PLN_90Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 16, 30, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(2), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_RegularDriver_PLN_120Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 17, 0, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(3), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_VipDriver_PLN_30Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.VIP);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 15, 30, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(0), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_VipDriver_PLN_60Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.VIP);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 16, 0, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(0), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_VipDriver_PLN_120Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.VIP);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 17, 0, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(2), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }

    @Test
    public void calculateMoney_VipDriver_PLN_150Minutes_MoneyCalculated() {
        //given
        Driver driver = new Driver(DriverType.VIP);
        String licensePlate = "XYZ1234";
        Car car = new Car(driver, licensePlate);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.addParking(parking);
        parking.setStartTime(LocalDateTime.of(2018, 4, 4, 15, 0, 0));
        parking.setStopTime(LocalDateTime.of(2018, 4, 4, 17, 30, 0));

        //when
        Money calculated = plnPaymentStrategy.calculateMoney(parking);

        //then
        Money expected = new Money(new BigDecimal(3.2), Currency.getInstance(CurrencyType.PLN.toString()));
        assertEquals(expected, calculated);
    }


    @TestConfiguration
    static class PlnPaymentStrategyTestConfiguration {
        @Bean
        PlnPaymentStrategy plnPaymentStrategy() {
            return new PlnPaymentStrategy();
        }
    }
}