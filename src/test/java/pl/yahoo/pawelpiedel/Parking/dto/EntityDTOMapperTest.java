package pl.yahoo.pawelpiedel.Parking.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Money;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EntityDTOMapperTest {
    @Autowired
    private EntityDTOMapper entityDTOMapper;

    @MockBean
    private CarService carService;

    @Test
    public void asEntity_CarNotInDb_NewEntityCreated() {
        //given
        String licensePlateNumber = "XYZ123";
        CarDTO carDTO = new CarDTO(licensePlateNumber);
        when(carService.findByLicencePlateNumber(licensePlateNumber)).thenReturn(null);

        //when
        Car entity = entityDTOMapper.asEntity(carDTO);

        //then
        assertEquals(carDTO.getLicensePlateNumber(), entity.getLicensePlateNumber());
        assertNull(entity.getId());
    }

    @Test
    public void asEntity_CarInDb_PersistedEntityAssigned() {
        //given
        String licensePlateNumber = "XYZ123";
        CarDTO carDTO = new CarDTO(licensePlateNumber);

        Driver driver = new Driver(DriverType.REGULAR);
        Car alreadyPersisted = new Car(driver, licensePlateNumber);
        when(carService.findByLicencePlateNumber(licensePlateNumber)).thenReturn(alreadyPersisted);

        //when
        Car entity = entityDTOMapper.asEntity(carDTO);

        //then
        assertEquals(alreadyPersisted, entity);
    }

    @Test
    public void asDTO_PaymentPassed_PaymentDTOReturned() {
        //given
        long testId = 1L;
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "testPlate";
        Car car = new Car(driver, licensePlateNumber);
        driver.setCars(Collections.singletonList(car));
        Place place = new Place(PlaceStatus.TAKEN);
        long placeId = 2L;
        place.setId(placeId);
        Parking parking = new Parking(car, place);
        parking.setStopTime(LocalDateTime.now());
        parking.setParkingStatus(ParkingStatus.COMPLETED);
        Payment payment = new Payment(parking, new Money(new BigDecimal(1.5), Currency.getInstance("PLN")));
        parking.setPayment(payment);
        car.addParking(parking);

        //when
        PaymentDTO paymentDTO = entityDTOMapper.asDTO(parking.getPayment());

        //then
        assertEquals(payment.getMoney().getCurrency(), paymentDTO.getMoney().getCurrency());
        assertEquals(payment.getMoney().getAmount(), paymentDTO.getMoney().getAmount());
        assertEquals(payment.getParking().getPlace().getId(), paymentDTO.getParking().getPlace().getId());
        assertEquals(payment.getParking().getCar().getLicensePlateNumber(), paymentDTO.getParking().getCar().getLicensePlateNumber());
        assertEquals(payment.getParking().getStartTime(), paymentDTO.getParking().getStartTime());
        assertEquals(payment.getParking().getStopTime(), paymentDTO.getParking().getStopTime());
        assertEquals(payment.getParking().getParkingStatus(), paymentDTO.getParking().getParkingStatus());

    }

    @TestConfiguration
    static class EntityDToMapperTestConfigurtion {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        public EntityDTOMapper entityDTOMapper(ModelMapper modelMapper, CarService carService) {
            return new EntityDTOMapper(modelMapper, carService);
        }
    }
}