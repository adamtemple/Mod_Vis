package Exam.Programs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Exam.Model.Potts;
import Exam.Model.Dynamics.Dynamics;

public class Question4_AverageEnergy {
	public static void main(String args[]) throws IOException{
		
			int N = 50;
			double T = 0.3;
			
			long seed = 123456;
			
			Potts test= new Potts(N,T, seed);
			Dynamics iterator = Dynamics.createDynamics(test);
			
			
			/**
			 * Outputting to three different files for ease. EAsier in exam conditions
			 */
			
			BufferedWriter outEnergy = new BufferedWriter(new FileWriter(new File("Average_Energy_v_Temperature.xvg")));
			
			int sweeps = 0, numOfSweeps = 7000;
		
			while(T < 2.6){
				//double [] energy = new double [numOfSweeps];
				double sum =0;
				while(sweeps < numOfSweeps){
					iterator.update(test);
					if(sweeps > 2500) sum += test.getE();
					sweeps++;
					if(sweeps % 100 == 0) System.out.println("Sweep number"+sweeps);
					//outEnergy.write(sweeps+"\t"+test.getE()+"\n");
					//outEnergy.flush();
				}
				double average = sum / (numOfSweeps - 2500);	//As not including those not in equilibrium
				average = average / (N*N);
				outEnergy.write(T+"\t"+average+"\n");
				outEnergy.flush();
				T += 0.1;
				test = new Potts(N,T, seed);
				sweeps=0;
			}
		
			outEnergy.close();
		
	}
}

