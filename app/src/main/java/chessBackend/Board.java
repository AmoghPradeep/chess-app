package chessBackend;


import java.util.*;
/*
What does Board do?

1. It stores the state of the board which includes :
          a. the position of all the pieces on the chess board.
          b. all the piece objects.
          c. score of each team.
2. You can make moves to the pieces through makeMove function.
3. Checks if the game is in a check or checkmate state.

Makes it easier to organize all the individual pieces.

This class can only be accessed by the Game class.
*/

class Board {
    /*
     * Scores are assigned as per the following rules :
     * Pawn : 10pt
     * Bishop and Knight : 30pt
     * Rook : 50pt
     * Queen : 90pt
     * King : 1000pt
     *
     * Team Colors :
     * White is assigned 1 and Black is -1.
     */

    private int[] score = new int[2];

    // Store positions of all white/ black pieces.

    private Piece[][] boardState = new Piece[9][9];

    // constructing board.
    Board() {
        score[0] = 1390;
        score[1] = 1390;

        // placing pieces...
        // couldn't think of a shorter way to place all the pieces on the board.

        // placing pawns.
        for (int i = 1; i <= 8; ++i) {
            boardState[2][i] = new Pawn(new Position(i, 2), this, 1);
            boardState[7][i] = new Pawn(new Position(i, 7), this, -1);
        }

        // placing kings.
        boardState[1][5] = new King(new Position(5, 1), this, 1);
        boardState[8][5] = new King(new Position(5, 8), this, -1);

        // placing queens.
        boardState[1][4] = new Queen(new Position(4, 1), this, 1);
        boardState[8][4] = new Queen(new Position(4, 8), this, -1);

        // placing rooks.
        boardState[1][1] = new Rook(new Position(1, 1), this, 1);
        boardState[1][8] = new Rook(new Position(8, 1), this, 1);
        boardState[8][1] = new Rook(new Position(1, 8), this, -1);
        boardState[8][8] = new Rook(new Position(8, 8), this, -1);

        // placing bishops.
        boardState[1][3] = new Bishop(new Position(3, 1), this, 1);
        boardState[1][6] = new Bishop(new Position(6, 1), this, 1);
        boardState[8][3] = new Bishop(new Position(3, 8), this, -1);
        boardState[8][6] = new Bishop(new Position(6, 8), this, -1);

        // placing knights.
        boardState[1][2] = new Knight(new Position(2, 1), this, 1);
        boardState[1][7] = new Knight(new Position(7, 1), this, 1);
        boardState[8][2] = new Knight(new Position(2, 8), this, -1);
        boardState[8][7] = new Knight(new Position(7, 8), this, -1);
    }

