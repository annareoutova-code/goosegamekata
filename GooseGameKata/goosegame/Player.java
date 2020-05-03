package goosegame;
/**
*	@author Anna
*	This class represents a player. Contains the get and set methods and their attributes: Name, current position, previous position:
* 	the setCurPosition method also sets the previous position.
*/
	
public class Player{
	
	private String name;
	private int curPosition = 0;
	private int prevPosition = 0;
	
	public Player(String name){
		setName(name);
		this.curPosition = 0;
		this.prevPosition = 0;
	}


	public String getName(){
		return name;
	}
	
		
	public void setName(String name){
		this.name = name;
	}
	

	public int getCurPosition(){
		return curPosition;
	}
	
	public void setCurPosition(int pos){
		this.prevPosition = this.curPosition;
		this.curPosition = pos;	
	}
	
	
	public int getPrevPosition(){
		return this.prevPosition;
	}
	
	public void setPrevPosition(int pos){
		this.prevPosition = pos;
	}
	


}