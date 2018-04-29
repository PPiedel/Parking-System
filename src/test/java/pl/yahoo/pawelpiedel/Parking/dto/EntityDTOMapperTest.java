package pl.yahoo.pawelpiedel.Parking.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.Car;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class EntityDTOMapperTest {
    @Autowired
    private EntityDTOMapper entityDTOMapper;

    @Test
    public void asEntity_CarDtoWithLicensePlate_LicensePlatesEqual() {
        //given
        String licensePlateNumber = "XYZ123";
        CarDTO carDTO = new CarDTO(licensePlateNumber);

        //when
        Car mapped = entityDTOMapper.asEntity(carDTO);

        //then
        assertEquals(carDTO.getLicensePlateNumber(), mapped.getLicensePlateNumber());
    }

    @TestConfiguration
    static class EntityDToMapperTestConfigurtion {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        public EntityDTOMapper entityDTOMapper(ModelMapper modelMapper) {
            return new EntityDTOMapper(modelMapper);
        }
    }
}