package MolecularDynamics.Programs;

import java.io.*;
import MolecularDynamics.Model.*;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;
import MolecularDynamics.Integrator.*;

public class EnergyTest_Density {

	public static void main(String args[]) throws IOException{
	
		int N = 10;
		double volFrac = 0.1;
		double [] box = {5.0,5.0,5.0};
		
		//ParticleSystem particles = new ParticleSystem(N, box);
		//particles = ParticleSystem.setVolumeFraction(particles, volFrac);
		Integrator iterator = new VelocityVerlet();
		Potential potential = new WicksChandlerAnderson();
		
		String filename = "EnergyTest_density_PE+KE_noNoseHoover_WCA.xvg";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	
		double step = 0.002, volFracIterations = 0.1;
		int timesteps = 10000;
		int numOfSims = 5;
		for(int i=0; i < numOfSims; i++){
			ParticleSystem particles = new ParticleSystem(N, box, potential);
			particles = ParticleSystem.setVolumeFraction(particles, volFrac);
			particles.setTimestep(step);
			System.out.println("Iteration "+(i+1)+" of "+numOfSims+". VolFrac = "+volFrac);
			for(int j=0;j<timesteps;j++){
				iterator.integrate(particles);
				if(j%3==0)out.write(j+"\t"+particles.getPE()+"\t"+particles.getKE()+"\t"+particles.getTotalE()+"\n");
				out.flush();
			}
			out.write("\n");
			volFrac += volFracIterations;
		}
		System.out.println("Finished");
		out.close();
	}
}
