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
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopTimeOnlyDTO;
import pl.yahoo.pawelpiedel.Parking.dto.PaymentDTO;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.driver.DriverService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingNotFoundException;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/parking")
public class ParkingController {
    private final Logger logger = LoggerFactory.getLogger(ParkingController.class);
    private final ParkingService parkingService;
    private final CarService carService;
    private final PlaceService placeService;
    private final DriverService driverService;
    private final EntityDTOMapper entityDTOMapper;

    @Autowired
    public ParkingController(ParkingService parkingService, CarService carService, PlaceService placeService, DriverService driverService, EntityDTOMapper entityDTOMapper) {
        this.parkingService = parkingService;
        this.carService = carService;
        this.placeService = placeService;
        this.driverService = driverService;
        this.entityDTOMapper = entityDTOMapper;
    }

    @PostMapping
    public ResponseEntity<?> startParkingMeter(@RequestBody @Valid CarDTO carDTO) {
        List<Place> availablePlaces = placeService.getAvailablePlaces();

        if (availablePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            Car car = entityDTOMapper.asEntity(carDTO);

            if (parkingService.isCarAlreadyParked(car.getLicensePlateNumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Place place = findPlaceForParking();
            place.setPlaceStatus(PlaceStatus.TAKEN);

            Parking parking = new Parking(car, place);
            parking.setParkingStatus(ParkingStatus.ONGOING);
            car.addparking(parking);

            if (carService.isUnknown(car)) {
                Driver newDriver = new Driver(DriverType.REGULAR);
                newDriver.setCars(Collections.singletonList(car));
                car.setDriver(newDriver);
                Driver saved = driverService.save(newDriver);
                URI location = buildUri(getLastAddedParking(saved.getCars().get(0)));
                return ResponseEntity.created(location).build();

            } else {
                Car saved = carService.save(car);
                URI location = buildUri(getLastAddedParking(saved));
                return ResponseEntity.created(location).build();
            }

        }

    }

    private Place findPlaceForParking() {
        return placeService.getNextFirstFreePlace();
    }

    private Parking getLastAddedParking(Car car) {
        return car.getParkings().get(car.getParkings().size() - 1);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> stopParkingMeter(@RequestBody @Valid ParkingStopTimeOnlyDTO parkingStopTimeOnlyDTO, @PathVariable("id") Long id) {
        LocalDateTime localDateTime = LocalDateTime.parse(parkingStopTimeOnlyDTO.getStopTime());
        Parking updated = new Parking();
        updated.setId(id);
        try {
            updated = parkingService.saveStopTime(localDateTime, id);

        } catch (ParkingNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        updated.setParkingStatus(ParkingStatus.COMPLETED);
        parkingService.save(updated);

        Place parkingPlace = updated.getPlace();
        placeService.freePlace(parkingPlace);

        return ResponseEntity.ok(updated);
    }

    private URI buildUri(Parking parking) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(parking.getId()).toUri();
    }

    @GetMapping(value = "/{id}/payment")
    public ResponseEntity<PaymentDTO> getPaymentDetails(@PathVariable("id") Long id) {
        Optional<Parking> parkingOptional = parkingService.getParking(id);

        return parkingOptional
                .map(Parking::getPayment)
                .map(payment -> new ResponseEntity<>(entityDTOMapper.asDTo(payment), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
