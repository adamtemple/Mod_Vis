package Exam.Programs;

import Exam.Model.Potts;
import Exam.Model.Dynamics.Dynamics;
import java.io.*;

public class Question3 {

	public static void main(String args[]) throws IOException{
		
		
		int N = 50;
		int number = N*N;
		double T = 0.5;
		
		long seed = 123456;
		
		Potts test= new Potts(N,T, seed);
		Dynamics iterator = Dynamics.createDynamics(test);
		
		double frac0, frac1, frac2;
		
		/**
		 * Outputting to three different files for ease. EAsier in exam conditions
		 */
		
		BufferedWriter out0 = new BufferedWriter(new FileWriter(new File("Question3_fraction_in_0.xvg")));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(new File("Question3_fraction_in_1.xvg")));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(new File("Question3_fraction_in_2.xvg")));
		
		int sweeps = 0;
		while(sweeps < 10000){
			iterator.update(test);
			sweeps++;
			if(sweeps % 100 == 0) System.out.println("Sweep number"+sweeps);
			frac0 = test.getNum0() / number;
			frac1 = test.getNum1() / number;
			frac2 = test.getNum2() / number;
			//System.out.println(frac0+"\t"+frac1+"\t"+frac2);	to check sum to 1!
			out0.write(sweeps+"\t"+frac0+"\n");
			out0.flush();
			out1.write(sweeps+"\t"+frac1+"\n");
			out1.flush();
			out2.write(sweeps+"\t"+frac2+"\n");
			out2.flush();
		}
		out0.close();
		out1.close();
		out2.close();
	}
	
}
