package pl.yahoo.pawelpiedel.Parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.dto.CarDTO;
import pl.yahoo.pawelpiedel.Parking.dto.EntityDTOMapper;
import pl.yahoo.pawelpiedel.Parking.service.car.CarService;
import pl.yahoo.pawelpiedel.Parking.service.parking.ParkingService;
import pl.yahoo.pawelpiedel.Parking.service.place.PlaceService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ParkingController.class)
public class ParkingControllerTest {
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
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
    public void startParkMeter_CarWithoutLicensePlateNumber_ClientErrorReturned() throws Exception {
        //given
        CarDTO carDTO = new CarDTO();

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
    public void startParkMeter_CarNullLicensePlateNumber_ClientErrorReturned() throws Exception {
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
    public void startParkMeter_NoPlacesAvailable_BadRequestReturned() throws Exception {
        //given
        String licensePlateNumber = "XYZ123";
        Car car = new Car(driverMock, licensePlateNumber);
        CarDTO carDTO = new CarDTO(licensePlateNumber);
        List<Parking> openedParkings = Collections.singletonList(parkingMock);
        when(parkingService.getOngoingParkings(car)).thenReturn(Collections.emptyList());
        when(placeService.getPlacesWithStatus(PlaceStatus.AVAILABLE)).thenReturn(Collections.emptyList());

        //when
        ResultActions resultActions = mockMvc.perform(
                post(API_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(carDTO)));

        //then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class MoviesControllerTestConfiguration {
        @Bean
        ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        EntityDTOMapper entityDTOMapper(ModelMapper modelMapper) {
            return new EntityDTOMapper(modelMapper);
        }
    }
}