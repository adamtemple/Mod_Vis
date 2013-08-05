package isingModel.Programs;

import isingModel.Model.Ising;
import isingModel.Model.Dynamics.Dynamics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class IsingTest2 {

	public static void main(String args[])throws IOException{
		
		int N = 40;
		double initialT = 5;
		double T = initialT;
		double dT = 0.1;
		
		Ising ising = new Ising(N,T);
		int x = 0; //anything for glauber, 1 for Kawasaki
		Dynamics iterator = Dynamics.createDynamics(ising,x);

		int valueToAverage = 1000;
		Double [] energy = new Double[valueToAverage];
		Double [] mag = new Double[valueToAverage];
		
			
		//PrintWriter energyOutput = new PrintWriter(new FileWriter(energyOutFile));
		PrintWriter M_Temperature = new PrintWriter(new FileWriter("m_v_T_"+N+"x"+N+".xvg"));
		PrintWriter E_Temperature = new PrintWriter(new FileWriter("E_v_T"+N+"x"+N+".xvg"));
		//PrintWriter chi_Time = new PrintWriter(new FileWriter("Chi_Time.xvg"));
		
		double time = 0.0;
		boolean inEq = false;
		int j=0;
		

		
		
	
			while(T >= 0.1){
				if(time == 0.0) System.out.println("Running for T="+T);
				iterator.update(ising);
				time++;
				
						
				if(inEq && time%10 == 0.0) { //If in equilibrium and every 10 time steps (to avoid correlation and oversampling)
					energy[j] = ising.getE();
					mag[j] = ising.getM();
					j++;
					//ising.calcEquilibrium();
					//ising.doFinalCalc();
			
				}
				if(time == 10000){		//Now in equilibrium so calculate values
					//ising.setInEquilibrium(true);
					inEq = true;
					System.out.println("Now calculating equilibrium values");
				}else if(time == 20000){
					double sumE =0.0, sumM = 0.0;
					for(int i=0;i<energy.length;i++){
						sumE += energy[i];
						sumM += mag[i];
					}
					sumE = sumE/(double)energy.length;
					sumM = sumM/(double)mag.length;
					
					System.out.println("E: "+sumE+"\tM: "+sumM);
					
					if( T > 0.00001 ){
						E_Temperature.write(T+"\t"+(sumE/N*N)+"\n");
						M_Temperature.write(T+"\t"+(sumM/N*N)+"\n");
						E_Temperature.flush();
						M_Temperature.flush();
				
					}
					//energyOutput.write("\n\n");//create separate data sets for each T
					//chi_Time.write("\n\n");						
					time = 0.0;
					T = T - dT;
					ising = new Ising(N,T);
					energy = new Double[valueToAverage];
					mag = new Double[valueToAverage];
					j=0;
					inEq = false;
				}
			}//While brackets

		
		
		E_Temperature.close();
		M_Temperature.close();
		
	}//Main brackets
		
	
}
