package isingModel.Viewers;

import isingModel.Controller.InteractiveIsing;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 * Interactive version of the Ising view class. Interacts with the Ising object
 * indirectly through the controller object passed to the constructor.
 * Mouselistener is from example and may not be needed here
 * @author s0831683
 *
 */
public class InteractiveIsingViewer extends JComponent {
	
	/**
	 * Number of milliseconds between frames of an animation
	 * May not be needed
	 */
	public static int ANIMATION_DELAY = 10;
	
		
	/**
	 * Field to hold the Ising system of which we will be displaying
	 */
	private InteractiveIsing controller;
	
	/**
	 * A timer object to perform the automatic updating of the window while the simulation is running
	 */
	private Timer autoUpdater = new Timer(ANIMATION_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) { repaint(); }		
	});
	
	// Constructor
	/**
	 * Create a viewer on top of the state object passed as the arguement
	 */
	public InteractiveIsingViewer(InteractiveIsing attachedTo) {
		controller = attachedTo;
		
		setOpaque(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(600,600));
		
	}
	
	//Painting
	/**
	 * This method is where the drawing happens.
	 * Automatically called when an update to the drawing is needed.
	 * Will probably need to come back to this
	 */
	protected void paintComponent(Graphics g) {
		controller.painting();
		
		// First, get the dimensions of the component
			int width = getWidth(), height = getHeight();

		// Clear the background if we are an opaque component
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		//Take size of component and make rectangles of this size 
		//for each spin
		int x =0, y= 0, N = controller.getSystemSize();
		int xS = width/N, yS = height/N;
		
		
		//System.out.println("width "+width+" Height "+height+" N "+N);
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(controller.getSpinState(i,j) == -1){
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
		//The viewer won't seem to paint while the simulation is running
		//Unless this is here
		//System.out.println("painting");
		
		
	}//paintComponent brackets
	
	
	//MESSAGES
	/**
	 * Recieved whenever the simulated system has started running
	 */
	public void simulationStarted(){
		//Ensure the display is up to date
		repaint();
		//Send messages at regular intervals using the autoUpdater Timer object
		autoUpdater.restart();
	}
	
	public void simulationFinished(){
		//Stop sending regular repaint messages
		autoUpdater.stop();
		
		//Ensure the display is up-to-date
		repaint();
	}
	
	public void simulationPaused(){
		//Stop sending regular repaint messages
		autoUpdater.stop();
		//Make sure the display is up to date
		repaint();
	}
	
}//class brackets
