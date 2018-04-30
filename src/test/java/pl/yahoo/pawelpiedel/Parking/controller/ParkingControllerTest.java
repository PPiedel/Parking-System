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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.yahoo.pawelpiedel.Parking.domain.Car;
import pl.yahoo.pawelpiedel.Parking.domain.driver.Driver;
import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.dto.ParkingStopTimeOnlyDTO;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingNotFoundException;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ParkingControllerTest.class);
    private static final String API_BASE_URL = "/api/parking";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @MockBean
    private CarService carService;

    @MockBean
    private PlaceService placeService;

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
        when(parkingService.getOngoingParkings(car)).thenReturn(openedParkings);

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
        Car car = new Car(driverMock, licensePlateNumber);
        CarDTO carDTO = new CarDTO(licensePlateNumber);
        when(parkingService.getOngoingParkings(car)).thenReturn(Collections.emptyList());
        when(placeService.getAvailablePlaces()).thenReturn(Collections.singletonList(placeMock));
        when(placeService.getNextFreePlace()).thenReturn(placeMock);
        Parking parking = new Parking(car, placeMock);
        Long parkingId = 1L;
        parking.setId(parkingId);
        when(parkingService.save(ArgumentMatchers.any())).thenReturn(parking);

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
        List<Parking> openedParkings = Collections.singletonList(parkingMock);
        when(parkingService.getOngoingParkings(car)).thenReturn(Collections.emptyList());
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
        Long testId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();

        ParkingStopTimeOnlyDTO parkingStopTimeOnlyDTO = new ParkingStopTimeOnlyDTO(localDateTime.toString());
        when(parkingService.save(any(LocalDateTime.class), eq(testId))).thenReturn(parkingMock);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(parkingStopTimeOnlyDTO)));

        //then
        resultActions
                .andExpect(status().isOk());

    }

    @Test
    public void stopParkMeter_ParkingNotExist_NotFoundReturned() throws Exception {
        //given
        Long notExistingId = 999L;
        LocalDateTime localDateTime = LocalDateTime.now();
        ParkingStopTimeOnlyDTO parkingStopTimeOnlyDTO = new ParkingStopTimeOnlyDTO(localDateTime.toString());
        when(parkingService.save(any(LocalDateTime.class), eq(notExistingId))).thenThrow(ParkingNotFoundException.class);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(parkingStopTimeOnlyDTO)));

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
        ParkingStopTimeOnlyDTO parkingStopTimeOnlyDTO = new ParkingStopTimeOnlyDTO(null);

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
        ParkingStopTimeOnlyDTO parkingStopTimeOnlyDTO = new ParkingStopTimeOnlyDTO("");

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
        ParkingStopTimeOnlyDTO parkingStopTimeOnlyDTO = new ParkingStopTimeOnlyDTO(dateTime);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch(API_BASE_URL + "/" + anyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString("")));

        //then
        resultActions
                .andExpect(status().is4xxClientError());
    }

    @TestConfiguration
    static class MoviesControllerTestConfiguration {
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