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
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.repository.ParkingRepository;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ParkingServiceImplTest {

    @Autowired
    private ParkingService parkingService;

    @MockBean
    private ParkingRepository parkingRepository;

    @Mock
    private Car carMock;

    @Mock
    private Parking parkingMock;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getOngoingParkings_NoOngoingParkings_EmptyListReturned() {
        //given
        String licensePlateNumber = "XYZ123";
        when(parkingRepository.findByCarLicensePlateNumberAndParkingStatus(licensePlateNumber, ParkingStatus.ONGOING)).thenReturn(Collections.emptyList());

        //when
        boolean isCarAlreadyParked = parkingService.isCarAlreadyParked(licensePlateNumber);

        //then
        assertFalse(isCarAlreadyParked);
    }

    @Test
    public void getOngoingParkings_OngoingParkinsExist_ListOfOngoingParkingsReturned() {
        //given
        String licensePlateNumber = "XYZ123";
        when(parkingRepository.findByCarLicensePlateNumberAndParkingStatus(licensePlateNumber, ParkingStatus.ONGOING)).thenReturn(Collections.singletonList(parkingMock));

        //when
        boolean isCarAlreadyParked = parkingService.isCarAlreadyParked(licensePlateNumber);

        //then
        assertTrue(isCarAlreadyParked);
    }

    @TestConfiguration
    static class ParkingServiceImplTestConfiguration {
        @Bean
        ParkingService parkingService(ParkingRepository parkingRepository) {
            return new ParkingServiceImpl(parkingRepository);
        }
    }
}