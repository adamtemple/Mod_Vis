package SIRS.Tests;

import java.io.IOException;

import SIRS.Analysis.Analysis;
import SIRS.DataOutput.Data_FileOutput;
import SIRS.Model.SIRS_System;
import SIRS.Model.Updater.Updater;

public class P1_P3_contourPlot {


	public static void main(String args[]) throws IOException{
		
		int N = 50, time =0;
		SIRS_System agents = new SIRS_System(N);
		Updater iterator = new Updater();
		Analysis analysis = new Analysis(agents);
		Data_FileOutput out = new Data_FileOutput();
		
		out.createContourOutput(agents);
		
		double prob1 = 0.0, prob3 = 0.0;
		double dP = 0.01;
		agents.setProb(0, prob1);
		agents.setProb(2, prob3);

		while(prob1 <= 0.5){
			while(prob3 <= 0.5){
					for(int k=0; k < 10000; k++){
						iterator.update(agents);
						analysis.doAnalysisStep(agents);
					}
					//System.out.println(agents.getProb(0));
					double avInfected = analysis.calculateNormalisedAverageInfected();
					out.doContourOutput(agents.getProb(2), agents.getProb(0), avInfected);
					prob3 += dP;
					agents = new SIRS_System(N);
					agents.setProb(2,prob3);
					agents.setProb(0,prob1);
					analysis.restartAnalysis();
			}
			prob1 += dP;
			prob3 = 0.0;
			agents.setProb(0,prob1);
			agents.setProb(2,prob3);
			out.newLine();
		}


		
		out.closeOutput();
		
	}//Main brackets
	
	
}
