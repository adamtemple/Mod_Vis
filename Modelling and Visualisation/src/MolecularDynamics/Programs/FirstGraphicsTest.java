package MolecularDynamics.Programs;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import MolecularDynamics.Integrator.Integrator;
import MolecularDynamics.Integrator.VelocityVerlet;
import MolecularDynamics.Model.Particle;
import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Vector;
import MolecularDynamics.Model.Potential.PolymerInteraction;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;
import MolecularDynamics.Viewers.ParticleSystemViewer;
public class FirstGraphicsTest {


		public static void main(String args[]) throws IOException{
			

			// Create the default double-pendulum condition
			int N = 3;
			double [] box = {25.0,25.0};
			Potential potential = new PolymerInteraction();
			ParticleSystem test = new ParticleSystem(N, box, potential);
			Integrator updater = new VelocityVerlet();
		
			/*
			 * Particle values for testing
			 * 
			 */
			Particle[] particles = new Particle[3];
			
			particles[0] = new Particle(1.0,0,new Vector(new double[]{5.0,5.0}),new Vector(new double[]{10.0,0.0}),new Vector(new double[]{0.0,0.0}));
			particles[1] = new Particle(1.0,0,new Vector(new double[]{5.0,4.0}),new Vector(new double[]{0.0,0.0}),new Vector(new double[]{0.0,0.0}));
			particles[2] = new Particle(1.0,0,new Vector(new double[]{5.0,3.0}),new Vector(new double[]{0.0,0.0}),new Vector(new double[]{0.0,0.0}));
			test.setParticles(particles);
			
			// Set up a Frame (window) to open on the screen
			JFrame.setDefaultLookAndFeelDecorated(false);
			JFrame frame = new JFrame("Particle System");
			frame.setPreferredSize(new Dimension(700,700));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			// Create a pendulum viewer, and place this within the frame
			ParticleSystemViewer view = new ParticleSystemViewer(test);
			frame.getContentPane().add(view);
			
			// Lay out the frame's contents, and put it on screen
			frame.pack();
			frame.setVisible(true);
			view.repaint();
			
			BufferedWriter out = new BufferedWriter(new FileWriter("test_energy.xvg"));
			int count = 0;
			while(true){
				updater.integrate(test);
				//System.out.println("Time: "+test.getTime()+"\tTemperature: "+test.getTemperature());
				out.write(test.getTime()+"\t"+test.getKE()+"\t"+test.getPE()+"\t"+test.getTotalE()+"\n");
				out.flush();
				count++;
				// Request repaint, and wait for it to actually have been done before proceeding
				synchronized(test) {

					// Uncomment the following line to help identify concurrency problems
//					System.out.println("Request redraw at t="+dp.getTime());

					view.repaint();
					try {
						test.wait();
					}
					catch (Exception ignore) { }
				}
				//break;
			}
					
		}
		
		
}



