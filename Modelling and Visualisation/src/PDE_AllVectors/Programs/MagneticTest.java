package PDE_AllVectors.Programs;

import java.awt.Dimension;
import javax.swing.JFrame;

import PDE_AllVectors.Iterator.*;
import PDE_AllVectors.Model.*;
import PDE_AllVectors.Viewers.BasicViewer;

public class MagneticTest {


	public static void main(String[] args) {
		
		double size = 5.0;
		
		Box space = new Box(size,size,size,3);
		//space.setCentralUnitCharge();
		space.setCentralQuadrapole();
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		JFrame frame = new JFrame("Test");
		frame.setPreferredSize(new Dimension(700,700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a pendulum viewer, and place this within the frame
		BasicViewer view = new BasicViewer(space);
		frame.getContentPane().add(view);
		
		// Lay out the frame's contents, and put it on screen
		frame.pack();
		frame.setVisible(true);
		//view.repaint();
		
		
		Iterator test = new Jacobi(space);
		System.out.println("System Size "+space.getTotalCellNumber());
		int count = 0;
		while(!test.isConverged()){
			test.iterate();
			view.repaint();
			//if(count % 1 == 0)test.isConverged();
			count++;
		}
		System.out.println("Done");
		//view.repaint();
	}

}
