package PDE_NoVectors.Viewers;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

import PDE_NoVectors.Model.Box;

public class BasicViewer extends JComponent{
	
	private Box space;
	  
	private int xN, yN, zN, chargePlane =0;
	boolean isScalarPotential;
	

	public BasicViewer(Box in, boolean booleanIn) {
		space= in;
		xN = space.getSpace().length - 2;
		yN = space.getSpace()[0].length - 2;
		zN = space.getSpace()[0][0].length - 2;
		setOpaque(true);
		setBackground(Color.WHITE);
		isScalarPotential = booleanIn;

		chargePlane = space.getSpace()[0][0].length / 2;
		/*
		for(int i=0; i < xN; i++){
			for(int j=0; j < yN; j++){
				for(int k=0; k < zN; k++){
					if(space.getCell(i, j, k).getCharge() != 0.0) chargePlane = k;
					}
				}
			}
		*/
		
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
			for(int i=1; i<xN; i++){
				for(int j=1; j<yN; j++){
					if(isScalarPotential)temp = space.getCell(i, j, chargePlane).getScalarPotential(); //REVIEW
					else temp = space.getCell(i, j, chargePlane).getVectorPotential().getMag();
					
					if(temp < 0) temp = -1.0 * temp;
					if(temp > maxPotential) maxPotential = temp;
				}
			}
			
		for(int i=1;i<xN;i++){
			for(int j=1;j<yN;j++){
				
				double scaledPotential;
				if(isScalarPotential) scaledPotential = space.getCell(i, j, chargePlane).getScalarPotential() / maxPotential;
				else scaledPotential = space.getCell(i, j, chargePlane).getVectorPotential().getMag() / maxPotential;
				
				
				if(scaledPotential > 0){
					g.setColor(Color.getHSBColor(0.5f, 1.0f,(float)scaledPotential));
				}else{
					scaledPotential = -1.0 * scaledPotential;
					g.setColor(Color.getHSBColor(1.0f, 1.0f,(float)scaledPotential));
				}
				
				g.fillRect(x,y,xS,yS);
			
				if(isScalarPotential){
					if(space.getCell(i, j, chargePlane).getCharge() != 0.0){
						if(space.getCell(i, j, chargePlane).getCharge() > 0 )g.setColor(Color.RED);
						if(space.getCell(i, j, chargePlane).getCharge() < 0 )g.setColor(Color.BLUE);
						g.fillOval(x,y,xS,yS);
					}
				}else{
					if(space.getCell(i, j, chargePlane).getVectorCharge().getComponent(2) != 0.0){
						if(space.getCell(i, j, chargePlane).getVectorCharge().getComponent(2) > 0 )g.setColor(Color.RED);
						if(space.getCell(i, j, chargePlane).getVectorCharge().getComponent(2) < 0 )g.setColor(Color.BLUE);
						g.fillOval(x,y,xS,yS);
					}
				}
				
			
				
				//Draw line from centre of box to point specifed by vector
				double xTemp, yTemp;
				if(isScalarPotential){
					double mag = space.getCell(i, j, chargePlane).getField1().getMag();
					xTemp = space.getCell(i, j, chargePlane).getField1().getComponent(0) / mag;
					yTemp = space.getCell(i, j, chargePlane).getField1().getComponent(1) / mag;
				}else{
					double mag = space.getCell(i, j, chargePlane).getField2().getMag();
					xTemp = space.getCell(i, j, chargePlane).getField2().getComponent(0) / mag;
					yTemp = space.getCell(i, j, chargePlane).getField2().getComponent(1) / mag;
				}
				
				
				
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
