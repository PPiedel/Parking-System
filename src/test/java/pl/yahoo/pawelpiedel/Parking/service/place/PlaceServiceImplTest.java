package pl.yahoo.pawelpiedel.Parking.service.place;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;
import pl.yahoo.pawelpiedel.Parking.repository.PlaceRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PlaceServiceImplTest {
    @Autowired
    private PlaceService placeService;

    @MockBean
    private PlaceRepository placeRepository;

    @Mock
    private Place placeMock;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAvailablePlaces_PlaceNotAvailable_EmptyListReturned() {
        //given
        when(placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE)).thenReturn(Collections.emptyList());

        //when
        List<Place> availablePlaces = placeService.getAvailablePlaces();

        //then
        assertTrue(availablePlaces.isEmpty());
    }

    @Test
    public void save_PlacePassed_SavedReturned() {
        //given
        when(placeRepository.save(any())).thenReturn(placeMock);

        //when
        Place saved = placeService.save(new Place());

        //then
        assertEquals(placeMock, saved);
    }

    @Test
    public void getPlaceForParking_ListOfPlacesPassed_PlaceAtFirstIndexReturned() {
        //given
        List<Place> availablePlaces = Collections.singletonList(placeMock);

        //when
        Place choosen = placeService.getPlaceForParking(availablePlaces);

        //then
        assertEquals(placeMock, choosen);
    }

    @Test
    public void releasePlace_PlacePassed_PlaseReleasedReturned() {
        //given
        Place place = new Place(PlaceStatus.TAKEN);

        //when
        Place released = placeService.releasePlace(place);

        //then
        assertEquals(released.getPlaceStatus(), PlaceStatus.AVAILABLE);
    }

    @TestConfiguration
    static class PlaceServiceImplTestConfiguration {
        @Bean
        public PlaceService placeService(PlaceRepository placeRepository) {
            return new PlaceServiceImpl(placeRepository);
        }
    }
}