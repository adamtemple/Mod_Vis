package Exam.Model;

import java.util.Random;

//import java.lang.Math;
public class Potts{
	
	// Height and width of the system, same in each dimension
	int N;
	//Value of interaction constant. Default is one
	double J = 1.0;
	
	/**Value of the total energy of the system given by
	 * E = -J [sum of n.n]Si.Sj
	 * Value of total magnetisation, the sum of each spin
	 */ 
	double E = 0;
	/**
	 * Running values of E for heat capacity and M for susecptibility
	 */
	//double 	avE = 0.0, sumE = 0.0, sumE2 = 0.0, errorE = 0.0; REMOVED FOR EXAM
			
	/**
	 * Value of the heat capacity of the system
	 * Number of configurations that the system has been through
	 * Stored as a double for ease in calculations later
	 * Also, Independent counter for configurations for equilibrium calculation
	 */
	//double cV = 0.0, confs = 0, equilibConfs = 0;  REMOVED FOR EXAM
	
	//boolean inEquilibrium = false;	//Is set to true when system reaches equilibrium  REMOVED FOR EXAM
	double confs =0;
	double k = 1.0, T;	//Temperature and boltzmanns constant
	Spin[][] system;				//The 2-d array of spins for the system
	
	double probability = 0.03;
		
