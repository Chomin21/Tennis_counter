package tennis;

import java.io.Serializable;

public class Player implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private boolean gender; //남:t, 여:f
	private char team;
	
	public Player(String name, boolean gender, char team) {
		this.name = name;
		this.gender = gender;
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setGender(boolean gender) {
		this.gender = gender;
	}
	
	public boolean getGender() {
		return gender;
	}


	public char getTeam() {
		return team;
	}


	public void setTeam(char team) {
		this.team = team;
	}


	@Override
	 public String toString() {
	      return  String.format("%s,%b,%c",  name, gender, team);
	   }

}
