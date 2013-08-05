package PDE_NoVectors.Programs;

import java.awt.Dimension;

import javax.swing.JFrame;

import PDE_NoVectors.Iterator.*;
import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Viewers.BasicViewer;



public class PDETest {


	public static void main(String[] args) {
		
		double size = 2.5;
		
		Box space = new Box(size,size,size);
		space.setCentralUnitCharge();
		//space.setCentralQuadrapole();
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		JFrame frame = new JFrame("Test");
		frame.setPreferredSize(new Dimension(700,700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a pendulum viewer, and place this within the frame
		BasicViewer view = new BasicViewer(space, true);
		frame.getContentPane().add(view);
		
		// Lay out the frame's contents, and put it on screen
		frame.pack();
		frame.setVisible(true);
		//view.repaint();
		
		
		Iterator test = new Jacobi(space);
		//Iterator test = new GaussSeidel(space);
		
		System.out.println("System Size "+space.getTotalCellNumber());
		int count = 0;
		while(!test.isConverged()){
			test.iterateScalarPotential();
			view.repaint();
			//if(count % 1 == 0)test.isConverged();
			count++;
		}
		System.out.println("Done");
		//view.repaint();
	}

}
