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
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest

public class ParkingRepositoryTest {
    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findByCarLicensePlateNumberAndParkingStatus_CarWithParkings_ParkingsReturned() {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        place = testEntityManager.persistAndFlush(place);

        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "XYZ123";
        Car car = new Car(driver, licensePlateNumber);
        Parking parking = new Parking(car, place);
        place.setPlaceStatus(PlaceStatus.TAKEN);
        parking.setParkingStatus(ParkingStatus.ONGOING);
        car.addParking(parking);
        driver.setCars(Collections.singletonList(car));
        testEntityManager.persist(driver);
        testEntityManager.flush();

        //when
        List<Parking> parkingList = parkingRepository.findByCarLicensePlateNumberAndParkingStatus(licensePlateNumber, ParkingStatus.ONGOING);

        //then
        assertThat(parkingList, hasSize(1));
    }

    @Test
    public void findByCarLicensePlateNumberAndParkingStatus_CarWithoutParkings_EmptyListReturned() {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        place = testEntityManager.persistAndFlush(place);

        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "XYZ123";
        Car car = new Car(driver, licensePlateNumber);
        driver.setCars(Collections.singletonList(car));
        testEntityManager.persist(driver);
        testEntityManager.flush();

        //when
        List<Parking> parkingList = parkingRepository.findByCarLicensePlateNumberAndParkingStatus(licensePlateNumber, ParkingStatus.ONGOING);

        //then
        assertThat(parkingList, hasSize(0));
    }

    @Test
    public void findByCarLicensePlateNumberAndParkingStatus_UnknownLicensePassed_EmptyListReturned() {
        //given
        String licensePlateNumber = "XYZ123";

        //when
        List<Parking> parkingList = parkingRepository.findByCarLicensePlateNumberAndParkingStatus(licensePlateNumber, ParkingStatus.ONGOING);

        //then
        assertThat(parkingList, hasSize(0));
    }
}