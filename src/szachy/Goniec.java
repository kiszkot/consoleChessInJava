package szachy;

/**
 * Definition of a Bishop
 *
 */

public class Goniec extends Bierka {
	
	/**
	 * Constructor for a Bishop
	 * @param color The color of the Piece
	 * @param position The position of the Piece
	 */
	public Goniec(String color, String position) {
		this.color = color + "B";
		this.position = position;
	}

	@Override
	public String getPosition() {
		return this.position;
	}

	@Override
	public String getColor() {
		return this.color.substring(0,1);
	}

	@Override
	public String toString() {
		return this.color;
	}
	
	@Override
	public boolean isBishop() {return true;}

}
