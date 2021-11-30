package chessBackend;

import java.util.ArrayList;

public class Pawn extends Piece {
    Pawn(Position position, Board board, int color) {
        super(position, board, color);
        this.value = 10;
        this.name = "Pawn";
        this.label = "P";
    }

    Pawn(Pawn pawn) {
        super(pawn);
        this.value = 10;
        this.name = "Pawn";
        this.label = "P";
    }

    // returns the set of valid moves.
    @Override
    ArrayList<Position> getMoves() {
        int x = this.position.getXCoordinate();
        int y = this.position.getYCoordinate();

        ArrayList<Position> moves = new ArrayList<>();

        // Pawn may move in 3 ways.
        if (board.getCellState(new Position(x, y + this.color)) == 0) {
            moves.add(new Position(x, y + this.color)); // one step ahead.

            if (((y == 2 && this.color == 1) || (y == 7 && this.color == -1)) && board.getCellState(new Position(x, y + 2 * this.color)) == 0)
                moves.add(new Position(x, y + 2 * this.color)); // two steps ahead if at start.

        }

        // Removing moves which are out of bounds and which have same colored pieces.

        moves.removeIf(p -> !p.insideBoard());

        if (board.getCellState(new Position(x + this.color, y + this.color)) == -this.color)
            moves.add(new Position(x + this.color, y + this.color)); // diagonally left to capture enemy pawn.

        if (board.getCellState(new Position(x - this.color, y + this.color)) == -this.color)
            moves.add(new Position(x - this.color, y + this.color)); // diagonally right to capture enemy

        return moves;
    }
}
