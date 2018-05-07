package pl.yahoo.pawelpiedel.Parking.service.parking;

public class ParkingNotFoundException extends Exception {

    public ParkingNotFoundException() {
    }

    public ParkingNotFoundException(String message) {
        super(message);
    }
}
