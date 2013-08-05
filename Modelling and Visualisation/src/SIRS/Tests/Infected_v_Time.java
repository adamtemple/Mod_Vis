package SIRS.Tests;

import java.io.IOException;

import SIRS.Analysis.Analysis;
import SIRS.DataOutput.Data_FileOutput;
import SIRS.Model.SIRS_System;
import SIRS.Model.Updater.Updater;

public class Infected_v_Time {

	public static void main(String args[]) throws IOException{
		
		int N = 50, time =0;
		SIRS_System agents = new SIRS_System(N);
		Updater iterator = new Updater();
		Analysis analysis = new Analysis(agents);
		Data_FileOutput out = new Data_FileOutput();
		out.createInfectedOutput(agents);
		
		while(agents.getProb(2) > 0){
			for(int i=0; i<20000; i++){
				iterator.update(agents);
				analysis.doAnalysisStep(agents);
				double infected = (double)agents.getInfected()/(double)(N*N);
				double avInfected = analysis.calculateNormalisedAverageInfected();
				out.doInfectedOutput(infected, time);
				time++;
			}
			time = 0;
			out.newLine();
			agents.setProb(2, agents.getProb(2) - 0.02);
			//analysis.restartAnalysis();
		}
		//out.closeOutput();
		
	}//Main brackets
	
}
