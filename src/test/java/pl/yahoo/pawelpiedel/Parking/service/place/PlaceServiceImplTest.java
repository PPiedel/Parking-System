package pl.yahoo.pawelpiedel.Parking.service.place;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.repository.PlaceRepository;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class PlaceServiceImplTest {
    @Autowired
    private PlaceService placeService;

    @MockBean
    private PlaceRepository placeRepository;

    @Test
    public void isAnyPlaceAvailable_AllPlacesNotAvailable_FalseReturned() {
        //given
        when(placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE)).thenReturn(Collections.emptyList());

        //when
        boolean isAnyPlaceAvailable = placeService.isAnyPlaceAvailable();

        //then
        assertFalse(isAnyPlaceAvailable);
    }

    @TestConfiguration
    static class PlaceServiceImplTestConfiguration {
        @Bean
        public PlaceService placeService(PlaceRepository placeRepository) {
            return new PlaceServiceImpl(placeRepository);
        }
    }
}