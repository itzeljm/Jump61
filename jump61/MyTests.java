package jump61;

import static jump61.Side.*;

import org.junit.Test;
import static org.junit.Assert.*;



/** Compilation of unit tests for everything in jump61.
 *  @author Itzel Martinez
 */
public class MyTests {
    /** separates lines. */
    private static final String NL = System.getProperty("line.separator");


    @Test
    public void testToString() {

        Board B = new MutableBoard(2);
        B.addSpot(RED, 1, 1);
        B.addSpot(BLUE, 2, 1);
        System.out.println(B.toString());
        assertEquals(1, B.numOfSide(BLUE));
        assertEquals(1, B.numOfSide(RED));
        assertEquals(6, B.numPieces());
        assertEquals(4, B.totalsquares());
    }



    @Test
    public void testgetWinner() {

        Board B = new MutableBoard(3);
        B.addSpot(RED, 1, 1);
        B.addSpot(BLUE, 2, 1);
        B.addSpot(RED, 1, 1);
        assertEquals(null, B.getWinner());

    }


    @Test
    public void testaddSppot() {

        Board B = new MutableBoard(2);
        B.addSpot(RED, 1, 1);
        B.addSpot(BLUE, 2, 1);
        B.addSpot(RED, 1, 1);
        assertEquals(RED, B.getWinner());

    }

    @Test
    public void testaddSpot2() {
        Board B = new MutableBoard(2);
        B.addSpot(BLUE, 1, 1);
        B.addSpot(BLUE, 1, 2);
        B.addSpot(RED, 2, 1);
        B.addSpot(RED, 2, 2);
        System.out.println(B.toString());
        B.addSpot(RED, 2, 2);
        System.out.println(B.toString());
        assertEquals(RED, B.getWinner());
        System.out.println(B.getWinner());


    }
}

