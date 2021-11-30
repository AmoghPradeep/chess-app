package chessBackend;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.*;

public abstract class Piece {
    int value; // Value of the piece in game.
    int color; // Black is -1 and White is 1.
    Position position; // Position on board.
    Board board; // Board on which piece is placed on.
    String name; // String containing name of piece.
    String label; // Piece Label. (P - Pawn etc)

    // constructors.
    Piece(Position position, Board board, int color) {
        this.position = position;
        this.color = color;
        this.board = board;
    }

    Piece(Piece piece){
        this.position = new Position(piece.getPosition());
        this.color = piece.color;
        this.board = new Board(piece.board);
    }

    // returns value of piece.
    public int getValue() {
        return this.value;
    }

    // returns color of piece.
    public int getColor() {
        return this.color;
    }

    // returns position of piece on the board.
    public Position getPosition() {
        return this.position;
    }

    public String getName() {
        return this.name;
    }

    public String getLabel() { return this.label; }

    // change position of piece.
    void setPosition(Position position) {
        this.position = position;
    }

    // Helper function. Returns false for invalid positions.
    boolean isValid(Position position) {
        // Halting if obstacle encountered.
        return position.insideBoard() && this.board.getCellState(position) != this.color;
    }

    // Get moves of piece. Overridden in specific implementations.
    ArrayList<Position> getMoves() {
        return null;
    }
}

