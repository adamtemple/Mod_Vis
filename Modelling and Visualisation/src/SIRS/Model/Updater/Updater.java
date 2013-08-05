package SIRS.Model.Updater;

import java.util.Random;

import SIRS.Model.SIRS_System;

public class Updater {

	int length;
	Random rand;
	long seed;
	int x, y;
	
	public Updater(){
		rand = new Random();
	}
	public Updater(long seed){
		this.seed = seed;
		rand = new Random(seed);
	}
	/**
	 * Update loop to update a system of agents
	 * @param agents the system for updating
	 */
	
	public void update(SIRS_System agents){
		
		length = agents.getSystemSize();
		for(int i=0; i < (length*length); i++){
			x = rand.nextInt(length);
			y = rand.nextInt(length);
		if(agents.getAgent(x, y).isImmune()) return; //if immune, do nothing	
			//Statements to deal with each case
			if(agents.getAgent(x,y).isSusceptible()){
				//Checks for infected n.n., if infected and the probability is larger than a random number, then changes state
				if(agents.checkInfected(x, y) && agents.getProb(0) >= Math.random()){
					agents.setInfected(x,y); 		//If n.n. is infected, gets infected with prob p1
				}
				
			}else if(agents.getAgent(x,y).isInfected()){
				if(agents.getProb(1) >= Math.random()){
					agents.setRecovered(x,y);
				}
				
			}else if(agents.getAgent(x,y).isRecovered()){
				if(agents.getProb(2) >= Math.random()){
					agents.setSusceptible(x,y);
				}
				
			}else{
				System.out.println("agent ("+x+","+y+") is not in a valid state");
			}
			
		}//For loop
	}
	
}//Classs brackets
