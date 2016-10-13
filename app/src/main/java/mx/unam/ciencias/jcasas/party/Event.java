package mx.unam.ciencias.jcasas.party;

import java.util.LinkedList;

/**
 * Created by jcasas on 13/10/16.
 * @author Juan Casas imhouses@ciencias.unam.mx
 * Class for managing the events for the application.
 * The class {@link Event} provides methods for view the guest list, the event info and
 * some other info related with.
 */

public class Event {

    private LinkedList<Guest> guests;

    public Event() {
        guests = new LinkedList<>();
    }



}
