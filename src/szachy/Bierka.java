package szachy;

/**
 * Abstract Piece class for basic piece functionality
 *
 */

public abstract class Bierka {
	
	/**
	 * The piece color, w for white, b for black
	 */
	protected String color;
	
	/**
	 * The piece position
	 */
	protected String position;
	
	/**
	 * True if the Bierka did not make a first move
	 */
	public boolean firstMove = true;
	
	/**
	 * Gets the Piece position
	 * @return String
	 */
	public abstract String getPosition();
	
	/**
	 * Gets the piece color
	 * @return String
	 */
	public abstract String getColor();
	
	/**
	 * Converts the piece to a String with piece color and figure letter
	 * @return String
	 */
	public abstract String toString();
	
	/**
	 * Set the piece position to the new position, basically moving the Piece
	 * @param position The position to move to
	 */
	public void move(String position) {
		this.firstMove = false;
		this.position = position;
	}
	
	/**
	 * Checks if the piece is a Rook
	 * @return boolean
	 */
	public boolean isRook() {return false;}
	
	/**
	 * Checks if the piece is a Pawn
	 * @return boolean
	 */
	public boolean isPawn() {return false;}
	
	/**
	 * Checks if the piece is a Bishop
	 * @return boolean
	 */
	public boolean isBishop() {return false;}
	
	/**
	 * Checks if the piece is a King
	 * @return boolean
	 */
	public boolean isKing() {return false;}
	
	/**
	 * Checks if the piece is a Queen
	 * @return boolean
	 */
	public boolean isQueen() {return false;}
	
	/**
	 * Checks if the piece is a Knight
	 * @return boolean
	 */
	public boolean isKnight() {return false;}
	
	/**
	 * Metoda sprawdzająca czy dana Bierka jest Czarna
	 * @return boolean
	 */
	public boolean isBlack() {
		return (this.getColor().compareTo("b") == 0) ? true : false;
	}
	
	/**
	 * Metoda sprawdzająca czy dana Bierka jest Biała
	 * @return boolean
	 */
	public boolean isWhite() {
		return (this.getColor().compareTo("w") == 0) ? true : false;
	}
	
	/**
	 * Metoda do promocji Pionka
	 * @param to Znak Bierki do promowania
	 * @return Bierka
	 */
	public Bierka promote(String to) {
		return this;
	}

}
