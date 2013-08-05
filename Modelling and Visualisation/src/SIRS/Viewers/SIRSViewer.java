package SIRS.Viewers;


import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;


import SIRS.Model.SIRS_System;

public class SIRSViewer extends JComponent {

	private SIRS_System agents;
	  

	private int N;
	

	public SIRSViewer(SIRS_System in) {
		agents = in;
		N = agents.getSystemSize();
		setOpaque(true);
		setBackground(Color.WHITE);
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
		//for each spin
		int x =0, y= 0;
		int xS = width/N, yS = height/N;
		
		
		//System.out.println("width "+width+" Height "+height+" N "+N);
		synchronized(agents){
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(agents.getAgent(i,j).isSusceptible()) g.setColor(Color.RED);
				if(agents.getAgent(i,j).isInfected()) g.setColor(Color.GREEN);
				if(agents.getAgent(i,j).isRecovered()) g.setColor(Color.BLUE);
			
				g.fillRect(x,y,xS,yS);
				x += xS;
			}
				y += yS;
				x=0;
			}	
		agents.notifyAll();
		}
	}//paintComponent brackets
	
	
}

