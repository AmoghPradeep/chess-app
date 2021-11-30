package chessBackend;

import java.util.ArrayList;

public class Rook extends Piece {
    Rook(Position position, Board board, int color) {
        super(position, board, color);
        this.value = 50;
        this.name = "Rook";
        this.label = "R";
    }

    Rook(Rook rook) {
        super(rook);
        this.value = 50;
        this.name = "Rook";
        this.label = "R";
    }

    // returns the set of valid moves.
    @Override
    ArrayList<Position> getMoves() {
        int x = this.position.getXCoordinate();
        int y = this.position.getYCoordinate();

        ArrayList<Position> moves = new ArrayList<>();

        int r, q;

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

        moves.removeIf(p -> !p.insideBoard() || this.board.getCellState(p) == this.color);


        return moves;
    }
}
