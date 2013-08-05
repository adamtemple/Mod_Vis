/**
 * This is a class to encapsulate the state of the double pendulum and the algorithm for iterating the equations of motion.
 * 
 * Public methods allow: setting the initial condition (via the constructors), iterating the solver by a desired number of
 * timesteps, and reading off the current state of the system. These public methods reuse the existing Particle3d and
 * Vector3d classes from the Computational Methods course. Internally, however, polar coordinates are used - however
 * this is complexity that is hidden away in the private methods.
 * 
 * @author Richard Blythe
 *
 */
public class DoublePendulum {

	/**
	 * Physical constants - here, the acceleration due to gravity (this sets the unit of length)
	 * We make this public so that it is available to users of the class, but final so that it
	 * cannot be changed by them
	 */
	public static final double GRAVITY = 10.0;

	/**
	 * Dimensionality of our problem
	 */
	private static final int DIMENSION = 4;

	/**
	 * Fields for the state of our system
	 */
	private double length1, mass1; // length and mass of pendulum one
	private double length2, mass2; // length and mass of pendulum one
	private double time = 0.0; // time since initial condition

	/* Internally, we represent the state of the system as a four-dimensional array:
	 *  state[0] = angle of pendulum one
	 *  state[1] = angle of pendulum two
	 *  state[2] = angular frequency of pendulum one
	 *  state[3] = angular frequency of pendulum two
	 */
	private double[] state = new double[DIMENSION];

	/**
	 * Fields for parameters that appear in the equations of motion, but only need to be calculated once
	 */
	private double alpha, oneplusalpha, oneplusalphagamma, alphabeta, gamma, oneoverbeta;

	//   CONSTRUCTOR

	/**
	 * Specific the initial condition using two particle objects, one for each pendulum. 
	 * We simulate only a pendulum in the plane, so the z coordinates are ignored.
	 * Pendulum 1 is fixed to the origin, and pendulum 2 is fixed to pendulum 1.
	 * 
	 * @param p1 initial position, velocity and mass of pendulum 1
	 * @param p2 initial position, velocity and mass of pendulum 2
	 */
	public DoublePendulum(Particle3d p1, Particle3d p2) {
		// Get the distance between particle2 and particle1 as a vector
		Vector3d p12 = Particle3d.relativeSeparation(p1, p2);
		// Get the velocity of particle2 relative particle1 as a vector
		Vector3d v12 = Particle3d.relativeVelocity(p1, p2);

		mass1 = p1.getMass();
		mass2 = p2.getMass();

		// Compute squares of the lengths initially
		length1 = p1.getPosition().getX() * p1.getPosition().getX() + p1.getPosition().getY() * p1.getPosition().getY();
		length2 = p12.getX()*p12.getX() + p12.getY()*p12.getY();

		// angles
		state[0] = Math.atan2(p1.getPosition().getX(), -p1.getPosition().getY());
		state[1] = Math.atan2(p12.getX(), -p12.getY());

		// angular velocities ( r cross v dot k ) over l squared
		state[2] = (p1.getPosition().getX() * p1.getVelocity().getY() - p1.getPosition().getY() * p1.getVelocity().getX())/length1;
		state[3] = (p12.getX() * v12.getY() - p12.getY() * v12.getX())/length2;

		// Now take the square roots of the lengths
		length1 = Math.sqrt(length1);
		length2 = Math.sqrt(length2);

		// Precompute parameters that appear in the equations of motion
		alpha = mass2/mass1;

		double beta = length2/length1;
		gamma = GRAVITY/length1;
		oneplusalpha = 1.0 + alpha;
		oneplusalphagamma = oneplusalpha * gamma;
		alphabeta = alpha * beta;
		oneoverbeta = 1.0/beta;
	}

	//   EQUATIONS OF MOTION AND THE SOLVER

	/*
	 * Fields for some workspace, to save us recreating it in each iteration of the solver
	 */

	// Array for holding derivatives of the state variables evaluated at a specified location
	private double dydt[] = new double[DIMENSION];
	// Arrays for holding previous and intermediate positions used in the solver
	private double[] prev = new double[DIMENSION], midpt = new double[DIMENSION];

	/**
	 * Routine for evaluating the right-hand side of the set of equations dy[i]/dt = ...
	 * This loads the array dydt[] which can then be read from other methods after
	 * calling. t is the time point, y is the set of coordinates y[i] to evaluate the
	 * right-hand side at.
	 */
	private void evaluateDyDt(double t, double[] y) {
		dydt[0] = y[2]; dydt[1] = y[3];
		double s01 = Math.sin(y[0] - y[1]);
		double c01 = Math.cos(y[0] - y[1]);
		double nu1 = y[2]*y[2]*s01 - gamma * Math.sin(y[1]);
		double nu2 = oneplusalphagamma * Math.sin(y[0]) + alphabeta * y[3]*y[3]*s01;
		double f = 1.0/(1.0 + alpha*s01*s01);
		dydt[2] = - f*(nu2 + alpha*c01*nu1);
		dydt[3] = f*oneoverbeta*(oneplusalpha*nu1 + c01*nu2); 
	}

