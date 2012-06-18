package com.pocketcookies.ctf.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.pocketcookies.ctf.shared.MapAreas;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author john.a.edmonds@gmail.com (John Edmonds)
 */
public class CTFClient implements EntryPoint {
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);

    private boolean mapsFinishedLoading = false;
    private MapAreas mapAreas = null;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final Grid grid = new Grid(2, 2);
        RootPanel.get("loading").add(grid);
        grid.setWidget(0, 0, new Image("images/spinner.gif"));
        grid.setText(0, 1, "Loading maps...");
        grid.setWidget(1, 0, new Image("images/spinner.gif"));
        grid.setText(1, 1, "Loading map areas...");

        greetingService.getMapAreas(new AsyncCallback<MapAreas>() {
            @Override
            public void onSuccess(MapAreas result) {
                mapAreas = result;
                grid.setWidget(1, 0, new Image("images/check.png"));
                if (isFinishedLoading()) {
                    onFinishedLoading();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                grid.setWidget(1, 0, new Image("images/x.png"));
            }
        });
        Maps.loadMapsApi("", "2", true, new Runnable() {
            @Override
            public void run() {
                mapsFinishedLoading = true;
                grid.setWidget(0, 0, new Image("images/check.png"));
                if (isFinishedLoading()) {
                    onFinishedLoading();
                }
            }
        });
    }

    private boolean isFinishedLoading() {
        return mapsFinishedLoading && mapAreas != null;
    }

    private void onFinishedLoading() {
        assert mapAreas != null : "onFinishedLoading called before map areas finished loading";
        assert mapsFinishedLoading : "onFinishedLoading called before maps finished loading.";

        final MapWidget map = new MapWidget(mapAreas.getPinkZone().get(0), 16);
        map.setSize("100%", "100%");
        DOM.getElementById("loading").removeFromParent();
        RootLayoutPanel.get().add(map);

        Geolocation.getIfSupported().getCurrentPosition(
                new Callback<Position, PositionError>() {
                    @Override
                    public void onSuccess(Position result) {
                        final Marker userLocation = new Marker(LatLng
                                .newInstance(result.getCoordinates()
                                        .getLatitude(), result.getCoordinates()
                                        .getLongitude()));
                        map.addOverlay(userLocation);
                        // Now that we have achieved success, we can start
                        // watching the position too.
                        // Note that this won't work until
                        // https://code.google.com/p/google-web-toolkit/source/detail?spec=svn11088&r=10858
                        // goes in.
                        Geolocation.getIfSupported().watchPosition(
                                new Callback<Position, PositionError>() {
                                    @Override
                                    public void onSuccess(Position result) {
                                        userLocation.setLatLng(LatLng
                                                .newInstance(result
                                                        .getCoordinates()
                                                        .getLatitude(), result
                                                        .getCoordinates()
                                                        .getLongitude()));
                                    }

                                    @Override
                                    public void onFailure(PositionError reason) {
                                        // TODO Auto-generated method stub
                                    }
                                });
                    }

                    @Override
                    public void onFailure(PositionError reason) {
                        // TODO Auto-generated method stub

                    }
                });

        addOverlays(map);
    }

    private void addOverlays(MapWidget map) {
        map.addOverlay(new Polygon(mapAreas.getPinkZone().toArray(
                new LatLng[mapAreas.getPinkZone().size()]), "#000000", 1, .5,
                "#ffaaaa", .5));
        map.addOverlay(new Polygon(mapAreas.getYellowZone().toArray(
                new LatLng[mapAreas.getYellowZone().size()]), "#000000", 1, .5,
                "#ffffaa", .5));
        map.addOverlay(new Polygon(mapAreas.getPinkFlagZone().toArray(
                new LatLng[mapAreas.getPinkFlagZone().size()]), "#000000", 1,
                .5, "#ffaaaa", .5));
        map.addOverlay(new Polygon(mapAreas.getYellowFlagZone().toArray(
                new LatLng[mapAreas.getYellowFlagZone().size()]), "#000000", 1,
                .5, "#ffffaa", .5));
    }
}
