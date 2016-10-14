package mx.unam.ciencias.jcasas.party;

import android.app.AlertDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by jcasas on 13/10/16.
 * Protected class {@link EventParser} for reading XML files related to an event.
 * The XML Event files are like this
 *
 * <event name="EVENT NAME" date="DATE">
 *  <info>
 *   DESCRIPTION
 *
 *  </info>
 *
 *  <guests number="NUMBER OF GUESTS">
 *      <item id="GUEST ID"
 *      name="GUEST NAME"
 *      photo="./photos/guests/id.jpg"/>
 *  </guests>
 *  <address>
 *  <addressinfo>
 *      HOW TO GET TO THE SPOT
 *  </addressinfo>
 *  </address>
 *  </event>
 */

public class EventParser {

    private XmlPullParserFactory xmlFactoryObject;
    private XmlPullParser xmlParser;
    private InputStream is;
    private String eventname;
    private String date;
    private String info;
    private String addressInfo;
    private LinkedList<Guest> guests;

    public String getEventname() {
        return eventname;
    }

    public String getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public LinkedList<Guest> getGuests() {
        return guests;
    }

    public EventParser(InputStream is) {
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlParser = xmlFactoryObject.newPullParser();
            this.is = is;
            guests = new LinkedList<>();
        } catch (XmlPullParserException xppe) {

        }
    }

    /**
     * Method for constructing {@link XmlPullParser} object.
     */
    public void construct() throws XmlPullParserException, IOException {
        /* Setting the file to xmlParser. */
        xmlParser.setInput(is, null);
        int event = xmlParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (xmlParser.getName().equals("event")) {
                        eventname = xmlParser.getAttributeValue(null,"name");
                        date = xmlParser.getAttributeValue(null,"date");
                    } else if (xmlParser.getName().equals("info")) {

                    }

            }
            event = xmlParser.next();
        }
    }

}
