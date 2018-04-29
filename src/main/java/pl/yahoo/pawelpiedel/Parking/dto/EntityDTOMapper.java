package pl.yahoo.pawelpiedel.Parking.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.Car;

@Component
public class EntityDTOMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public EntityDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Car asEntity(CarDTO carDTO) {
        return modelMapper.map(carDTO, Car.class);
    }

    @Configuration
    static class MapperConfiguration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }
}
