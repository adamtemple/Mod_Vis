package SIRS.Model;


/**
 * The agent class are the cells in the lattice. They can take 3 states
 * @author s0831683
 *
 */
public class Agent {
	
	//The different states an agent can take
	public boolean 	susceptible = false,
					infected = false,
					recovered = false,
					immune = false;				//by default agent is always not immune
	//Number of total and initial states the agent can take
	protected static int states = 3;
	
	public Agent(){
		double rand = (double)states * Math.random();
		if(rand <= 1.0) susceptible = true;
		if(rand > 1.0 && rand <= 2.0) infected = true;
		if(rand > 2.0 && rand <= 3.0) recovered = true;
	}
	
	//Allows specification of the initial state of the agent
	//Value of the input must be between 0 and 1
	public Agent(double in){
		in = (double)states * in;
		if(in > (double)states){
			System.out.println("Random value input for creation of agent state is outside number of possible initial states");
			return;
		}
		if(in <= 1.0) susceptible = true;
		if(in > 1.0 && in <= 2.0) infected = true;
		if(in > 2.0 && in <= 3.0) recovered = true;
	}
	
	//SETTERS
	public void setSusceptible(){
		susceptible = true;
		infected = false;
		recovered = false;
	}
	public void setInfected(){
		susceptible = false;
		infected = true;
		recovered = false;
	}
	public void setRecovered(){
		susceptible = false;
		infected = false;
		recovered = true;
	}
	public void setImmune(){
		immune = true;
		susceptible = false;
		infected = false;
		recovered = false;
	}
	
	//GETTERS
	public static int getNumStates(){
		return states;
	}
	
	public boolean isSusceptible(){
		return susceptible;
	}
	public boolean isInfected(){
		return infected;
	}
	public boolean isRecovered(){
		return recovered;
	}
	public boolean isImmune(){
		return immune;
	}
}
