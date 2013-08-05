package SIRS.Tests;

import java.io.IOException;

import SIRS.Analysis.Analysis;
import SIRS.DataOutput.Data_FileOutput;
import SIRS.Model.SIRS_System;
import SIRS.Model.Updater.Updater;

public class Prob1_InfectedPlot {

	public static void main(String args[]) throws IOException{
		
		int N = 50;
		SIRS_System agents = new SIRS_System(N);
		Updater iterator = new Updater();
		Analysis analysis = new Analysis(agents);
		Data_FileOutput out = new Data_FileOutput();
		
		out.createProbabilityOutput(agents, 1); //which prob we are plotting
		
		double prob1 = 0.0, prob3 = 0.0;	//initialising probabilities
		double dP1 = 0.1;	//Precision
		double dP3 = 0.01;	//Number of data sets
		agents.setProb(0, prob1);
		agents.setProb(2, prob3);
					
		while(prob3 <= 1){
			while(prob1 <= 1){
					for(int k=0; k < 5000; k++){
						iterator.update(agents);
						analysis.doAnalysisStep(agents);
					}
					//System.out.println(agents.getProb(0));
					double avInfected = analysis.calculateNormalisedAverageInfected();
					out.doProbabilityOutput(avInfected,agents.getProb(0));
					prob1 += dP1;
					agents = new SIRS_System(N);
					agents.setProb(0,prob1);
					agents.setProb(2,prob3);
					analysis.restartAnalysis();
			}
			prob1 = 0.0;
			prob3 += dP3;
			agents.setProb(0,prob1);
			agents.setProb(2,prob3);
			out.newLine();
		}
		out.closeOutput();
		
	}//Main brackets

	
}
