package pl.yahoo.pawelpiedel.Parking.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;

@Component
public class EntityDTOMapper {
    private final ModelMapper modelMapper;
    private final CarService carService;

    @Autowired
    public EntityDTOMapper(ModelMapper modelMapper, CarService carService) {
        this.modelMapper = modelMapper;
        this.carService = carService;
    }


    public Car asEntity(CarDTO carDTO) {
        Car entity = carService.findByLicencePlateNumber(carDTO.getLicensePlateNumber());
        if (entity == null) {
            entity = modelMapper.map(carDTO, Car.class);
        }
        return entity;
    }

    public PaymentDTO asDTo(Payment payment) {
        return modelMapper.map(payment, PaymentDTO.class);
    }


}
