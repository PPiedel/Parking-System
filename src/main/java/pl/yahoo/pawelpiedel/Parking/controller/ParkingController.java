package pl.yahoo.pawelpiedel.Parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value = "/api/parking")
public class ParkingController {
    private final ParkingService parkingService;
    private final CarService carService;
    private final PlaceService placeService;
    private final EntityDTOMapper entityDTOMapper;


    @Autowired
    public ParkingController(ParkingService parkingService, CarService carService, PlaceService placeService, EntityDTOMapper entityDTOMapper) {
        this.parkingService = parkingService;
        this.carService = carService;
        this.placeService = placeService;
        this.entityDTOMapper = entityDTOMapper;
    }

    @PostMapping
    public ResponseEntity<?> startParkingMeter(@RequestBody @Valid CarDTO carDTO) {
        Car car = entityDTOMapper.asEntity(carDTO);

        List<Parking> carOngoingParkings = parkingService.getOngoingParkings(car);
        List<Place> availablePlaces = placeService.getPlacesWithStatus(PlaceStatus.AVAILABLE);
        if (!carOngoingParkings.isEmpty() || availablePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

    }

    private URI buildUri(Parking parking) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(parking.getId()).toUri();
    }
}
