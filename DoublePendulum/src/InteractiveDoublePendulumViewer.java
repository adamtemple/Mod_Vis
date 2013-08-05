import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * An interactive version of the pendulum viewer. This class interacts with a <code>DoublePendulum</code> object
 * indirectly via the controller object passed to the constructor when it is created.  It uses a <code>MouseListener</code>
 * to track mouse movements and thereby manipulation of the initial positions and velocities via methods provided by the
 * <code>InteractiveDoublePendulum</code> class.
 * 
 * @author Richard Blythe
 *
 */
public class InteractiveDoublePendulumViewer extends JComponent {
	
	/**
	 * Radius of each bob in the display
	 */
	private static final double BOB_RADIUS = 10;
		
	/**
	 * Width and height of velocity arrows
	 */
	private static final int VELOCITY_ARROW_WIDTH = 6;
	private static final int VELOCITY_ARROW_HEIGHT = 12;
	
	/**
	 * Physical size represented by the height of the window (in metres)
	 */
	private static final double REAL_HEIGHT = 1.0;
	
	/**
	 * Number of milliseconds between frames of an animation. 50ms = 20fps
	 */
	private static final int ANIMATION_DELAY = 50;

	/**
	 * Amount to scale velocities by to draw them in the display
	 */
	private static final double VELOCITY_FACTOR = 0.1;
	
	/**
	 * Field to hold the pendulum whose state we want to view
	 */
	private InteractiveDoublePendulum controller;
	
