package chessBackend;

import java.util.ArrayList;

// returns optimal move for black (ai's color) by looking ahead some number of moves.
// uses minimax algorithm with alpha beta pruning for improved performance.
// easy, medium, and hard difficulties look 2, 3, and 4 moves ahead respectively.
// Increasing the look ahead distance increases the time taken exponentially.

class Minimax {
    Board board;
    int depth;
    int calls;
    Minimax(Board board, int depth) {
        this.board = board;
        this.depth = depth;
        this.calls = 0;
    }


    // Returns optimal move for current board state.
    // finding optimal move for team given in 'color' parameter.

    // color = 1 corresponds to ai's turn.

    Move findOptimalMove(int color) {
        Piece[][] boardState = board.getBoardState();

        int optimalScore;

        if (color == 1) optimalScore = Integer.MIN_VALUE;
        else optimalScore = Integer.MAX_VALUE;

        Move optimalMove = null;

        int alpha = Integer.MIN_VALUE; // alpha stores black's best move so far.
        int beta = Integer.MAX_VALUE; // beta stores white's best move so far.

        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                if (boardState[i][j] != null && boardState[i][j].getColor() == color) {

                    ArrayList<Position> moves = boardState[i][j].getMoves();
                    Position p1 = boardState[i][j].getPosition();

                    for (Position p2 : moves) {

                        // creating temporary board with speculative move performed.
                        Board tempBoard = new Board(board);
                        tempBoard.makeMove(p1, p2);
                        // recurse on temporary board.
                        int score = minimaxHelper(tempBoard, 3, alpha, beta, -color);
                        // updating optimal move if suitable.

                        if (isBetter(optimalScore, score, color)) {
                            optimalScore = score;
                            optimalMove = new Move (p1, p2);
                        }

                        alpha = Math.max(alpha, score);
                    }
                }
            }
        }
        return optimalMove;
    }
    /*
        TODO:
        1. Add alpha beta pruning to this.
        2. Check for any bugs.
     */

    // Helper function.
    /*
        Why is this helper function necessary ?
        During the recursion process, just the score for each state is necessary, as its the factor
        used to choose the optimal move.
        So we can get away by only returning the optimal score for each recursive call.

        But since we also need the move taken to get to the optimal state, we also have to return that.

        We have 2 choices now:
        1. Create a new data structure that stores both the move taken and the score. (Can be a bit slower?)
        2. Ignore move and just return the score for all but the 1st level of recursion. (Adds repetitive code :( )

        Because the amount of recursive calls can be extremely large, I want to minimize the execution time
        of each call, so i decided to go with the 2 function approach.

     */
    private int minimaxHelper(Board board, int currDepth, int alpha, int beta, int color) {
        calls ++;
        if (currDepth == 0) {
            return board.getScore();
        }

        int optimalScore;

        if (color == 1) optimalScore = Integer.MIN_VALUE; // for black (maximizing)
        else optimalScore = Integer.MAX_VALUE; // for white (minimizing)

        Piece[][] boardState = board.getBoardState();
        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){
                if (boardState[i][j] != null && boardState[i][j].getColor() == color) {
                    ArrayList<Position> moves = boardState[i][j].getMoves();
                    Position p1 = boardState[i][j].getPosition();

                    for (Position p2 : moves) {
                        Board tempBoard = new Board(board);
                        tempBoard.makeMove(p1, p2);

                        int score = minimaxHelper(tempBoard, currDepth - 1,alpha, beta, -color);

                        if (color == 1){
                            alpha = Math.max(alpha, score);
                            if (alpha >= beta){
                                return alpha;
                            }
                        }
                        else{
                            beta = Math.min(beta, score);
                            if (alpha >= beta){
                                return beta;
                            }
                        }

                        if (isBetter(optimalScore, score, color)) {
                            optimalScore = score;
                        }
                    }
                }
            }
        }

        return optimalScore;
    }





    // Helper function.
    // Returns if new is better than old depending on color.
    // for color = 1 (black) larger value is better.
    private static boolean isBetter(int oldV, int newV, int color){
        if (color == 1){
            // maximizing function.
            return oldV < newV;
        }
        // for black color, lower is better.
        else{
            // minimizing function.
            return oldV > newV;
        }
    }

    private int getRandom(int range){
        return (int) ((Math.random() * 1000000000) % range);
    }
}

