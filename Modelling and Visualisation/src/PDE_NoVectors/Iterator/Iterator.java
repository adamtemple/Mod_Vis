package PDE_NoVectors.Iterator;

import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Model.Vector;



public abstract class Iterator {

	public Box space;
	public double accuracy = 0.0001;
	
	public int numberConverged = 0;
	public double convergenceFraction = 0.0;
	
	
	public Iterator(Box space){
		this.space = space;
	}
	//Used to update the local copy of space if parameters are changed
	public void updateSpace(Box in){
		space = in;
		numberConverged = 0;
		convergenceFraction = 0;
	}

	//Called within updating loops to check for the convergence of check cell
	public void checkCellConvergence(double diff){
		diff = Math.sqrt(diff * diff);
		if(diff != 0.0 && diff <= accuracy)numberConverged++;
		//else if( diff == 0.0 && convergenceFraction >0.4) numberConverged++;	
		
		//The else if handles the case where the difference is indeed zero after many iterations
		//Initially, many of the differences will be zero, but they are not converged, only not iterated
		//This is handled by the first if statement. This second one was added as there were seen to 
		//to be problems when considering a central unit charge and usign the Jacobi algorithm
	}
	public void doConvergenceCalculation(){
		convergenceFraction = (double)numberConverged / (double)space.getTotalCellNumber();
		//System.out.println("Convergence fraction "+convergenceFraction);
		numberConverged = 0;
	}
	
	public boolean isConverged(){
		if(convergenceFraction == 1.0) return true;
		else return false;
	}


	public abstract boolean isDoOverRelaxation();
	public abstract void setDoOverRelaxation(boolean doOverRelaxation);
	
	//Method to update the the system until accuracy required is reached
	public abstract void iterateScalarPotential();
	
	public abstract void iterateVectorPotential();
	
	public abstract boolean isJacobi();
	public abstract boolean isGauss();

	public double getConvergenceFraction() {
		return convergenceFraction;
	}
	public void setConvergenceFraction(double convergenceFraction) {
		this.convergenceFraction = convergenceFraction;
	}
	
	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}



	
	

	
}
