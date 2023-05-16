package lms.logistics;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Maintains a doubly linked list to maintain the links for each node.
 * Has previous and next item.
 * The path can't have an empty node, as it will throw an illegal
 * argument exception.
 * @version 1.0
 * @ass2
 */
public class Path {
    /** */
    public Transport node;
    /** */
    public Path previous;

    public Path next;
    /**
     * 
     * @param path
     */
    public Path(Path path) {
        this.node = path.getNode();
        this.previous = path.getPrevious();
        this.next = path.getNext();
    }

    /**
     * 
     * @param node
     * @throws IllegalArgumentException
     */
    public Path(Transport node) throws IllegalArgumentException {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        this.node = node;
        this.previous = null;
        this.next = null;
    }

    /**
     * 
     * @param node
     * @param previous
     * @param next
     * @throws IllegalArgumentException
     */
    public Path(Transport node, Path previous, Path next) throws IllegalArgumentException {
        this.node = node;
        this.previous = previous;
        this.next = next;
    }

    /**
     * Iterates through the previous paths to find the first path
     * @return the head of the path
     */
    public Path head() {
        Path holder = this;
        while(holder.getPrevious() != null) {
            holder = holder.getPrevious();
        }
        return holder;
    }

    /**
     * @return The Transport Node of this path
     */
    public Transport getNode() {
        return this.node;
    }

    /**
     * Iterates through the next paths to find the last path
     * @return The tail of the path
     */
    public Path tail() {
        Path holder = this;
        while (holder.getNext() != null) {
            holder = holder.getNext();
        }
        return holder;
    }

    /**
     * 
     * @return The previous path of this one
     */
    public Path getPrevious() {
        return this.previous;
    }

    /**
     * Sets the previous element of this path to the path given
     * @param path The Path to be assigned to the previous Path of this Path. 
     */
    public void setPrevious(Path path) {
        this.previous = path;
    }

    /**
     * @return The path that is assigned next after this one
     */
    public Path getNext() {
        return this.next;
    }

    /**
     * Sets the given path to be the next Path of this Path
     * @param path the path that will be assigned as next
     */
    public void setNext(Path path) {
        this.next = path;
    }

    /**
     * This method takes a Transport Consumer,
     * using the Consumer&lt;T&gt; functional interface from java.util.
     * It finds the tail of the path and calls
     * Consumer&lt;T&gt;'s accept() method with the tail node as an argument.
     * Then it traverses the Path until the head is reached,
     * calling accept() on all nodes.
     *
     * This is how we call the tick method for all the different transport items.
     *
     * @param consumer Consumer&lt;Transport&gt;
     * @see java.util.function.Consumer
     * @provided
     */
    public void applyAll(Consumer<Transport> consumer) {
        Path path = tail(); // IMPORTANT: go backwards to aid tick
        do {
            consumer.accept(path.node);
            path = path.previous;
        } while (path != null);
    }

    /**
     * @return A string formatted version which includes all the connected paths to this one
     */
    @Override
    public String toString() {
        Path head = this.head();

        return String.format("START -> %sEND", this.finder(head));
    }
    
    /**
     * A recursive function used to find all the connected paths
     * @param path The Path that will be iterated through
     * @return A string containing each Path
     */
    private String finder(Path path) {
        Path holder = path;
        if (holder == null) {
            return "";
        }
        return holder.getNode() + " -> " + this.finder(holder.getNext());
    }

    /**
     * @param o
     * @return A boolean value representing whether the given value is the same as this Path
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Path) {
            Path path = (Path) o;
            if (this.getNode() == path.getNode()) {
                return true;
            }

        }
        return false;
    }

}
