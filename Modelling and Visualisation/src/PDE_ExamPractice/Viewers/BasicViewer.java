package PDE_ExamPractice.Viewers;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

import PDE_ExamPractice.Model.Box;

public class BasicViewer extends JComponent{
	
	private Box space;
	private int N;

	public BasicViewer(Box in) {
		space= in;
		N = space.getN();
		setOpaque(true);
		setBackground(Color.WHITE);
	}
	
	public void updateSpace(Box in){
		space = in;
	}
	
	/**
	 * This is the method in which all the drawing happens. It is called automatically by Swing when the component needs to
	 * be drawn or updated.
	 * 
	 * @param g current graphics context (an object that provides the drawing methods)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// First, get the dimensions of the component
			int width = getWidth(), height = getHeight();

		// Clear the background if we are an opaque component
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		//Take size of component and make rectangles of this size 
		//for each block
		int x =0, y= 0;
		int xS = width/N, yS = height/N;
		
	
		//System.out.println("width "+width+" Height "+height+" N "+N);
		synchronized(space){
			
			//Find the largest potential to define scale
			double maxPotential = 0.0, temp = 0.0;
			for(int i=0; i<N; i++){
				for(int j=0; j<N; j++){
					temp = space.getCell(i, j).getScalarPotential(); //REVIEW
					if(temp < 0) temp = -1.0 * temp;
					if(temp > maxPotential) maxPotential = temp;
				}
				//System.out.println("MAX "+temp);
			}
			
		for(int i=0;i<space.getN();i++){
			for(int j=0;j<space.getN();j++){
				
				double scaledPotential = space.getCell(i, j).getScalarPotential() / maxPotential;
				
				if(scaledPotential > 0){
					g.setColor(Color.getHSBColor(0.5f, 1.0f,(float)scaledPotential));
				}else{
					scaledPotential = -1.0 * scaledPotential;
					g.setColor(Color.getHSBColor(1.0f, 1.0f,(float)scaledPotential));
				}
				//if(space.getCell(i, j).getScalarPotential() == 1) g.setColor(Color.RED);
				g.fillRect(x,y,xS,yS);
			

				if(space.getCell(i, j).getCharge() != 0.0){
					if(space.getCell(i, j).getCharge() > 0 )g.setColor(Color.RED);
					if(space.getCell(i, j).getCharge() < 0 )g.setColor(Color.BLUE);
					g.fillOval(x,y,xS,yS);

				}
				/*
				//Draw line from centre of box to point specifed by vector
				double xTemp, yTemp;
				if(isScalarPotential){
					double mag = space.getCell(i, j).getField1().getMag();
					xTemp = space.getCell(i, j).getField1().getComponent(0) / mag;
					yTemp = space.getCell(i, j).getField1().getComponent(1) / mag;
				}else{
					double mag = space.getCell(i, j).getField2().getMag();
					xTemp = space.getCell(i, j).getField2().getComponent(0) / mag;
					yTemp = space.getCell(i, j).getField2().getComponent(1) / mag;
				}
				
				
				
				int x2 = x + xS/2 + (int) (xS/2 * xTemp);
				int y2 = y + yS/2 + (int) (yS/2 * yTemp);
				g.setColor(Color.white);
				g.drawLine(x + xS/2, y + yS/2, x2, y2);
			*/
				y += yS;
			}
				x += xS;
				
				y=0;
			}	
		space.notifyAll();
		}
	}//paintComponent brackets
	
}
