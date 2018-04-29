package pl.yahoo.pawelpiedel.Parking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void whenDriverWithCarSavedFindByLicensePlateNumberShouldReturnCar() {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        String testPlate = "XYZ21";
        Car persisted = new Car(driver, testPlate);
        driver.setCars(Collections.singletonList(persisted));
        testEntityManager.persist(driver);
        testEntityManager.flush();

        //when
        Car found = carRepository.findByLicensePlateNumber(testPlate);

        //then
        assertEquals(persisted.getLicensePlateNumber(), found.getLicensePlateNumber());
    }

    @Test
    public void whenCarNotExistFindByLicensePlateNumberShouldReturnNull() {
        //given
        String notExistingPlate = "XYZ21";

        //when
        Car found = carRepository.findByLicensePlateNumber(notExistingPlate);

        //then
        assertNull(found);
    }
}