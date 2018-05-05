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
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopDTO;
import pl.yahoo.pawelpiedel.Parking.dto.PaymentDTO;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.driver.DriverService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.payment.PaymentService;
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
    private final PaymentService paymentService;
    private final EntityDTOMapper entityDTOMapper;

    @Autowired
    public ParkingController(ParkingService parkingService, CarService carService, PlaceService placeService, DriverService driverService, PaymentService paymentService, EntityDTOMapper entityDTOMapper) {
        this.parkingService = parkingService;
        this.carService = carService;
        this.placeService = placeService;
        this.driverService = driverService;
        this.paymentService = paymentService;
        this.entityDTOMapper = entityDTOMapper;
    }

    @PostMapping
    public ResponseEntity<?> startParkingMeter(@RequestBody @Valid CarDTO carDTO) {
        List<Place> availablePlaces = placeService.getAvailablePlaces();

        if (availablePlaces.isEmpty() || parkingService.isCarAlreadyParked(carDTO.getLicensePlateNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            Car car = entityDTOMapper.asEntity(carDTO);

            Place place = placeService.getPlaceForParking(availablePlaces);

            createParking(car, place);

            if (carService.isUnknown(car)) {
                Driver newDriver = new Driver(DriverType.REGULAR);
                assignCarToDriver(car, newDriver);
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

    private void createParking(Car car, Place place) {
        Parking parking = new Parking(car, place);
        parking.setParkingStatus(ParkingStatus.ONGOING);
        car.addParking(parking);
    }

    private URI buildUri(Parking parking) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(parking.getId()).toUri();
    }

    private void assignCarToDriver(Car car, Driver newDriver) {
        newDriver.setCars(Collections.singletonList(car));
        car.setDriver(newDriver);
    }

    private Parking getLastAddedParking(Car car) {
        return car.getParkings().get(car.getParkings().size() - 1);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> stopParkingMeter(@RequestBody @Valid ParkingStopDTO parkingStopDTO, @PathVariable("id") Long id) {
        LocalDateTime stopTime = LocalDateTime.parse(parkingStopDTO.getStopTime());
        CurrencyType currencyType = CurrencyType.valueOf(parkingStopDTO.getCurrencyType());
        //TODO currency validation

        Optional<Parking> parkingOptional = parkingService.findParkingById(id);
        if (parkingOptional.isPresent()) {
            Parking parking = parkingOptional.get();

            if (stopTime.isAfter(parking.getStartTime())){
                parkingService.stopParkingAtTime(parking, stopTime);
                parkingService.save(parking);

                paymentService.assignPaymentInGivenCurrency(parking, currencyType);

                Place released = placeService.releasePlace(parking.getPlace());
                placeService.save(released);
                return ResponseEntity.ok(entityDTOMapper.asDTO(parking));
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping(value = "/{id}/payment")
    public ResponseEntity<PaymentDTO> getPaymentDetails(@PathVariable("id") Long id) {
        Optional<Parking> parkingOptional = parkingService.findParkingById(id);

        return parkingOptional
                .map(Parking::getPayment)
                .map(payment -> new ResponseEntity<>(entityDTOMapper.asDTO(payment), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
