package Exam.Programs;

import java.awt.Dimension;

import javax.swing.JFrame;

import Exam.Model.*;
import Exam.Model.Dynamics.Dynamics;
import Exam.Viewers.PottsViewer;

public class PottsViewerTest {
public static void main(String args[]){
	int N = 50;
	double T = 0.5;
	
	
	Potts test= new Potts(N,T);
	
	Dynamics iterator = Dynamics.createDynamics(test);
	
	// Set up a Frame (window) to open on the screen
	JFrame.setDefaultLookAndFeelDecorated(false);
	JFrame frame = new JFrame("SIRS");
	frame.setPreferredSize(new Dimension(700,700));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	// Create a pendulum viewer, and place this within the frame
	PottsViewer view = new PottsViewer(test);
	frame.getContentPane().add(view);
	
	// Lay out the frame's contents, and put it on screen
	frame.pack();
	frame.setVisible(true);
	
	for(;;) {
		iterator.update(test);
		// Request repaint, and wait for it to actually have been done before proceeding
		synchronized(test) {

			// Uncomment the following line to help identify concurrency problems
//			System.out.println("Request redraw at t="+dp.getTime());

			view.repaint();
			try {
				test.wait();
			}
			catch (Exception ignore) { }
		}
	}
	
	
	
}
}
