package SIRS.Viewers;

import isingModel.Controller.InteractiveIsing;

import javax.swing.JComponent;
import javax.swing.Timer;

import SIRS.Controller.InteractiveSIRS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InteractiveSIRSViewer extends JComponent{

	/**
	 * Number of milliseconds between frames of an animation
	 * May not be needed
	 */
	public static int ANIMATION_DELAY = 1;
	
	/**
	 * Field to hold the Ising system of which we will be displaying
	 */
	private InteractiveSIRS controller;
	
	/**
	 * A timer object to perform the automatic updating of the window while the simulation is running
	 */
	private Timer autoUpdater = new Timer(ANIMATION_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) { repaint(); }		
	});
	
	//CONSTRUCTOR
	
	public InteractiveSIRSViewer(InteractiveSIRS attachedTo){
		controller = attachedTo;
		setOpaque(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(800,800));
	}
	
	//PAINTING
	protected void paintComponent(Graphics g){
		controller.painting();
		int width = getWidth(), height = getHeight();
		
		if(isOpaque()){
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		//Get size for agents
		int x =0, y= 0, N = controller.getSystemSize();
		int xS = width/N, yS = height/N;
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(controller.getAgent(i,j).isSusceptible())g.setColor(Color.RED);
				if(controller.getAgent(i,j).isInfected())g.setColor(Color.GREEN);
				if(controller.getAgent(i,j).isRecovered())g.setColor(Color.BLUE);
				if(controller.getAgent(i, j).isImmune())g.setColor(Color.WHITE);
				g.fillRect(x,y,xS,yS);
				x += xS;
			}
			y += yS;
			x = 0;
		}
	}
	
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
	
}//class brackets
