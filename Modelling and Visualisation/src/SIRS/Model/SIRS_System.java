package SIRS.Model;

import java.util.Random;



public class SIRS_System {

	
	
	int N; //System Size
	long seed; //Seed used for initialisation of system state
	
	//The 2-D array of agents which makes up the system
	Agent[][] agents;
	
	double [] prob = new double [Agent.getNumStates()]; //Array for probabilities 1 S-I, 2 I-R, 3 R-S
	
	int S = 0, I =0, R =0, Immune = 0;
	double immuneFraction = 0.0;
	//CONSTRUCTORS
	
	//Creates a random array of agents of a given size
	public SIRS_System(int in){
		N = in;
		agents = new Agent[N][N];
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				agents[i][j] = new Agent();
				if(agents[i][j].isSusceptible()) S++;
				if(agents[i][j].isInfected()) I++;
				if(agents[i][j].isRecovered()) R++;
			}
		}
		for(int i=0;i<prob.length;i++){
			prob[i] = 0.5;		//Defaults probabilities to 0.5
		}
		System.out.println("System with (p1,p2,p3) = ("+prob[0]+","+prob[1]+","+prob[2]+")");
	}
	
	//Creates a system with a set of given probabilitites
	public SIRS_System(int in, double [] probIn){
		this(in);
		if(probIn.length != prob.length){
			System.out.println("Error in creating system- probability array not correct lentgth");
			return;
		}
		for(int i=0;i<prob.length;i++){
			prob[i] = probIn[i];
		}
		System.out.println("System with (p1,p2,p3) = ("+prob[0]+","+prob[1]+","+prob[2]+")");
	}
	
	//Creates a system based upon a given seed
	public SIRS_System(int in, long seed){
		this.seed = seed;
		N = in;
		Random rand = new Random(seed);
		agents = new Agent[N][N];
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				agents[i][j] = new Agent(rand.nextDouble());
				if(agents[i][j].isSusceptible()) S++;
				if(agents[i][j].isInfected()) I++;
				if(agents[i][j].isRecovered()) R++;
			}
		}
		for(int i=0;i<prob.length;i++){
			prob[i] = 0.5;		//Defaults probabilities to 0.5
		}
		System.out.println("System with (p1,p2,p3) = ("+prob[0]+","+prob[1]+","+prob[2]+")");
	}
	
	public SIRS_System(int in, long seed, double [] probIn){
		this(in,seed);
		if(probIn.length != prob.length){
			System.out.println("Error in creating system- probability array not correct lentgth");
			return;
		}
		for(int i=0;i<prob.length;i++){
			prob[i] = probIn[i];
		}
		System.out.println("System with (p1,p2,p3) = ("+prob[0]+","+prob[1]+","+prob[2]+")");
	}
	
	//SETTERS
	/**
	 * Sets the probabilities of the system.
	 * Needs to reference 0,1,2 for probs 1,2,3
	 */
	public void setProb(int n, double input){
		if(input > 1){
			System.out.println("Probability is larger than one");
			return;
		}
		if(n > prob.length){
			System.out.println("Array out of bounds; must reference probs 0,1,2");
			return;
		}
		prob[n] = input;
		System.out.println("Changing p"+(n+1)+" to "+input);
	}
	
	public void setProb(double[] in){
		if(prob.length != in.length){
			System.out.println("Array length not correct");
			return;
		}
		for(int i=0;i<prob.length;i++){
			prob[i] = in[i];
			System.out.println("Setting P"+(i+1)+" to "+in[i]);
		}
	}
	/**
	 * Method to set the number of immune agents
	 * @param fraction
	 */
	
	public void setImmuneFraction(double fraction){
		if(fraction < 0.0 || fraction > 1.0){
			System.out.println("The immunity fraction entered is outside the permitted range");
			return;
		}
		int numAgents = N*N;
		immuneFraction = fraction;
		int numImmune = (int)( fraction * (double)numAgents ); //Need to cast to a double to get fraction then back again to int
		//Immune = numImmune;
		//Chooses agents at random to set to immune
		Random rand = new Random();
		for(int i=0;i<numImmune;i++){
			int x = rand.nextInt(N);
			int y = rand.nextInt(N);
			if(!agents[x][y].isImmune()){
				//If the agent isn't immune, if it is in state x, reduce the number of agents in state x
				//and change agent to immune
				if(agents[x][y].isInfected()) I--;
				if(agents[x][y].isRecovered()) R--;
				if(agents[x][y].isSusceptible()) S--;
				agents[x][y].setImmune();
				Immune++;
			}else{
				i--; //If chosen an already immune agent, try again
			}
		}
	}
	
	
	//GETTERS
	public Agent getAgent(int x, int y){return agents[doPBC(x)][doPBC(y)];}
	public int getSystemSize(){return N;}
	public double getProb(int i){return prob[i];}
	public int getSusceptible(){return S;}
	public int getInfected(){return I;}
	public int getRecovered(){return R;}
	public int getImmune(){return Immune;}
	public double getImmuneFraction(){return immuneFraction;}
	
	//METHODS
	
	//Methods which change the values of the specified agents
	public void setSusceptible(int x, int y){
		agents[x][y].setSusceptible();
		changedToSusceptible();
	}
	public void setInfected(int x, int y){
		agents[x][y].setInfected();
		changedToInfected();
	}

	public void setRecovered(int x, int y){
		agents[x][y].setRecovered();
		changedToRecovered();
	}
	
	//Methods to update counters in system
	private void changedToSusceptible(){
		R--;
		S++;
	}
	private void changedToInfected(){
		S--;
		I++;
	}
	private void changedToRecovered(){
		I--;
		R++;
	}
	/**
	 * Returns true if any of the 8 nearest neighbours to the specified agent are infected
	 */
	public boolean checkInfected(int x, int y){
		if(agents[doPBC(x+1)][doPBC(y+1)].isInfected()) return true;
		if(agents[doPBC(x+1)][doPBC(y)].isInfected()) 	return true;
		if(agents[doPBC(x+1)][doPBC(y-1)].isInfected()) return true;
		if(agents[doPBC(x)][doPBC(y+1)].isInfected()) 	return true;
		if(agents[doPBC(x)][doPBC(y-1)].isInfected()) 	return true;
		if(agents[doPBC(x-1)][doPBC(y+1)].isInfected()) return true;
		if(agents[doPBC(x-1)][doPBC(y)].isInfected()) 	return true;
		if(agents[doPBC(x-1)][doPBC(y-1)].isInfected()) return true;
		return false;
	}
	
	/**
	 * Method to do the periodic boundary conditions
	 * @param in value to do PBC on
	 * @return
	 */
	private int doPBC(int in){
		return ((in+N)%N);
	}
	
	
}
