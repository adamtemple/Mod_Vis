package SIRS.Tests;

import java.io.IOException;

import SIRS.Analysis.Analysis;
import SIRS.DataOutput.Data_FileOutput;
import SIRS.Model.SIRS_System;
import SIRS.Model.Updater.Updater;

public class InfectionWaves {

	public static void main(String args[]) throws IOException{
		
		int N = 250, time =0;
		SIRS_System agents = new SIRS_System(N);
		Updater iterator = new Updater();
		Analysis analysis = new Analysis(agents);
		Data_FileOutput out = new Data_FileOutput();
		out.createInfectedOutput(agents);
		
		double [] probs = {0.938,0.844,0.043};
		agents.setProb(probs);
		
		while(true){
			for(int i=0; i<15000; i++){
				iterator.update(agents);
				analysis.doAnalysisStep(agents);
				double infected = agents.getInfected();
				//double avInfected = analysis.calculateNormalisedAverageInfected();
				out.doInfectedOutput(infected, time);
				time++;
			}
			time = 0;
			out.newLine();
			agents = new SIRS_System(N);
			agents.setProb(probs);
			analysis.restartAnalysis();
		}
		//out.closeOutput();
		
		
		
	}//Main brackets
	
	
}
