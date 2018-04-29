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
import pl.yahoo.pawelpiedel.Parking.repository.CarRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void whenCarSavedThenFindByLicenceNumberShouldReturnCar() {
        //given
        String testLicencePlate = "XYZ123";
        when(carRepository.findByLicensePlateNumber(testLicencePlate)).thenReturn(carMock);

        //when
        Car found = carService.findByLicenceNumber(testLicencePlate);

        //then
        assertEquals(carMock, found);
    }

    @Test
    public void whenCarDoesntExistThenFindByLicenceNumberShouldReturnNull() {
        //given no car
        String testLicencePlate = "XYZ123";

        //when
        Car found = carService.findByLicenceNumber(testLicencePlate);

        //then
        assertNull(found);
    }


    @TestConfiguration
    static class CarServiceImplTestConfiguration {
        @Bean
        CarService carService(CarRepository carRepository) {
            return new CarServiceImpl(carRepository);
        }
    }
}