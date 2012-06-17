package com.pocketcookies.ctf.server;

import java.io.InputStream;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pocketcookies.ctf.client.GreetingService;
import com.pocketcookies.ctf.shared.MapAreas;
import com.pocketcookies.ctf.shared.MapAreas.Point;

/**
 * The server side implementation of the RPC service.
 * 
 * @author john.a.edmonds@gmail.com (John Edmonds)
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
        GreetingService {

    private MapAreas mapAreas;

    @Override
    public void init() throws ServletException {
        super.init();
        final InputStream stream = getServletContext().getResourceAsStream(
                "/WEB-INF/ctf-areas.xml");
        assert stream != null;
        try {
            mapAreas = loadMapAreas(DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(stream));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private MapAreas loadMapAreas(Document doc) {
        ImmutableList<Point> yellowArea = null;
        ImmutableList<Point> pinkArea = null;
        ImmutableList<Point> yellowFlagArea = null;
        ImmutableList<Point> pinkFlagArea = null;
        final NodeList areas = doc.getElementsByTagName("area");
        for (int i = 0; i < areas.getLength(); i++) {
            ImmutableList.Builder<Point> builder = ImmutableList.builder();
            final Element area = (Element) areas.item(i);
            final String areaName = area.getAttribute("name");
            final String coordinatesText = ((Element) area
                    .getElementsByTagName("coordinates").item(0))
                    .getTextContent();
            final String[] coordinateLines = coordinatesText.split("\n");
            for (String coordinateLine : coordinateLines) {
                final String trimmedCoordinates = coordinateLine.trim();
                if (trimmedCoordinates.isEmpty()) {
                    continue;
                }
                String[] coordinates = trimmedCoordinates.split(",");
                builder.add(new Point(Double.parseDouble(coordinates[1]),
                        Double.parseDouble(coordinates[0])));
            }
            if ("Yellow Area".equals(areaName)) {
                yellowArea = builder.build();
            } else if ("Pink Area".equals(areaName)) {
                pinkArea = builder.build();
            } else if ("Yellow Flag Area".equals(areaName)) {
                yellowFlagArea = builder.build();
            } else if ("Pink Flag Area".equals(areaName)) {
                pinkFlagArea = builder.build();
            } else {
                throw new IllegalArgumentException("No such area: " + areaName);
            }
        }
        assert yellowArea != null : "Yellow area is null.";
        assert pinkArea != null : "Pink area is null.";
        assert yellowFlagArea != null : "Yellow flagarea is null.";
        assert pinkFlagArea != null : "Pink flag area is null.";
        return new MapAreas(yellowArea, pinkArea, yellowFlagArea, pinkFlagArea);
    }

    @Override
    public MapAreas getMapAreas() {
        return mapAreas;
    }
}
