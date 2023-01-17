package szachy;

/**
 * Definition of a Knight
 */

public class Skoczek extends Bierka {
	
	/**
	 * Constructor for a Knight
	 * @param color The color of the Piece
	 * @param position The position of the Piece
	 */
	public Skoczek(String color, String position) {
		this.color = color + "N";
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
	public boolean isKnight() {return true;}

}
