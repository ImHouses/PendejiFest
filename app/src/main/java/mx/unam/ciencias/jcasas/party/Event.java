package mx.unam.ciencias.jcasas.party;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by jcasas on 13/10/16.
 * @author Juan Casas imhouses@ciencias.unam.mx
 * Class for managing the events for the application.
 * The class {@link Event} provides methods for view the guest list, the event info and
 * some other info related with.
 */

public class Event {

    private EventParser parser;
    private LinkedList<Guest> guests;
    private String name;
    private String date;
    private String description;
    private String addressInfo;

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
    }

    public LinkedList<Guest> getGuests() {
        return guests;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAddressInfo() {
        return addressInfo;
    }
}
