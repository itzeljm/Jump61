package jump61;

import java.util.Observable;


import static jump61.Side.*;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered 0 - size()-1, in
 *  row 2 numbered size() - 2*size() - 1, etc.
 *
 *  The class extends java.util.Observable in case one wants a class (such
 *  as a GUI component) to be notified when the board changes.
 *  @author Itzel Martinez
 */
abstract class Board extends Observable {

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        unsupported("clear");
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        unsupported("copy");
    }

    /** Return the number of rows and of columns of THIS. */
    abstract int size();

    /** Returns the contents of the square at row R, column C
     *  1 <= R, C <= size (). */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }

    /** Returns the contents of square #N, numbering squares by rows, with
     *  squares in row 1 n-

     umber 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    abstract Square get(int n);

    /** Returns the total number of spots on the board. */
    abstract int numPieces();

    /** Returns the Side of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }


    /** Return the row number for square #N. */
    final int row(int n) {
        return n / size() + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return n % size() + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }


    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Side player, int n) {

        if (!exists(n)) {
            return false;
        }

        if (!player.playableSquare(get(n).getSide())) {
            return false;
        }

        return true;

    }


    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Side player) {

        int determinator = (numPieces() + totalsquares()) % 2;

        if (!player.equals(WHITE)) {
            return true;
        }

        if (player.equals(RED) && determinator == 0) {
            return true;
        }


        if (player.equals(BLUE) && determinator == 1) {
            return true;
        }

        return false;





    }


    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Side getWinner() {
        Side winner = null;



        int totalsquares = totalsquares();
        if (numOfSide(RED) == totalsquares) {
            winner = RED;
        }
        if (numOfSide(BLUE) == totalsquares) {
            winner = BLUE;
        }

        return winner;

    }


    /** Return the number of squares of given COLOR. */
    abstract int numOfSide(Side color);

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Side player, int r, int c) {
        unsupported("addSpot");
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Side player, int n) {
        unsupported("addSpot");
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Clear the undo
     *  history. */
    void set(int r, int c, int num, Side player) {
        unsupported("set");
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white).  Clear the undo history. */
    void set(int n, int num, Side player) {
        unsupported("set");
    }

    /** Undo the effects of one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        unsupported("undo");
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        String boardrep = "===\n";

        for (int r = 1; r <= this.size(); r++) {
            boardrep += "    ";
            for (int c = 1; c <= this.size(); c++) {
                Square a = this.get(r, c);
                Side color = a.getSide();
                int spot = a.getSpots();

                switch (color) {
                case WHITE:
                    boardrep += spot + "- ";
                    break;
                case RED:
                    boardrep += spot + "r ";
                    break;
                case BLUE:
                    boardrep += spot + "b ";
                    break;
                default:
                    System.out.print("invalid side.");
                    break;
                }
            }

            boardrep = boardrep.substring(0, boardrep.length() - 1);
            boardrep += "\n";

        }
        boardrep += "===";
        return boardrep;
    }



    /** Returns an external rendition of me, suitable for
     *  human-readable textual display.  This is distinct from the dumped
     *  representation (returned by toString). */
    public String toDisplayString() {
        StringBuilder out = new StringBuilder(toString());
        out.delete(0, 3 + NL_LENGTH);
        out.delete(out.length() - 3, out.length());

        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int size = size();
        int n;
        n = 0;
        if (r > 1) {
            n += 1;
        }
        if (c > 1) {
            n += 1;
        }
        if (r < size) {
            n += 1;
        }
        if (c < size) {
            n += 1;
        }
        return n;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    /** Indicate fatal error: OP is unsupported operation. */
    private void unsupported(String op) {
        String msg = String.format("'%s' operation not supported", op);
        throw new UnsupportedOperationException(msg);
    }

    /** The length of an end of line on this system. */
    private static final int NL_LENGTH =
        System.getProperty("line.separator").length();

    /*My Methods */

    /** Returns the total number of squares in that board. */
    int totalsquares() {
        return size() * size();

    }
}
