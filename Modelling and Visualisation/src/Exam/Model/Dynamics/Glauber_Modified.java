package Exam.Model.Dynamics;
import 	Exam.Model.*;

import java.util.Random;
import java.lang.Math;

public class Glauber_Modified extends Dynamics{
		Random rand;
		long seed;
		int x, y, length;
		double KT, J, dE;
		Potts potts;
		double[] boltzmannFactors = new double [5]; //i.e 0 to 8J
		double prob;
		double E1, E2;
		
		//CONSTRUCTOR
		
		public Glauber_Modified(Potts in){
			rand = new Random();
			potts = in;
			calcBoltzmannFactors(potts);
			System.out.println("Running with Glauber_Modified Dynamics");
		}
		//Allows for error checking with seed
		public Glauber_Modified(Potts in, long seed){
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
		//Need to have 1J, 2J 3J 4J to handle all cases where energy is increased,
		//Those which decrease energy are always accepted
		private void calcBoltzmannFactors(Potts in){
			KT = in.getKT();
			J = in.getJ();
			for(int i=0; i<boltzmannFactors.length; i++){		//goes:  0, 1, 2, 3, 4 
				boltzmannFactors[i] = Math.exp( (-1.0 * i * J) / KT );
			}
		}
		
		/**
		 * update method which updates the flips of the ising model using metropol
		 */
		//nextInt(n) picks from zero (inclusive) to N exclusive
		public void update(Potts in){
			length = in.getN();
			if(in.getKT() != KT || in.getJ() != J) calcBoltzmannFactors(in);	//If the system params have changed, recalculated stored values
			for(int i=0;i<(length*length);i++){
			
				//NEW PART: modifying another cell
				
				int xNew = rand.nextInt(length);
				int yNew = rand.nextInt(length);
				
				double E0New = in.calcLocalE(xNew, yNew);		//Needed to correctly change the energy
				modifyCell(in, xNew, yNew );	//See below
				double E1New = in.calcLocalE(xNew, yNew);
				
				double dENew = E0New - E1New;
				//pick random spin
				x = rand.nextInt(length);
				y = rand.nextInt(length);
				
				E1 = in.calcLocalE(x,y);
				
				int state = in.getSpinState(x, y);
				in.changeSpin(x,y);
	
				E2 = in.calcLocalE(x,y);
				
			
				dE = E2-E1;
				
				//System.out.println("kT = "+in.getKT());
				//If T=0, then always accept the move
				if( (KT == 0.0 || KT < 0.000001) && dE <= 0.0) {
					in.update(x,y,dE);
					return;
				}
			
				if(dE <= 0.0){	//if reduces energy then always accept move
					in.update(x,y,dE);
				}else{
					if(dE == 1.0*J){
						prob = boltzmannFactors[1];
						//System.out.println("in J boltzmann factor");
					}else if(dE == 2.0*J){
						prob = boltzmannFactors[2];
						//System.out.println("in 2J boltzmann factor");
					}else if(dE == 3.0*J){
						prob = boltzmannFactors[3];
						//System.out.println("in 3J boltzmann factor");
					}else if(dE == 4.0*J){
						prob = boltzmannFactors[4];
						//System.out.println("here");
					}else{
						System.out.println("Error in Calculating probabilities in Glauber update "+dE);
					}
						
					if(prob < Math.random()){ //if prob is less than random number, undo change, otherwise keep change and update energy
						in.setSpinState(x, y, state);	//Flip spins back i.e. undo change
						//System.out.println("Met won in Glauber");
					}else{
						in.update(x,y,dE+dENew);	//Need to modify the update of the eneryg to include the any modified states
					}
				}
			
			}
		}//update
	
		/**
		 * Method to modify a randomly chosen cell
		 * @param in
		 * @param x
		 * @param y
		 */
		public void modifyCell(Potts in, int x, int y){
			double prob = in.getProbability();
			if(prob > Math.random()){
				if(in.getSpinState(x, y) == 0) in.setSpinState(x, y, 1);
				if(in.getSpinState(x, y) == 1) in.setSpinState(x, y, 2);
				if(in.getSpinState(x, y) == 2) in.setSpinState(x, y, 0);
			}
			
			
		}
	
}//class
