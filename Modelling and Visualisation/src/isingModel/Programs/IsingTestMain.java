package isingModel.Programs;

import isingModel.Model.Ising;
import isingModel.Model.Dynamics.Dynamics;

import java.io.*;


public class IsingTestMain {

public static void main(String args[])throws IOException{
	
	int N = 40;
	double initialT = 3.0;
	double T = initialT;
	double dT = 0.02;
	int numOfTemperatures = ((int)(T/dT)) + 1;	//Just in case rounding is incorrect
	int numOfRuns = 10;
	
	Ising ising = new Ising(N,T);
	int x = 0; //anything for glauber, 1 for Kawasaki
	Dynamics iterator = Dynamics.createDynamics(ising,x);
	
	double[][] chiArray = new double[numOfRuns][numOfTemperatures];
	double[][] cvArray = new double[numOfRuns][numOfTemperatures];
	
	String energyOutFile = N+"N_"+T+"K_energy.xvg";
	PrintWriter energyOutput = new PrintWriter(new FileWriter(energyOutFile));
	PrintWriter chi_Temperature = new PrintWriter(new FileWriter("Chi_Temperature_"+N+"x"+N+".xvg"));
	PrintWriter cV_Temperature = new PrintWriter(new FileWriter("cV_Temperature_"+N+"x"+N+".xvg"));
	//PrintWriter chi_Time = new PrintWriter(new FileWriter("Chi_Time.xvg"));
	
	double time = 0.0;
	boolean inEq = false;
	int j=0;
	//for(int i=0;i<numOfRuns;i++){
		while(T>0.5){
			if(time == 0.0) System.out.println("Running for T="+T);
			iterator.update(ising);
			time++;
			
			if(time%100 ==0)energyOutput.write(time+"\t"+ising.getM()+"\n");
			energyOutput.flush();
			
			if(inEq && time%100 == 0.0) { //If in equilibrium and every 20 time steps (to avoid correlation and oversampling)
				ising.calcEquilibrium();
				//ising.doFinalCalc();
		
			}
			if(time == 20000){		//Now in equilibrium so calculate values
				//ising.setInEquilibrium(true);
				inEq = true;
				System.out.println("Now calculating equilibrium values");
			}else if(time == 30000){
				//ising.calcEquilibrium();
				ising.doFinalCalc();
				System.out.println("T = "+T+"\tChi = "+ising.getChi()+"\tCv = "+ising.getCv());
				if( T > 0.00001 ){
					chi_Temperature.write(T+"\t"+ising.getChi()+"\n");
					cV_Temperature.write(T+"\t"+ising.getCv()+"\n");
					chi_Temperature.flush();
					cV_Temperature.flush();
					//chiArray[i][j] = ising.getChi();
					//cvArray[i][j] = ising.getCv();
				}
				energyOutput.write("\n\n");//create separate data sets for each T
				//chi_Time.write("\n\n");						
				time = 0.0;
				T = T - dT;
				ising = new Ising(N,T);
				j++;
				inEq = false;
			}
		}//While brackets
	/*	T = initialT;
		time = 0.0;
		j = 0;
		ising = new Ising(N,T);
		chi_Temperature.write("\n\n");
		cV_Temperature.write("\n\n");
	}*/
	/*	
	T = initialT;
	chi_Temperature.write("\n Averages \n");
	cV_Temperature.write("\n Averages \n");
	for(int i=0;i<numOfTemperatures;i++){
		double sumM = 0.0, sumE = 0.0, sum2M = 0.0, sum2E = 0.0;
		for(int k=0;k<numOfRuns;k++){
			sumM += chiArray[k][i];
			sum2M += chiArray[k][i]*chiArray[k][i];
			sumE += cvArray[k][i];
			sum2E += cvArray[k][i]*cvArray[k][i];
			
		}
		double avChi = sumM/(double)numOfRuns;
		double var1M = sum2M/(double)numOfRuns;
		double var2M = avChi*avChi;
		double errorM = Math.sqrt(var1M - var2M) / (double)numOfRuns;
		chi_Temperature.write(T+" "+avChi+" "+errorM+"\n");
		double avCv = sumE/(double)numOfRuns;
		double var1E = sum2E/(double)numOfRuns;
		double var2E = avCv*avCv;
		double errorE = Math.sqrt(var1E - var2E) / (double)numOfRuns;
		cV_Temperature.write(T+" "+avCv+" "+errorE+"\n");
		
		T = T - dT;
	}
	*/
	energyOutput.close();
	//chi_Time.close();
	chi_Temperature.close();
	cV_Temperature.close();
	
}//Main brackets
	
	
}
