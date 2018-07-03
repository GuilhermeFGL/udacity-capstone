package com.guilhermefgl.rolling.mock;

import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TripMock {

    private static Place startPlace = new Place() {{
        setPlaceName("Santos, SP - Brazil");
        setPlaceLatitude(-23.8642341);
        setPlaceLongitude(-46.3602693);
    }};
    private static Place endPlace = new Place() {{
        setPlaceName("Antofagasta - Chile");
        setPlaceLatitude(-23.6283541);
        setPlaceLongitude(-70.4747683);
    }};
    private static ArrayList<Place> places = new ArrayList<Place>() {{
        add(new Place() {{
            setPlaceName("Maringá PR - Brasil");
            setPlaceLatitude(-23.4108875);
            setPlaceLongitude(-52.0406484);
        }});
        add(new Place() {{
            setPlaceName("Assunção - Paraguai");
            setPlaceLatitude(-25.2968485);
            setPlaceLongitude(-57.6331085);
        }});
        add(new Place() {{
            setPlaceName("Juan José Castelli, CH - Argentina");
            setPlaceLatitude(-25.9515881);
            setPlaceLongitude(-60.6393901);
        }});
        add(new Place() {{
            setPlaceName("Salta - Argentina");
            setPlaceLatitude(-24.7960685);
            setPlaceLongitude(-65.5006684);
        }});
    }};

    public static ArrayList<Trip> getTripList() {
        try {
            return new ArrayList<Trip>() {{
                add(new Trip(){{
                    setTripName("Atacama Desert");
                    setTripBannerUrl("http://departmentofwandering.com/wp-content/uploads/2017/11/atacama.jpg");
                    setTripDistance("3350 KM");
                    setTripDuration("6 days");
                    setTripDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("22/03/2020 12:30"));
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
                    setTripDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/01/2022 12:30"));
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
                    setTripDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/12/2020 12:30"));
                    setPlaceStart(startPlace);
                    setPlaceEnd(endPlace);
                    setPlacesPoints(places);
                    setPersons(UserMock.getTripListUser());
                }});
            }};
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<Trip> getMyTripList() {
        try {
            return new ArrayList<Trip>() {{
                add(new Trip(){{
                    setTripName("Atacama Desert");
                    setTripBannerUrl("http://departmentofwandering.com/wp-content/uploads/2017/11/atacama.jpg");
                    setTripDistance("3350 KM");
                    setTripDuration("6 days");
                    setTripDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("22/03/2020 12:30"));
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
                    setTripDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("12/12/2020 12:30"));
                    setPlaceStart(startPlace);
                    setPlaceEnd(endPlace);
                    setPlacesPoints(places);
                    setPersons(UserMock.getTripListUser());
                }});
            }};
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    public static Trip getMyCurrentTrip() {
        try {
            return new Trip() {{
                setTripName("Atacama Desert");
                setTripDistance("3350 KM");
                setTripDuration("6 days");
                setTripDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("22/03/2020 12:30"));
                setTripBannerUrl("http://departmentofwandering.com/wp-content/uploads/2017/11/atacama.jpg");
                setPlaceStart(startPlace);
                setPlaceEnd(endPlace);
                setPlacesPoints(places);
                setPersons(UserMock.getTripListUser());
            }};
        } catch (Exception ignored) {
            return new Trip();
        }
    }
}
