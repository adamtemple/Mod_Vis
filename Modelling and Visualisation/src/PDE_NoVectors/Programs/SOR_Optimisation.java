package PDE_NoVectors.Programs;

import PDE_NoVectors.Iterator.*;
import PDE_NoVectors.Model.*;
import java.io.*;

public class SOR_Optimisation {

	public static void main(String args[]) throws IOException{
		
		int N = 50;
		Box space = new Box(N,N,N);
		
		double w = 0.0;
		
		Iterator test= new GaussSeidel(space, true, w);
		
		String filename = "SOR_Test_Central_Unit_Charge.xvg";
		
		space.setCentralUnitCharge();
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		
		
		System.out.println("System Size "+space.getTotalCellNumber());
		
		int count = 0;
		double dw = 0.1;
	
		
		while(w < 2.0){
			System.out.println("w = "+w+" of w up to "+2.0);
			while(!test.isConverged()){
				test.iterateScalarPotential();
				//if(count % 1 == 0)test.isConverged();
				count++;
			}
			out.write(w+"\t"+count+"\n");
			out.flush();
			count = 0;
			w += dw;
			space = new Box(N,N,N);
			space.setCentralUnitCharge();
			test = new GaussSeidel(space, true, w);
		}
		System.out.println("Done");
		out.close();
		
	}//Main
}//Class
