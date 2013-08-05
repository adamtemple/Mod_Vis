package MolecularDynamics.Programs;

import java.io.*;

import MolecularDynamics.Integrator.Integrator;
import MolecularDynamics.Integrator.VelocityVerlet;
import MolecularDynamics.Integrator.VelocityVerlet_NoseHoover;
import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Vector;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.VanDerWaals;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;

public class RMSD_Test {

	public static void main(String args[]) throws IOException{

		double[] boxVectors = {5.0,5.0,5.0};
		Potential potential = new VanDerWaals();
		ParticleSystem particles = new ParticleSystem(1, boxVectors, potential);
		Integrator iterator = new VelocityVerlet();

		
		double temperature = 0.5;
		
		double volFrac = 0.5;
		
		Vector initialPosition;
		
		particles = ParticleSystem.setVolumeFraction(particles, volFrac);
		initialPosition =  particles.getParticle(0).getPosition();
		particles.setTargetTemperature(temperature);
	
		String filename = "RMSD_WCA-Fluid_Density_"+volFrac+"_T_"+particles.getTargetTemperature()+".xvg";
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		
		Vector displacement;
		double displacementMagSum = 0.0;
		double RMSD;
		
		int iterations = 25000;
		int numOfSims = 10;
		for(int j=0; j< numOfSims; j++){
			System.out.println("Run "+(j+1)+" of "+numOfSims);
			for(int i=0; i<iterations; i++){
				iterator.integrate(particles);
				//Calculate and print RMSD
				displacement = Vector.addVector(particles.getParticle(0).getPosition(), Vector.scalarProduct(-1.0
										, initialPosition));
				displacementMagSum += displacement.getMagSquared();
				
				RMSD = Math.sqrt(displacementMagSum / (double)(i+1));
				out.write(particles.getTime()+"\t"+RMSD+"\n");
				out.flush();
				if(i!=0 && i%(iterations/2)==0)System.out.println("Halfway");
			}
			particles = ParticleSystem.setVolumeFraction(particles, volFrac);
			potential = new WicksChandlerAnderson();
			initialPosition =  particles.getParticle(0).getPosition();
			particles.setTargetTemperature(temperature);
			iterator = new VelocityVerlet_NoseHoover();
			
			out.write("\n");
			
			displacementMagSum = 0.0;
		}
		System.out.println("Finished");
		out.close();
	}//Main brackets
}//Class brackets
