package jump61;
import java.util.Stack;

import static jump61.Side.*;
import static jump61.Square.square;

/** A Jump61 board state that may be modified.
 *  @author Itzel Martinez
 */
class MutableBoard extends Board {
    /**the board that I am currently making (this). */
    private Square[][] _board;

    /** the history of the board represented as a square array.*/
    private Stack<Square[][]> _history;


    /**how many spots are in our board. */
    private int addSpotsCount;

    /** a copy of the board.*/
    private Square[][] _copy;
    /**number of moves in my board.*/
    private int numberOfMoves;

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        if (N > 1) {
            _board = new Square[N][N];

            for (int r = 1; r <= N; r++) {
                for (int c = 1; c <= N; c++) {
                    Square sq = Square.square(WHITE, 1);
                    _board[r - 1][c - 1] = sq;
                }
            }
        }
        addSpotsCount = 0;
        _history = new Stack<Square[][]>();


    }


    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo _history is clear. */
    MutableBoard(Board board0) {

        _board = new Square[board0.size()][board0.size()];

        for (int r = 1; r <= size(); r++) {
            for (int c = 1; c <= size(); c++) {
                _board[r - 1][c - 1] = Square.square(WHITE, 0);

            }
        }

        _history = new Stack<Square[][]>();
        copy(board0);
        addSpotsCount = 0;
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo _history and sets the number of moves to 0. */
    @Override
    void clear(int N) {
        announce();
        numberOfMoves = 0;
        _history = new Stack<Square[][]>();

        if (N >= 2) {
            _board = new Square[N][N];
            for (int r = 1; r <= N; r++) {
                for (int c = 1; c <= N; c++) {
                    Square sq = Square.square(WHITE, 1);
                    _board[r - 1][ c - 1] = sq;
                }
            }

        }
    }

    /** Copy the contents of BOARD into me. */
    @Override
    void copy(Board board) {

        for (int r = 1; r <= this.size(); r++) {
            for (int c = 1; c <= this.size(); c++) {

                Square a  = board.get(r, c);
                Side color = a.getSide();
                int spots = a.getSpots();

                this.set(r, c, spots, color);

            }
        }

    }


    /** Copy the contents of BOARD into me, without modifying my undo
     *  _history.  Assumes BOARD and I have the same size.
     ***undo _history just clear the _history so stack might have a clear method

     */
    private void internalCopy(MutableBoard board) {

        for (int r = 1; r <= board.size(); r++) {
            for (int c = 1; c <= board.size(); c++) {

                Square a  = board.get(r, c);
                Side color = a.getSide();
                int spots = a.getSpots();

                this.set(r, c, spots, color);

            }
        }
    }





    @Override
    int size() {
        return _board[0].length;
    }


    @Override
    Square get(int n) {
        int r = row(n) - 1;
        int c = col(n) - 1;
        return _board[r][c];
    }

    /** Return the number of squares of given SIDE. */
    @Override
    int numOfSide(Side side) {
        int numcolor = 0;

        for (int n = 0; n < totalsquares(); n++) {
            Square sq = this.get(n);
            if (sq.getSide().equals(side)) {
                numcolor += 1;
            }
        }


        return numcolor;
    }

    @Override
    int numPieces() {

        int totnumpieces = 0;

        for (int i = 0; i < totalsquares(); i++) {
            Square b = get(i);
            totnumpieces += b.getSpots();
        }

        return totnumpieces;
    }


    @Override
    void addSpot(Side player, int r, int c) {

        numberOfMoves += 1;
        markUndo();
        addSpot(player, sqNum(r, c));
        announce();

    }

    @Override
    void addSpot(Side player, int n) {

        if (exists(n) && getWinner() == null) {
            Square existing = this.get(n);
            int spots = existing.getSpots();
            int neighbors = this.neighbors(row(n), col(n));

            if (spots + 1 > neighbors) {
                this.set(row(n), col(n), 1, player);

                if (getWinner() == null) {
                    distribute(player, row(n), col(n));
                }

            }

            if (spots + 1 <= neighbors) {
                this.set(row(n), col(n), spots + 1, player);
            }

            announce();
        }
    }

    @Override
    void set(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), square(player, num));
    }

    @Override
    void set(int n, int num, Side player) {
        internalSet(n, square(player, num));
        announce();
    }


    @Override
    void undo() {


        if (!_history.empty()) {
            _copy = _history.pop();
            _board = _copy;
        }


    }

    /** Record the beginning of a move in the undo _history.
        PUSH ITEM FROM A STACK, have to keep track of boards  */
    private void markUndo() {


        Square[][] copy2 = new Square[this.size()][this.size()];


        for (int r = 1; r <= this.size(); r++) {
            for (int c = 1; c <= this.size(); c++) {
                Square a  = this.get(r, c);
                Side color = a.getSide();
                int spots = a.getSpots();
                Square sq = Square.square(color, spots);
                copy2[r - 1][c - 1] = sq;
            }
        }

        _history.push(copy2);
    }


    /** Set the contents of the square with index IND to SQ. Update counts
     *  of numbers of squares of each color.  */
    private void internalSet(int ind, Square sq) {

        int br =  row(ind) - 1;
        int bc =  col(ind) - 1;
        _board[br][bc] = sq;

    }

    /** Notify all Observers of a change. */
    private void announce() {
        setChanged();
        notifyObservers();
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MutableBoard)) {
            return obj.equals(this);
        } else {

            return false;
        }
    }

    @Override
    public int hashCode() {

        return 0;
    }


    /** ITZEL MADE METHODS. */



    /** Method that distributes one spot to each neighbor.
     * from the spots at square at (r,c) in the board, returns nothing,
     * it takes in side PLAYER and row R and column C.
     */
    public void distribute(Side player, int r, int c) {

        if (exists(r, c - 1)) {
            addSpot(player, sqNum(r, c - 1));

        }

        if (exists(r, c + 1)) {
            addSpot(player, sqNum(r, c + 1));
        }

        if (exists(r - 1, c)) {
            addSpot(player, sqNum(r - 1, c));
        }

        if (exists(r + 1, c)) {
            addSpot(player, sqNum(r + 1, c));
        }


    }

}



