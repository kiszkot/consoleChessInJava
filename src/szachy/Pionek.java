package szachy;

/**
 * Definition of a Pawn
 *
 */

public class Pionek extends Bierka {
	
	/**
	 * Constructor for a Pawn
	 * @param color The color of the Piece
	 * @param position The position of the Piece
	 */
	public Pionek(String color, String position) {
		this.color = color + "P";
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
	public boolean isPawn() {return true;}
	
	@Override
	public Bierka promote(String to) {
		Bierka ret;
		switch(to) {
		case "R":
			ret = new Wieza(this.getColor(), this.position);
			break;
		case "B":
			ret = new Goniec(this.getColor(), this.position);
			break;
		case "N":
			ret = new Skoczek(this.getColor(), this.position);
			break;
		case "Q":
			ret = new Hetman(this.getColor(), this.position);
			break;
		default:
			ret = this;
			break;
		}
		
		return ret;
	}
}
