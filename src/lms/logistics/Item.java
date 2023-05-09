package lms.logistics;

import java.util.Objects;

/**
 * 
 */
public class Item {
    /** The name given for this item */
    private String name;
    /**
     * @param name
     * @throws IllegalArgumentException
     */
    public Item(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * 
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
     * 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.toString());
        return hash;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return this.name;
    }
}






