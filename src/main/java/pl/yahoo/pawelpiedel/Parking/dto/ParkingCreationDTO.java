package pl.yahoo.pawelpiedel.Parking.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ParkingCreationDTO {
    @NotNull
    @NotEmpty
    private CarDTO carDTO;

    public CarDTO getCarDTO() {
        return carDTO;
    }

    public void setCarDTO(CarDTO carDTO) {
        this.carDTO = carDTO;
    }

}