    // Copies other board.
    Board(Board copyBoard){

        Piece[][] copyBoardState = copyBoard.getBoardState();

        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){

                // Inorder to create a deep-copy and not just make the new board reference the old one,
                // we need to make new pieces and place them in the same order as that of copyBoard.

                // statements like Class obj1 = obj2 creates only a shallow copy. Any changes made to obj1
                // will reflect in obj2.

                if (copyBoardState[i][j] != null) {
                    Position copyPosition = new Position(copyBoardState[i][j].getPosition());
                    int copyColor = copyBoardState[i][j].getColor();
                    Piece copyPiece = copyBoardState[i][j];

                    if (copyPiece instanceof Pawn) {
                        this.boardState[i][j] = new Pawn(copyPosition, this, copyColor);
                    } else if (copyPiece instanceof Bishop) {
                        this.boardState[i][j] = new Bishop(copyPosition, this, copyColor);
                    } else if (copyPiece instanceof Knight) {
                        this.boardState[i][j] = new Knight(copyPosition, this, copyColor);
                    } else if (copyPiece instanceof Rook) {
                        this.boardState[i][j] = new Rook(copyPosition, this, copyColor);
                    } else if (copyPiece instanceof Queen) {
                        this.boardState[i][j] = new Queen(copyPosition, this, copyColor);
                    } else if (copyPiece instanceof King) {
                        this.boardState[i][j] = new King(copyPosition, this, copyColor);
                    }

                }
            }
        }
        this.score = new int[]{copyBoard.getBlackScore(), copyBoard.getWhiteScore()};
    }

    // getScore functions.

    int getWhiteScore() {
        return score[1];
    }

    int getBlackScore() {
        return score[0];
    }

    int getScore() {
        return score[1] - score[0];
    }


    // Returns Board as Matrix of pieces.
    Piece[][] getBoardState() {
        return boardState;
    }

    // get set of possible moves for any piece.
    ArrayList<Position> getMoves(Position p) {
        Piece piece = boardState[p.getYCoordinate()][p.getXCoordinate()];
        if (piece == null)
            return null;

        return piece.getMoves();
    }

    // Deletes piece at position p, and subtracts its value from team's score.
    private void removePiece( Position p) {
        if (boardState[p.getYCoordinate()][p.getXCoordinate()] == null)
            return;
        Piece piece = boardState[p.getYCoordinate()][p.getXCoordinate()];
        score[Math.max(piece.getColor(), 0)] -= piece.getValue();
    }

    // Change position of piece in board.
    // Deletes any piece at end position.
    void makeMove(Position p1, Position p2) {
        removePiece(p2);
        int x1 = p1.getXCoordinate(), y1 = p1.getYCoordinate();
        int x2 = p2.getXCoordinate(), y2 = p2.getYCoordinate();

        boardState[y1][x1].setPosition(p2);

        boardState[y2][x2] = boardState[y1][x1];
        boardState[y1][x1] = null;
    }

    void makeMove(Move m) {
        if (m != null)
            makeMove(m.p1, m.p2);
    }



    // checks if king is in danger.
    boolean check(int color){
        King k = this.getKing(color);
        return this.dangerAtPosition(k.getPosition(), color);
    }

    // checks if position p is at attack distance from any of opponent pieces.
    // used by checkmate, and check functions.
    boolean dangerAtPosition(Position p, int color) {
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                if (boardState[i][j] != null && boardState[i][j].getColor() == -color) {
                    ArrayList<Position> moves = getMoves(new Position(j, i));
                    if (moves == null) continue;
                    for (Position m : moves) {
                        if (m.equals(p))
                            return true;
                    }
                }
            }
        }

        return false;
    }


    // Simulates all moves for given team. If each move made creates another "check" state,
    // checkmate is true.
    boolean checkmate(int color){
        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){
                if (boardState[i][j] != null && boardState[i][j].getColor() == color){
                    ArrayList<Position> moves = boardState[i][j].getMoves();
                    Position p1 = boardState[i][j].getPosition();

                    for (Position p2 : moves) {
                        Board tempBoard = new Board(this);
                        tempBoard.makeMove(p1, p2);
                        if (!tempBoard.check(color))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    // returns King of given team. (used by checkmate functions.)
    King getKing(int color){
        King k = null;
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                if (boardState[i][j] instanceof King && boardState[i][j].getColor() == color) {
                    k = (King) boardState[i][j];
                }
            }
        }
        return k;
    }

    // DEBUGGING helper functions. (don't add anything to final app can be deleted after production)

    // prints out chess board to display on the command line. (used for cli debugging)
    String debugBoard(){
        String boardString = this.getScore() + "\n";
        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j) {
                String cell = "_";
                if (boardState[i][j] != null){
                    cell = boardState[i][j].getLabel();
                    if (boardState[i][j].getColor() == -1) {
                        cell = cell.toLowerCase();
                    }
                }
                boardString += cell;
            }
            boardString += "\n";
        }
        return boardString;
    }


    // get status of any cell :
    // 1 - occupied by white.
    // -1 - occupied by black.
    // 0 - empty.
    // (cli debugging tool)
    int getCellState(Position p) {
        if (!p.insideBoard())
            return 0;

        int x = p.getXCoordinate();
        int y = p.getYCoordinate();

        if (boardState[y][x] == null)
            return 0;
        return boardState[y][x].getColor();
    }
}