package plansza;

import java.util.*;
import szachy.*;

/**
 * Basic Chess Board functionality
 */

public class Szachownica{
	
	/**
	 * Converts the horizontal board letter to an integer
	 */
	private HashMap<String,Integer> parseH = new HashMap<String,Integer>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put("a",0); put("b",1); put("c",2); put("d",3); put("e",4); put("f",5);
		put("g",6); put("h",7);
	}};
	
	/**
	 * Converts the horizontal integer to a board letter
	 */
	private HashMap<Integer,String> unparseH = new HashMap<Integer,String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put(0,"a"); put(1,"b"); put(2,"c"); put(3,"d"); put(4,"e"); put(5,"f");
		put(6,"g"); put(7,"h");
	}};
	
	//private HashMap<String,Bierka> graveyard = new HashMap<String,Bierka>();
	
	/**
	 * Board Bierka array
	 */
	private Bierka[][] board = new Bierka[8][8];
	
	/**
	 * List of taken pieces by available moves
	 */
	private List<String> takes = new ArrayList<String>();
	
	/**
	 * Lista ruchów roszady
	 */
	private List<String> castlingList = new ArrayList<String>();
	
	/**
	 * Scanner do wyboru promocji Pionek
	 */
	Scanner scan = new Scanner(System.in);
	
	/**
	 * Constructor for a board
	 */
	public Szachownica() {
		takes.clear();
		//pawns placement
		for(int i=0; i<8; i++) {
			board[1][i] = new Pionek("w", unparseH.get(i) + 2);
			board[6][i] = new Pionek("b", unparseH.get(i) + 7);
		}
		
		//rook placement
		board[0][0] = new Wieza("w", unparseH.get(0) + 1);
		board[0][7] = new Wieza("w", unparseH.get(7) + 1);
		board[7][0] = new Wieza("b", unparseH.get(0) + 8);
		board[7][7] = new Wieza("b", unparseH.get(7) + 8);
		
		//knight placement
		board[0][1] = new Skoczek("w", unparseH.get(1) + 1);
		board[0][6] = new Skoczek("w", unparseH.get(6) + 1);
		board[7][1] = new Skoczek("b", unparseH.get(1) + 8);
		board[7][6] = new Skoczek("b", unparseH.get(6) + 8);
		
		//bishop placement
		board[0][2] = new Goniec("w", unparseH.get(2) + 1);
		board[0][5] = new Goniec("w", unparseH.get(5) + 1);
		board[7][2] = new Goniec("b", unparseH.get(2) + 8);
		board[7][5] = new Goniec("b", unparseH.get(5) + 8);
		
		//king placement
		board[0][4] = new Krol("w", unparseH.get(4) + 1);
		board[7][4] = new Krol("b", unparseH.get(4) + 8);
		
		//queen placement
		board[0][3] = new Hetman("w", unparseH.get(3) + 1);
		board[7][3] = new Hetman("b", unparseH.get(3) + 8);
	}
	
	/**
	 * Copies the current board array to a new board array
	 * @param old The array to copy from
	 * @return Bierka[][]
	 */
	private Bierka[][] copyBoard(Bierka[][] old){
		Bierka[][] ret = new Bierka[8][8];
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				ret[i][j] = old[i][j];
			}
		}
		return ret;
	}
	
	/**
	 * Converts the board to a String for console displaying
	 * @return String
	 */
	public String toString() {
		String ret = "";
		for(int i=7; i>=0; i--) {
			ret += (i+1) + "|";
			for(int j=0; j<8; j++) {
				if(board[i][j] == null) ret = ((i+j)%2 != 0) ? ret + "\u2591\u2591|" : ret + "\u2592\u2592|";
				else ret = ret + board[i][j].toString() + "|";
			}
			ret = ret + "\n";
		}
		ret += " |";
		for(int i=0; i<8; i++) {
			ret += unparseH.get(i) + " |";
		}
		return ret;
	}
	
	/**
	 * Parses the current position from standard notation to array coordinates
	 * @param position The String in standard notation
	 * @return int[]
	 */
	private int[] parsePosition(String position) {
		int[] ret = new int[2];
		ret[1] = parseH.get(position.substring(0,1));
		ret[0] = Integer.parseInt(position.substring(1,2))-1;
		return ret;
	}
	
	/**
	 * Returns an array of possible moves for a given position
	 * @param position The position to check
	 * @param player The current player, 1 - White, 2 - Black
	 * @return String[]
	 */
	public String[] possibleMoves(String position, int player) {
		List<String> ret = new ArrayList<String>();
		int[] pos = parsePosition(position);
		Bierka piece = board[pos[0]][pos[1]];
		
		if(piece.isRook()) {
			possibleRook(pos, this.board, ret, this.takes, player);
		} else if(piece.isKnight()) possibleKnight(pos, this.board, ret, this.takes, player);
		else if(piece.isBishop()) possibleBishop(pos, this.board, ret, this.takes, player);
		else if(piece.isQueen()) possibleQueen(pos, this.board, ret, this.takes, player);
		else if((piece.isKing())) possibleKing(pos, this.board, ret, this.takes, player);
		else if(piece.isPawn()) possiblePawn(pos, this.board, ret, this.takes, player);
		
		return ret.toArray(new String[0]);
	}
	
	/**
	 * Returns an array of possible moves for a given position
	 * @param position The position to check
	 * @param board The board to modify
	 * @param player The current player, 1 - White, 2 - Black
	 * @return String[]
	 */
	public String[] possibleMoves(String position, Bierka[][] board, int player) {
		List<String> ret = new ArrayList<String>();
		List<String> takes = new ArrayList<String>();
		int[] pos = parsePosition(position);
		Bierka piece = board[pos[0]][pos[1]];
		
		if(piece.isRook()) {
			possibleRook(pos, board, ret, takes, player);
		} else if(piece.isKnight()) possibleKnight(pos, board, ret, takes, player);
		else if(piece.isBishop()) possibleBishop(pos, board, ret, takes, player);
		else if(piece.isQueen()) possibleQueen(pos, board, ret, takes, player);
		else if((piece.isKing())) possibleKing(pos, board, ret, takes, player);
		else if(piece.isPawn()) possiblePawn(pos, board, ret, takes, player);
		
		return ret.toArray(new String[0]);
	}
	
	/**
	 * Checks the possible moves for a Rook
	 * @param position The position to check
	 * @param moves The array where to put the possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 */
	private void possibleRook(int[] position, Bierka[][] board, List<String> moves, List<String> takes, int player) {
		int h = position[1];
		int v = position[0];
		//Vertical
		for(int i=v+1; i<8; i++) {
			Bierka tmp = board[i][h];
			if(tmp == null) moves.add(unparseH.get(h) + String.valueOf(i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h) + String.valueOf(i+1));
						takes.add(unparseH.get(h) + String.valueOf(i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h) + String.valueOf(i+1));
						takes.add(unparseH.get(h) + String.valueOf(i+1));
					}
				}
				break;
			}
		}
		for(int i=v-1; i>=0; i--) {
			Bierka tmp = board[i][h];
			if(tmp == null) moves.add(unparseH.get(h) + String.valueOf(i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h) + String.valueOf(i+1));
						takes.add(unparseH.get(h) + String.valueOf(i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h) + String.valueOf(i+1));
						takes.add(unparseH.get(h) + String.valueOf(i+1));
					}
				}
				break;
			}
		}
		// Horizontal
		for(int i=h+1; i<8; i++) {
			Bierka tmp = board[v][i];
			if(tmp == null) moves.add(unparseH.get(i) + String.valueOf(v+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(i) + String.valueOf(v+1));
						takes.add(unparseH.get(i) + String.valueOf(v+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(i) + String.valueOf(v+1));
						takes.add(unparseH.get(i) + String.valueOf(v+1));
					}
				}
				break;
			}
		}
		for(int i=h-1; i>=0; i--) {
			Bierka tmp = board[v][i];
			if(tmp == null) moves.add(unparseH.get(i) + String.valueOf(v+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(i) + String.valueOf(v+1));
						takes.add(unparseH.get(i) + String.valueOf(v+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(i) + String.valueOf(v+1));
						takes.add(unparseH.get(i) + String.valueOf(v+1));
					}
				}
				break;
			}
		}
	}
	
	/**
	 * Checks the possible moves for a Knight
	 * @param position The position to check
	 * @param moves The array where to put the possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 */
	private void possibleKnight(int[] position, Bierka[][] board, List<String> moves, List<String> takes, int player) {
		int h = position[1];
		int v = position[0];
		Bierka tmp;
		// Vertical
		if(v+2 < 8) for(int i=-1; i<=1; i+=2) {
			if(h+i < 8 && h+i >= 0) tmp = board[v+2][h+i];
			else continue;
			if(tmp == null) moves.add(unparseH.get(h+i) + String.valueOf(v+2+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v+2+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v+2+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v+2+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v+2+1));
					}
				}
			}
		}
		if(v-2 >= 0) for(int i=-1; i<=1; i+=2) {
			if(h+i < 8 && h+i >= 0) tmp = board[v-2][h+i];
			else continue;
			if(tmp == null) moves.add(unparseH.get(h+i) + String.valueOf(v-2+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v-2+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v-2+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v-2+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v-2+1));
					}
				}
			}
		}
		// Horizontal
		if(h+2 < 8) for(int i=-1; i<=1; i+=2) {
			if(v+i < 8 && v+i >= 0) tmp = board[v+i][h+2];
			else continue;
			if(tmp == null) moves.add(unparseH.get(h+2) + String.valueOf(v+i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h+2) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h+2) + String.valueOf(v+i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h+2) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h+2) + String.valueOf(v+i+1));
					}
				}
			}
		}
		if(h-2 >= 0) for(int i=-1; i<=1; i+=2) {
			if(v+i < 8 && v+i >= 0) tmp = board[v+i][h-2];
			else continue;
			if(tmp == null) moves.add(unparseH.get(h-2) + String.valueOf(v+i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h-2) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h-2) + String.valueOf(v+i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h-2) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h-2) + String.valueOf(v+i+1));
					}
				}
			}
		}
	}
	
	/**
	 * Checks the possible moves for a Bishop
	 * @param position The position to check
	 * @param moves The array where to put the possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 */
	private void possibleBishop(int[] position, Bierka[][] board, List<String> moves, List<String> takes, int player) {
		int h = position[1];
		int v = position[0];
		// UR
		for(int i=1; h+i<8 && v+i<8; i++) {
			Bierka tmp = board[v+i][h+i];
			if(tmp == null) moves.add(unparseH.get(h+i) + String.valueOf(v+i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v+i+1));
					} 
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v+i+1));
					} 
				}
				break;
			}
		}
		// UL
		for(int i=1; h-i>=0 && v+i<8; i++) {
			Bierka tmp = board[v+i][h-i];
			if(tmp == null) moves.add(unparseH.get(h-i) + String.valueOf(v+i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h-i) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h-i) + String.valueOf(v+i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h-i) + String.valueOf(v+i+1));
						takes.add(unparseH.get(h-i) + String.valueOf(v+i+1));
					}
				}
				break;
			}
		}
		// DR
		for(int i=1; h+i<8 && v-i>=0; i++) {
			Bierka tmp = board[v-i][h+i];
			if(tmp == null) moves.add(unparseH.get(h+i) + String.valueOf(v-i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v-i+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v-i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v-i+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v-i+1));
					}
				}
				break;
			}
		}
		// DL
		for(int i=1; h-i>=0 && v-i>=0; i++) {
			Bierka tmp = board[v-i][h-i];
			if(tmp == null) moves.add(unparseH.get(h-i) + String.valueOf(v-i+1));
			else {
				if(player == 1) {
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h-i) + String.valueOf(v-i+1));
						takes.add(unparseH.get(h-i) + String.valueOf(v-i+1));
					}
				} else {
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h-i) + String.valueOf(v-i+1));
						takes.add(unparseH.get(h-i) + String.valueOf(v-i+1));
					}
				}
				break;
			}
		}
	}
	
	/**
	 * Checks the possible moves for a Queen
	 * @param position The position to check
	 * @param moves The array where to put the possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 */
	private void possibleQueen(int[] position, Bierka[][] board, List<String> moves, List<String> takes, int player) {
		possibleRook(position, board, moves, takes, player);
		possibleBishop(position, board, moves, takes, player);
	}
	
	/**
	 * Checks the possible moves for a King
	 * @param position The position to check
	 * @param moves The array where to put the possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 */
	private void possibleKing(int[] position, Bierka[][] board, List<String> moves, List<String> takes, int player) {
		int h = position[1];
		int v = position[0];
		Bierka tmp, pos = board[v][h];
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {
				if(h+i < 8 && h+i >= 0 && v+j < 8 && v+j >= 0) tmp = board[v+j][h+i];
				else continue;
				if(tmp == null) moves.add(unparseH.get(h+i) + String.valueOf(v+j+1));
				else {
					if((player == 1 && tmp.isBlack()) || ((player == 2 && tmp.isWhite()))) {
						moves.add(unparseH.get(h+i) + String.valueOf(v+j+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v+j+1));
					}
				}
			}
		}
		if(pos.firstMove) checkCastling(position, board, moves, player);
	}
	
	/**
	 * Funckja pomocnicza do sprawdzenia wykonania Roszady
	 */
	private void checkCastling(int[] position, Bierka[][] board, List<String> moves, int player) {
		int h = position[1];
		int v = position[0];
		Bierka tmp;
		for(int i=1; h+i<7; i++) {
			tmp = board[v][h+i];
			if(tmp != null) return;
		}
		tmp = board[v][7];
		if(tmp == null) {}
		else if((player == 1 && tmp.isRook() && tmp.isWhite() && tmp.firstMove) || (player == 2 && tmp.isRook() && tmp.isBlack() && tmp.firstMove)) {
			moves.add(unparseH.get(h+2) + String.valueOf(v+1));
			castlingList.add(unparseH.get(h+2) + String.valueOf(v+1));
		}
		for(int i=1; h-i>0; i++) {
			tmp = board[v][h-i];
			if(tmp != null) return;
		}
		tmp = board[v][0];
		if(tmp == null) {}
		else if((player == 1 && tmp.isRook() && tmp.isWhite() && tmp.firstMove) || (player == 2 && tmp.isRook() && tmp.isBlack() && tmp.firstMove)) {
			moves.add(unparseH.get(h-2) + String.valueOf(v+1));
			castlingList.add(unparseH.get(h-2) + String.valueOf(v+1));
		}
	}
	
	/**
	 * Checks the possible moves for a Pawn
	 * @param position The position to check
	 * @param moves The array where to put the possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 */
	private void possiblePawn(int[] position, Bierka[][] board, List<String> moves, List<String> takes, int player) {
		int h = position[1];
		int v = position[0];
		Bierka tmp;
		if(player == 1) { // White
			if(v+1 < 8) {
				for(int i=-1; i<=1; i+=2) {
					if(h+i >=0 && h+i < 8) tmp = board[v+1][h+i];
					else continue;
					if(tmp == null) continue;
					if(tmp.getColor().compareTo("b") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v+1+1));
						takes.add(unparseH.get(h+i) + String.valueOf(v+1+1));
					}
				}
				tmp = board[v+1][h];
				if(tmp == null) {
					moves.add(unparseH.get(h) + String.valueOf(v+1+1));
					if(v == 1) {
						tmp = board[v+2][h];
						if(tmp == null) moves.add(unparseH.get(h) + String.valueOf(v+2+1));
					}
				}
			}
		} else { // Black
			if(v-1 >= 0) {
				for(int i=-1; i<=1; i+=2) {
					if(h+i < 8 && h+i >= 0) tmp = board[v-1][h+i];
					else continue;
					if(tmp == null) continue;
					if(tmp.getColor().compareTo("w") == 0) {
						moves.add(unparseH.get(h+i) + String.valueOf(v));
						takes.add(unparseH.get(h+i) + String.valueOf(v));
					}
				}
				tmp = board[v-1][h];
				if(tmp == null) {
					moves.add(unparseH.get(h) + String.valueOf(v));
					if(v == 6) {
						tmp = board[v-2][h];
						if(tmp == null) moves.add(unparseH.get(h) + String.valueOf(v-2+1));
					}
				}
			}
		}
	}
	
	/**
	 * Moves a selected piece to the selected location
	 * @param from Position to move from
	 * @param to Position to move to
	 */
	public void move(String from, String to) {
		int[] fromI = parsePosition(from);
		int[] toI = parsePosition(to);
		
		Bierka piece;
		if(castlingList.contains(to)) {
			castling(fromI, toI, this.board);
			castlingList.clear();
			takes.clear();
			return;
		}
		if(this.takes.contains(to)) piece = null;
		else piece = this.board[toI[0]][toI[1]];
		
		this.board[toI[0]][toI[1]] = board[fromI[0]][fromI[1]];
		this.board[toI[0]][toI[1]].move(to);
		this.board[fromI[0]][fromI[1]] = piece;
		if(piece != null) this.board[fromI[0]][fromI[1]].move(from);
		
		if(this.board[toI[0]][toI[1]].isPawn()) {
			this.board[toI[0]][toI[1]] = promote(this.board[toI[0]][toI[1]]);
		}
		
		takes.clear();
	}
	
	/**
	 * Move function to test if after the move there is a check
	 * @param from Position to move from
	 * @param to Position to move to
	 * @param board The temporary board
	 * @param takes The temporary list of takes
	 */
	private void move(String from, String to, Bierka[][] board, List<String> takes) {
		int[] fromI = parsePosition(from);
		int[] toI = parsePosition(to);
		
		Bierka piece;
		if(castlingList.contains(to)) {
			castling(fromI, toI, board);
			return;
		}
		
		if(takes.contains(to)) {
			piece = null;
		}
		else piece = board[toI[0]][toI[1]];
		
		board[toI[0]][toI[1]] = board[fromI[0]][fromI[1]];
		board[fromI[0]][fromI[1]] = piece;
		
		if(board[toI[0]][toI[1]].isPawn()) {
			board[toI[0]][toI[1]] = promote(board[toI[0]][toI[1]]);
		}
	}
	
	/**
	 * Funkcja do wykonania roszady
	 */
	private void castling(int[] from, int[] to, Bierka[][] board) {
		Bierka fromP = board[from[0]][from[1]];
		if(to[1] > from[1]) {
			board[to[0]][to[1]] = fromP;
			board[from[0]][from[1]] = null;
			board[to[0]][to[1]-1] = board[from[0]][7];
			board[to[0]][7] = null;
		} else if(to[1] < from[1]) {
			board[to[0]][to[1]] = fromP;
			board[from[0]][from[1]] = null;
			board[to[0]][to[1]+1] = board[from[0]][0];
			board[to[0]][0] = null;
		}
	}
	
	/**
	 * Funkcja pomocnicza do promocji Pionek
	 * @param piece Pionek do promocji
	 * @return Bierka
	 */
	private Bierka promote(Bierka piece) {
		int[] position = parsePosition(piece.getPosition());
		String choice = "";
		if((position[0] == 7 && piece.isWhite()) || (position[0] == 0 && piece.isBlack())) {
			while(piece.isPawn()) {
				try {
					System.out.println("B, N, Q, R ?");
					choice = scan.nextLine();
					piece = piece.promote(choice);
				} catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}		
		return piece;
	}
	
	/**
	 * Checks if the selected piece is a valid piece
	 * @param position The selected position
	 * @param player The current player, 1 - White, 2 - Black
	 * @return boolean
	 */
	public boolean validPiece(String position, int player) {
		int[] pos = parsePosition(position);
		Bierka tmp = board[pos[0]][pos[1]];
		if(tmp == null) return false;
		else if(tmp.getColor().compareTo("w") == 0 && player == 1) return true;
		else if(tmp.getColor().compareTo("b") == 0 && player == 2) return true;
		else return false;
	}
	
	/**
	 * Checks if the move is valid
	 * @param from The position to move from
	 * @param to The position to check
	 * @param moves The array of possible moves
	 * @param player The current player, 1 - White, 2 - Black
	 * @return boolean
	 */
	public boolean validMove(String from, String to, String[] moves, int player) {
		Bierka[][] board = copyBoard(this.board);
		List<String> takes = List.copyOf(this.takes);
		for(String m : moves) {
			if(m.compareToIgnoreCase(to) == 0) {
				board = copyBoard(this.board);;
				takes = List.copyOf(this.takes);
				move(from, to, board, takes);
				int res = checkCheck(board);
				if(res == player) {
					System.out.println("You are in check!");
					return false;
				}
				else return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if any player is in check.
	 * @param tmpBoard Temporary board for moving (does not actually work)
	 * @return 0 - no check, 1 - white, 2, black
	 */
	private int checkCheck(Bierka[][] tmpBoard) {
		Bierka tmp;
		String[] moves;
		int[] pos;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				tmp = tmpBoard[i][j];
				if(tmp == null) continue;
				else if(tmp.getColor().compareTo("w") == 0){
					moves = possibleMoves(unparseH.get(j) + String.valueOf(i+1), tmpBoard, 1);
					for(String m : moves) {
						pos = parsePosition(m);
						tmp = tmpBoard[pos[0]][pos[1]];
						if(tmp == null) {}
						else if(tmp.isKing() && tmp.isBlack()) return 2;
					}
				} else {
					moves = possibleMoves(unparseH.get(j) + String.valueOf(i+1), tmpBoard, 2);
					for(String m : moves) {
						pos = parsePosition(m);
						tmp = tmpBoard[pos[0]][pos[1]];
						if(tmp == null) {}
						else if(tmp.isKing() && tmp.isWhite()) return 1;
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * Funkcja do sprawdzania kto wygrał 
	 * @param player Aktualny gracz, 1 - Biały, 2 - Czarny
	 * @return String
	 */
	public String checkWinner(int player) {
		Bierka[][] tmpBoard = copyBoard(this.board);
		int res = checkCheck(tmpBoard);
		if(res == player && checkDraw(player)) {
			if(res == 1) return "Black";
			else return "White";
		}
		if(checkDraw(player)) return "Draw";
		return "";
	}
	
	/**
	 * Funkcja pomocnicza do wyznaczania patu
	 * @param player Aktualny gracz, 1 - Biały, 2 - Czarny
	 * @return boolean
	 */
	private boolean checkDraw(int player) {
		String[] moves;
		String from;
		for(int i=0; i<8; i++) {
			for(int j=1; j<=8; j++) {
				if(this.board[j-1][i] == null) continue;
				from = unparseH.get(i) + String.valueOf(j);
				if(!validPiece(from, player)) continue;
				moves = possibleMoves(from, player);
				if(moves.length != 0) {
					for(String m : moves) {
						if(validMove(from, m, moves, player)) return false;
					}
				}
			}
		}
		return true;
	}
}
