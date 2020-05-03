package goosegame;

/**
*@author Anna
*
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


/**
* This is the class that manages:
* - adding players
* - the moves of the players
* - the order of the players' moves
* - error messages
*/
public class Initialization{
	
	private final static int MAX_PLAYERS = 4;
	private final static int MIN_PLAYERS = 2;
	private final static int MAX_RETRY = 10;
	public List<Player> pList = new ArrayList<Player>();
	private boolean winnerExists;
	private boolean gameStarted;
	private int countError = 0;
	GooseGame gGame;
	Scanner scan = new Scanner(System.in);		
	
	/**
	*	Method that handles invocations of the following methods:
	*	- executeCommand;
	*	- printMenu;
	*	- getPlayerCommand;
	*	- manageErrorMessage
	*/
	public void initGame(){	
		String command = "";
		String pName = "";	
		int nPlayer = 0;
		boolean alreadyExists;
		int executed;		
		System.out.println("\n" + "*************" + "Start Game! " + "*************" + "\n");			
		do{	
			printMenu();
			command = getPlayerCommand();
			alreadyExists = false;
			executed = executeCommand(command);
			if(executed > 1){
				manageErrorMessage(executed);
			}else{
				countError = 0;		
			}					
		}while (pList.size() < MIN_PLAYERS);
			
		do{	
			alreadyExists = false;
			if(pList.size() <= MAX_PLAYERS && gameStarted == false){
				printMenu();
			}
			command = getPlayerCommand();
			executed = executeCommand(command.trim());
			if(executed > 1){
				manageErrorMessage(executed);
			}else{
				countError = 0;	
				if(gameStarted == true) {	
					if(gGame.getWinnerExists() == true){
						this.winnerExists = true;
					}
				}
			}					
			
		}while(winnerExists != true);	
		System.exit(0);
	}
	
	/**
	* 	This method receives a player's name as input and returns true if the player already exists in the
	* 	list of players; 
	*	False otherwise;
	*
	*/
	public boolean checkIfPlayerExists(String pName){
		for(int i = 0; i < pList.size(); i++){
					if(pList.get(i).getName().equalsIgnoreCase(pName)){
						return true;
					}
		}
		return false;
	}	
	
	/**
* This method receives a command as input and verifies its correctness both at the syntax level
* that at the level of appropriateness.
* If the command starts with "add player" and the players are <MAX_PLAYERS then call the method "checkIfPlayerExists"
* to check if this player already exists. Adds the player only if he does not exist in the player list.
* If the command starts with "move" + "PLAYERS NAME" then:
* - if the game has already started, it makes the player perform the move;
* - if the game has not started, create the game instance and then have the player perform the move;
*
* Returns an integer from the value:
* - 1 if the command has been executed;
* - 2 if the command (in this case "add player") was not executed because the player already exists;
* - 3 if the command has not been executed for an error other than type 2;
	*/
	public int executeCommand(String command){
		String result = "";
		String addPlayer = "add player";
		int executed = 3;
		int moveCmd = command.indexOf("move");
		int addPlayerCmd = command.indexOf(addPlayer);
		if(moveCmd == 0) {
			if(pList.size() >= MIN_PLAYERS){
				for(Player player : pList){
					if(command.trim().equalsIgnoreCase("move " + player.getName())){
						if(gameStarted == false){
							gGame = new GooseGame(pList);
							gameStarted = true;
						}						
						result = gGame.movePawn(player);
						System.out.println(result);
						executed = 1;
						break;								
					}
				}
			}		
		}else {
			if((addPlayerCmd == 0 && command.trim().length() > addPlayer.length()) && (pList.size() < MAX_PLAYERS)){				
				command = command.substring(addPlayer.length()).trim();					
				countError = 0;							
				boolean	alreadyExists = checkIfPlayerExists(command);	
				if(gameStarted == false){
					if(alreadyExists == false){
						command = Character.toUpperCase(command.charAt(0)) + command.substring(1);
						Player player = new Player(command);
						pList.add(player);
						System.out.print("Player: ");
						for(int i = 0; i < pList.size(); i++){
							if(i < (pList.size()-1)){
								System.out.print(pList.get(i).getName() + ", ");
							}else{
								System.out.print(pList.get(i).getName() + "\n");
							}
						}
						executed = 1;		
					}else{
						executed = 2;
					}
				}	
			}		
		}
		return executed;
	}
	
	
	/**
	*  This method prints the appropriate menu for players.
	*  In particular if the game has not started yet and the players are at least MIN_PLAYERS and less than MAX_PLAYERS it is possible
	*  start the game or add new players.
	*  If the players have reached MAX_PLAYERS it is only possible to start playing by writing a move.
	*  If the players are fewer than MAX_PLAYERS but a move has already been made by one of the players then it will no longer be
	*  possible to add new players
	*/
	public void printMenu(){
		if(pList.size() < MIN_PLAYERS){
			System.out.println("\n" + "Write 'add player' and player name"+ "\n");	
		}else if(pList.size() >= MIN_PLAYERS && pList.size() < MAX_PLAYERS && gameStarted == false){
			System.out.println("\n" + "- For add new player write 'add player' and player name"
				+ "\n" + "- For START GooseGame write 'move ' and player name" + "\n");
		}else{
			if(pList.size() == MAX_PLAYERS && gameStarted == false){
				System.out.println("\n" + "* For START GooseGame write 'move ' and player name" + "\n");
			}else{
				if(gameStarted == true){
					System.out.println("\n" + "* For move player write 'move ' and player name" + "\n");
				}
			}
		}
	}
	
	/**
	* A command written by the player is read with this method.
	* Returns a string written in lowercase and trimmed.
	*/
	public String getPlayerCommand(){
		String command = scan.nextLine().trim().toLowerCase();
		return command;
	}
	
	/**
	* This method handles error messages based on the parameter received at the input
	* and based on whether the game has already started.
	* if an error is repeated maxRetry + 1 times then the game ends.
	* Does not return data.
	*/
	public void manageErrorMessage(int executed){
			if(gameStarted == true){
				System.out.println("-- Error! You can write only move command");
			}else{
				if(executed == 2) {
					System.out.println("-- This name already exist." + "\n");
				}else{
					System.out.println("-- Error! This command is not recognize. Retry please.");
				}
			}	
			countError++;	
			if(countError == MAX_RETRY){
			System.out.println("\t" + "-- Max retry reached. The game is ended." + "\n");
				System.exit(0);
			}		
	}
	
}