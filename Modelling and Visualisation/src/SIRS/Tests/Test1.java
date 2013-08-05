package SIRS.Tests;


import java.awt.Dimension;
import javax.swing.JFrame;
import SIRS.Model.*;
import SIRS.Model.Updater.Updater;
import SIRS.Viewers.SIRSViewer;

public class Test1 {

	public static void main(String args[]){
		

		// Create the default double-pendulum condition
		int N = 200;
		double [] probs = {0.05,0.09,0.2};
		
		SIRS_System agents = new SIRS_System(N, probs);
		Updater updater = new Updater();
	
		
		// Set up a Frame (window) to open on the screen
		JFrame.setDefaultLookAndFeelDecorated(false);
		JFrame frame = new JFrame("SIRS");
		frame.setPreferredSize(new Dimension(700,700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a pendulum viewer, and place this within the frame
		SIRSViewer view = new SIRSViewer(agents);
		frame.getContentPane().add(view);
		
		// Lay out the frame's contents, and put it on screen
		frame.pack();
		frame.setVisible(true);
		
		// Do timesteps of 1e-7 second, and update the viewer every 1/100s; keep running forever
		
		for(;;) {
			updater.update(agents);
			// Request repaint, and wait for it to actually have been done before proceeding
			synchronized(agents) {

				// Uncomment the following line to help identify concurrency problems
//				System.out.println("Request redraw at t="+dp.getTime());

				view.repaint();
				try {
					agents.wait();
				}
				catch (Exception ignore) { }
			}
		}
				
	}
}