	/**
	 * Perform n timesteps, each of length h, of the integration algorithm.
	 * We use a 4th order Runge-Kutta algorithm here.
	 * 
	 * @param n number of timesteps to iterate
	 * @param h duration of each timestep
	 */
	public void iterate(int n, double h) {
		final double HALF = 0.5 * h, THIRD = h / 3.0, SIXTH = h / 6.0;
		for(int step=0; step<n; step++) {
			for(int i=0; i<DIMENSION; i++) midpt[i] = prev[i] = state[i];
			// Load dydt with the a vector
			evaluateDyDt(time, midpt);

			// Here we start updating state, so to make this thread-safe, we need to obtain a lock to continue

			synchronized(this) {
				// Add 1/6 a to state, and load midpt with the intermediate time point y + a/2
				for(int i=0; i<DIMENSION; i++) {
					state[i] += SIXTH * dydt[i]; 
					midpt[i] = prev[i] + HALF * dydt[i];
				}
				// Load dydt with the b vector
				evaluateDyDt(time + HALF, midpt);
				// Add 1/3 b to state, and load midpt with the intermediate time point y + b/2
				for(int i=0; i<DIMENSION; i++) {
					state[i] += THIRD * dydt[i];
					midpt[i] = prev[i] + HALF * dydt[i];
				}
				// Load dydt with the c vector
				evaluateDyDt(time + HALF, midpt);
				// Add 1/3 c to state, and load midpt with the final point y + c
				for(int i=0; i<DIMENSION; i++) {
					state[i] += THIRD * dydt[i];
					midpt[i] = prev[i] + h * dydt[i];
				}
				// Load dydt with the d vector
				evaluateDyDt(time, midpt);
				// Add 1/6 d to state
				for(int i=0; i<DIMENSION; i++) state[i] += SIXTH * dydt[i];
				// Increment the clock
				time += h;
			}
			// The lock is now released, and other threads are ok to read the state of the system
		}
	}

	//   ACCESSORS

	/**
	 * Get the position and velocity of the first pendulum as a Particle3d object
	 * 
	 * @return pendulum1 data
	 */	
	public Particle3d getPendulum1() {
		return new Particle3d(mass1, 0.0,
				new Vector3d(length1*Math.sin(state[0]), -length1*Math.cos(state[0]), 0.0), // Position
				new Vector3d(length1*state[2]*Math.cos(state[0]), length1*state[2]*Math.sin(state[0]), 0.0)   // Velocity
		);
	}

	/**
	 * Get the position and velocity of the second pendulum as a Particle3d object

	 * @return pendulum2 data
	 */	
	public Particle3d getPendulum2() {
		return new Particle3d(mass2, 0.0,
				new Vector3d(length1*Math.sin(state[0]) + length2*Math.sin(state[1]), -length1*Math.cos(state[0])-length2*Math.cos(state[1]), 0.0), // Position
				new Vector3d(length1*state[2]*Math.cos(state[0]) + length2*state[3]*Math.cos(state[1]), 
						length1*state[2]*Math.sin(state[0]) + length2*state[3]*Math.sin(state[1]), 0.0)		// Velocity
		);
	}

	/**
	 * Get the time that has elapsed since the start of the simulation
	 * 
	 * @return	time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Get the maximum distance of the second bob from the origin; handy for visualisation routines
	 * 
	 * @return maximum possible distance from origin to bob 2
	 */
	public double getMaximumLength() {
		return length1+length2;
	}


	//   MAIN METHOD

	/**
	 * A basic main method for setting up an initial condition, iterating the equations of motion, and writing the particle coordinates
	 * to the standard output. These data can be redirected to a file and viewed using Xmgrace.
	 * 
	 * @param args	command line arguments: if first
	 */
	public static void main(String args[]) {

		// Create a double-pendulum condition with shorter arc lengths than the default configuration
		// Let the transverse velocity of the lower pendulum be a parameter specified at the command line
		double tvel = 4.0;
		if(args.length > 0) {
			try {
				tvel = Double.parseDouble(args[0]); // If argument specified is not a number, this generates an error (exception)
			}
			catch(Exception ignore) {
				// Do nothing - just use the default velocity if an invalid argument is specified
			}
		}

		DoublePendulum dp = new DoublePendulum(
				new Particle3d(1.0, 0.0, new Vector3d(0.0,-0.2,0.0), new Vector3d(0.0,0.0,0.0)),
				new Particle3d(1.0, 0.0, new Vector3d(0.0,-0.4,0.0), new Vector3d(tvel,0.0,0.0))
		);

		// Do timesteps of 1/1000 second, printing out the ke, pe and total energy every 100 timesteps (1/10s) for 10s
		for(int i=0; i<100; i++) {
			dp.iterate(100, 0.001);
			Particle3d p1 = dp.getPendulum1(), p2 = dp.getPendulum2();
			double ke = p1.kineticEnergy() + p2.kineticEnergy();
			double pe = DoublePendulum.GRAVITY * ( p1.getMass() * p1.getPosition().getY() + p2.getMass() * p2.getPosition().getY() );
			System.out.println(dp.getTime() + " " + ke + " " + pe + " " + (ke+pe));
		}

	}
}
