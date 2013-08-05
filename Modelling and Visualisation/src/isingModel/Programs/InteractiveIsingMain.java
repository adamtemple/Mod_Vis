package isingModel.Programs;

import isingModel.Controller.InteractiveIsing;
import java.io.IOException;
import javax.swing.JFrame;

public class InteractiveIsingMain {
	
	public static void main(String args[]) throws IOException{
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		
		// We need to switch all further execution to the Swing thread, as everything is now at the control of the user through the Swing interface
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			
				new InteractiveIsing();
				
			}
		});
		
	}
}
