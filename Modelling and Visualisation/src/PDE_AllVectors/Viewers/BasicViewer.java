package PDE_AllVectors.Viewers;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import PDE_AllVectors.Model.Box;
import SIRS.Model.SIRS_System;

public class BasicViewer extends JComponent{
	
	private Box space;
	  
	private int xN, yN, zN, chargePlane =0;
	

	public BasicViewer(Box in) {
		space= in;
		xN = space.getNumCells()[0];
		yN = space.getNumCells()[1];
		zN = space.getNumCells()[2];
		setOpaque(true);
		setBackground(Color.WHITE);

		for(int i=0; i < xN; i++){
			for(int j=0; j < yN; j++){
				for(int k=0; k < zN; k++){
					if(space.getCell(i, j, k).getCharge(0) != 0.0) chargePlane = k;
					}
				}
			}	
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
		int xS = width/xN, yS = height/yN;
		
	
		//System.out.println("width "+width+" Height "+height+" N "+N);
		synchronized(space){
			
			//Find the largest potential to define scale
			double maxPotential = 0.0, temp = 0.0;
			for(int i=1;i<xN;i++){
				for(int j=1;j<yN;j++){
					temp = space.getCell(i, j, chargePlane).getPotential(0); //REVIEW
					if(temp < 0) temp = -1.0 * temp;
					if(temp > maxPotential) maxPotential = temp;
				}
			}
			
		for(int i=1;i<xN;i++){
			for(int j=1;j<yN;j++){
				
				double scaledPotential = space.getCell(i, j, chargePlane).getPotential(0) / maxPotential;
				
				if(scaledPotential > 0){
					g.setColor(Color.getHSBColor(0.5f, 1.0f,(float)scaledPotential));
				}else{
					scaledPotential = -1.0 * scaledPotential;
					g.setColor(Color.getHSBColor(1.0f, 1.0f,(float)scaledPotential));
				}
				
				g.fillRect(x,y,xS,yS);
			
				if(space.getCell(i, j, chargePlane).getCharge(0) != 0.0){
					if(space.getCell(i, j, chargePlane).getCharge(0) > 0 )g.setColor(Color.RED);
					if(space.getCell(i, j, chargePlane).getCharge(0) < 0 )g.setColor(Color.BLUE);
					g.fillOval(x,y,xS,yS);
				}
				
				//Draw line from centre of box to point specifed by vector
				
				double mag = space.getCell(i, j, chargePlane).getField().getMag();
				double xTemp = space.getCell(i, j, chargePlane).getField().getComponent(0) / mag;
				double yTemp = space.getCell(i, j, chargePlane).getField().getComponent(1) / mag;
				
				int x2 = x + xS/2 + (int) (xS/2 * xTemp);
				int y2 = y + yS/2 + (int) (yS/2 * yTemp);
				g.setColor(Color.white);
				g.drawLine(x + xS/2, y + yS/2, x2, y2);
			
				y += yS;
			}
				x += xS;
				
				y=0;
			}	
		space.notifyAll();
		}
	}//paintComponent brackets
	
}
