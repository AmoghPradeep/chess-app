package chessBackend;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Game game = new Game(1, 3);
        Scanner sc = new Scanner(System.in);

        String input = "";

        while(!input.equals("end")){
            System.out.println("Enter Input : ");
            input = sc.nextLine();

            if (input.equals("display")){
                Piece[][] board = game.getBoard();
                for (int i = 1; i <= 8; ++i){
                    for (int j = 1; j <= 8; ++j) {
                        String cell = null;

                        if (board[i][j] == null)
                            cell = "_";
                        else{
                            cell = board[i][j].getLabel();
                            if (board[i][j].getColor() == -1) {
                                cell = cell.toLowerCase(Locale.ROOT);
                            }
                        }

                        System.out.print(cell);
                    }
                    System.out.println();
                }
            }

            if (input.equals("get move")){
                int x = sc.nextInt();
                int y = sc.nextInt();

                ArrayList<Position> moves = game.getMoves(new Position(x, y));
                if (moves == null) {
                    System.out.println("No moves.");
                    continue;
                }
                System.out.println("Moves : ");
                for (Position move : moves){
                    System.out.println(move.toString());
                }
            }

            if (input.equals("make move")){
                System.out.println("initial : ");
                int x = sc.nextInt();
                int y = sc.nextInt();
                System.out.println("final : ");
                int a = sc.nextInt();
                int b = sc.nextInt();
                game.makeMove(new Position(x, y), new Position(a, b), 1);
            }
        }
    }
}
