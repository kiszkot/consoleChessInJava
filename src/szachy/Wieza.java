package szachy;

/**
 * Definition of a Rook
 *
 */

public class Wieza extends Bierka {
	
	/**
	 * Constructor for a Rook
	 * @param color The color of the Piece
	 * @param position The position of the Piece
	 */
	public Wieza(String color, String position) {
		this.color = color + "R";
		this.position = position;
	}
	
	public String getColor() {
		return this.color.substring(0,1);
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public String toString() {
		return this.color;
	}
	
	@Override
	public boolean isRook() {return true;}
}
