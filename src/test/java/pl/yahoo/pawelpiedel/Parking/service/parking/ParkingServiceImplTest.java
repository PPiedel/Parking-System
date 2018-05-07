package pl.yahoo.pawelpiedel.Parking.service.parking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.repository.ParkingRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ParkingServiceImplTest {

    @Autowired
    private ParkingService parkingService;

    @MockBean
    private ParkingRepository parkingRepository;

    @Mock
    private Parking parkingMock;

    @Mock
    private Car car;

    @Mock
    private Place place;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isCarAlreadyParked_CarAlreadyParked_TrueReturned() {
        //given
        String testLicensePlate = "TEST";
        when(parkingRepository.findByCarLicensePlateNumberAndParkingStatus(testLicensePlate, ParkingStatus.ONGOING)).thenReturn(Collections.singletonList(parkingMock));

        //when
        boolean isCarAlreadyParked = parkingService.isCarAlreadyParked(testLicensePlate);

        //then
        assertTrue(isCarAlreadyParked);
    }

    @Test
    public void isCarAlreadyParked_CarNotParked_FalseReturned() {
        //given
        String testLicensePlate = "TEST";
        when(parkingRepository.findByCarLicensePlateNumberAndParkingStatus(testLicensePlate, ParkingStatus.ONGOING)).thenReturn(Collections.emptyList());

        //when
        boolean isCarAlreadyParked = parkingService.isCarAlreadyParked(testLicensePlate);

        //then
        assertFalse(isCarAlreadyParked);
    }

    @Test
    public void save_ParkingSaved_SavedReturned() {
        //given
        when(parkingRepository.save(any())).thenReturn(parkingMock);

        //when
        Parking saved = parkingService.save(new Parking());

        //then
        assertEquals(parkingMock, saved);
    }

    @Test
    public void findParkingById_ParkingFound_OptionalWithParkingReturned() {
        //given
        long testId = 1L;
        when(parkingRepository.findById(testId)).thenReturn(Optional.of(parkingMock));

        //when
        Optional<Parking> parkingOptional = parkingService.findParkingById(testId);

        //then
        assertEquals(parkingMock, parkingOptional.get());
    }

    @Test
    public void findParkingById_ParkingNotFound_EmptyOptionalReturned() {
        //given
        long testId = 1L;
        when(parkingRepository.findById(testId)).thenReturn(Optional.empty());

        //when
        Optional<Parking> parkingOptional = parkingService.findParkingById(testId);

        //then
        assertFalse(parkingOptional.isPresent());
    }

    @Test
    public void stopParking_ParkingPassed_UpdatedparkingReturned() {
        //given
        Parking parking = new Parking(car, place);

        //when
        LocalDateTime stopTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Parking stopped = parkingService.stopParkingAtTime(parking, stopTime);

        //then
        assertEquals(stopTime, stopped.getStopTime());
        assertEquals(ParkingStatus.COMPLETED, stopped.getParkingStatus());
    }

    @TestConfiguration
    static class ParkingServiceImplTestConfiguration {
        @Bean
        ParkingService parkingService(ParkingRepository parkingRepository) {
            return new ParkingServiceImpl(parkingRepository);
        }
    }
}