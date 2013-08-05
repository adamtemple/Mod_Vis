package SIRS.Tests;

import java.io.*;

import SIRS.Analysis.Analysis;
import SIRS.DataOutput.Data_FileOutput;
import SIRS.Model.SIRS_System;
import SIRS.Model.Updater.Updater;

public class ImmuneTest {

	public static void main(String args[]) throws IOException{
			
			int N = 250;
			double immuneFraction = 0.0;
			double dI = 0.01;
			
			SIRS_System agents = new SIRS_System(N);
			Updater iterator = new Updater();
			Analysis analysis = new Analysis(agents);
			
			BufferedWriter out = new BufferedWriter(new FileWriter("Immune_Fraction_Analysis_"+N+"x"+N+".xvg"));
			
			double [] probs = {0.938,0.844,0.043};
			agents.setProb(probs);
			agents.setImmuneFraction(immuneFraction);
			
			while(immuneFraction <= 1.0){
				System.out.println("Iterating System");
			
				 	analysis = new Analysis(agents);
					for(int i=0; i< 7000; i++){
						iterator.update(agents);
						analysis.doAnalysisStep(agents);
						//System.out.println(analysis.calculateAverageInfected());
					}
					
				double avInfected = analysis.calculateNormalisedAverageInfected();
							
				out.write(immuneFraction+"\t"+avInfected+"\n");
				out.flush();
				System.out.println("Immune: "+immuneFraction+"\t avI: "+avInfected);
				immuneFraction += dI;
			
				agents = new SIRS_System(N);
				agents.setProb(probs);
				agents.setImmuneFraction(immuneFraction);
				analysis.restartAnalysis();
			}
			out.close();
			
		}//Main brackets
	}

	

