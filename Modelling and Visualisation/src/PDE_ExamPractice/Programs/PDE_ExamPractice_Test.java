package PDE_ExamPractice.Programs;

import java.awt.Dimension;

import javax.swing.JFrame;

import PDE_ExamPractice.Iterator.*;
import PDE_ExamPractice.Model.Box;
import PDE_ExamPractice.Viewers.BasicViewer;



public class PDE_ExamPractice_Test {


	public static void main(String[] args) {
		
		int size = 100;
		double radius = 2.0;
		
		Box space = new Box(size, radius);
		//space.setCentralUnitCharge();
		space.setCentralDroplet();
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		JFrame frame = new JFrame("Test");
		frame.setPreferredSize(new Dimension(850,850));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a pendulum viewer, and place this within the frame
		BasicViewer view = new BasicViewer(space);
		frame.getContentPane().add(view);
		
		// Lay out the frame's contents, and put it on screen
		frame.pack();
		frame.setVisible(true);
		view.repaint();
		
		
		Iterator test = new FisherIterator(space);
		System.out.println("System Size "+space.getTotalCellNumber());
		int count = 0;
		
		
		
		while(!test.isConverged()){
			test.iterateScalarPotential();
			view.repaint();
			System.out.println(space.getCell(0, 0).getCharge());
			//if(count % 1 == 0)test.isConverged();
			count++;
		}
		
		//System.out.println("Done");
		//view.repaint();
	}

}
