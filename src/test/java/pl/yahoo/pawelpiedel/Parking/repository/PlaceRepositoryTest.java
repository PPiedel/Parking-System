package pl.yahoo.pawelpiedel.Parking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.yahoo.pawelpiedel.Parking.domain.place.Place;
import pl.yahoo.pawelpiedel.Parking.domain.place.PlaceStatus;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class PlaceRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void findByPlaceStatus_AvailablePlacesInDB_ListOfPlacesReturned() {
        //given
        Place place1 = new Place(PlaceStatus.AVAILABLE);
        Place place2 = new Place(PlaceStatus.AVAILABLE);
        Place place3 = new Place(PlaceStatus.AVAILABLE);

        testEntityManager.persist(place1);
        testEntityManager.persist(place2);
        testEntityManager.persist(place3);
        testEntityManager.flush();

        //when
        List<Place> availablePlaces = placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE);

        //then
        assertThat(availablePlaces, hasSize(3));
    }

    @Test
    public void findbyPlaceStatus_NoAvailablePlaces_EmptyListReturned() {
        //given
        Place place1 = new Place(PlaceStatus.TAKEN);
        Place place2 = new Place(PlaceStatus.TAKEN);
        Place place3 = new Place(PlaceStatus.TAKEN);

        testEntityManager.persist(place1);
        testEntityManager.persist(place2);
        testEntityManager.persist(place3);
        testEntityManager.flush();

        //when
        List<Place> availablePlaces = placeRepository.findByPlaceStatusIs(PlaceStatus.AVAILABLE);

        //then
        assertThat(availablePlaces, hasSize(0));
    }
}