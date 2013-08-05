package isingModel.Model.Dynamics;
import isingModel.Model.Ising;

import java.util.Random;
import java.lang.Math;

public class Kawasaki extends Dynamics{
	
	Random rand;
	long seed;
	int x1,x2,y1,y2, temp1, temp2;
	int length;
	double KT, J, dE;
	double prob, E1, E2;
	Ising ising;
	double[] boltzmannFactors = new double [9];
	
	public Kawasaki(Ising in){
		rand = new Random();
		ising = in;
		calcBoltzmannFactors(ising);
		System.out.println("Running with Kawasaki Dynamics");
	}
	//Allows for error checking
	public Kawasaki(Ising in, long seed){
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
	 * @param in system in which values have been altered
	 */
	private void calcBoltzmannFactors(Ising in){
		KT = in.getKT();
		J = in.getJ();
		
		for(int i=0; i<boltzmannFactors.length; i++){
			//-2.0 so j will go from -8 to 8 i.e.  0 2 4 6 8 10 12 14 16
			boltzmannFactors[i] = Math.exp( (-2.0 * i * J) / KT );
					
		}
	}
	
	public void update(Ising in){
		//System.out.println("in kawasaki update method");
		length = in.getN();
		if(in.getKT() != KT || in.getJ() != J) calcBoltzmannFactors(in);//If the system params have changed, recalculated stored values
		for(int i=0;i<(length*length);i++){
		
			x1 = rand.nextInt(length);
			y1 = rand.nextInt(length);
			x2 = rand.nextInt(length);
			y2 = rand.nextInt(length);
			
			
			
			temp1 = in.getSpinState(x1,y1);
			temp2 = in.getSpinState(x2,y2);
			
		
			E1 = in.calcLocalE(x1,y1,x2,y2);
						
			in.setSpinState(x1, y1, temp2);//swap spins
			in.setSpinState(x2, y2, temp1);
					
			E2 = in.calcLocalE(x1,y1,x2,y2);
			
			dE = E2 -E1;
			
			if( (KT == 0.0 || KT < 0.000001) && dE <= 0.0) {
				in.update(x1,y1,x2,y2,dE);
				return;
			}
		
			if(dE <= 0){
				in.update(x1, y1,x2,y2,dE);
			}else{
				if(dE == 2.0*J){
					prob = boltzmannFactors[1];
				}else if(dE == 4.0*J){
					prob = boltzmannFactors[2];
				}else if(dE == 6.0*J){
					prob = boltzmannFactors[3];
				}else if(dE == 8.0*J){
					prob = boltzmannFactors[4];
				}else if(dE == 10.0*J){
					prob = boltzmannFactors[5];
				}else if(dE == 12.0*J){
					prob = boltzmannFactors[6];
				}else if(dE == 14.0*J){
					prob = boltzmannFactors[7];
				}else if(dE == 16.0*J){
					prob = boltzmannFactors[8];
				}else{
					System.out.println("Error in Calculating probabilities in Kawasaki update");
					System.out.println("dE = "+dE);
				}
				if(prob < Math.random()){
					in.setSpinState(x1,y1,temp1);
					in.setSpinState(x2,y2,temp2);
				}else{
					in.update(x1, y1,x2,y2,dE);
				}
			}
			if(in.isInEquilibrium()) in.calcEquilibrium();
		}
	}


}//Class brackets
