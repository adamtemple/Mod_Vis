package SIRS.Analysis;

import SIRS.Model.SIRS_System;

public class Analysis {

	double sumI = 0, sumI2 = 0;		//Summing variables for Average Infection
	int n = 0, N;					//n is number of configurations sampled, N is system size
	double normalisingFactor;		//Factor to normalise the average Infected
	
	//CONSTRUCTOR
	public Analysis(SIRS_System in){
		N = in.getSystemSize();
		normalisingFactor = (double)N*N;
	}
	
	//Method which will calculate required analytical quantities, in this case average I
	public void doAnalysisStep(SIRS_System in){
		if(N != in.getSystemSize())return;
		sumI += in.getInfected();
		sumI2 += in.getInfected()*in.getInfected();
		n++;
	}
	
	//Method to do the average infected calculation
	public double calculateAverageInfected(){
		double average = sumI / (double)n;
		double error = sumI2 /(double)n - average*average;
		//double [] solution = {average,error};
		//return solution;
		return average;
	}
	
	//Additional method to give the normalised infected number if required
	public double calculateNormalisedAverageInfected(){
		double average = sumI / (double)n;
		double error = sumI2 /(double)n - average*average;
		
		//double [] solution = {average /(double)N,error / (double)N};
		//return solution;
		return average/normalisingFactor;
	}
	
	//Method which allows reuse of the Analysis object if want to restart analysis on same system.
	public void restartAnalysis(){
		sumI = 0;
		sumI2 = 0;
		n = 0;
	}
}
