package jump61;

import java.util.ArrayList;

/** An automated Player.
 *  @author Itzel Martinez
 */
class AI extends Player {

    /** Time allotted to all but final search depth (milliseconds). */
    private static final long TIME_LIMIT = 15000;

    /** Number of calls to minmax between checks of elapsed time. */
    private static final long TIME_CHECK_INTERVAL = 10000;

    /** Number of milliseconds in one second. */
    private static final double MILLIS = 1000.0;




    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Side color) {
        super(game, color);
    }


    /** Ask my game to make my next move.  Assumes that I am of the
     *  proper color and that the game is not yet won. */
    @Override
    void makeMove() {
        Game game = getGame();
        Board board = getBoard();
        Side opside = getSide().opposite();
        int n;

        ArrayList<Integer> moves = new ArrayList<Integer>();
        minmax(getSide(), board, 3, Integer.MAX_VALUE, moves);

        if (!moves.isEmpty()) {
            n = moves.get(0);
        } else {
            n = 1;
        }

        if (board.isLegal(getSide(), n)) {
            game.makeMove(n);
            game.reportMove(getSide(), board.row(n), board.col(n));

        }
    }





    /** Return the minimum of CUTOFF and the minmax value of board B
     *  (which must be mutable) for player P to a search depth of D
     *  (where D == 0 denotes statically evaluating just the next move).
     *  If moves is not null and cutoff is not exceeded, set moves to
     *  a list of all highest-scoring MOVES for P; clear it if
     *  non-null and cutoff is exceeded. the contents of B are
     *  invariant over this call. */

    private int minmax(Side p, Board b, int d, int cutoff,
                       ArrayList<Integer> moves) {

        Side oppside;
        int returnval;
        MutableBoard newboard = new MutableBoard(b);
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;


        if (b.getWinner() == p) {
            return Integer.MAX_VALUE;
        }
        if (b.getWinner() == p.opposite()) {
            return Integer.MIN_VALUE;
        }

        if (d == 0) {
            int[] n = bestMove();
            moves.add(n[0]);
            return n[1];

        } else if (p == getSide()) {
            for (int n = 0; n < newboard.totalsquares(); n++) {
                if (b.isLegal(getSide(), n)) {
                    moves.add(n);
                    newboard.addSpot(getSide(), n);
                    int score1 = minmax(getSide().opposite(),
                                        newboard, d - 1, cutoff, moves);
                    newboard.undo();

                    if (score1 > highest) {
                        highest = score1;
                        moves.clear();
                        moves.add(n);
                    }

                    if (highest >= lowest) {
                        return highest;
                    }
                }
            }
            return highest;
        } else {
            for (int n = 0; n < newboard.size() * newboard.size(); n++) {
                if (b.isLegal(getSide().opposite(), n)) {
                    moves.add(n);
                    newboard.addSpot(getSide().opposite(), n);
                    int score2 = minmax(getSide(),
                        newboard, d - 1, cutoff, moves);
                    newboard.undo();

                    if (score2 < lowest) {
                        lowest = score2;
                    }
                }
            }
        }
        return lowest;
    }


    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */

    private int staticEval(Side p, Board b) {
        Side oppside = p.opposite();

        if (b.getWinner() == p) {
            return Integer.MAX_VALUE;
        }
        if (b.getWinner() != p) {
            return Integer.MIN_VALUE;

        } else {
            return (b.numOfSide(p) - b.numOfSide(oppside));
        }
    }



    /** returns best move using static eval to evaluate how good the move is. */
    private int[] bestMove() {
        Board board = getBoard();
        Side currside = getSide();
        int currentstatic = staticEval(currside, board);
        int[] move = new int[2];
        int[] move2 = new int[2];


        for (int n = 0; n < board.totalsquares(); n++) {

            if (board.isLegal(currside, n)) {
                move[0] = n;
                move[1] = currentstatic;
                MutableBoard copy = new MutableBoard(board);
                copy.addSpot(currside, n);
                int newstatic = staticEval(currside, copy);
                copy.undo();

                if (newstatic > currentstatic) {
                    move[0] = n;
                    move[1] = newstatic;
                }

                if (currentstatic ==  newstatic) {
                    move2[0] = n;
                    move2[1] = newstatic;
                }
            }
        }

        return move;

    }



}










