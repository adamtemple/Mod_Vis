package PDE_NoVectors.Programs;

import java.awt.Dimension;

import javax.swing.JFrame;

import PDE_NoVectors.Iterator.*;
import PDE_NoVectors.Iterator.Iterator;
import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Viewers.BasicViewer;

public class MagneticWireTest {


	public static void main(String[] args) {
		
		double size = 3.0;
		
		Box space = new Box(size,size,size);
		
		space.setTwoCentralWires();
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		JFrame frame = new JFrame("Test");
		frame.setPreferredSize(new Dimension(700,700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a pendulum viewer, and place this within the frame
		BasicViewer view = new BasicViewer(space, false);
		frame.getContentPane().add(view);
		
		// Lay out the frame's contents, and put it on screen
		frame.pack();
		frame.setVisible(true);
		//view.repaint();
		
		
		Iterator test = new GaussSeidel(space,true, 1.5);
		System.out.println("System Size "+space.getTotalCellNumber());
		int count = 0;
		while(!test.isConverged()){
			test.iterateVectorPotential();
			view.repaint();
			//if(count % 1 == 0)test.isConverged();
			count++;
		}
		System.out.println("Done");
		//view.repaint();
	}

}
