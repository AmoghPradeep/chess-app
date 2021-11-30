package chessBackend;
// Datastructure to store positions of pieces in the chess board.

public class Position{
    private int x, y; // Matrix location (x, y)
    private String location; // Chess-board locations (alpha-numeric)

    public Position(int x, int y){
        this.setPosition(x, y);
    }

    public Position(String s){
        this.setPosition(s);
    }

    public Position(Position p){
        this.setPosition(p.getXCoordinate(), p.getYCoordinate());
    }

    // Change position of piece using co-ordinates or location strings.
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
        location = Character.toString((char) (x + 96)) + y;
    }

    public void setPosition(String newlocation){
        this.location = newlocation;
        this.x = (int) location.charAt(0) - 96;
        this.y = Character.getNumericValue(location.charAt(1));
    }

    // Compare 2 Positions.
    public boolean equals(Position position){
        if (x == position.getXCoordinate() && y == position.getYCoordinate()){
            return true;
        }
        return false;
    }

    // Checks if current position is inside a 8x8 chess board.
    public boolean insideBoard(){
        if (this.x > 8 || this.y > 8)
            return false;
        if (this.x < 1 || this.y < 1)
            return false;
        return true;
    }

    public int getXCoordinate(){
        return x;
    }

    public int getYCoordinate(){
        return y;
    }

    public String getLocation(){
        return location;
    }

    public String toString() { return x + ", " + y; }
}
