package pl.yahoo.pawelpiedel.Parking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopTimeDTO;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingNotFoundException;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/api/parking")
public class ParkingController {
    private final Logger logger = LoggerFactory.getLogger(ParkingController.class);
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
    public ResponseEntity<?> startParkingMeter(@RequestBody @Valid @NotNull CarDTO carDTO) {
        Car car = entityDTOMapper.asEntity(carDTO);

        List<Parking> carOngoingParkings = parkingService.getOngoingParkings(car);
        List<Place> availablePlaces = placeService.getAvailablePlaces();
        if (!carOngoingParkings.isEmpty() || availablePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            Place place = placeService.getNextFreePlace();
            place.setPlaceStatus(PlaceStatus.TAKEN);

            Parking entity = new Parking(car, place);
            Parking saved = parkingService.save(entity);

            URI location = buildUri(saved);
            return ResponseEntity.created(location).build();
        }

    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> stopParkingMeter(@RequestBody @Valid @NotNull ParkingStopTimeDTO parkingStopTimeDTO, @PathVariable("id") Long id) {
        LocalDateTime localDateTime = LocalDateTime.parse(parkingStopTimeDTO.getStopTime());
        try {
            Parking updated = parkingService.save(localDateTime, id);
            logger.debug("Parking after update : " + updated);
        } catch (ParkingNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Parking stopped");
    }

    private URI buildUri(Parking parking) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(parking.getId()).toUri();
    }
}
