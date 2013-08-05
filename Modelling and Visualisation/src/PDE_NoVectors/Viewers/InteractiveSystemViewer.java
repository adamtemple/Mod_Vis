package PDE_NoVectors.Viewers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import PDE_NoVectors.Controller.InteractivePDE;

public class InteractiveSystemViewer extends JComponent{

	/**
	 * Number of milliseconds between frames of an animation
	 * May not be needed
	 */
	public static int ANIMATION_DELAY = 10;
	
	/**
	 * Field to hold the system of which we will be displaying
	 */
	private InteractivePDE controller;
	
	/**
	 * A timer object to perform the automatic updating of the window while the simulation is running
	 */
	private Timer autoUpdater = new Timer(ANIMATION_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) { repaint(); }		
	});
	
	private boolean isScalarPotential = true;
	
	public InteractiveSystemViewer(InteractivePDE attachedTo){
		controller = attachedTo;

		setOpaque(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(800,800));
	}
	//Painting
	protected void paintComponent(Graphics g) {
		controller.painting();
		
	
		// First, get the dimensions of the component
		int width = getWidth(), height = getHeight();
		
		int plane = controller.getSpace().getSpace()[0][0].length /2;
		
		// Clear the background if we are an opaque component
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		//Take size of component and make rectangles of this size 
		//for each block
		int x =0, y= 0;
		int xS = width/controller.getN(), yS = height/controller.getN();
		
	
			//Find the largest potential to define scale
			double maxPotential = 0.0, temp = 0.0;
			for(int i=1; i < controller.getSpace().getSpace().length - 1 ; i++){
				for(int j=1; j < controller.getSpace().getSpace()[i].length - 1; j++){
					
					if(isScalarPotential)temp = controller.getSpace().getCell(i, j, plane).getScalarPotential(); //REVIEW
					else temp = controller.getSpace().getCell(i, j, plane).getVectorPotential().getMag();
					
					if(temp < 0) temp = -1.0 * temp;
					if(temp > maxPotential) maxPotential = temp;
					
				}
			}
			
		for(int i=1;i < controller.getSpace().getSpace().length - 1;i++){
			for(int j=1;j < controller.getSpace().getSpace()[i].length - 1;j++){
				
				double scaledPotential;
				if(isScalarPotential) scaledPotential = controller.getSpace().getCell(i, j, plane).getScalarPotential() / maxPotential;
				else scaledPotential = controller.getSpace().getCell(i, j, plane).getVectorPotential().getMag() / maxPotential;
			
				if(scaledPotential > 0){
					g.setColor(Color.getHSBColor(0.5f, 1.0f,(float)scaledPotential));
				}else{
					scaledPotential = -1.0 * scaledPotential;
					g.setColor(Color.getHSBColor(1.0f, 1.0f,(float)scaledPotential));
				}
				
				g.fillRect(x,y,xS,yS);
			
				if(isScalarPotential){
					if(controller.getSpace().getCell(i, j, plane).getCharge() != 0.0){
						if(controller.getSpace().getCell(i, j, plane).getCharge() > 0 )g.setColor(Color.RED);
						if(controller.getSpace().getCell(i, j, plane).getCharge() < 0 )g.setColor(Color.BLUE);
						g.fillOval(x,y,xS,yS);
					}
				}else{
					if(controller.getSpace().getCell(i, j, plane).getVectorCharge().getComponent(2) != 0.0){
						if(controller.getSpace().getCell(i, j, plane).getVectorCharge().getComponent(2) > 0 )g.setColor(Color.RED);
						if(controller.getSpace().getCell(i, j, plane).getVectorCharge().getComponent(2) < 0 )g.setColor(Color.BLUE);
						g.fillOval(x,y,xS,yS);
					}
				}
				
			
				
				//Draw line from centre of box to point specifed by vector
				double xTemp, yTemp;
				if(isScalarPotential){
					double mag = controller.getSpace().getCell(i, j, plane).getField1().getMag();
					xTemp = controller.getSpace().getCell(i, j, plane).getField1().getComponent(0) / mag;
					yTemp = controller.getSpace().getCell(i, j, plane).getField1().getComponent(1) / mag;
				}else{
					double mag = controller.getSpace().getCell(i, j, plane).getField2().getMag();
					xTemp = controller.getSpace().getCell(i, j, plane).getField2().getComponent(0) / mag;
					yTemp = controller.getSpace().getCell(i, j, plane).getField2().getComponent(1) / mag;
				}
				
				
				//First two terms take the point in the centre of the square
				int x2 = x + xS/2 + (int) (xS/2 * xTemp);
				int y2 = y + yS/2 + (int) (yS/2 * yTemp);
				g.setColor(Color.white);
				g.drawLine(x + xS/2, y + yS/2, x2, y2);
				
				x2 = x + xS/2 - (int) (xS/2 * xTemp);
				y2 = y + yS/2 - (int) (yS/2 * yTemp);
				g.setColor(Color.RED);
				g.drawLine(x + xS/2, y + yS/2, x2, y2);
			
				y += yS;
			}
				x += xS;
				
				y=0;
			}	
		
		
		
	}	//paintComponent brackets
	
	//MESSAGES
	public void simulationStarted(){
		repaint();
		autoUpdater.restart();
	}
	public void simulationPaused(){
		autoUpdater.stop();
		repaint();
	}
	public void simulationReset(){
		simulationStarted();
		repaint();
	}
	
}
