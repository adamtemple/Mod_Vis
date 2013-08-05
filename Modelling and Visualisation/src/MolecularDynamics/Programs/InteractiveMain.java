package MolecularDynamics.Programs;

import java.io.IOException;

import javax.swing.JFrame;

import MolecularDynamics.Controller.InteractiveParticleSystem;
import MolecularDynamics.Integrator.Integrator;
import MolecularDynamics.Integrator.VelocityVerlet;
import MolecularDynamics.Integrator.VelocityVerlet_NoseHoover;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;
import SIRS.Controller.InteractiveSIRS;

public class InteractiveMain {
	public static void main(String args[]) throws IOException{
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		
		// We need to switch all further execution to the Swing thread, as everything is now at the control of the user through the Swing interface
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				double [] in = {10.0,10.0};
				int N = 50;
				Integrator integrator = new VelocityVerlet();
				Potential potential = new WicksChandlerAnderson();
				new InteractiveParticleSystem(N, in, integrator,potential);
				
			}
		});
		
	}
}


