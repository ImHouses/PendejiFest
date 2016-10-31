package mx.unam.ciencias.jcasas.party;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by jcasas on 13/10/16.
 * @author Juan Casas imhouses@ciencias.unam.mx
 * Class for managing the events for the application.
 * The class {@link Event} provides methods for view the guest list, the event info and
 * some other info related with.
 */

public class Event {

    private class EventParser {
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
         */


        private XmlPullParserFactory xmlFactoryObject;
        private XmlPullParser xmlParser;
        private InputStream is;
        private String eventname;
        private String date;
        private String info;
        private String addressInfo;
        private String address;
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

        public String getEventname() {
            return this.eventname;
        }

        public String getDate() {
            return this.date;
        }

        public String getInfo() {
            return this.info;
        }

        public String getAddressInfo() {
            return this.addressInfo;
        }

        public LinkedList<Guest> getGuests() {
            return this.guests;
        }

        public String getAddress() {
            return this.address;
        }

        /**
         * Method for constructing {@link XmlPullParser} object.
         */
        public void construct() throws XmlPullParserException, IOException {
        /* Setting the file to xmlParser. */
            xmlParser.setInput(is, null);
            int event = xmlParser.getEventType();
            String TAG_NAME = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                TAG_NAME = xmlParser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlParser.getName().equals("event")) {
                            eventname = xmlParser.getAttributeValue(null, "name");
                            date = xmlParser.getAttributeValue(null, "date");
                        } else if (xmlParser.getName().equals("address")) {
                            address = xmlParser.getAttributeValue(null,"value");
                        } else if (TAG_NAME.equals("info")) {
                            xmlParser.next();
                            info = xmlParser.getText();
                        } else if (TAG_NAME.equals("addressinfo")) {
                            xmlParser.next();
                            addressInfo = xmlParser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;

                }
                event = xmlParser.next();
            }
        }

        private void invite(XmlPullParser xps) {
            try {
                xps.next();
                String name = xps.getName();
                int event = xps.getEventType();
                while (!name.equals(guests)) {
                /* Para que amarre. */
                    if (event == XmlPullParser.START_TAG) {
                        int guestId = Integer.parseInt(xps.getAttributeValue(null, "id"));
                        String guestName = xps.getAttributeValue(null, "name");
                        guests.add(new Guest(guestName, guestId));
                    }
                    event = xps.next();
                }
            } catch (XmlPullParserException xppe) {
                xppe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        /**
         * Gets the RAW xml, just text.
         * @return the string that contains the xml.
         */
        public String rawXml() {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            String s;
            String xml = "";
            try {
                while ((s = br.readLine()) != null) {
                    xml += s;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return xml;
        }
    }
    /* The parser of the event. */
    private EventParser parser;
    /* The guest's list. */
    private LinkedList<Guest> guests;
    /* Event's name. */
    private String name;
    /* The date */
    private String date;
    /* Description & info. */
    private String description;
    /* Address indications. */
    private String addressInfo;
    /* Address. */
    private String address;

    /* Constructor with an input. */
    public Event(InputStream is) {
        parser = new EventParser(is);
        try {
            parser.construct();
        } catch (XmlPullParserException xppe) {
            xppe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        guests = parser.getGuests();
        name = parser.getEventname();
        date = parser.getDate();
        description = parser.getInfo();
        addressInfo = parser.getAddressInfo();
        address = parser.getAddress();
    }
    /* Standard constructor. */
    public Event(String name, String date, String description, String addressInfo, String address) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.addressInfo = addressInfo;
        this.address = address;
    }

    /**
     * Returns the list of guests.
     * @return The list of guests.
     */
    public LinkedList<Guest> getGuests() {
        return guests;
    }

    /**
     * Getter for the name.
     * @return The name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the date.
     * @return The date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter for the description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return hints of the address.
     * @return addressinfo
     */
    public String getAddressInfo() {
        return addressInfo;
    }

    /**
     * Getter for the address.
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns a String representation of the event.
     * @return A String representation of the event.
     */
    public String toString() {
        return String.format("Name: %s\n" +
                                "Date: %s\n" +
                                "Description: %s\n"+
                                "Address Info: %s\n"+
                                "Address: %s",name,description,addressInfo,address);
    }
}
