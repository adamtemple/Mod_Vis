package MolecularDynamics.Viewers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;
import MolecularDynamics.Controller.InteractiveParticleSystem;
import MolecularDynamics.Model.Vector;



public class InteractiveSystemViewer extends JComponent{
	/**
	 * Number of milliseconds between frames of an animation
	 * May not be needed
	 */
	public static int ANIMATION_DELAY = 10;
	
	/**
	 * Field to hold the system of which we will be displaying
	 */
	private InteractiveParticleSystem controller;
	
	/**
	 * A timer object to perform the automatic updating of the window while the simulation is running
	 */
	private Timer autoUpdater = new Timer(ANIMATION_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) { repaint(); }		
	});
	
	double xScaling, yScaling, zScaling = 0.0;
	
	public InteractiveSystemViewer(InteractiveParticleSystem attachedTo){
		controller = attachedTo;
		//xScaling = 1.0 / controller.getParticles().getBox(0);
		//yScaling = 1.0 / controller.getParticles().getBox(1);
		setOpaque(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(800,800));
	}
	//Painting
	protected void paintComponent(Graphics g) {
		controller.painting();
		// First, get the dimensions of the component
			int width = getWidth(), height = getHeight();

		// Clear the background if we are an opaque component
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}

		double xScaling = (width)/controller.getBox()[0];
		double yScaling = (height)/controller.getBox()[1];

		
		//int ballWidth = (int)(xScaling), ballHeight = (int)(yScaling);
		//g.setColor(Color.BLACK);
		//g.drawRect(0, 0, width - 50, height -50);
		synchronized(controller){
		
		for(int i=0; i<controller.getSystemSize(); i++){
			int ballWidth = (int)(xScaling), ballHeight = (int)(yScaling);
			g.setColor(Color.RED);
			if(i==0) g.setColor(Color.BLUE);
			synchronized(controller.getParticles()){
				Vector position = controller.getParticles().getParticle(i).getPosition();
				
				int xPos = (int)(xScaling*position.getComponent(0));
				int yPos = (int)height - (int)(yScaling*position.getComponent(1));
			
				if(controller.getBox().length == 3.0){
					double zScaling = 1.0 - position.getComponent(2) / controller.getBox()[2];
					//Particles at zero are big, those further away are smaller
					//double zScaling  = (position.getComponent(2) - controller.getBox()[2]/2.0) / controller.getBox()[2]/2.0;
					if(zScaling < 0.0) zScaling = zScaling*(-1.0);
					if(zScaling >1.0) System.out.println("zScaling is incorrect");
					ballWidth *= zScaling;
					ballHeight *= zScaling;
					g.setColor(Color.getHSBColor(0.5f, (float)zScaling, 0.5f));
				}
				//Move the cirlces so they are specified by central points
				//g.drawRect(xPos, yPos, ballWidth, ballHeight);
				xPos = xPos - ballWidth/2;
				yPos = yPos - ballHeight/2;
				g.fillOval(xPos, yPos , ballWidth, ballHeight);
				
				
				//This code allows the balls to smoothly move across the boundary conditions
				if(xPos+ballWidth > width)g.fillOval(xPos-width, yPos, ballWidth, ballHeight);
				if(yPos+ballHeight > height)g.fillOval(xPos, yPos-height, ballWidth, ballHeight);
				
				if(xPos-ballWidth < width)g.fillOval(xPos+width, yPos, ballWidth, ballHeight);
				if(yPos-ballHeight < height)g.fillOval(xPos, yPos+height, ballWidth, ballHeight);
				
				//Code to draw lines for polymer
				if(controller.getPotential().isPolymer() && i!= controller.getSystemSize() - 1){
					g.setColor(Color.BLACK);
					Vector position2 = controller.getParticles().getParticle(i+1).getPosition();
				
					int xPos2 = (int)width - (int)(xScaling*position2.getComponent(0));
					int yPos2 = (int)height - (int)(yScaling*position2.getComponent(1));
					
					//xPos2 = xPos2 + ballWidth/2;
					//yPos2 = yPos2 + ballHeight/2;
					
					//g.drawLine(xPos+ballWidth/2, yPos + ballWidth/2, xPos2 , yPos2);
					//g.drawRect(xPos2, yPos2, ballWidth, ballHeight);
				}
				
				}
			
		
			
			
				controller.notifyAll();
			}
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
	}
			
		
}
