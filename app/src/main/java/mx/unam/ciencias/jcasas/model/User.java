package mx.unam.ciencias.jcasas.model;

import java.util.LinkedList;

/**
 * Created by juanh on 30/10/2016.
 */

public class User {

    /* The invitations. */
    private LinkedList<Event> invitations;
    /* The events a user have organized. */
    private LinkedList<Event> userEvents;
    /* The events a user is going to. */
    private LinkedList<Event> confirmations;
    /* The name of the user. */
    private String userName;
    /* The email of the user. */
    private String email;
    /* The password hash code. */
    private int pass;

    /** Constructor */
    public User(String userName, String email, String pass) {
        this.userName = userName;
        this.email = email;
        this.pass = pass.hashCode();
        this.userEvents = new LinkedList<>();
    }



}
