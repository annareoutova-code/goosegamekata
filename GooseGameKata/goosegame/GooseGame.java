package goosegame;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;


/**
*	@author Anna
*  This class contains useful methods for rolling the dice, moving the pawn,
*  manage the various cases that a player may encounter: Bridge, Goose, Finish
*	
*/

public class GooseGame{
	
	private final static int[] GOOSE = {5,9,14,18,23,27,32,36,41,45,50,54,59};
	private final static int BRIDGE = 6;
	private final static int FINISH = 63;
	
	private boolean winnerExists = false;

	List<Player> pList;

	/**
	*	Constructor who receives a list of players at the entrance
	*
	*/	
	public GooseGame(List<Player> pList){
		this.pList = pList;	
	}
	
	/**
	*	Method that checks if a winner exists. Returns a Boolean.
	*
	*/
	public boolean getWinnerExists(){
		return winnerExists;
	}
	
	/**
	*	Method that allows you to randomly roll 2 dice in place of the user
	* Return an array with 2 values
	*/
	public int[] launchDice(){
		int[] tmpDice = new int[2];
		int min = 1;
		int max = 6;
		Random rand = new Random();
		tmpDice[0] = rand.nextInt((max - min) + 1) + min;
		tmpDice[1] = rand.nextInt((max - min) + 1) + min;
		return tmpDice;
	}
	
	/**
	* Method that allows you to move a checker. Call the launchDice method.
	* Manages the cases in which a player may encounter or the bridge, the goose, the winnings, or
	* if a cell is already busy, by calling the appropriate methods.
	*
	*/
	public String movePawn(Player pl){
		boolean isGoose;
		String name = pl.getName();			
		int[]dice = launchDice();
		int destPosition = pl.getCurPosition() + dice[0] + dice[1];	
		String result = name + " rolls " + dice[0] + ", " + dice[1] + ". " + name + " moves from ";	
		if(pl.getCurPosition() == 0) {
			result += "Start to ";
		}else{
			result += pl.getCurPosition() + " to ";
		}
		
		if(destPosition <= FINISH) {
			if(destPosition == BRIDGE){
				destPosition = BRIDGE * 2;
				result += "The Bridge. " + name + " jumps to ";
			}		
			result += destPosition;	
			if(destPosition < FINISH){		
				do{			
					isGoose = checkIfGoose(destPosition); 
					if(isGoose == true){
						result += ", The Goose. " + name + " moves again and goes to ";
						destPosition += dice[0] + dice[1];
						if(destPosition <= FINISH){
							result += destPosition;
						}
					}				
				}while(isGoose == true);			    
			}
			
			if(destPosition == FINISH) {
				result +=". " + name + " Wins!!";
				winnerExists = true;
			}		
		}
		
		if(destPosition > FINISH) {
			  destPosition = FINISH - (destPosition - FINISH);
			  result += FINISH + ". " + name + " bounces! " + name + " returns to " + destPosition;
		}
		
		pl.setCurPosition(destPosition);
		String occupied = manageIfCellIsOccupied(pList, pl);
		if(occupied != ""){
			 result += occupied;
		}
		return result;				
	}
	
	/**
	* Check if a cell matches the Goose and return true if so	*
	*/
	 public boolean checkIfGoose(int cell){
	 	for(int i = 0; i < GOOSE.length; i++){
	 		if(cell == GOOSE[i]){
	 			return true;
	 		}
	 	}
	 	return false;
	 }
	 
	/**
	*	Check if a cell is occupied by someone. Compose a string if yes e
	* 	returns it to the calling method
	*
	*/	public String manageIfCellIsOccupied(List<Player> list, Player pl){
		Player pl2; 
		String result = "";
		for(int i = 0; i < list.size(); i++){
			if((pl.getName() != list.get(i).getName()) && (pl.getCurPosition() == list.get(i).getCurPosition())){
				pl2 = list.get(i);	
				result += ". On " +  pl.getCurPosition() + " there is " + pl2.getName() + " who returns to ";
				pl2.setCurPosition(pl.getPrevPosition());
				result += pl2.getCurPosition();			
			}
		}	
		return result;	
	}
}