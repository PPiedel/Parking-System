package pl.yahoo.pawelpiedel.Parking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findCarByLicensePlate_CarSavedInDB_CarReturned() {
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
    public void findCarByLicensePlate_CarNotSavedInDB_NullReturned() {
        //given
        String notExistingPlate = "XYZ21";

        //when
        Car found = carRepository.findByLicensePlateNumber(notExistingPlate);

        //then
        assertNull(found);
    }

    @Test
    public void saveCar_updatedKnownCarPassed_CarInDBUpdated() {
        //given
        //add free place
        Place place = new Place(PlaceStatus.AVAILABLE);
        testEntityManager.persistAndFlush(place);

        //add driver
        Driver driver = new Driver(DriverType.REGULAR);
        driver = testEntityManager.persistAndFlush(driver);

        //add car to driver
        String testPlate = "XYZ21";
        Car car = new Car(driver, testPlate);
        car = testEntityManager.persistAndFlush(car);

        //when
        //add parking to car
        Parking parking = new Parking(car, place);
        place.setPlaceStatus(PlaceStatus.TAKEN);
        car.addParking(parking);
        Car updated = carRepository.save(car);

        assertEquals(1, carRepository.findAll().size());
        assertEquals(1, updated.getParkings().size());
        assertEquals(testPlate, updated.getLicensePlateNumber());

    }
}