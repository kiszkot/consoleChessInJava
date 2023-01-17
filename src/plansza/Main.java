package plansza;

import java.util.*;

/**
 * Wejsciowa funkcja gry w Szachy.
 * 
 * Stalemate: c2-c4, h7-h5, h2-h4, a7-a5, d1-a4, a8-a6, a4-a5, a6-h6, a5-c7
 * f7-f6, c7-d7, e8-f7, d7-b7, d8-d3, b7-b8, d3-h7, b8-c8, f7-g6, c8-e6.
 * 
 * Fools Mate: e2-e4, g7-g5, b1-c3, f7-f5, d1-h5
 * 
 */

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		Szachownica board = new Szachownica();
		String[] moves;
		
		int player = 1;
		String from = "", to = "", winner = "";
		System.out.println("Typing \"no\" will result in forfeit if at piece selction or return to selction at move selection");
		while(winner.isEmpty()) {
			
			System.out.println(board.toString());
			try {
				
				System.out.printf("Player : %d, choose piece\n", player);
				from = scan.nextLine();
				from = from.substring(0,2);
				if(from.compareTo("no")==0) break;
				
				if(!board.validPiece(from, player)) {
					System.out.println("Invalid"); continue;
				}
				
				moves = board.possibleMoves(from, player);
				for(String m : moves) System.out.print(m + " ");
				System.out.println("");
				
				System.out.printf("Where to?\n");
				to = scan.nextLine();
				to = to.substring(0,2);
				if(to.compareTo("no")==0) continue;
				if(!board.validMove(from, to, moves, player)) {
					System.out.println("Invalid"); continue;
				}
				
				board.move(from, to);
				
				if(++player > 2) player = 1;
				winner = board.checkWinner(player);
				
			} catch(Exception e) {
				System.out.println("An error occured, press enter to continue...");
				e.printStackTrace();
				scan.nextLine();
			}
			
			
		}
		
		if(winner.isEmpty()) {
			if(player == 1) winner = "Black";
			else winner = "White";
		}
		System.out.printf("The winner is: %s", winner);
		System.out.println(board.toString());
		scan.close();
	}

}
