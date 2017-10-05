package chess;

import chess.pieces.Piece;

import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * This class provides the basic CLI interface to the Chess game.
 */
public class CLI {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final BufferedReader inReader;
    private final PrintStream outStream;

    private GameState gameState = null;

    public CLI(InputStream inputStream, PrintStream outStream) {
        this.inReader = new BufferedReader(new InputStreamReader(inputStream));
        this.outStream = outStream;
        writeOutput("Welcome to Chess!");
    }

    /**
     * Write the string to the output
     * @param str The string to write
     */
    private void writeOutput(String str) {
        this.outStream.println(str);
    }

    /**
     * Retrieve a string from the console, returning after the user hits the 'Return' key.
     * @return The input from the user, or an empty-length string if they did not type anything.
     */
    private String getInput() {
        try {
            this.outStream.print("> ");
            return inReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from input: ", e);
        }
    }

    void startEventLoop() {
        writeOutput("Type 'help' for a list of commands.");
        doNewGame();

        while (true) {
            showBoard();
            Player currentPlayer = gameState.getCurrentPlayer();

            if (gameState.isGameOver()) {
                writeOutput("GAME OVER!\n\nThe winner is: " + currentPlayer.other());
                break;
            } else if (gameState.isInCheck()) {
                writeOutput(currentPlayer + " is in check");
            } else {
                writeOutput(currentPlayer + "'s Move");
            }

            String input = getInput();
            if (input == null) {
                break; // No more input possible; this is the only way to exit the event loop
            } else if (input.length() > 0) {
                if (input.equals("help")) {
                    showCommands();
                } else if (input.equals("new")) {
                    doNewGame();
                } else if (input.equals("quit")) {
                    writeOutput("Goodbye!");
                    System.exit(0);
                } else if (input.equals("board")) {
                    writeOutput("Current Game:");
                } else if (input.equals("list")) {
                    showList();
                } else if (input.startsWith("move")) {
                    doMove(input);
                } else {
                    writeOutput("I didn't understand that.  Type 'help' for a list of commands.");
                }
            }
        }
    }

    private void doNewGame() {
        gameState = new GameState();
        gameState.reset();
    }

    private void showList() {
        Map<Piece,Set<Move>> moveMap = gameState.findPossibleMoves();
        writeOutput(gameState.getCurrentPlayer() + "'s Possible Moves: ");
        for (Piece piece : moveMap.keySet()) {
            Set<Move> moves = moveMap.get(piece);
            for (Move move : moves) {
                writeOutput("    " + move);
            }
        }
    }

    private void doMove(String input) {
        String moveStr = input.substring(4).trim();
        try {
            boolean executed = gameState.makeMove(moveStr);
            if (!executed) {
                writeOutput("Invalid move: " + gameState.getCurrentPlayer() + " would be in check");
            }
        } catch (InvalidMoveException ex) {
            writeOutput("That move doesn't make sense.");
        } catch (InvalidPositionException ex) {
            writeOutput("Please specify a move as 'colrow colrow'. For instance, 'move d2 d4'");
        }
    }

    private void showBoard() {
        writeOutput(getBoardAsString());
    }

    private void showCommands() {
        writeOutput("Possible commands: ");
        writeOutput("    'help'                       Show this menu");
        writeOutput("    'quit'                       Quit Chess");
        writeOutput("    'new'                        Create a new game");
        writeOutput("    'board'                      Show the chess board");
        writeOutput("    'list'                       List all possible moves");
        writeOutput("    'move <colrow> <colrow>'     Make a move");
    }

    /**
     * Display the board for the user(s)
     */
    String getBoardAsString() {
        StringBuilder builder = new StringBuilder();

        printColumnLabels(builder);
        for (int i = GameState.MAX_ROW; i >= GameState.MIN_ROW; i--) {
            printSeparator(builder);
            printSquares(i, builder);
        }

        printSeparator(builder);
        printColumnLabels(builder);

        return builder.toString();
    }


    private void printSquares(int rowLabel, StringBuilder builder) {
        builder.append(rowLabel);

        for (char c = GameState.MIN_COLUMN; c <= GameState.MAX_COLUMN; c++) {
            Piece piece = gameState.getPieceAt(new Position(c, rowLabel));
            char pieceChar = piece == null ? ' ' : piece.getIdentifier();
            builder.append(" | ").append(pieceChar);
        }
        builder.append(" | ").append(rowLabel).append(NEWLINE);
    }

    private void printSeparator(StringBuilder builder) {
        builder.append("  +---+---+---+---+---+---+---+---+").append(NEWLINE);
    }

    private void printColumnLabels(StringBuilder builder) {
        builder.append("   ");
        for (char c = GameState.MIN_COLUMN; c <= GameState.MAX_COLUMN; c++) {
            builder.append(" ").append(c).append("  ");
        }

        builder.append(NEWLINE);
    }

    public static void main(String[] args) {
        CLI cli = new CLI(System.in, System.out);
        cli.startEventLoop();
    }
}
