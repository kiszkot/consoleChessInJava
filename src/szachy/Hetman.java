package szachy;

/**
 * Definition of a Queen
 */

public class Hetman extends Bierka {
	
	/**
	 * Constructor for a Queen
	 * @param color The color of the Piece
	 * @param position The position of the Piece
	 */
	public Hetman(String color, String position) {
		this.color = color + "Q";
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
	public boolean isQueen() {return true;}

}
