package pl.yahoo.pawelpiedel.Parking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.repository.DriverRepository;

import java.util.Collections;

@SpringBootApplication
public class ParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner loadData(DriverRepository repository) {
		return (args) -> {
		    Driver regular = new Driver(DriverType.REGULAR);
            Car car = new Car(regular,"REGULAR_PLATE");
            regular.setCars(Collections.singletonList(car));
            repository.save(regular);

            Driver vip = new Driver(DriverType.VIP);
            Car vipCar = new Car(vip, "VIP_PLATE");
            vip.setCars(Collections.singletonList(vipCar));
            repository.save(vip);
		};
	}


}
