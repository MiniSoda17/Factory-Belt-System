package lms.logistics;

import java.util.Objects;

/**
 * A class that represents an Item
 */
public class Item {
    /** The name given for this item */
    private String name;

    /**
     * Constructs an Item with the given name
     * 
     * @param name The name that will be assigned to this Item
     * @throws IllegalArgumentException will throw if the given name is null or empty
     */
    public Item(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * Gets a true or false depending if the given object has the same value as this object
     * 
     * @param object An object that will be compared to, to see if its the same as this object
     * @return A boolean value 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } if (o == null) {
            return false;
        } if (o instanceof Item) {
            Item test = (Item) o;
            if (test.hashCode() == this.hashCode()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the hashcode of this Item
     * @return An integer that represents the hashcode of this Item
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.toString());
        return hash;
    }

    /**
     * Gets the name of this Item
     * @return A string that represents the name of this Item
     */
    @Override
    public String toString() {
        return this.name;
    }
}






