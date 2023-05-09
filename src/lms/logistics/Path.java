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
        this.node = path.node;
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
     * 
     * @return
     */
    public Path head() {
        Path holder = this;
        while(holder.previous() != null) {
            holder = holder.previous();
        }
        return holder;
    }

    /**
     * 
     * @return
     */
    public Transport getNode() {
        return this.node;
    }

    /**
     * 
     * @return
     */
    public Path tail() {
        Path holder = this;
        while (holder.next() != null) {
            holder = holder.next();
        }
        return holder;
    }

    /**
     * 
     * @return
     */
    public Path previous() {
        return this.previous;
    }

    /**
     *  
     */ 
    public void previous(Path path) {
        this.previous = path;
    }

    /**
     * @return
     */
    public Path next() {
        return this.next;
    }

    /**
     * 
     * @param path
     */
    public void next(Path path) {
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
     * 
     */
    public String toString() {
        Path head = this.head();

        return String.format("START -> %sEND", this.finder(head));
    }

    private String finder(Path path) {
        Path holder = path;
        if (holder == null) {
            return "";
        }
        return holder.getNode() + " -> " + this.finder(holder.next());
    }

    /**
     * @param o
     * @return
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
