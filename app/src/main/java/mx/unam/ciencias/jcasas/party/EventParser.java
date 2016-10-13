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
 *  <info>
 *      HOW TO GET TO THE SPOT
 *  </info>
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
        String name = "";
        while (event != XmlPullParser.END_DOCUMENT)
        {
            name = xmlParser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(name.equals("event")){
                        eventname = xmlParser.getAttributeValue(null,"name");
                        date = xmlParser.getAttributeValue(null,"date");
                    } else if (name.equals("guests")) {
                        event = xmlParser.next();
                        name = xmlParser.getName();
                        while (name.equals("item")) {
                            int id = Integer.parseInt(xmlParser.getAttributeValue(null,"id"));
                            String guestName = xmlParser.getAttributeValue(null,"name");
                            guests.add(new Guest(guestName,id));
                            xmlParser.next();
                            name = xmlParser.getName();
                        }
                    } else if (name.equals("address")) {
                        xmlParser.next();
                        xmlParser.next();
                        addressInfo = xmlParser.getText();
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (name.equals("info")) {
                        info = xmlParser.getText();
                    }
                case XmlPullParser.END_TAG:

                    break;
            }
            event = xmlParser.next();
        }
    }

}
