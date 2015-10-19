package jump61;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import java.io.PrintWriter;

import static jump61.Side.*;

/** A GUI component that displays a Jump61 board, and converts mouse clicks
 *  on that board to commands that are sent to the current Game.
 *  @author Itzel Martinez
 */
class BoardWidget extends Pad {

    /** Length of the side of one square in pixels. */
    private static final int SQUARE_SIZE = 50;
    /** Width and height of a spot. */
    private static final int SPOT_DIM = 8;
    /** Minimum separation of center of a spot from a side of a square. */
    private static final int SPOT_MARGIN = 10;
    /** Width of the bars separating squares in pixels. */
    private static final int SEPARATOR_SIZE = 3;
    /** Width of square plus one separator. */
    private static final int SQUARE_SEP = SQUARE_SIZE + SEPARATOR_SIZE;

    /** Colors of various parts of the displayed board. */
    private static final Color
        NEUTRAL = Color.WHITE,
        SEPARATOR_COLOR = Color.BLACK,
        SPOT_COLOR = Color.BLACK,
        RED_TINT = new Color(255, 200, 200),
        BLUE_TINT = new Color(200, 200, 255);

    /** A new BoardWidget that monitors and displays GAME and its Board, and
     *  converts mouse clicks to commands to COMMANDWRITER. */
    BoardWidget(Game game, PrintWriter commandWriter) {
        _game = game;
        _board = _bufferedBoard = game.getBoard();
        _side = _board.size() * SQUARE_SEP + SEPARATOR_SIZE;
        setPreferredSize(_side, _side);
        setMouseHandler("click", this, "doClick");
        _commandOut = commandWriter;
    }

    /* .update and .paintComponent are synchronized because they are called
     *  by three different threads (the main thread, the thread that
     *  responds to events, and the display thread.  We don't want the
     *  saved copy of our Board to change while it is being displayed. */

    /** Update my display depending on any changes to my Board.  Here, we
     *  save a copy of the current Board (so that we can deal with changes
     *  to it only when we are ready for them), and resize the Widget if the
     *  size of the Board should change. */
    synchronized void update() {
        _bufferedBoard = new MutableBoard(_board);
        int side0 = _side;
        _side = _board.size() * SQUARE_SEP + SEPARATOR_SIZE;
        if (side0 != _side) {
            setPreferredSize(_side, _side);
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
    }

    /** Color and display the spots on the square at row R and column C
     *  on G.  (Used by paintComponent). */
    private void displaySpots(Graphics2D g, int r, int c) {
    }

    /** Draw one spot centered at position (X, Y) on G. */
    private void spot(Graphics2D g, int x, int y) {
        g.setColor(SPOT_COLOR);
        g.fillOval(x - SPOT_DIM / 2, y - SPOT_DIM / 2, SPOT_DIM, SPOT_DIM);
    }

    /** Respond to the mouse click depicted by EVENT. */
    public void doClick(MouseEvent event) {

        int x = event.getX() - SEPARATOR_SIZE,
            y = event.getY() - SEPARATOR_SIZE;

        int r = 1;
        int c = 1;
        _commandOut.printf("%d %d%n", r, c);
    }

    /** The Game I am playing. */
    private Game _game;
    /** The Board I am displaying. */
    private Board _board;
    /** An internal snapshot of _board (to prevent race conditions). */
    private Board _bufferedBoard;
    /** Dimension in pixels of one side of the board. */
    private int _side;
    /** Destination for commands derived from mouse clicks. */
    private PrintWriter _commandOut;
}
