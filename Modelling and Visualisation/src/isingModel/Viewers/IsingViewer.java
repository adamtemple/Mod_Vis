package isingModel.Viewers;

import isingModel.Model.Ising;

import java.awt.Graphics;
import javax.swing.JComponent;
import java.awt.Color;

public class IsingViewer extends JComponent {


	/**
	 * Field to hold the system which we will be drawing
	 * @param ising The Ising object we will be working with
	 */
	private Ising ising;
	
		  
	/**
	 * Field hold the size of the Ising system.
	 * Done for efficiency so this isn't worked out in each loop
	 */
	private int N;
	
	/**
	 * Create an Ising viewer. The system state is passed
	 * as argument to the constructor.
	 * 
	 * @param dpview Double pendulum state to view
	 */
	public IsingViewer(Ising in) {
		ising = in;
		N = ising.getN();
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
		synchronized(ising){
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(ising.getSpinState(i,j) == -1){
					g.setColor(Color.BLUE);
				}else{
					g.setColor(Color.RED);
				}	
				g.fillRect(x,y,xS,yS);
				x += xS;
			}
				y += yS;
				x=0;
			}	
		ising.notifyAll();
		}
	}//paintComponent brackets
	
	
}
