package pl.yahoo.pawelpiedel.Parking.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
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