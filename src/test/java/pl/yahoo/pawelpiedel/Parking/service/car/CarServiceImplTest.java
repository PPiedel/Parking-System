package pl.yahoo.pawelpiedel.Parking.service.car;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.repository.CarRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CarServiceImplTest {
    @Autowired
    private CarService carService;

    @MockBean
    private CarRepository carRepository;

    @Mock
    private Car carMock;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findCarByLicenseNumber_CarExists_CarReturned() {
        //given
        String testLicencePlate = "XYZ123";
        when(carRepository.findByLicensePlateNumber(testLicencePlate)).thenReturn(carMock);

        //when
        Car found = carService.findByLicencePlateNumber(testLicencePlate);

        //then
        assertEquals(carMock, found);
    }

    @Test
    public void findCarByLicenseNumber_CarNotExists_NullReturned() {
        //given no car
        String testLicencePlate = "XYZ123";

        //when
        Car found = carService.findByLicencePlateNumber(testLicencePlate);

        //then
        assertNull(found);
    }

    @Test
    public void save_CarSaved_CarReturned() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        Long id = 1L;
        driver.setId(id);
        String licensePlateNumber = "XYZ1234";
        Car saved = new Car(driver, licensePlateNumber);
        saved.setId(id);
        when(carRepository.save(any())).thenReturn(saved);

        //when
        Car entity = new Car(driver, licensePlateNumber);
        Car car = carService.save(entity);

        //then
        assertEquals(id, car.getId());
        assertEquals(licensePlateNumber, car.getLicensePlateNumber());
        assertEquals(driver, car.getDriver());
    }

    @Test
    public void isUnknown_CarIsUnknown_TrueReturned() {
        //given
        Car car = new Car(null, "TEST_PLATE");

        //when
        boolean isUnknown = carService.isUnknown(car);

        //then
        assertTrue(isUnknown);
    }

    @Test
    public void isUnknown_CarIsKnown_FalseReturned() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        driver.setId(1L);
        Car car = new Car(driver, "TEST_PLATE");
        car.setId(1L);

        //when
        boolean isUnknown = carService.isUnknown(car);

        //then
        assertFalse(isUnknown);
    }


    @TestConfiguration
    static class CarServiceImplTestConfiguration {
        @Bean
        CarService carService(CarRepository carRepository) {
            return new CarServiceImpl(carRepository);
        }
    }
}