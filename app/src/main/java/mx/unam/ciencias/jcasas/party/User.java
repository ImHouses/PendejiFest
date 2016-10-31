package mx.unam.ciencias.jcasas.party;

import java.util.LinkedList;

/**
 * Created by juanh on 30/10/2016.
 */

public interface User {

    public LinkedList<Event> events = new LinkedList<>();

    public LinkedList<Event> getEvents();

}
