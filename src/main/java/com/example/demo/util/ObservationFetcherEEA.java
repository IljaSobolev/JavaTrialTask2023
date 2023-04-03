package com.example.demo.util;

import java.util.HashMap;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.IOException;
import java.lang.Exception;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import com.example.demo.modal.Observation;
import com.example.demo.util.XMLParser;

/**
  * The {@link com.example.demo.util.ObservationFetcher} specific to the interface at <a>https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php</a>
  */
public class ObservationFetcherEEA implements ObservationFetcher {
    /**
      * Implements the method described at {@link com.example.demo.util.ObservationFetcher}
      */
    @Override
    public HashMap<String, Observation> fetch() {
        URL url;
        InputStream is = null;
        try {
            url = new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
            is = url.openConnection().getInputStream();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();

        XMLParser handler = new XMLParser();

        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return handler.getObservations();
    }
}
