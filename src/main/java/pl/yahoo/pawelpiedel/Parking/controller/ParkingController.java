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
import pl.yahoo.pawelpiedel.Parking.domain.payment.CurrencyType;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;
import pl.yahoo.pawelpiedel.Parking.domain.payment.PaymentFactory;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopDTO;
import pl.yahoo.pawelpiedel.Parking.dto.PaymentDTO;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.driver.DriverService;
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
            car.addParking(parking);

            if (carService.isUnknown(car)) {
                Driver newDriver = new Driver(DriverType.REGULAR);
                assign(car, newDriver);
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

    private URI buildUri(Parking parking) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(parking.getId()).toUri();
    }

    private void assign(Car car, Driver newDriver) {
        newDriver.setCars(Collections.singletonList(car));
        car.setDriver(newDriver);
    }

    private Place findPlaceForParking() {
        return placeService.getNextFirstFreePlace();
    }

    private Parking getLastAddedParking(Car car) {
        return car.getParkings().get(car.getParkings().size() - 1);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> stopParkingMeter(@RequestBody @Valid ParkingStopDTO parkingStopDTO, @PathVariable("id") Long id) {
        LocalDateTime stopTime = LocalDateTime.parse(parkingStopDTO.getStopTime());
        CurrencyType currencyType = CurrencyType.valueOf(parkingStopDTO.getCurrencyType());
        //TODO currency validation

        Optional<Parking> optional = parkingService.findParkingById(id);
        if (optional.isPresent()) {
            Parking parking = optional.get();
            parking.setStopTime(stopTime);
            parking.setParkingStatus(ParkingStatus.COMPLETED);

            Payment payment = PaymentFactory.getPayment(parking, currencyType);
            parking.setPayment(payment);

            parkingService.save(parking);

            Place parkingPlace = parking.getPlace();
            placeService.freePlace(parkingPlace);

            return ResponseEntity.ok(optional);
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping(value = "/{id}/payment")
    public ResponseEntity<PaymentDTO> getPaymentDetails(@PathVariable("id") Long id) {
        Optional<Parking> parkingOptional = parkingService.findParkingById(id);

        return parkingOptional
                .map(Parking::getPayment)
                .map(payment -> new ResponseEntity<>(entityDTOMapper.asDTo(payment), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
