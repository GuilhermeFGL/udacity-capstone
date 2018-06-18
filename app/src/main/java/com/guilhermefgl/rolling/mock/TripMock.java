package com.guilhermefgl.rolling.mock;

import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;

import java.util.ArrayList;

public class TripMock {

    private static Place startPlace = new Place() {{
        setPlaceName("Santos, SP - Brazil");
    }};
    private static Place endPlace = new Place() {{
        setPlaceName("Antofagasta - Chile");
    }};
    private static ArrayList<Place> places = new ArrayList<Place>() {{
        add(new Place() {{
            setPlaceName("Maringá PR - Brasil");
        }});
        add(new Place() {{
            setPlaceName("Assunção - Paraguai");
        }});
        add(new Place() {{
            setPlaceName("Juan José Castelli, CH - Argentina");
        }});
        add(new Place() {{
            setPlaceName("Salta - Argentina");
        }});
    }};

    public static ArrayList<Trip> getTripList() {
        return new ArrayList<Trip>() {{
            add(new Trip(){{
                setTripName("Atacama Desert");
                setTripBannerUrl("http://departmentofwandering.com/wp-content/uploads/2017/11/atacama.jpg");
                setTripDistance("3350 KM");
                setTripDuration("6 days");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
                setPersons(UserMock.getTripListUser());
            }});
            add(new Trip(){{
                setTripName("Route 66");
                setTripBannerUrl("https://www.mygrandcanyonpark.com/.image/c_limit%2Ccs_srgb%2Cq_auto:good%2Cw_860/MTUwNjU1NDA2NDM0MTY2MTM2/route-66-sunset_adobe_940.webp");
                setTripDistance("5750 KM");
                setTripDuration("15 days");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(new ArrayList<Place>());
                setPersons(UserMock.getTripListUser());
            }});
            add(new Trip(){{
                setTripName("South Island Circuit");
                setTripBannerUrl("https://i.pinimg.com/originals/46/90/35/469035b340f59ae1765caf1b169b2649.jpg");
                setTripDistance("2500 KM");
                setTripDuration("2 days");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
                setPersons(UserMock.getTripListUser());
            }});
        }};
    }

    public static ArrayList<Trip> getMyTripList() {
        return new ArrayList<Trip>() {{
            add(new Trip(){{
                setTripName("Atacama Desert");
                setTripBannerUrl("http://departmentofwandering.com/wp-content/uploads/2017/11/atacama.jpg");
                setTripDistance("3350 KM");
                setTripDuration("6 days");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
                setPersons(UserMock.getTripListUser());
            }});
            add(new Trip(){{
                setTripName("South Island Circuit");
                setTripBannerUrl("https://i.pinimg.com/originals/46/90/35/469035b340f59ae1765caf1b169b2649.jpg");
                setTripDistance("2500 KM");
                setTripDuration("2 days");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
                setPersons(UserMock.getTripListUser());
            }});
        }};
    }

    public static Trip getMyCurrentTrip() {
        return new Trip(){{
            setTripName("Atacama Desert");
            setTripDistance("3350 KM");
            setTripDuration("6 days");
            setTripBannerUrl("http://departmentofwandering.com/wp-content/uploads/2017/11/atacama.jpg");
            setPlaceStart(startPlace);
            setPlaceEnd(endPlace);
            setPlacesPoints(places);
            setPersons(UserMock.getTripListUser());
        }};
    }
}