	/**
	 * A timer object to perform the automatic updating of the window while the simulation is running
	 */
	private Timer autoUpdater = new Timer(ANIMATION_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) { repaint(); }		
	});
	
	//  USER INTERACTION
	
	/**
	 * A hotspot is a circular draggable region, centred on x,y; radius r 
	 */
	private static class Hotspot {
		int x, y, r;
	}
	
	// We will always have 4 hotspots
	private Hotspot[] hotspot = new Hotspot[4];
	private int activeHotspot = -1; // which hotspot are we dragging? (-1 = none)
	private Point displacement = new Point(0,0); // displacement of active hotspot
		
	
	/**
	 * A class that observes mouse movements to allow the user to set the bob positions
	 */
	private MouseAdapter mouseFollower = new MouseAdapter() {
		private Point lastClick = null; // start of drag, if drag in progress

		@Override
		public void mousePressed(MouseEvent e) {
			lastClick = e.getPoint();
			// Process the hotspots in REVERSE order, so that he who is plotted last (frontmost) is detected first
			for(int i=hotspot.length-1; i>=0; i--) {
				int dx = lastClick.x - hotspot[i].x, dy = lastClick.y - hotspot[i].y;
				if(dx*dx + dy*dy <= hotspot[i].r*hotspot[i].r) {
					activeHotspot = i;
					displacement.x = displacement.y = 0;
					addMouseMotionListener(mouseFollower);
					break;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(activeHotspot == -1) return;
			Point p = e.getPoint();
			displacement.x = p.x - lastClick.x;
			displacement.y = p.y - lastClick.y;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(activeHotspot == -1) return;
			Point p = e.getPoint();
			displacement.x = p.x - lastClick.x;
			displacement.y = p.y - lastClick.y;
			hotspotDragEnded();
			removeMouseMotionListener(mouseFollower);
			activeHotspot = -1;
			repaint();
		}
		
	};

		
	// CONSTRUCTOR
	
	/**
	 * Create a DoublePendulum viewer on top of the double pendulum
	 * state object passed as the argument to the constructor
	 * 
	 * @param attachedTo the interactive double pendulum controller that we are attached to
	 */
	public InteractiveDoublePendulumViewer(InteractiveDoublePendulum attachedTo) {
		controller = attachedTo;
		setOpaque(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(600,600));
		// Set up hotspots
		for(int i=0; i<hotspot.length; i++) {
			hotspot[i] = new Hotspot();
		}
		// Listen for mouse events
		addMouseListener(mouseFollower);
	}
	
	// COORDINATE SYSTEM AND HOTSPOTS
	
	/**
	 * Position of origin (ox, oy) and scale factors (sx, sy) in transformation from real coordinates (x,y) to
	 * screen coordinates (ox+sx*x, oy+sy*y) 
	 */
	private double ox, oy, sx, sy;
	
	private void setupCoordinateSystem() {
		int width = getWidth(), height = getHeight();
		// Origin goes in the centre of the window
		ox = (double)width/2.0;
		oy = (double)height/2.0;
		// We need a minus sign here to take into account that a negative real-space coordinate is a positive component-space coordinate
		sy = -(double)height/REAL_HEIGHT;
		// Set the x scaling to be the same as the y scaling - and correct for the minus sign
		sx = -sy;
	}
	
	private void hotspotDragEnded() {
		// Use the displacement of the hotspot and current coordinate system to work out the new state to pass to the controller
		Particle3d[] state = controller.getPendulumState();
		if(activeHotspot<2) {
			// Position vector has changed
			Vector3d pos = state[activeHotspot].getPosition();
			pos.setX(pos.getX() + (double)displacement.x/sx);
			pos.setY(pos.getY() + (double)displacement.y/sy);
			controller.setPosition(activeHotspot, pos);
		}
		else {
			// Velocity vector has changed
			Vector3d vel = state[activeHotspot % 2].getVelocity();
			vel.setX(vel.getX() + (double)displacement.x/(VELOCITY_FACTOR * sx));
			vel.setY(vel.getY() + (double)displacement.y/(VELOCITY_FACTOR * sy));		
			controller.setVelocity(activeHotspot % 2, vel);
		}
		
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
	private void fillCircle(Graphics g, double cx, double cy, double r) {
		g.fillOval((int)Math.round(cx-r), (int)Math.round(cy-r), (int)(2.0*r), (int)(2.0*r));
	}
	
	/**
	 *  A helper function to draw an arrow pointing from (fx,fy) to (tx,ty) with a head of half-width w and length h
	 *  NB: a more sophisticated approach would involve the Graphics2d object
	 * 
	 * @param g		current graphics context
	 * @param fx	x-coordinate of line start (from-position)
	 * @param fy	y-coordinate of line start (from-position)
	 * @param tx	x-coordinate of line end   (to-position)
	 * @param ty	y-coordinate of line end   (to-position)
	 * @param w		half-width of arrow
	 * @param h		length (height) of arrow
	 */
	private void drawArrow(Graphics g, double fx, double fy, double tx, double ty, double w, double h) {
		g.drawLine((int)Math.round(fx), (int)Math.round(fy), (int)Math.round(tx), (int)Math.round(ty));
		double dx = tx-fx, dy = ty-fy;
		double length = dx*dx + dy*dy;
		if(length == 0) return;
		// Calculate vertices of arrowhead
		length = Math.sqrt(length);
		dx/=length; dy/=length;
		int xs[] = new int[]{ (int)Math.round(tx), (int)Math.round(tx-h*dx-w*dy), (int)Math.round(tx-h*dx+w*dy) };
		int ys[] = new int[]{ (int)Math.round(ty), (int)Math.round(ty-h*dy+w*dx), (int)Math.round(ty-h*dy-w*dx) };
		g.fillPolygon(xs, ys, 3);
	}
	
	/**
	 * This is the method in which all the drawing happens. It is called automatically by Swing when the component needs to
	 * be drawn or updated.
	 * 
	 * @param g current graphics context (an object that provides the drawing methods)
	 */
	protected void paintComponent(Graphics g) {

		// Clear the background if we are an opaque component
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		setupCoordinateSystem();
		
		// Get the most recent simulation state: all thread-safety issues are delegated to the controller object, so
		// we don't need any synchronized blocks here
		Particle3d[] state = controller.getPendulumState();
		
		// Convert particle to screen positions
		double p1x = ox + sx * state[0].getPosition().getX(), p1y = oy + sy * state[0].getPosition().getY();
		double p2x = ox + sx * state[1].getPosition().getX(), p2y = oy + sy * state[1].getPosition().getY();

		if(!controller.isRunning()) {
			// Compute hotspot positions (NOTE: this is slightly inefficient, only really need to do this when the state is updated)
			hotspot[0].x = (int)Math.round(p1x); hotspot[0].y = (int)Math.round(p1y); hotspot[0].r = (int)BOB_RADIUS;
			hotspot[1].x = (int)Math.round(p2x); hotspot[1].y = (int)Math.round(p2y); hotspot[1].r = (int)BOB_RADIUS;
			// If we happen to be dragging one of the particles, move it before drawing
			if(activeHotspot == 0) { p1x += displacement.x; p1y += displacement.y; }
			else if(activeHotspot == 1) { p2x += displacement.x; p2y += displacement.y; }
		}
		
		// Draw lines from the origin to the first bob, and thence to the second bob
		g.setColor(Color.BLUE);
		g.drawLine((int)Math.round(ox), (int)Math.round(oy), (int)Math.round(p1x), (int)Math.round(p1y));
		g.drawLine((int)Math.round(p1x), (int)Math.round(p1y), (int)Math.round(p2x), (int)Math.round(p2y));
		
		// Now add some bobs
		g.setColor(Color.RED);
		fillCircle(g, p1x, p1y, BOB_RADIUS);
		fillCircle(g, p2x, p2y, BOB_RADIUS);
		
		// Now draw velocity arrows if simulation is not running
		if(!controller.isRunning()) {
			double v1x = VELOCITY_FACTOR * sx * state[0].getVelocity().getX(), v1y = VELOCITY_FACTOR *  sy * state[0].getVelocity().getY();
			double v2x = VELOCITY_FACTOR * sx * state[1].getVelocity().getX(), v2y = VELOCITY_FACTOR *  sy * state[1].getVelocity().getY();
			hotspot[2].x = hotspot[0].x + (int)Math.round(v1x); hotspot[2].y = hotspot[0].y + (int)Math.round(v1y); hotspot[2].r = VELOCITY_ARROW_HEIGHT;
			hotspot[3].x = hotspot[1].x + (int)Math.round(v2x); hotspot[3].y = hotspot[1].y + (int)Math.round(v2y); hotspot[3].r = VELOCITY_ARROW_HEIGHT;
			// If we happen to be dragging one of the velocity arrows, move it before drawing
			if(activeHotspot == 2) { v1x += displacement.x; v1y += displacement.y; }
			else if(activeHotspot == 3) { v2x += displacement.x; v2y += displacement.y; }
			
			// Plot arrows
			g.setColor(Color.BLACK);
			drawArrow(g, p1x, p1y, p1x+v1x, p1y+v1y, VELOCITY_ARROW_WIDTH, VELOCITY_ARROW_HEIGHT);
			drawArrow(g, p2x, p2y, p2x+v2x, p2y+v2y, VELOCITY_ARROW_WIDTH, VELOCITY_ARROW_HEIGHT);
		}
				
	}

	//  MESSAGES
	
	/**
	 * This is received whenever the simulation displayed by this view has started running
	 */
	public void simulationStarted() {
		// Stop following the mouse while the simulation is running
		removeMouseListener(mouseFollower);
		// Ensure display is up-to-date
		repaint();
		// Send repaint messages at regular intervals using the autoUpdater Timer object
		autoUpdater.restart();
	}

	/**
	 * This is received whenever the simulation displayed by this view has stopped running
	 */
	public void simulationFinished() {
		// Stop sending regular repaint messages
		autoUpdater.stop();
		// Ensure display is up-to-date
		repaint();
		// Start following the mouse again
		addMouseListener(mouseFollower);
	}
	
}
