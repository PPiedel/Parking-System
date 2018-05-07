package pl.yahoo.pawelpiedel.Parking.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.domain.payment.CurrencyType;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopDTO;
import pl.yahoo.pawelpiedel.Parking.service.driver.DriverService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "src/test/resources/application.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ParkingControllerIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(ParkingControllerIntegrationTest.class);
    private static final String API_BASE_URL = "/api/parking";
    private static final String DEFAULT_CURRENCY = "PLN";

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
    public void startParkMeter_ValidCardDTOPassed_CarAlreadyInDb_CreatedWithLocationReturned() throws Exception {
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

    @Test
    public void stopParkMeter_ValidDateTimeDTOPassed_ParkingExist_ParkingStopped() throws Exception {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        placeService.save(place);

        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "ABC123";
        Car car = new Car(driver, licensePlateNumber);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        driverService.save(driver);

        CarDTO carDTO = new CarDTO(licensePlateNumber);
        MvcResult postResult = mockMvc.perform(post(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(carDTO)))
                .andReturn();
        String location = postResult.getResponse().getHeader("Location");

        //when
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(localDateTime.toString(), DEFAULT_CURRENCY);
        ResultActions resultActions = mockMvc.perform(patch(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingStopDTO)));
        logger.info(asJsonString(parkingStopDTO));

        //then
        MvcResult stopResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        logger.info(stopResult.getResponse().getContentAsString());

        assertEquals(1, placeService.getAvailablePlaces().size());
    }

    @Test
    public void stopParkMeter_OnlyDatePassed_ParkingExist_ClientErrorReturned() throws Exception {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        placeService.save(place);

        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "ABC123";
        Car car = new Car(driver, licensePlateNumber);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        driverService.save(driver);

        CarDTO carDTO = new CarDTO(licensePlateNumber);
        MvcResult postResult = mockMvc.perform(post(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(carDTO)))
                .andReturn();
        String location = postResult.getResponse().getHeader("Location");

        //when
        String onlyDate = "2018-12-03";
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(onlyDate, DEFAULT_CURRENCY);
        ResultActions resultActions = mockMvc.perform(patch(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingStopDTO)));

        //then

        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void stopParkMeter_EmptyDateTimePassed_ParkingExist_ClientError() throws Exception {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        placeService.save(place);

        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "ABC123";
        Car car = new Car(driver, licensePlateNumber);
        car.setDriver(driver);
        driver.setCars(Collections.singletonList(car));
        driverService.save(driver);

        CarDTO carDTO = new CarDTO(licensePlateNumber);
        MvcResult postResult = mockMvc.perform(post(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(carDTO)))
                .andReturn();
        String location = postResult.getResponse().getHeader("Location");

        //when
        String onlyDate = "";
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(onlyDate, DEFAULT_CURRENCY);
        ResultActions resultActions = mockMvc.perform(patch(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingStopDTO)));

        //then
        MvcResult stopResult = resultActions
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @Test
    public void getPaymentDetails_ParkingExist_ZeroMinutesParking_DetailsReturned() throws Exception {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        Place savedPlace = placeService.save(place);
        String licensePlateNumber = "ABC123";
        CarDTO carDTO = new CarDTO(licensePlateNumber);

        //parking started
        MvcResult postResult = mockMvc.perform(post(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(carDTO)))
                .andReturn();
        String location = postResult.getResponse().getHeader("Location");

        //parking stopped
        LocalDateTime stopTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(stopTime.toString(), DEFAULT_CURRENCY);
        mockMvc.perform(patch(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingStopDTO)));

        //when
        ResultActions paymentDetailsResult = mockMvc.perform(get(location + "/payment")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        paymentDetailsResult
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.money.amount").value(0))
                .andExpect(jsonPath("$.money.currency").value(CurrencyType.PLN.toString()))
                .andExpect(jsonPath("$.parking.car.licensePlateNumber", is(licensePlateNumber)))
                .andExpect(jsonPath("$.parking.place.id", is(savedPlace.getId().intValue())))
                .andExpect(jsonPath("$.parking.place.placeStatus", is(PlaceStatus.AVAILABLE.toString())))
                .andExpect(jsonPath("$.parking.parkingStatus", is(ParkingStatus.COMPLETED.toString())))
                .andExpect(jsonPath("$.parking.stopTime").value(stopTime.toString()));

    }

    @Test
    public void getPaymentDetails_ParkingOngoing_PaymentNotAssignedYet_NotFoundReturned() throws Exception {
        //given
        Place place = new Place(PlaceStatus.AVAILABLE);
        Place savedPlace = placeService.save(place);
        String licensePlateNumber = "ABC123";
        CarDTO carDTO = new CarDTO(licensePlateNumber);

        //parking started
        MvcResult postResult = mockMvc.perform(post(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(carDTO)))
                .andReturn();
        String location = postResult.getResponse().getHeader("Location");

        //when
        ResultActions paymentDetailsResult = mockMvc.perform(get(location + "/payment")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        paymentDetailsResult
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPaymentDetails_ParkingNotExist_NotFoundReturned() throws Exception {
        //given
        //no parking

        //when
        ResultActions paymentDetailsResult = mockMvc.perform(get("http://localhost/api/parking/999" + "/payment")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        paymentDetailsResult
                .andExpect(status().isNotFound());
    }

}
