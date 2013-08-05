package isingModel.Model;

//import java.lang.Math;
public class Ising{
	
	// Height and width of the system, same in each dimension
	int N;
	//Value of interaction constant. Default is one
	double J = 1.0;
	
	/**Value of the total energy of the system given by
	 * E = -J [sum of n.n]Si.Sj
	 * Value of total magnetisation, the sum of each spin
	 */ 
	double E = 0, M =0;
	/**
	 * Running values of E for heat capacity and M for susecptibility
	 */
	double 	avE = 0.0, sumE = 0.0, sumE2 = 0.0, errorE = 0.0,
			avM =0.0, sumM = 0, sumM2 = 0.0, errorM = 0.0;

	/**
	 * Value of the susceptibility and heat capacity of the system
	 * Number of configurations that the system has been through
	 * Stored as a double for ease in calculations later
	 * Also, Independent counter for configurations for equilibrium calculation
	 */
	double chi = 0.0, cV = 0.0, confs = 0, equilibConfs = 0;
	
	boolean inEquilibrium = false;	//Is set to true when system reaches equilibrium
	
	double k = 1.0, T, inverseKT;	//Temperature and boltzmanns constant
	Spin[][] system;				//The 2-d array of spins for the system
		
	
		//CONSTRUCTOR
	/**
	 * Creates a N-size square lattices with random spins
	 * @param size Size of the Square lattice
	 * @param temp The temperature that the system will be simulated at
	 */
	public Ising(int size, double temp){
		N = size;
		setT(temp);
		system = new Spin[N][N];
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				system[i][j] = new Spin(i,j);
				M += system[i][j].getState();
							
			}// j loop
		}// i loop
		calcTotalE();
		double frac = M / (N*N);
		System.out.println("Initial energy of the system E = "+E);
		System.out.println("Initial magnetisation M = "+frac);
	}// constructor brackets
	
	//Constructor that will create a new version of the system at a different temperature
	public Ising(Ising in, double temp){
		N = in.getN();
		setT(temp);
		system = new Spin[N][N];
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				system[i][j] = new Spin(in.getSpinState(i,j));
			}
		}
		calcTotalE();
		System.out.println("Initial energy of the system E="+E);
	}

	//METHODS TO ACT ON SPINS IN SYSTEM
	
	//Flips the spin of the spin specified by x and y values must be from 0, N-1
	public void flipSpin(int i, int j){
		system[i][j].flipSpin();
	}
	
	//Return the state of the spin specified by x and y lattice parameters
	public int getSpinState(int i, int j){
		return system[i][j].getState();
	}
	
	//Set the spin of a specified spin to the specified value
	public void setSpinState(int i, int j, int temp){
		system[i][j].setState(temp);
	}
		
	
	//UPDATE AND CALCULATION METHODS
	
	/**
	 * Method that will calculate the local energy of wrt to a specified spin
	 * Accounts for all edge cases
	 */
	
	public double calcLocalE(int i, int j){
	
		boolean test = true;
		//Checks up, down left and right interactions to see if spins are the same or different
		//If the same -J is added to dE, if different +J is added to dE
		//isEquals returns 1 if spins are the same (adding -J to dE) and -1 if different (adding J to dE)
		//If statement left in to allow testing and to show the method used works and both are the same
		if(test){
		double dE = -J * ( 
					system[(i+N)%N][(j+N)%N].isEqual(system[(i-1+N)%N][(j+N)%N]) +	//up
					system[(i+N)%N][(j+N)%N].isEqual(system[(i+1+N)%N][(j+N)%N]) +	//down
					system[(i+N)%N][(j+N)%N].isEqual(system[(i+N)%N][(j-1+N)%N]) +	//left
					system[(i+N)%N][(j+N)%N].isEqual(system[(i+N)%N][(j+1+N)%N]) 	//right
		);
		return dE;
		}else{
			double dE =0;
			dE += -J *system[(i+N)%N][(j+N)%N].getState() * system[(i-1+N)%N][(j+N)%N].getState(); //up
			dE += -J *system[(i+N)%N][(j+N)%N].getState() * system[(i+1+N)%N][(j+N)%N].getState(); //down
			dE += -J * system[(i+N)%N][(j+N)%N].getState() * system[(i+N)%N][(j-1+N)%N].getState(); //left
			dE += -J * system[(i+N)%N][(j+N)%N].getState() * system[(i+N)%N][(j+1+N)%N].getState(); //right
			return dE;
		}
	}
	
	/**
	 * Method calculate the local energy changes of the system
	 * 	using Kawasaki dynamics i.e. two spins swapped.
	 *	Need to be careful if spins swapped are neighbours
	 */
	public double calcLocalE(int i1, int j1, int i2, int j2){
		double dE = calcLocalE(i1,j1) + calcLocalE(i2,j2);
		int a1 = (i1+N)%N;
		int b1 = (j1+N)%N;
		int a2 = (i2+N)%N;
		int b2 = (j2+N)%N;
		int jDiff = a1-a2;
		int iDiff = b1-b2;
		//First: if in same column, and within 1 row, Second: if in same row, and within one column
		if( (jDiff == 0 && (iDiff == -1 || iDiff == 1)) || (iDiff == 0 && (jDiff == -1 || jDiff == 1))  ){
			//Removes contributions from if spins are n.n's.
			//-2 to remove -J if spins equal and +J if different (isEquals returns 1/-1 if equal/different)
			dE += -J * -2.0 * system[a1][b1].isEqual(system[a2][b2]);
		}	
		return dE;
	}
	
	
	
	/**
	 * Updates the both the magnetisation and energy in one go.
	 * Also updates the number of configurations that the system has been through
	 */
	public void update(int i, int j, double dE){
		confs++;
		updateE(dE);
		updateM(i,j);
	}
	public void update(int i1, int j1, int i2, int j2, double dE){
		confs++;
		updateE(dE);
		updateM(i1,j1);
		updateM(i2,j2);
	}
	//Updates the total energy
	public void updateE(double dE){
		E += dE;
	}

	/**
	 * Method to update the total magnetisation of the system.
	 * Multiplied by 2 since flipping spins removes from one state and adds to another,
	 * a total swing of 2.
	 * This is only called when the spins change
	 * @param i Row of spin changed//Up and down interactions
	 * @param j Column of spin changed
	 */
	public void updateM(int i, int j){
		M += 2 * system[i][j].getState();
	}
	
	private boolean doFinalCalc = false;
	/**
	 * Method to set whether to do the equilibrium calculations or not
	 * @param in
	 */
	public void setDoFinalCalc(boolean in){
		doFinalCalc = in;
	}
	/**
	 * Method to do the required calculations once the system reaches equilibrium
	 */
	public void calcEquilibrium(){
		equilibConfs++;
		sumE += E;
		sumE2 += E*E;
		sumM2 += M*M;
		//if(M < 0){
		//	sumM += -M;
		//}else{
			sumM += M;
			//	}
		if(doFinalCalc)doFinalCalc();
	}
	/**
	 * Does the final calculations of cV and Chi averaging over the equilibrium states
	 */
	public void doFinalCalc(){
		avE = sumE /equilibConfs;
		avM = sumM /equilibConfs;
		double eVar1 = sumE2/equilibConfs;
		double eVar2 = avE * avE;
		double mVar1 = sumM2 /equilibConfs;
		double mVar2 = avM * avM;
		cV = inverseKT * (eVar1 - eVar2)/(double)(N*N);
		chi = inverseKT * (mVar1 - mVar2) / (double)(N*N);
		//System.out.println("Chi: "+chi+"\t"+"Cv: "+cV+"\tfrom "+equilibConfs+" confirmations");
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
				
				dE += -J *system[(i+N)%N][(j+N)%N].getState() * system[(i-1+N)%N][(j+N)%N].getState(); //up
				dE += -J * system[(i+N)%N][(j+N)%N].getState() * system[(i+N)%N][(j-1+N)%N].getState(); //left
				
				//Only need to calculate 'left' and 'up' neighbour pairs if looping over whole system
				//Hence, do not need to 'down' and 'right' interactions as these would give double counting
				//These are still included below for debugging or other purposes
				//dE += -J *system[(i+N)%N][(j+N)%N].getState() * system[(i+1+N)%N][(j+N)%N].getState(); //down
				//dE += -J * system[(i+N)%N][(j+N)%N].getState() * system[(i+N)%N][(j+1+N)%N].getState(); //right
				
			}
		}
		E = dE;
		
	}
	
	
	//BASIC GETTERS
	
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
	//Always returns the absolute value of magnetisation
	public double getM(){
		if(M < 0){
			return -M / (N*N);	
		}else{
			return M / (N*N);
		}
	}
		
	public double getAvE(){
		return avE;
	}
	public double getChi(){
		return chi;
	}
	public double getCv(){
		return cV;
	}
	public Spin[][] getState(){
		return system;
	}

	//BASIC SETTERS
		
	public void setT(double temp){
		T = temp;
		inverseKT = 1.0/(k*T);
	}
	public void setInEquilibrium(boolean in){
		inEquilibrium =in;
	}
	public boolean isInEquilibrium(){
		return inEquilibrium;
	}
	
	
	
	
}// class brackets
	
	
	
	
