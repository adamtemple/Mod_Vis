import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * A noninteractive version of the pendulum viewer. This class provides a custom Swing component whose
 * <code>paintComponent</code> method displays the start of the <code>DoublePendulum</code> object
 * that is supplied to the constructor when this object is created.
 * 
 * This class also provides a <code>main</code> method that demonstrates the use of the custom
 * component embedded within a <code>JFrame</code>
 * 
 * @author Richard Blythe
 *
 */
public class DoublePendulumViewer extends JComponent {
	
	/**
	 * Radius of each bob in the display
	 */
	private static final double BOB_RADIUS = 10;
	
	/**
	 * Field to hold the pendulum whose state we want to view
	 */
	private DoublePendulum dp;

	// CONSTRUCTOR
	
	/**
	 * Create a DoublePendulum viewer on top of the double pendulum
	 * state object passed as the argument to the constructor
	 * 
	 * @param dpview Double pendulum state to view
	 */
	public DoublePendulumViewer(DoublePendulum dpview) {
		dp = dpview;
		setOpaque(true);
		setBackground(Color.WHITE);
	}
	
	// PAINTING
	
	/**
	 *  A helper function to draw a filled circle of specified radius and centre
	 * 
	 * @param g current graphics context
	 * @param cx centre x position
	 * @param cy centre y position
	 * @param r radius
	 */
	private void drawCircle(Graphics g, double cx, double cy, double r) {
		g.fillOval((int)Math.round(cx-r), (int)Math.round(cy-r), (int)(2.0*r), (int)(2.0*r));
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
		
		// Set up the coordinate system; the idea is that the real-space point (x,y) maps to the onscreen point (ox+sx*x, oy+sy*y)
		// where (ox,oy) is the position of the origin, and sx, sy are scale factors

		// The origin goes at the centre; in Swing, the y coordinate increases from 0 at the top to height at the bottom
		double ox = (double)width/2.0, oy = (double)height/2.0;
		// Fit the full possible vertical length of the pendulum into the display
		// We need a minus sign here to take into account that a negative real-space coordinate is a positive component-space coordinate
		double sy = -(double)height/2.0/dp.getMaximumLength();
		// Set the x scaling to be the same as the y scaling - and correct for the minus sign
		double sx = -sy;
		
		// Get the real-space positions of the two bobs: we need to acquire the lock on the DoublePendulum object
		// to ensure that the state is consistent; we also notify any waiting threads that the data have been obtained
		Vector3d p1, p2;
		synchronized(dp) {
			
			// Uncomment the following line to help identify concurrency problems
//			System.out.println("Drawing position at t="+dp.getTime());
			
			p1 = dp.getPendulum1().getPosition();
			p2 = dp.getPendulum2().getPosition();
			
			// Uncomment the following line to help identify concurrency problems
//			System.out.println("Data received, t="+dp.getTime());
			
			dp.notifyAll();
		}
		
		// Draw lines from the origin to the first bob, and thence to the second bob
		g.setColor(Color.BLUE);
		g.drawLine((int)ox, (int)oy, (int)(ox+sx*p1.getX()), (int)(oy+sy*p1.getY()));
		g.drawLine((int)(ox+sx*p1.getX()), (int)(oy+sy*p1.getY()), (int)(ox+sx*p2.getX()), (int)(oy+sy*p2.getY()));
		
		// Now add some bobs
		g.setColor(Color.RED);
		drawCircle(g, ox+sx*p1.getX(), oy+sy*p1.getY(), BOB_RADIUS);
		drawCircle(g, ox+sx*p2.getX(), oy+sy*p2.getY(), BOB_RADIUS);
		
	}

	/**
	 * A basic main method for setting up an initial condition, iterating the equations of motion, and displaying a real-time visualisation of the system
	 * 
	 * @param args	command line arguments, currently ignored
	 */
	public static void main(String args[]) {
		
		// Create the default double-pendulum condition
		DoublePendulum dp = new DoublePendulum(
				new Particle3d(1.0, 0.0, new Vector3d(0.0,-0.2,0.0), new Vector3d(0.0,0.0,0.0)),
				new Particle3d(1.0, 0.0, new Vector3d(0.0,-0.4,0.0), new Vector3d(4.0,0.0,0.0))
			);
		
		// Set up a Frame (window) to open on the screen
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Double pendulum");
		frame.setPreferredSize(new Dimension(600,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a pendulum viewer, and place this within the frame
		DoublePendulumViewer view = new DoublePendulumViewer(dp);
		frame.getContentPane().add(view);
		
		// Lay out the frame's contents, and put it on screen
		frame.pack();
		frame.setVisible(true);
		
		// Do timesteps of 1e-7 second, and update the viewer every 1/100s; keep running forever
		
		for(;;) {
			dp.iterate((int)1e5, 1e-7);
			// Request repaint, and wait for it to actually have been done before proceeding
			synchronized(dp) {

				// Uncomment the following line to help identify concurrency problems
//				System.out.println("Request redraw at t="+dp.getTime());

				view.repaint();
				try {
					dp.wait();
				}
				catch (Exception ignore) { }
			}
		}
				
	}
	
}
