package pl.yahoo.pawelpiedel.Parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.driver.DriverType;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.parking.ParkingStatus;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Money;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopDTO;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.driver.DriverService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingNotFoundException;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ParkingControllerTest.class);
    private static final String API_BASE_URL = "/api/parking";
    private static final String DEFAULT_CURRENCY = "PLN";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @MockBean
    private CarService carService;

    @MockBean
    private PlaceService placeService;

    @MockBean
    private DriverService driverService;

    @Mock
    private Driver driverMock;

    @Mock
    private Parking parkingMock;

    @Mock
    private Place placeMock;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void startParkMeter_CarAlreadyParked_BadRequestReturned() throws Exception {
        //given
        String licensePlateNumber = "XYZ123";
        Car car = new Car(driverMock, licensePlateNumber);
        CarDTO carDTO = new CarDTO(licensePlateNumber);
        List<Parking> openedParkings = Collections.singletonList(parkingMock);
        when(parkingService.isCarAlreadyParked(car.getLicensePlateNumber())).thenReturn(true);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().isBadRequest());

    }

    @Test
    public void startParkMeter_ValidCarDTOPassed_CreatedStatusWithLocationReturned() throws Exception {
        //given
        String licensePlateNumber = "XYZ123";
        Driver driver = new Driver(DriverType.REGULAR);
        Car car = new Car(driver, licensePlateNumber);
        driver.setCars(Collections.singletonList(car));

        CarDTO carDTO = new CarDTO(licensePlateNumber);
        when(parkingService.isCarAlreadyParked(car.getLicensePlateNumber())).thenReturn(false);
        when(placeService.getAvailablePlaces()).thenReturn(Collections.singletonList(placeMock));
        when(placeService.findPlaceForParking()).thenReturn(placeMock);
        when(driverService.save(any())).thenReturn(driver);
        Parking parking = new Parking(car, placeMock);
        Long parkingId = 1L;
        parking.setId(parkingId);
        car.setParkings(Collections.singletonList(parking));
        when(carService.save(ArgumentMatchers.any())).thenReturn(car);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost" + API_BASE_URL + "/" + parkingId)));

    }

    @Test
    public void startParkMeter_CarDTOWithEmptyLicensePlateNumber_ClientErrorReturned() throws Exception {
        //given
        CarDTO carDTO = new CarDTO();
        carDTO.setLicensePlateNumber("");

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void startParkMeter_CarDTOWithNullLicensePlateNumber_ClientErrorReturned() throws Exception {
        //given
        String nullLicensePlateNumber = null;
        CarDTO carDTO = new CarDTO(nullLicensePlateNumber);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void startParkMeter_EmptyJSONPassed_ClientErrorReturned() throws Exception {
        //given
        String emptyJSON = "";

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJSON));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void startParkMeter_NoPlacesAvailable_BadRequestReturned() throws Exception {
        //given
        String licensePlateNumber = "XYZ123";
        Car car = new Car(driverMock, licensePlateNumber);
        CarDTO carDTO = new CarDTO(licensePlateNumber);
        when(parkingService.isCarAlreadyParked(car.getLicensePlateNumber())).thenReturn(false);
        when(placeService.getAvailablePlaces()).thenReturn(Collections.emptyList());

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    public void stopParkMeter_ValidParkingStopTimeDTOPassed_ParkingInDb_EntityUpdated() throws Exception {
        //given
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "XYZ123";
        Car car = new Car(driver, licensePlateNumber);
        driver.setCars(Collections.singletonList(car));
        Long testId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Place place = new Place(PlaceStatus.TAKEN);
        Parking parking = new Parking(car, place);
        parking.setId(testId);

        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(localDateTime.toString(), DEFAULT_CURRENCY);
        Parking updated = new Parking(car, place);
        updated.setId(testId);
        updated.setStopTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        when(parkingService.findParkingById(testId)).thenReturn(Optional.of(updated));

        //when
        ResultActions resultActions = mockMvc.perform(patch(API_BASE_URL + "/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(parkingStopDTO)));

        //then
        resultActions
                .andExpect(status().isOk());

    }

    @Test
    public void stopParkMeter_ParkingNotExist_NotFoundReturned() throws Exception {
        //given
        Long notExistingId = 999L;
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(localDateTime.toString(), DEFAULT_CURRENCY);
        when(parkingService.saveStopTime(any(LocalDateTime.class), eq(notExistingId))).thenThrow(ParkingNotFoundException.class);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(parkingStopDTO)));

        //then
        resultActions
                .andExpect(status().isNotFound());

    }

    @Test
    public void stopParkMeter_EmptyJSONPassed_ClientErrorReturned() throws Exception {
        //given
        Long anyId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + anyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString("")));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void stopParkMeter_NullDateTimePassed_ClientErrorReturned() throws Exception {
        //given
        Long anyId = 1L;
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(null, DEFAULT_CURRENCY);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + anyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString("")));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void stopParkMeter_EmptyDateTimePassed_ClientErrorReturned() throws Exception {
        //given
        Long anyId = 1L;
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO("", DEFAULT_CURRENCY);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + anyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString("")));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void stopParkMeter_DateTimeInWrongFormatPassed_ClientErrorReturned() throws Exception {
        //given
        Long anyId = 1L;
        String dateTime = "1994-02-02'T'";
        ParkingStopDTO parkingStopDTO = new ParkingStopDTO(dateTime, DEFAULT_CURRENCY);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + anyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString("")));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getPaymentDetails_PaymentAssigned_PaymentDetailsReturned() throws Exception {
        //given
        long testId = 1L;
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "testPlate";
        Car car = new Car(driver, licensePlateNumber);
        driver.setCars(Collections.singletonList(car));

        Place place = new Place(PlaceStatus.AVAILABLE);
        long placeId = 2L;
        place.setId(placeId);
        Parking parking = new Parking(car, place);
        parking.setStopTime(LocalDateTime.now());
        parking.setParkingStatus(ParkingStatus.COMPLETED);
        Money money = new Money(new BigDecimal(1.5), Currency.getInstance("PLN"));
        parking.setPayment(new Payment(parking, money));
        car.addParking(parking);

        when(parkingService.findParkingById(testId)).thenReturn(Optional.of(parking));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_BASE_URL + "/" + testId + "/payment").contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.money.amount").value(money.getAmount().toString()))
                .andExpect(jsonPath("$.money.currency").value(money.getCurrency().toString()))
                .andExpect(jsonPath("$.parking.place.id").value(place.getId()))
                .andExpect(jsonPath("$.parking.car.licensePlateNumber").value(licensePlateNumber))
                .andExpect(jsonPath("$.parking.startTime").value(parking.getStartTime().toString()))
                .andExpect(jsonPath("$.parking.stopTime").value(parking.getStopTime().toString()))
                .andExpect(jsonPath("$.parking.parkingStatus").value(parking.getParkingStatus().toString()));
    }

    @Test
    public void getPaymentDetails__PaymentNotAssigned_NotFoundReturned() throws Exception {
        //given
        long testId = 1L;
        Driver driver = new Driver(DriverType.REGULAR);
        String licensePlateNumber = "testPlate";
        Car car = new Car(driver, licensePlateNumber);
        driver.setCars(Collections.singletonList(car));

        Place place = new Place(PlaceStatus.TAKEN);
        long placeId = 2L;
        place.setId(placeId);
        Parking parking = new Parking(car, place);
        parking.setStopTime(LocalDateTime.now());
        parking.setParkingStatus(ParkingStatus.ONGOING);
        car.addParking(parking);

        when(parkingService.findParkingById(testId)).thenReturn(Optional.of(parking));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_BASE_URL + "/" + testId + "/payment").contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getPaymentDetails__NotExistingParkingIdPassed_ClientErrorReturned() throws Exception {
        //given
        Long testId = 1L;
        when(parkingService.findParkingById(testId)).thenReturn(Optional.empty());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_BASE_URL + "/" + testId + "/payment").contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @TestConfiguration
    static class ParkingControllerTestConfiguration {
        @Bean
        ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        EntityDTOMapper entityDTOMapper(ModelMapper modelMapper, CarService carService) {
            return new EntityDTOMapper(modelMapper, carService);
        }
    }
}