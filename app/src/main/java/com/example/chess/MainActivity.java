package com.example.chess;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import chessBackend.Game;
import chessBackend.Move;
import chessBackend.Piece;
import chessBackend.Position;

import androidx.gridlayout.widget.GridLayout;


import java.util.ArrayList;
import java.util.HashMap;


/*
    TODO:
    1. Add introduction page.
    2. Create a multiplayer (offline) mode.
    3. Create a main menu page.
    4. Fix the checkmate / game-end system.
 */

public class MainActivity extends AppCompatActivity {
    int boardSide;
    int cellSide;

    HashMap<String, Integer> resources = new HashMap<>();
    ImageView[][] androidBoard = new ImageView[8][8];
    Piece[][] gameBoard;

    Game game;

    ArrayList<Position> active; // Moves of the selected piece.
    Position selected;

    boolean boardCreated;
    int turn;
    int checkMate;
    boolean threadStarted = false;

    Thread thread = new Thread() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView aiThinkingTag = (TextView) findViewById(R.id.aiThinking);
                    makeAiMove();
                    aiThinkingTag.setAlpha(0f);
                }
            });
        }
    };

    class AI extends Thread {
        @Override
        public void run() {
            while (true) {
                if (turn == 1) {
                    if (!threadStarted){
                        thread.start();
                        threadStarted = true;
                        turn = - turn;
                    }
                    else {
                        thread.run();
                        turn = -turn;
                    }
                }

                if (checkMate != 0){
                    for (int i = 0; i < 8; ++i){
                        for (int j = 0; j < 8; ++j){
                            androidBoard[i][j].setOnClickListener(null);
                        }
                    }

                    if (checkMate == 1){
                        TextView wonText = (TextView) findViewById(R.id.wonText);
                        wonText.setAlpha(1f);
                    }
                    else {
                        TextView lostText = (TextView) findViewById(R.id.lostText);
                        lostText.setAlpha(1f);
                    }
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout board = (GridLayout) findViewById(R.id.boardLayout);
        active = new ArrayList<>();
        AI ai = new AI();
        ai.start();
        turn = -1;

        boardCreated = false;
        // Initializing board.

        board.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (board.getWidth() > 0 && board.getHeight() > 0 && !boardCreated) {
                this.boardSide = board.getMeasuredWidth();
                this.cellSide = boardSide / 8;

                // loading all image resources into hashmap.
                resources.put("Pawn1", R.drawable.pawn_white);
                resources.put("Pawn-1", R.drawable.pawn_black);
                resources.put("Bishop1", R.drawable.bishop_white);
                resources.put("Bishop-1", R.drawable.bishop_black);
                resources.put("Knight1", R.drawable.knight_white);
                resources.put("Knight-1", R.drawable.knight_black);
                resources.put("Rook1", R.drawable.rook_white);
                resources.put("Rook-1", R.drawable.rook_black);
                resources.put("Queen1", R.drawable.queen_white);
                resources.put("Queen-1", R.drawable.queen_black);
                resources.put("King1", R.drawable.king_white);
                resources.put("King-1", R.drawable.king_black);
                resources.put("circle", R.drawable.grey_circle);

                int[] boardLocation = new int[2];
                board.getLocationInWindow(boardLocation);

                for (int i = 0; i < board.getChildCount(); ++i) {
                    View cell = board.getChildAt(i);

                    if (cell instanceof ImageView) {
                        // Adjusting height and width of every cell.
                        ImageView marker = (ImageView) cell;

                        marker.getLayoutParams().width = cellSide;
                        marker.getLayoutParams().height = cellSide;

                        // Indexing each marker and storing them in a matrix.
                        int[] markerLocation = new int[2];
                        marker.getLocationInWindow(markerLocation);

                        int rowNumber = (markerLocation[1] - boardLocation[1]) / cellSide;
                        int colNumber = (markerLocation[0] - boardLocation[0]) / cellSide;

                        marker.setTag("M" + Integer.toString(rowNumber) + Integer.toString(colNumber));
                        androidBoard[rowNumber][colNumber] = marker;
                    }
                }
                this.game = new Game(1, 5);


                // Populating board with pieces.

                this.gameBoard = game.getBoard(); // Stores backend board. (Piece objects)

                for (int i = 0; i < 8; ++i) {
                    for (int j = 0; j < 8; ++j) {
                        if (gameBoard[i + 1][j + 1] != null) {
                            Piece piece = gameBoard[i + 1][j + 1];
                            androidBoard[i][j].setImageResource(resources.get(piece.getName() + Integer.toString(-piece.getColor())));
                            androidBoard[i][j].setAlpha(1f);
                        }
                    }
                }


                for (int i = 0; i < 8; ++i) {
                    for (int j = 0; j < 8; ++j) {
                        androidBoard[i][j].setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View view) {
                                                                      try {
                                                                          if (cellClick(view)) {
                                                                              turn = -turn;

                                                                              TextView aiThinkingTag = (TextView) findViewById(R.id.aiThinking);
                                                                              aiThinkingTag.setAlpha(1f);
                                                                          }
                                                                      } catch (Exception e) {
                                                                          e.printStackTrace();
                                                                      }
                                                                  }
                                                              }
                        );
                    }
                }

                boardCreated = true;
            }
        });

    }


    boolean cellClick(View view) throws Exception {
        if (gameBoard == null) return false;
        Log.i("check ", String.valueOf(game.check()));
        int row = Character.getNumericValue(view.getTag().toString().charAt(1));
        int col = Character.getNumericValue(view.getTag().toString().charAt(2));

        // If selected cell is active. (Is a move of selected piece)
        Position clicked = new Position(col + 1, row + 1);

        for (Position a : active) {
            if (a.equals(clicked)) {
                makePlayerMove(clicked);
                return true;
            }
        }

        clearMarkers();

        // If selected cell is inactive.
        if (gameBoard[row + 1][col + 1] == null) return false;
        // If selected cell is black (not under player's control).
        if (gameBoard[row + 1][col + 1].getColor() == 1) return false;

        getMoves(row, col);
        return false;
    }

    private void getMoves(int row, int col) {
        ArrayList<Position> moves = game.getMoves(new Position(col + 1, row + 1));

        if (moves == null) return;

        selected = new Position(col + 1, row + 1);
        for (Position move : moves) {
            active.add(move);
            int r = move.getYCoordinate() - 1;
            int c = move.getXCoordinate() - 1;
            // For kill moves.
            if (gameBoard[r + 1][c + 1] != null) {
                androidBoard[r][c].setBackgroundColor(Color.rgb(255, 0, 0));
            }
            // Normal moves.
            else {
                androidBoard[r][c].setAlpha(0.5f);
            }
        }

    }

    private void makePlayerMove(Position p) throws Exception {

        int x1 = selected.getXCoordinate();
        int y1 = selected.getYCoordinate();
        int x2 = p.getXCoordinate();
        int y2 = p.getYCoordinate();


        androidBoard[y1 - 1][x1 - 1].setImageResource(R.drawable.grey_circle);
        androidBoard[y2 - 1][x2 - 1].setImageResource(resources.get(gameBoard[y1][x1].getName() + Integer.toString(-gameBoard[y1][x1].getColor())));
        androidBoard[y2 - 1][x2 - 1].setAlpha(1f);

        game.makeMove(selected, p, -1);

        clearMarkers();
    }

    // Iterates through the board and clears all markers (Grey Circles, Kill markers etc.)
    public void clearMarkers() {

        active.clear();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                androidBoard[i][j].setBackgroundColor(Color.TRANSPARENT);
                if (gameBoard[i + 1][j + 1] == null) {
                    androidBoard[i][j].setAlpha(0f);
                }
            }
        }

        checkMate = game.checkmate();
    }

    public void makeAiMove() {
        Move aimove = game.getAiMove();

        int n1 = aimove.getStart().getXCoordinate();
        int m1 = aimove.getStart().getYCoordinate();
        int n2 = aimove.getDest().getXCoordinate();
        int m2 = aimove.getDest().getYCoordinate();

        androidBoard[m1 - 1][n1 - 1].setImageResource(R.drawable.grey_circle);
        androidBoard[m2 - 1][n2 - 1].setImageResource(resources.get(gameBoard[m2][n2].getName() + Integer.toString(-gameBoard[m2][n2].getColor())));
        androidBoard[m2 - 1][n2 - 1].setAlpha(1f);

        if (game.check())
            Toast.makeText(this, "Check !", Toast.LENGTH_LONG).show();
        clearMarkers();
    }

    // converts px to dp.
    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}