package pl.yahoo.pawelpiedel.Parking.service.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.payment.CurrencyType;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;


    @Test
    public void assignPaymentToParking_ParkingPassed_PaymentAssigned() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        Car car = new Car(driver, "TEST_PLATE");
        driver.setCars(Collections.singletonList(car));
        Parking parking = new Parking(car, new Place(PlaceStatus.TAKEN));
        car.setParkings(Collections.singletonList(parking));
        parking.setStopTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        //when
        Parking parkingWithPayment = paymentService.assignPaymentInGivenCurrency(parking, CurrencyType.PLN);

        //then
        assertNotNull(parkingWithPayment.getPayment());
    }

    @TestConfiguration
    static class PaymentServiceImplTestConfiguration {
        @Bean
        PaymentService paymentService() {
            return new PaymentServiceImpl();
        }
    }
}