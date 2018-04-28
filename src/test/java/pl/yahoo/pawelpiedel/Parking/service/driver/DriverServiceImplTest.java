package pl.yahoo.pawelpiedel.Parking.service.driver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Driver;
import pl.yahoo.pawelpiedel.Parking.repository.DriverRepository;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class DriverServiceImplTest {
    @Autowired
    private DriverService driverService;

    @MockBean
    private DriverRepository driverRepository;

    @Test
    public void findByIdShouldReturnOptionalWithValue() {
        //given
        Driver testDriver = new Driver();
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
    public void findByIdShouldReturnEmptyOptional() {
        //given
        long notExistingId = 9999L;

        //when
        Optional<Driver> foundDriver = driverService.findById(notExistingId);

        //then
        assertFalse(foundDriver.isPresent());
    }

    @TestConfiguration
    static class DriverServiceImplTestConfiguration {
        @Bean
        public DriverService driverService(DriverRepository driverRepository) {
            return new DriverServiceImpl(driverRepository);
        }
    }


}