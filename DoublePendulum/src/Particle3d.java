/**
 * 
 * A partial implementation of the Particle3d class from the Computation Methods course.
 * A particle is an object with a vector-valued position and velocity, and scalar-valued
 * mass and charge.
 * 
 * We leave out the textfile parser and the leap routines, as we will not be using these
 * in the DoublePendulum project.
 * 
 * @author Richard Blythe
 *
 */

public class Particle3d {

	//  FIELDS

	private double mass;	// Particle mass
	private double charge;	// Particle charge

	Vector3d position;		// Particle position vector
	Vector3d velocity;		// Particle velocity vector


	//  CONSTRUCTORS

	/**
	 * The default constructor creates a particle at rest at the origin with no mass or charge
	 */
	public Particle3d() {
		setMass(0.0); 
		setCharge(0.0);
		setPosition(new Vector3d());
		setVelocity(new Vector3d());
	}

	/**
	 * The alternative constructor allows all particle parameters to be set
	 * 
	 * @param m	mass
	 * @param c	charge
	 * @param p	position
	 * @param v	velocity
	 */
	public Particle3d(double m,double c, Vector3d p, Vector3d v) {
		setMass(m);
		setCharge(c);
		setPosition(p);
		setVelocity(v);
	}


	//   ACCESSORS

	/**
	 * Get the particle's mass
	 * 
	 * @return mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * Set the particle's mass
	 * @param m new mass
	 */
	public void setMass(double m) {
		mass = m;
	}

	/**
	 * Get the particle's charge
	 * 
	 * @return charge
	 */
	public double getCharge() {
		return charge;
	}

	/**
	 * Set the particle's charge
	 * 
	 * @param c new charge
	 */
	public void setCharge(double c) {
		charge = c;
	}

	/**
	 * Get the particle's position as a vector
	 * 
	 * @return position
	 */
	public Vector3d getPosition() {
		return position;
	}

	/**
	 * Set the particle's position vector. Note: a reference to the Vector3d object is taken (the object is not copied)
	 * so if you subsequently change this vector, the particle's position will change too
	 * 
	 * @param p new position
	 */
	public void setPosition(Vector3d p) {
		position = p;
	}

	/**
	 * Get the particle's velocity as a vector
	 * 
	 * @return velocity
	 */
	public Vector3d getVelocity() {
		return velocity;

	}

	/**
	 * Set the particle's velocity vector. Note: a reference to the Vector3d object is taken (the object is not copied)
	 * so if you subsequently change this vector, the particle's velocity will change too
	 * 
	 * @param v new velocity
	 */
	public void setVelocity(Vector3d v) {
		velocity = v;
	}

	//  OTHER PARTICLE PROPERTIES

	/**
	 * Obtain the kinetic energy of the particle
	 * 
	 * @return kinetic energy
	 */
	public double kineticEnergy() { 
		// Kinetic Energy is 1/2 m v^2
		return 0.5*mass*Vector3d.dot(velocity,velocity); 
	}

	/**
	 * Provide a string representation of the particle object; this allows a particle
	 * to be used anywhere where a string is expected.
	 * 
	 * @return particle data as a string
	 */
	public String toString()
	{
		return "{x= " + position + ", v=" + velocity + ", m=" + mass + ", q=" + charge + "}";
	}

	//   STATIC METHODS FOR OPERATIONS INVOLVING MORE THAN ONE PARTICLE

	/**
	 * Obtain the position of particle B relative to particle A as a vector
	 * 
	 * @param A
	 * @param B
	 * @return relative distance from A to B
	 */
	static public Vector3d relativeSeparation(Particle3d A,Particle3d B)
	{
		return Vector3d.sub(B.getPosition(), A.getPosition());
	}

	/**
	 * Obtain the velocity of particle B relative to particle A as a vector
	 * 
	 * @param A
	 * @param B
	 * @return  velocity of B as seen by A
	 */
	static public Vector3d relativeVelocity(Particle3d A,Particle3d B)
	{
		return Vector3d.sub(B.getVelocity(), A.getVelocity());
	}

}
