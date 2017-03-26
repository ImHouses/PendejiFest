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
    public LinkedList<Guest> getGuests() { return guests; }

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
