package pl.yahoo.pawelpiedel.Parking.service.place;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.repository.PlaceRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PlaceServiceImplTest {
    @Autowired
    private PlaceService placeService;

    @MockBean
    private PlaceRepository placeRepository;

    @Test
    public void getAvailablePlaces_PlaceNotAvailable_EmptyListReturned() {
        //given
        when(placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE)).thenReturn(Collections.emptyList());

        //when
        List<Place> availablePlaces = placeService.getAvailablePlaces();

        //then
        assertTrue(availablePlaces.isEmpty());
    }

    @TestConfiguration
    static class PlaceServiceImplTestConfiguration {
        @Bean
        public PlaceService placeService(PlaceRepository placeRepository) {
            return new PlaceServiceImpl(placeRepository);
        }
    }
}