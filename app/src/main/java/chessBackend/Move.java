package chessBackend;

public class Move {
    Position p1;
    Position p2;

    public Move(Position p1, Position p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Position getStart() {
        return this.p1;
    }

    public Position getDest() {
        return this.p2;
    }
}
