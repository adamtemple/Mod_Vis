package MolecularDynamics.Programs;

import java.io.*;

import MolecularDynamics.Integrator.*;
import MolecularDynamics.Model.*;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.VanDerWaals;



public class SimpleTotalEnergyTest {

	public static void main(String args[]) throws IOException{
	
		int N = 10;
		
		double [] box = {5.0,5.0,5.0};
		
		//ParticleSystem particles = new ParticleSystem(N, box);
		//particles = ParticleSystem.setVolumeFraction(particles, volFrac);
		Integrator iterator = new VelocityVerlet_NoseHoover();
		Potential potential = new VanDerWaals();
		
		double volFrac = 0.5;
		double step = 0.001;
		String filename = "Total_EnergyTest_density_"+volFrac+"+dt_"+step+".xvg";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	
		ParticleSystem particles = new ParticleSystem(N, box, potential);
		particles = ParticleSystem.setVolumeFraction(particles, volFrac);
		particles.setTimestep(step);
		int timesteps = 10000;
		
		for(int i=0; i<timesteps; i++){
			iterator.integrate(particles);
			out.write(particles.getTime()+"\t"+particles.getTotalE()+"\n");
			out.flush();
		}
	
		System.out.println("Finished");
		out.close();
	}
}