	double num0, num1, num2;
		//CONSTRUCTOR
	/**
	 * Creates a N-size square lattices with random spins
	 * @param size Size of the Square lattice
	 * @param temp The temperature that the system will be simulated at
	 */
	public Potts(int size, double temp){
		N = size;
		setT(temp);
		system = new Spin[N][N];
		double temp0 =0, temp1 =0 , temp2 = 0;
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				system[i][j] = new Spin();
				if(system[i][j].getState() == 0)num0++;
				if(system[i][j].getState() == 1)num1++;
				if(system[i][j].getState() == 2)num2++;
			}// j loop
		}// i loop
		calcTotalE();
		System.out.println("Initial energy of the system E = "+E);
	}// constructor brackets
	
	/**
	 * Seeded constructor for same initial condition each time
	 * @param size
	 * @param temp
	 * @param seed
	 */
	
	public Potts(int size, double temp, long seed){
		this(size,temp);
		Random rand = new Random(seed);
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
			system[i][j].setState(rand.nextInt(3));  //3 as nextInt() generates int exclusively. i.e. 3 will gen 0 or 1 or 2 
			}											//uniformly
								
		}
	}

	//METHODS TO ACT ON SPINS IN SYSTEM
	
	//Return the state of the spin specified by x and y lattice parameters
	public int getSpinState(int i, int j){
		return system[i][j].getState();
	}
	
	//Set the spin of a specified spin to the specified value
	public void setSpinState(int i, int j, int temp){
		if(system[i][j].getState() == temp){
			//do nothing
		}else{	//handle all other possible changes
			if(system[i][j].getState() == 0 && temp == 1){
				num0--;
				num1++;
			}else if(system[i][j].getState() == 0 && temp == 2){
				num0--;
				num2++;
			}else if(system[i][j].getState() == 1 && temp == 0){
				num1--;
				num0++;
			}else if(system[i][j].getState() == 1 && temp == 2){
				num1--;
				num2++;
			}else if(system[i][j].getState() == 2 && temp == 1){
				num2--;
				num1++;
			}else if(system[i][j].getState() == 2 && temp == 0){
				num2--;
				num0++;
			}
		}
		system[i][j].setState(temp);
		
	}
	
	public void changeSpin(int i, int j){
		int state = system[i][j].getState();
		
		Spin temp = new Spin();
		
		//ensures the same state is not given to each spin
		//while(temp.getState() != system[i][j].getState()){
			setSpinState(i,j,temp.getState());	//see above
			
		//}
		
	}
	
	
	//UPDATE AND CALCULATION METHODS
	
	/**
	 * Method that will calculate the local energy of wrt to a specified spin
	 * Accounts for all edge cases
	 */
	
	public double calcLocalE(int i, int j){
	
		//Checks up, down left and right interactions to see if spins are the same or different
		//If the same -J is added to dE, if different +J is added to dE
		//isEquals returns 1 if spins are the same (adding -J to dE) and -1 if different (adding J to dE)
		//If statement left in to allow testing and to show the method used works and both are the same
		
			
			int temp = 0;
			if(system[doPBC(i)][doPBC(j)].isEqual(system[doPBC(i-1)][doPBC(j)]) ) temp++; 	//up
			if(system[doPBC(i)][doPBC(j)].isEqual(system[doPBC(i+1)][doPBC(j)]) ) temp++; 	//down
			if(system[doPBC(i)][doPBC(j)].isEqual(system[doPBC(i)][doPBC(j-1)]) ) temp++; 	//left
			if(system[doPBC(i)][doPBC(j)].isEqual(system[doPBC(i)][doPBC(j+1)]) ) temp++; 	//right
			
			double dE = -J * temp;
			return dE;
	}
	
	/**
	 * Updates the both the magnetisation and energy in one go.
	 * Also updates the number of configurations that the system has been through
	 */
	public void update(int i, int j, double dE){
		confs++;
		updateE(dE);
	}
	public void update(int i1, int j1, int i2, int j2, double dE){
		confs++;
		updateE(dE);

	}
	//Updates the total energy
	public void updateE(double dE){
		E += dE;
	}

	
	/**
	 * Method to initialise the total energy value once the system has been created.
	 * Only the 'up' and 'left' interactions are considered to avoid double counting, the
	 * lattice is summed over
	 */
	private void calcTotalE(){
		double dE = 0;
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				
				//Avoids double counting
				if( system[doPBC(i)][doPBC(j)].isEqual( system[doPBC(i-1)][doPBC(j)] )) dE += -J;	//up
				if( system[doPBC(i)][doPBC(j)].isEqual( system[doPBC(i)][doPBC(j-1)] )) dE += -J;	//left
		
			}
		}
		E = dE;
		
	}
	
	/**
	 * Method to do the periodic boundary conditions
	 * @param in value to do PBC on
	 * @return
	 */
	private int doPBC(int in){
		return ((in+N)%N);
	}
	
	
	//BASIC GETTERS
	
	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getNum0() {
		return num0;
	}

	public void setNum0(double num0) {
		this.num0 = num0;
	}

	public double getNum1() {
		return num1;
	}

	public void setNum1(double num1) {
		this.num1 = num1;
	}

	public double getNum2() {
		return num2;
	}

	public void setNum2(double num2) {
		this.num2 = num2;
	}

	/**
	 * Method to get the size of the system
	 * @return Size of system
	 */
	public int getN(){
		return N;
	}
	/**
	 * Method to return the value of J, the interaction strength. By default this is one
	 *	
	 * @return J, the interaction strength (Default 1)
	 */
	public double getJ(){
		return J;
			}
	public double getT(){
		return T;
	}
	public double getKT(){
		return k*T;
	}
	public double getE(){
		return E;
	}

	public Spin[][] getState(){
		return system;
	}

	//BASIC SETTERS
		
	public void setT(double temp){
		T = temp;
	}

	

	/*  REMOVED FOR EXAM
	private boolean doFinalCalc = false;
	/**
	 * Method to set whether to do the equilibrium calculations or not
	 * @param in
	 
	public void setDoFinalCalc(boolean in){
		doFinalCalc = in;
	}
	*/
	/**
	 * Method to do the required calculations once the system reaches equilibrium
	 */
	/*  REMOVED FOR EXAM
	public void calcEquilibrium(){
		equilibConfs++;
		sumE += E;
		sumE2 += E*E;
		if(doFinalCalc)doFinalCalc();
	}
	*/
	/**
	 * Does the final calculations of cV and Chi averaging over the equilibrium states
	 */
	/*  REMOVED FOR EXAM
	public void doFinalCalc(){
		avE = sumE /equilibConfs;
		double eVar1 = sumE2/equilibConfs;
		double eVar2 = avE * avE;

		cV =  (eVar1 - eVar2)/(double)(N*N);
		
		//System.out.println("Chi: "+chi+"\t"+"Cv: "+cV+"\tfrom "+equilibConfs+" confirmations");
	}
	*/
	
	
}// class brackets
	
	
	
	
