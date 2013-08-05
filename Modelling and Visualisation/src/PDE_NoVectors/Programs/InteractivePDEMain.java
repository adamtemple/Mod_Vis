package PDE_NoVectors.Programs;

import javax.swing.JFrame;

import PDE_NoVectors.Controller.InteractivePDE;
import PDE_NoVectors.Iterator.*;


public class InteractivePDEMain {
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		
		// We need to switch all further execution to the Swing thread, as everything is now at the control of the user through the Swing interface
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				int N = 50;
				new InteractivePDE(N);
				
			}
		});
	}
}
