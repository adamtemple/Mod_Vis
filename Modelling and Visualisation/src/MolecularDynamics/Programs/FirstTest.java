package MolecularDynamics.Programs;

import java.io.*;

import MolecularDynamics.Integrator.Integrator;
import MolecularDynamics.Integrator.VelocityVerlet;
import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;

public class FirstTest {

	public static void main(String args[]) throws IOException{
		
		int N = 2;
		double [] box = {10.0, 10.0};
		Potential potential = new WicksChandlerAnderson();
		ParticleSystem test = new ParticleSystem(N, box, potential);
		BufferedWriter out = new BufferedWriter(new FileWriter("test.xvg"));
		Integrator iterator = new VelocityVerlet();
		int x = 0;
		while(x < 5000){
			iterator.integrate(test);
			//System.out.println(test.getParticle(25).getVelocity().getComponent(0)+"\t"+test.getParticle(25).getVelocity().getComponent(1));
			//out.write(test.getTime()+"\t"+test.getParticle(25).getVelocity().getComponent(1)+"\n");
			out.flush();
			x++;
		}
		
		
	}//Main
	
	
	
}
