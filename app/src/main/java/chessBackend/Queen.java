package chessBackend;

import java.util.ArrayList;

public class Queen extends Piece {
    Queen(Position position, Board board, int color) {
        super(position, board, color);
        this.value = 90;
        this.name = "Queen";
        this.label = "Q";
    }

    Queen(Queen queen) {
        super(queen);
        this.value = 90;
        this.name = "Queen";
        this.label = "Q";
    }

    // returns the set of valid moves.
    @Override
    ArrayList<Position> getMoves() {
        int x = this.position.getXCoordinate();
        int y = this.position.getYCoordinate();

        ArrayList<Position> moves = new ArrayList<>();

        int r, q;

        // tracing all 4 diagonals.
        for (r = -1; r <= 1; ++r) {
            for (q = -1; q <= 1; ++q) {

                if (r * q == 0)
                    continue;

                for (int i = 1; i < 8; ++i) {
                    Position newPosition = new Position(x + i * r, y + i * q);
                    // Halting if obstacle encountered.
                    if (!super.isValid(newPosition))
                        break;
                    moves.add(newPosition);

                    if (this.board.getCellState(newPosition) == -this.color)
                        break;
                }
            }
        }

        // tracing all 4 axes'.
        for (r = -1; r <= 1; ++r) {
            for (q = -1; q <= 1; ++q) {

                if (r * q != 0)
                    continue;
                if (r == q && r == 0)
                    continue;

                for (int i = 1; i < 8; ++i) {
                    Position newPosition = new Position(x + i * r, y + i * q);
                    // Halting if obstacle encountered.
                    if (!super.isValid(newPosition))
                        break;
                    moves.add(newPosition);

                    if (this.board.getCellState(newPosition) == -this.color)
                        break;
                }
            }
        }

        // Removing moves which are out of bounds and which have same colored pieces.

        moves.removeIf(p -> !p.insideBoard() || this.board.getCellState(p) == this.color);


        return moves;
    }
}
