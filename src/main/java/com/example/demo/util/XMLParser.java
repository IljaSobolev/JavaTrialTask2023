package com.example.demo.util;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.service.ObservationService;
import com.example.demo.modal.Observation;

/**
  * Does XML parsing specific to <a>https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php</a>
  */
public class XMLParser extends DefaultHandler {
    private String currentAttribute = "";
    private String currentCity = "";

    private HashMap<String, Observation> observations = new HashMap<>();

    private Observation currentObservation;
    private int currentTimestamp;

    /**
      * Returns all observations from the XML file.
      * @return the hashmap mapping city names to weather observations.
      */
    public HashMap<String, Observation> getObservations() {
        return observations;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String data = new String(Arrays.copyOfRange(ch, start, start + length));

        if(data.trim().isEmpty()) return;

        if(currentAttribute.equals("name")) {
            currentCity = data;
        }

        if(currentAttribute.equals("wmocode")) currentObservation.setWMOCode(data);
        else if(currentAttribute.equals("name")) currentObservation.setName(data);
        else if(currentAttribute.equals("airtemperature")) currentObservation.setAirTemperature(Float.parseFloat(data));
        else if(currentAttribute.equals("windspeed")) currentObservation.setWindSpeed(Float.parseFloat(data));
        else if(currentAttribute.equals("phenomenon")) currentObservation.setWeatherPhenomenon(data);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equals("observations")) {
            currentTimestamp = Integer.parseInt(attributes.getValue("timestamp"));
        }

        if(qName.equals("name")) {
            currentObservation = new Observation();
        }

        currentAttribute = qName;
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) {
        if(qName.equals("station")) {
            currentObservation.setTimestamp(Timestamp.valueOf(LocalDateTime.ofEpochSecond(currentTimestamp, 0, ZoneOffset.UTC)));
            observations.put(currentCity, currentObservation);
        }
    }
}
