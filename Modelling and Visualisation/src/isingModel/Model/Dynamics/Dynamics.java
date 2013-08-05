package isingModel.Model.Dynamics;

import isingModel.Model.Ising;

public abstract class Dynamics {

/**
 * Factory method to create the dynamics wanted for the Ising system,
 * The type of dynamics required is specified by the integer argument being
 * 1 to specify Kawasaki dynamics, otherwise Glauber is used by default
 * 
 * Several version exist to allow specification of a seed to repeat measurements along with defaults using only an
 * ising as an input to give default glauber dynamics
 * 
 * @param in Parameter to choose which dynamics to use. 1 for Kawasaki, no value or other values for Glauber
 * @return Returns either an instance of Glauber or Kawasaki dynamics for the updater
 */
	
	public static Dynamics createDynamics(Ising ising, int in){
		if(in != 1 ){
			return new Glauber(ising);
		}else{
			return new Kawasaki(ising);
		}
	}
	public static Dynamics createDynamics(Ising ising, int in, long seed){
		if(in != 1 ){
			return new Glauber(ising, seed);
		}else{
			return new Kawasaki(ising, seed);
		}
	}
	
	public static Dynamics createDynamics(Ising ising, long seed){
		return new Glauber(ising, seed);
	}
	public static Dynamics createDynamics(Ising ising){
		return new Glauber(ising);
	}
	
	public abstract void setSeed(long seed);
	public abstract void update(Ising in);
	
}
