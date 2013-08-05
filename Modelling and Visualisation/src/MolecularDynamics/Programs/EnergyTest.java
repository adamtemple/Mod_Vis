package MolecularDynamics.Programs;

import java.io.*;
import MolecularDynamics.Integrator.Integrator;
import MolecularDynamics.Integrator.VelocityVerlet;
import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.VanDerWaals;

public class EnergyTest {

	public static void main(String args[]) throws IOException{
	
		int N = 10;
		double volFrac = 0.5;
		double [] box = {5.0,5.0,5.0};
		
		//ParticleSystem particles = new ParticleSystem(N, box);
		//particles = ParticleSystem.setVolumeFraction(particles, volFrac);
		Integrator iterator = new VelocityVerlet();
		Potential potential = new VanDerWaals();
		
		String filename = "EnergyTest_timestep_noNoseHoover_VDW.xvg";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	
		double step = 0.002, stepIterations = 0.004;
		int timesteps = 10000;
		int numOfSims = 15;
		for(int i=0; i < numOfSims; i++){
			ParticleSystem particles = new ParticleSystem(N, box, potential);
			particles = ParticleSystem.setVolumeFraction(particles, volFrac);
			
			particles.setTimestep(step);
			System.out.println("Iteration "+(i+1)+" of "+numOfSims+". Timestep = "+step);
			for(int j=0;j<timesteps;j++){
				iterator.integrate(particles);
				if(j%3==0)out.write(j+"\t"+particles.getTotalE()+"\n");
				out.flush();
			}
			out.write("\n");
			step += stepIterations;
		}
		System.out.println("Finished");
		out.close();
	}
}
