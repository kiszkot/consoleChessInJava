package szachy;

/**
 * Definition of a King
 */

public class Krol extends Bierka {
	
	/**
	 * Constructor for a King
	 * @param color The color of the Piece
	 * @param position The position of the Piece
	 */
	public Krol(String color, String position) {
		this.color = color + "K";
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
	public boolean isKing() {return true;}

}
