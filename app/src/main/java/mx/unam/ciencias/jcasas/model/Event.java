package mx.unam.ciencias.jcasas.model;

import java.util.LinkedList;

/**
 * Created by jcasas on 13/10/16.
 * @author Juan Casas imhouses@ciencias.unam.mx
 * Class for managing the events for the application.
 * The class {@link Event} provides methods for view the guest list, the event info and
 * some other info related with.
 */

public class Event {

    /**
     * Class for coordinates.
     */
    private class Location {

        /* The latitude. */
        private long latitude;
        /* The longitude. */
        private long longitude;

        public Location(long latitude, long longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * Return the latitude.
         * @return the latitude.
         */
        public long getLatitude() {
            return latitude;
        }

        /**
         * Return the longitude.
         * @return the longitude.
         */
        public long getLongitude() {
            return longitude;
        }
    }

    /* The guest's list. */
    private LinkedList<Guest> guests;
    /* Event's name. */
    private String name;
    /* The date */
    private Date date;
    /* Description & info. */
    private String description;
    /* Address. */
    private String address;
    /* Location. */
    private Location location;
    /* If the guests can invite someone else. */
    private boolean friendsInvites;

    /* Standard constructor. */
    public Event(String name,
                 Date date,
                 String description,
                 String address,
                 long latitude,
                 long longitude,
                 boolean friendsInvites) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.address = address;
        this.location = new Location(latitude, longitude);
        this.friendsInvites = friendsInvites;
    }

    /* Constructor without coordinates. */
    public Event(String name,
                 Date date,
                 String description,
                 String address,
                 boolean friendsInvites) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.address = address;
        this.friendsInvites = friendsInvites;
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
    public Date getDate() {
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
                                "Address: %s", name, description, address);
    }
}
