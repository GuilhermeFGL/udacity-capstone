package com.guilhermefgl.rolling.mock;

import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;

import java.util.ArrayList;

public class TripMock {

    private Place startPlace = new Place() {{
        setPlaceName("Start Place");
    }};
    private Place endPlace = new Place() {{
        setPlaceName("End Place");
    }};
    private ArrayList<Place> places = new ArrayList<Place>() {{
        add(new Place() {{
            setPlaceName("Break Point 1");
        }});
        add(new Place() {{
            setPlaceName("Break Point 2");
        }});
        add(new Place() {{
            setPlaceName("Break Point 3");
        }});
    }};

    public ArrayList<Trip> getTripList() {
        return new ArrayList<Trip>() {{
            add(new Trip(){{
                setTripName("Trip 1");
                setTripBannerUrl("");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
            }});
            add(new Trip(){{
                setTripName("Trip 2");
                setTripBannerUrl("");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
            }});
            add(new Trip(){{
                setTripName("Trip 3");
                setTripBannerUrl("");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
            }});
        }};
    }

    public ArrayList<Trip> getMyTripList() {
        return new ArrayList<Trip>() {{
            add(new Trip(){{
                setTripName("My Trip 1");
                setTripBannerUrl("");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
            }});
            add(new Trip(){{
                setTripName("My Trip 2");
                setTripBannerUrl("");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
            }});
        }};
    }

    public Trip getMyCurrentTrip() {
        return new Trip(){{
            setTripName("My Trip 1");
            setTripBannerUrl("");
            setPlaceStart(startPlace);
            setPlaceEnd(endPlace);
            setPlacesPoints(places);
        }};
    }
}
