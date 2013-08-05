package Exam.Programs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Exam.Model.Potts;
import Exam.Model.Dynamics.Dynamics;
import Exam.Analysis.*;

public class Question4_Variance {
	public static void main(String args[]) throws IOException{
		
		int N = 50;
		double T = 0.3;
		
		long seed = 123456;
		
		Potts test= new Potts(N,T, seed);
		Dynamics iterator = Dynamics.createDynamics(test);

		
		BufferedWriter outEnergy = new BufferedWriter(new FileWriter(new File("Variance_v_Temperature.xvg")));
		
		int sweeps = 0, numOfSweeps = 7000, equiSteps = 2500;
	
		while(T < 2.6){
			double [] energy = new double [(numOfSweeps - equiSteps)];
			
			double sum = 0, sum2 =0; //For Variance
			int i=0;
			while(sweeps < numOfSweeps){
				iterator.update(test);
				if(sweeps > equiSteps) {
					energy[i] = test.getE();
					sum += test.getE();
					sum2 += test.getE() * test.getE();
					i++;
				}
				sweeps++;
				if(sweeps % 100 == 0) System.out.println("Sweep number"+sweeps);
				//outEnergy.write(sweeps+"\t"+test.getE()+"\n");
				//outEnergy.flush();
			}
			double average = sum / (numOfSweeps - equiSteps);	//As not including those not in equilibrium
			
			double var1 = sum2 / (numOfSweeps - equiSteps);
			double var2 = average*average;
			double variance = var1 - var2;
			
			variance = variance / (N*N); //normalisation
			double error = ErrorCalculations.jackknife(energy);		//see Analysis.ErrorCalculations
			
			outEnergy.write(T+"\t"+variance+"\t"+error+"\n");
			outEnergy.flush();
			T += 0.1;
			test = new Potts(N,T, seed);
			sweeps=0;
		}
	
		outEnergy.close();
	
}
	
	
	
}
