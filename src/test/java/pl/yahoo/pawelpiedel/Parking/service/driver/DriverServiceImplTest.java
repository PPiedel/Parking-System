package pl.yahoo.pawelpiedel.Parking.service.driver;

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
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.repository.DriverRepository;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DriverServiceImplTest {
    @Autowired
    private DriverService driverService;

    @MockBean
    private DriverRepository driverRepository;

    @Mock
    private Driver driverMock;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findDriver_DriverExists_OptionalWithValueReturned() {
        //given
        Driver testDriver = new Driver(DriverType.REGULAR);
        long testId = 1L;
        testDriver.setId(testId);
        when(driverRepository.findById(testId)).thenReturn(Optional.of(testDriver));

        //when
        Optional<Driver> foundDriver = driverService.findById(testId);

        //then
        assertTrue(foundDriver.isPresent());
        assertEquals(foundDriver.get(), testDriver);
    }

    @Test
    public void findDriver_DriverNotExists_EmptyOptionalReturned() {
        //given
        long notExistingId = 9999L;

        //when
        Optional<Driver> foundDriver = driverService.findById(notExistingId);

        //then
        assertFalse(foundDriver.isPresent());
    }

    @Test
    public void save_DriverSaved_DriverReturned() {
        //given
        when(driverRepository.save(any())).thenReturn(driverMock);

        //when
        Driver saved = driverService.save(new Driver(DriverType.REGULAR));

        //then
        assertEquals(driverMock, saved);
    }

    @TestConfiguration
    static class DriverServiceImplTestConfiguration {
        @Bean
        public DriverService driverService(DriverRepository driverRepository) {
            return new DriverServiceImpl(driverRepository);
        }
    }


}