package Exam.Model.Dynamics;

import Exam.Model.*;

public abstract class Dynamics {

/**
 * Factory method to create the dynamics wanted for the Potts system,
 * The type of dynamics required is specified by the integer argument being
 * 1 to specify Kawasaki dynamics, otherwise Glauber is used by default
 * 
 * Several version exist to allow specification of a seed to repeat measurements along with defaults using only an
 * Potts as an input to give default glauber dynamics
 * 
 * @param in Parameter to choose which dynamics to use. 1 for Kawasaki, no value or other values for Glauber
 * @return Returns either an instance of Glauber or Kawasaki dynamics for the updater
 */
	
	public static Dynamics createDynamics(Potts Potts, int in){
			if(in ==0)return new Glauber(Potts);
			else return new Glauber_Modified(Potts);
	}
	public static Dynamics createDynamics(Potts Potts,int in, long seed){
			if(in == 0)return new Glauber(Potts, seed);
			else return new Glauber_Modified(Potts,seed);
	}
	public static Dynamics createDynamics(Potts Potts){
		return new Glauber(Potts);
	
	}
	public static Dynamics createDynamics(Potts Potts, long seed){
		return new Glauber(Potts, seed);
	}
	
	public abstract void setSeed(long seed);
	public abstract void update(Potts in);
	
}
