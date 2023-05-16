package test;

import lms.logistics.*;
import lms.logistics.belts.Belt;
import lms.logistics.container.Producer;
import lms.logistics.container.Receiver;

import org.junit.Test;
import static org.junit.Assert.*;


import org.junit.Before;

public class PathTest {
    private Path path1;
    private Path path2;
    private Path path3;
    private Path path4;
    private Path path5;
    private Path path1Copy;
    private Path path3Copy;
    private Producer producer1;
    private Belt belt2;
    private Belt belt3;
    private Belt belt4;
    private Receiver receiver5;


    @Before
    public void setUp() {
        producer1 = new Producer(1, new Item("aa"));
        belt2 = new Belt(2);
        belt3 = new Belt(3);
        belt4 = new Belt(4);
        receiver5 = new Receiver(5, new Item("bb"));

        path1 = new Path(producer1);
        path2 = new Path(belt2);  
        path3 = new Path(belt3);  
        path4 = new Path(belt4);  
        path5 = new Path(receiver5);

        path1Copy = new Path(producer1);
        path3Copy = new Path(belt3);

        path1.setNext(path2);
        path2.setPrevious(path1);
        path2.setNext(path3);
        path3.setPrevious(path2);
        path3.setNext(path4);
        path4.setPrevious(path3);
        path4.setNext(path5);
        path5.setPrevious(path4);
    }

    @Test
    public void headTest1() {
        assertEquals(path1, path3.head());
    }

    @Test
    public void headTest2() {
        assertEquals(path1, path4.head());
    }

    @Test
    public void headTest3() {
        assertEquals(path1, path5.head());
    }

    @Test
    public void getNodeTest1() {
        assertEquals(producer1, path1.getNode());
    }

    @Test
    public void getNodeTest2() {
        assertEquals(belt3, path3.getNode());
    }

    @Test
    public void getNodeTest3() {
        assertEquals(receiver5, path5.getNode());
    }

    @Test
    public void tailTest1() {
        assertEquals(path5, path1.tail());
    }

    @Test
    public void tailTest2() {
        assertEquals(path5, path2.tail());
    }

    @Test
    public void tailTest3() {
        assertEquals(path5, path3.tail());
    }

    @Test
    public void getPreviousTest1() {
        assertEquals(path1, path2.getPrevious());
    }

    @Test
    public void getPreviousTest2() {
        assertEquals(path3, path4.getPrevious());
    }

    @Test
    public void getPreviousTest3() {
        assertEquals(path4, path5.getPrevious());
    }

    @Test
    public void setPreviousTest1() {
        path5.setPrevious(path2);
        assertEquals(path2, path5.getPrevious());
    }

    @Test
    public void setPreviousTest2() {
        path2.setPrevious(path5);
        assertEquals(path5, path2.getPrevious());
    }

    @Test
    public void setPreviousTest3() {
        path2.setPrevious(path5);
        assertEquals(path5, path2.getPrevious());
    }

    @Test
    public void getNextTest1() {
        assertEquals(path2, path1.getNext());
    }

    @Test
    public void getNextTest2() {
        assertEquals(path5, path4.getNext());
    }

    @Test
    public void getNextTest3() {
        assertEquals(path3, path2.getNext());
    }

    @Test
    public void setNext1() {
        path1.setNext(path3);
        assertEquals(path3, path1.getNext());
    }

    @Test
    public void setNext2() {
        path2.setNext(path4);
        assertEquals(path4, path2.getNext());
    }

    @Test
    public void setNext3() {
        path3.setNext(path5);
        assertEquals(path5, path3.getNext());
    }

    @Test
    public void toStringTest1() {
        String test = "START -> <Producer-1> -> <Belt-2> -> <Belt-3> -> <Belt-4> -> <Receiver-5> -> END";
        assertEquals(test, path3.toString());
    }

    @Test
    public void toStringTest2() {
        String test = "START -> <Producer-1> -> <Belt-2> -> <Belt-3> -> <Belt-4> -> <Receiver-5> -> END";
        assertEquals(test, path5.toString());
    }

    @Test
    public void toStringTest3() {
        String test = "START -> <Producer-1> -> <Belt-2> -> <Belt-3> -> <Belt-4> -> <Receiver-5> -> END";
        assertEquals(test, path1.toString());
    }

    @Test
    public void equalsTest1() {
        assertFalse(path1.equals(path3));
    }

    @Test
    public void equalsTest2() {
        assertTrue(path1.equals(path1Copy));
    }
    
    @Test
    public void equalsTest3() {
        assertTrue(path3.equals(path3Copy));
    }
}
