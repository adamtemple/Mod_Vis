import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * The InteractiveDoublePendulum is a class that manages a fully interactive version of the double-pendulum simulation.
 * It is a controller object in the 'Model-View-Controller' pattern. It acts as a broker between the InteractiveDoublePendulumViewer
 * and DoublePendulumControlPanel (the views) that the user interacts with, and the DoublePendulum class (the model, borrowed
 * from the noninteractive versions of the simulation) that handles the actual simulation of the physics  
 * 
 * @author Richard Blythe
 *
 */

public class InteractiveDoublePendulum extends JFrame {

	// Current simulation state
	private DoublePendulum dp = null;
	
	// Timestep for RK algorithm
	private double timeStep = 1e-7;

	// A local copy of the simulation state: this is needed to avoid concurrency problems
	private Particle3d state[] = new Particle3d[2];
	
	// The user interface objects we interact with
	private InteractiveDoublePendulumViewer viewer = new InteractiveDoublePendulumViewer(this);
	private DoublePendulumControlPanel controls = new DoublePendulumControlPanel(this);
	
	// Background thread to run the simulation in
	private Thread background = null;

	
	//  CONSTRUCTOR
	
	/**
	 * This constructor creates and displays the window containing the view of the DoublePendulum
	 * and controls that allow it to be manipulated by the user
	 */
	public InteractiveDoublePendulum() {
		// Create the frame (window)
		super("Interactive Double Pendulum");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Create a default initial condition
		state[0] = new Particle3d(1.0, 0.0, new Vector3d(0.0,-0.2,0.0), new Vector3d(0.0,0.0,0.0) );
		state[1] = new Particle3d(1.0, 0.0, new Vector3d(0.0,-0.4,0.0), new Vector3d(4.0,0.0,0.0));
				
		// Lay out the viewer and controls
		getContentPane().add(viewer, BorderLayout.CENTER);
		getContentPane().add(controls, BorderLayout.SOUTH);
		pack();

		// Open the window
		setVisible(true);
				
		// Everything now happens at the behest of the user: other methods in this class will be called
		// as the user clicks buttons etc - we don't need to do anything else now
	}
	
	//  ACCESSORS
	
	/**
	 * Obtain the current state of the simulation. Note, this is done in a thread-safe way, so the
	 * state returned will be consistent
	 * 
	 * @return the masses, positions and velocities of the pendulum bobs as an array
	 * 
	 */
	public Particle3d[] getPendulumState() {
		// If the simulation is running, we will need to update our local copy of the state
		if(background != null) {
			// Obtain the lock on the DoublePendulum object to ensure the state is consistent
			synchronized(dp) {
				state[0] = dp.getPendulum1();
				state[1] = dp.getPendulum2();
			}
		}
		return state;
	}
	
	/**
	 * Set the mass of one of the particles in the initial condition of the next run started by the startSimulation() method
	 * This method only has effect when the simulation is not running
	 * 
	 * @param particle	particle number (1=upper, 2=lower)
	 * @param mass		mass
	 */
	public void setMass(int particle, double mass) {
		if(background != null) return;
		if(particle>=0 && particle<state.length && mass>=0.0) state[particle].setMass(mass);
	}
	
	/**
	 * Set the position of one of the particles in the initial condition of the next run started by the startSimulation() method
	 * This method only has effect when the simulation is not running
	 * 
	 * @param particle	particle number (1=upper, 2=lower)
	 * @param pos		position
	 */
	public void setPosition(int particle, Vector3d pos) {
		if(background != null) return;
		if(particle>=0 && particle<state.length) state[particle].setPosition(pos);
	}
	
	/**
	 * Set the velocity of one of the particles in the initial condition of the next run started by the startSimulation() method
	 * This method only has effect when the simulation is not running
	 * 
	 * @param particle	particle number (1=upper, 2=lower)
	 * @param vel		velocity
	 */
	public void setVelocity(int particle, Vector3d vel) {
		if(background != null) return;
		if(particle>=0 && particle<state.length) state[particle].setVelocity(vel);
	}
	
	/**
	 * Set the timestep for each iteration of the RK algorithm
	 * 
	 * @param step desired timestep
	 */
	public void setTimeStep(double step) {
		timeStep = step;
	}
	
	/**
	 * Get time step
	 * 
	 * @return current time step
	 */
	public double getTimeStep() {
		return timeStep;
	}
	
	/**
	 * Determine whether the simulation is running or not
	 * 
	 * @return true if simulation running, false otherwise
	 */
	public boolean isRunning() {
		return background != null;
	}
	
	//  ACTIONS
	
	/** 
	 * Start running a simulation in the background. Does nothing if the simulation is already running.
	 */
	public void startSimulation() {
		// Return immediately if there is a background thread running
		if(background != null) return;
		// Set up a new simulation with the desired initial condition (since this may be different from the state in which the last simulation ended)
		dp = new DoublePendulum(state[0], state[1]);
		// Run it in a new background thread
		background = new Thread() {
			@Override
			public void run() {

				// Communicate with the view objects on the Swing thread
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// Inform the view objects that the simulation has started
						controls.simulationStarted();
						viewer.simulationStarted();
					}
				});

				// Repeatedly iterate the equations of motion until the thread is interrupted
				while(!isInterrupted()) {
					dp.iterate((int)1e5, timeStep);
				}
				// Update the local copy of the state with that of the DoublePendulum object
				getPendulumState();
				
				// Communicate with the view objects on the Swing thread
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// Remove the reference to the background thread (note, we do this
						// on the Swing thread to avoid concurrency problems)
						background = null;
						// Inform the view objects that the simulation has finished
						viewer.simulationFinished();
						controls.simulationFinished();
					}
				});
				
			}
		};
		background.start();
	}
	
	public void stopSimulation() {
		// Check the a background thread is actually running
		if(background == null) return;
		// Send the interrupt message to the background thread to stop it at a convenient point
		background.interrupt();
	}
	
	
	//  MAIN METHOD
	
	/**
	 * Creates and runs the interactive simulation
	 * 
	 * @param args command-line arguments, ignored in the interactive simulation since everything is set up via the user interface
	 */
	public static void main(String args[]) {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		// We need to switch all further execution to the Swing thread, as everything is now at the control of the user through the Swing interface
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InteractiveDoublePendulum();
			}
		});
		
	}
	
}
