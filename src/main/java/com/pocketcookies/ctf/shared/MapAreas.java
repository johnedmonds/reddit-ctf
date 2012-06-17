package com.pocketcookies.ctf.shared;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.maps.client.geom.LatLng;

/**
 * Stores the areas on a map.
 * 
 * @author john.a.edmonds@gmail.com (John Edmonds)
 *
 */
public class MapAreas implements Serializable {
    public static class Point implements Serializable {
        private double lat;
        private double lng;

        public Point(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public Point() {
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    // What's considered "in" for the yellow team.
    private ImmutableList<Point> yellowZone;
    // What's considered "in" for the pink team.
    private ImmutableList<Point> pinkZone;
    // Where the yellow team's flag can be placed.
    private ImmutableList<Point> yellowFlagZone;
    // Where the pink team's flag can be placed.
    private ImmutableList<Point> pinkFlagZone;

    public MapAreas(List<Point> yellowZone, List<Point> pinkZone,
            List<Point> yellowFlagZone, List<Point> pinkFlagZone) {
        this.yellowZone = ImmutableList.<Point> copyOf(yellowZone);
        this.pinkZone = ImmutableList.<Point> copyOf(pinkZone);
        this.yellowFlagZone = ImmutableList.<Point> copyOf(yellowFlagZone);
        this.pinkFlagZone = ImmutableList.<Point> copyOf(pinkFlagZone);
    }

    public MapAreas() {
    }

    public List<LatLng> getYellowZone() {
        return toGoogleMapsLatLng(yellowZone);
    }

    public List<LatLng> getPinkZone() {
        return toGoogleMapsLatLng(pinkZone);
    }

    public List<LatLng> getYellowFlagZone() {
        return toGoogleMapsLatLng(yellowFlagZone);
    }

    public List<LatLng> getPinkFlagZone() {
        return toGoogleMapsLatLng(pinkFlagZone);
    }

    private static final Function<Point, LatLng> TO_GOOGLE_MAPS_LAT_LNG = new Function<Point, LatLng>() {
        @Override
        public LatLng apply(Point arg0) {
            return LatLng.newInstance(arg0.getLat(), arg0.getLng());
        }
    };

    private static List<com.google.gwt.maps.client.geom.LatLng> toGoogleMapsLatLng(
            List<Point> coordinates) {
        return Lists.transform(coordinates, TO_GOOGLE_MAPS_LAT_LNG);
    }
}
