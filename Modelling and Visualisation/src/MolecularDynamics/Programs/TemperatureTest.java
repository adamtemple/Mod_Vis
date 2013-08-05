package MolecularDynamics.Programs;

import java.io.*;

import MolecularDynamics.Integrator.*;
import MolecularDynamics.Model.*;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;


public class TemperatureTest {

	public static void main(String args[]) throws IOException{
	
		int N = 10;
		double volFrac = 0.5;
		double [] box = {5.0,5.0,5.0};
		
		//ParticleSystem particles = new ParticleSystem(N, box);
		//particles = ParticleSystem.setVolumeFraction(particles, volFrac);
		Integrator iterator = new VelocityVerlet_NoseHoover();
		Potential potential = new WicksChandlerAnderson();
		
		String filename = "Temperature_NoseHoover_WCA_Q=10.0.xvg";
		BufferedWriter output = new BufferedWriter(new FileWriter(filename));
		
		//Code to do temperatures for different timesteps
	/*
		double step = 0.00, stepIterations = 0.002;
		int timesteps = 2500;
		for(int i=0; i < 50; i++){
			ParticleSystem particles = new ParticleSystem(N, box);
			particles = ParticleSystem.setVolumeFraction(particles, volFrac);
			particles.setTimestep(step);
			System.out.println("Iteration "+(i+1)+" of "+ 50+". Timestep = "+step);
			for(int j=0;j<timesteps;j++){
				iterator.update(particles);
				out.write(j+"\t"+particles.getTotalE()+"\n");
				out.flush();
			}
			out.write("\n");
			step += stepIterations;
		}
	}
	*/
		//Code to do temperature for different temperatues with and w/o Nose-Hoover
		double step = 0.00, stepIterations = 0.002;
		int timesteps = 10000;
		double targetTemperature = 0.1;
		for(int i=0; i < 4; i++){
			ParticleSystem particles = new ParticleSystem(N, box, potential);
			particles = ParticleSystem.setVolumeFraction(particles, volFrac);
			

			particles.setTargetTemperature(targetTemperature);
			particles.generateVelocities();
			((VelocityVerlet_NoseHoover) iterator).setRelaxationTerm(10.0);
			System.out.println("Iteration "+(i+1)+" of "+ 4+". Temperature = "+targetTemperature);
			for(int j=0;j<timesteps;j++){
				iterator.integrate(particles);
				output.write(j+"\t"+particles.getTemperature()+"\n");
				output.flush();
				//if(j % 100==0)System.out.println(particles.getTemperature());
			}
			output.write("\n");
			targetTemperature += 0.5;
		}
		System.out.println("Finished");
		output.close();
	}

}