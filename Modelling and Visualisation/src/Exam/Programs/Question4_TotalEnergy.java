package Exam.Programs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Exam.Model.Potts;
import Exam.Model.Dynamics.Dynamics;

public class Question4_TotalEnergy {
	public static void main(String args[]) throws IOException{
		
			int N = 50;
			int number = N*N;
			double T = 0.3;
			
			long seed = 123456;
			
			Potts test= new Potts(N,T, seed);
			Dynamics iterator = Dynamics.createDynamics(test);
			
			double frac0, frac1, frac2;
			
			/**
			 * Outputting to three different files for ease. EAsier in exam conditions
			 */
			
			BufferedWriter outEnergy = new BufferedWriter(new FileWriter(new File("Total_Energy.xvg")));
			
			int sweeps = 0;
			
			while(T < 2.6){
				while(sweeps < 10000){
					iterator.update(test);
					sweeps++;
					if(sweeps % 100 == 0) System.out.println("Sweep number"+sweeps);
					outEnergy.write(sweeps+"\t"+test.getE()+"\n");
					outEnergy.flush();
				}
				outEnergy.write("\n");
				outEnergy.flush();
				T += 0.1;
				test = new Potts(N,T, seed);
				sweeps=0;
			}
		
			outEnergy.close();
		
	}
}
