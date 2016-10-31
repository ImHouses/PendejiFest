package mx.unam.ciencias.jcasas.party;

/**
 * Created by jcasas on 13/10/16.
 *
 * Class for Guests representing, every Guest has it's own id (Integer => 0) and it's own name.
 * This class provides methods for getting and setting the data of a guest.
 * <code>compareTo()</code> method compares a the guest using the id.
 */

public class Guest implements Comparable<Guest> {

    /* The name of the guest. */
    private String name;
    /* The id of the guest. */
    private int id;

    /* Constructor. */
    public Guest(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Getter for the name.
     * @return The name of the guest.
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name of the guest.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the id.
     * @return The id of the guest.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the guest.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * CompareTo for comparing objects of type Guest using the id for comparing them.
     * @param g The guest for compare.
     * @return An integer > 0 if g is less, 0 if the objects are equal and an integer < 0 if
                g is higher.
     */
    @Override
    public int compareTo(Guest g) {
        return id - g.id;
    }

    /**
     * Compares this object attributes with the g attributes.
     * @param g A guest.
     * @return <code>true</code> if the objects are equal, <code>false</code> otherwhise.
     */
    public boolean equals(Guest g) {
        if (this.id != g.id) return false;
        if (!this.name.equals(g.name)) return false;
        return true;
    }


}
