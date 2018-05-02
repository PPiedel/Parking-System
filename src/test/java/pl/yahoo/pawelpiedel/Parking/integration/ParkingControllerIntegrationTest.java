package pl.yahoo.pawelpiedel.Parking.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.service.driver.DriverService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingControllerIntegrationTest {
    private static final String API_BASE_URL = "/api/parking";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private DriverService driverService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void startParkMeter_ValidCarDTOPassed_PlaceAvailable_NoOngoingParkings_CreatedWithLocationReturned() throws Exception {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        placeService.save(place);

        String licensePlateNumber = "XYZ123";
        CarDTO carDTO = new CarDTO(licensePlateNumber);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost")));

    }

    @Test
    public void startParkMeter_ValidCardDTOPassed_CarAlreadyInDb_CarParkingsUpdated_CreatedWithLocationReturned() throws Exception {
        //given
        //one place available
        Place place = new Place(PlaceStatus.AVAILABLE);
        placeService.save(place);

        //driver with car in db
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "ABC123";
        Car car = new Car(driver, licensePlateNumber);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        driverService.save(driver);

        //when
        CarDTO carDTO = new CarDTO(licensePlateNumber);
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost")));

    }
}