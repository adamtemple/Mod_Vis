package isingModel.Model.Dynamics;
import isingModel.Model.Ising;

import java.util.Random;
import java.lang.Math;

public class Glauber extends Dynamics {

	Random rand;
	long seed;
	int x, y, length;
	double KT, J, dE;
	Ising ising;
	double[] boltzmannFactors = new double [5]; //i.e 0 to 8J
	double prob;
	double E1, E2;
	
	//CONSTRUCTOR
	
	public Glauber(Ising in){
		rand = new Random();
		ising = in;
		calcBoltzmannFactors(ising);
		System.out.println("Running with Glauber Dynamics");
	}
	//Allows for error checking with seed
	public Glauber(Ising in, long seed){
		this(in);
		rand = new Random(seed);
	}
	
	
	/**
	 * Method to set the seed of the iterator object
	 */
	public void setSeed(long seed){
		this.seed = seed;
		rand = new Random(seed);
	}
	
	
	/**
	 * Method to change the boltzmann factors if the interaction potential, temperature or k is altered
	 * This allows better efficiency than having to called Math.exp each time in the iteration loop
	 * @param in system in which values have been altered
	 */
	//Only really need 4J and 8J as dE = -8,-4,0,4,8 and negative and zero values of dE
	//are always accepted
	private void calcBoltzmannFactors(Ising in){
		KT = in.getKT();
		J = in.getJ();
		for(int i=0; i<boltzmannFactors.length; i++){		//goes:  0, 1, 2, 3, 4 to give 0 to 8 in exponential
			boltzmannFactors[i] = Math.exp( (-2.0 * i * J) / KT );
			
			
		}
		
	}
	
	/**
	 * update method which updates the flips of the ising model using metropol
	 */
	//nextInt(n) picks from zero (inclusive) to N exclusive
	public void update(Ising in){
		length = in.getN();
		if(in.getKT() != KT || in.getJ() != J) calcBoltzmannFactors(in);	//If the system params have changed, recalculated stored values
		for(int i=0;i<(length*length);i++){
		
			x = rand.nextInt(length);
			y = rand.nextInt(length);
			
			E1 = in.calcLocalE(x,y);
			in.flipSpin(x,y);
			E2 = in.calcLocalE(x,y);
			
		
			dE = E2-E1;
			
			//dE = in.calcLocalE(x,y);
			//System.out.println("kT = "+in.getKT());
			//If T=0, then always accept the move
			if( (KT == 0.0 || KT < 0.000001) && dE <= 0.0) {
				in.update(x,y,dE);
				return;
			}
		
			if(dE <= 0.0){
				in.update(x,y,dE);
			}else{
				if(dE == 2.0*J){
					prob = boltzmannFactors[1];
					System.out.println("in 2J boltzmann factor");
				}else if(dE == 4.0*J){
					prob = boltzmannFactors[2];
					//System.out.println("in 4J boltzmann factor");
				}else if(dE == 6.0*J){
					prob = boltzmannFactors[3];
					System.out.println("in 6J boltzmann factor");
				}else if(dE == 8.0*J){
					prob = boltzmannFactors[4];
					//System.out.println("here");
				}else{
					System.out.println("Error in Calculating probabilities in Glauber update");
				}
					
				if(prob < Math.random()){ //if prob is less than random number, undo change, otherwise keep change and update energy
					in.flipSpin(x,y);	//Flip spins back i.e. undo change
					//System.out.println("Met won in Glauber");
				}else{
					in.update(x,y,dE);
				}
			}
			
			if(in.isInEquilibrium()) in.calcEquilibrium();
		}
		
	}
}//class brackets
