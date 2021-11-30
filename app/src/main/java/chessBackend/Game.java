package chessBackend;
import java.util.ArrayList;


// Interaction between user and game carried out by Game class.

public class Game {
    int turn;
    int playerCount;
    int difficulty;
    Board board;
    Minimax ai;

    public Game(int playerCount, int difficulty){
        this.turn = -1;
        this.playerCount = playerCount;
        this.difficulty = difficulty;
        this.board = new Board();
        this.ai = new Minimax(board, difficulty);
    }

    // Performs move provided by user on board.
    public void makeMove(Position p1, Position p2, int color) throws Exception {
        if (color != turn){
            throw  new Exception("invalid turn");
        }
        if (p1 == null || p2 == null) {
            throw new Exception("invalid move");
        }
        ArrayList<Position> moves = board.getMoves(p1);
        boolean valid = false;
        for (Position move : moves){
            if (move.equals(p2))
                valid = true;
        }

        if (!valid){
            throw new Exception("invalid move");
        }

        board.makeMove(p1, p2);
    }

    public Move getAiMove(){
        Move m = ai.findOptimalMove(turn*-1);
        board.makeMove(m);
        return m;
    }

    public boolean check(){
        return board.check(-1);
    }

    public int checkmate (){
        if (board.check(1) && board.checkmate(1)){
            return 1;
        }
        if (board.check(-1) && board.checkmate(-1)){
            return -1;
        }
        return 0;
    }
    public Piece[][] getBoard(){
        return board.getBoardState();
    }

    public ArrayList<Position> getMoves(Position p){
        return board.getMoves(p);
    }
}
