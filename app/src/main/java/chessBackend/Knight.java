package chessBackend;

import java.util.ArrayList;

public class Knight extends Piece {
    Knight(Position position, Board board, int color) {
        super(position, board, color);
        this.value = 30;
        this.name = "Knight";
        this.label = "H";
    }

    Knight(Knight knight) {
        super(knight);
        this.value = 30;
        this.name = "Knight";
        this.label = "H";
    }

    // returns the set of valid moves.
    @Override
    ArrayList<Position> getMoves() {
        int x = this.position.getXCoordinate();
        int y = this.position.getYCoordinate();

        ArrayList<Position> moves = new ArrayList<>();

        moves.add(new Position(x + 2, y + 1));
        moves.add(new Position(x + 2, y - 1));
        moves.add(new Position(x + 1, y + 2));
        moves.add(new Position(x + 1, y - 2));
        moves.add(new Position(x - 2, y + 1));
        moves.add(new Position(x - 2, y - 1));
        moves.add(new Position(x - 1, y + 2));
        moves.add(new Position(x - 1, y - 2));

        // Removing moves which are out of bounds and which have same colored pieces.

        moves.removeIf(p -> !p.insideBoard() || this.board.getCellState(p) == this.color);

        return moves;
    }
}
