package pl.yahoo.pawelpiedel.Parking.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ParkingCreationDTO {
    @NotNull
    @NotEmpty
    private CarDTO carDTO;

    @NotNull
    @NotEmpty
    private String startTime;

    public CarDTO getCarDTO() {
        return carDTO;
    }

    public void setCarDTO(CarDTO carDTO) {
        this.carDTO = carDTO;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
