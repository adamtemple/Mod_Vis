/**
 * This is an implementation of the Vector3d class that satifies the requirements of
 * Checkpoint 2 of the Computational Methods course.
 * 
 * It allows one to define and manipulate vectors in a three-dimensional space.
 * 
 * @author Richard Blythe
 *
 */
public class Vector3d {
	
	//  FIELDS
	
	// The x, y and z coordinates
	private double x, y, z;
	
	
	//  CONSTRUCTORS
	
	/**
	 * The default constructor creates a vector of zero length
	 */
	public Vector3d() {
		this(0.0, 0.0, 0.0);
	}
	
	
	/**
	 * This alternative constructor allows one to create a vector with
	 * arbitrary elements
	 * 
	 * @param x	x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	public Vector3d(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;		
	}
	
	//   ACCESSORS
	
	/**
	 * Set the x-coordinate of the vector
	 * 
	 * @param x new x-coordinate
	 */
	public void setX(double x) { this.x = x; }
		
	/**
	 * Set the y-coordinate of the vector
	 * 
	 * @param y new y-coordinate
	 */
	public void setY(double y) { this.y = y; }
	
	/**
	 * Set the z-coordinate of the vector
	 * 
	 * @param z new z-coordinate
	 */
	public void setZ(double z) { this.z = z; }
	
	/**
	 * Get the x-coordinate of the vector
	 * 
	 * @return x-coordinate
	 */
	public double getX() { return x; }
	
	/**
	 * Get the y-coordinate of the vector
	 * 
	 * @return y-coordinate
	 */
	public double getY() { return y; }
	
	/**
	 * Get the z-coordinate of the vector
	 * 
	 * @return z-coordinate
	 */
	public double getZ() { return z; }
	
	/**
	 * Get the square of the length of the vector
	 * 
	 * @return	square length of vector
	 */
	public double norm2() {
		return dot(this,this);
	}
	
	/**
	 * Get the length of the vector, i.e., sqrt(norm2)
	 * 
	 * @return	length of the vector
	 */
	public double norm() {
		return Math.sqrt(dot(this,this));
	}

	
	//  STATIC METHODS COMBINING PAIRS OF VECTORS, OR A SCALAR WITH A VECTOR
	
	/**
	 * Form the sum of two vectors
	 * 
	 * @param v first vector
	 * @param w second vector
	 * @return v plus w
	 */
	public static Vector3d add(Vector3d v, Vector3d w) {
		return new Vector3d(v.x+w.x, v.y+w.y, v.z+w.z);
	}

	/**
	 * Form the difference between two vectors
	 * 
	 * @param v first vector
	 * @param w second vector
	 * @return v minus w
	 */
	public static Vector3d sub(Vector3d v, Vector3d w) {
		return new Vector3d(v.x-w.x, v.y-w.y, v.z-w.z);
	}

	/**
	 * Form the dot product of two vectors
	 * 
	 * @param v	first vector
	 * @param w second vector
	 * @return v dot w
	 */
	public static double dot(Vector3d v, Vector3d w) {
		return v.x*w.x + v.y*w.y + v.z*w.z;
	}
	
	/**
	 * Form the cross product of two vectors
	 * 
	 * @param v	first vector in cross product
	 * @param w second vector in cross product
	 * @return v cross w
	 */
	public static Vector3d cross(Vector3d v, Vector3d w) {
		return new Vector3d(v.y*w.z - v.z*w.y, v.z*w.x - v.x*w.z, v.x*w.y - v.y*w.x);
	}
	
	/**
	 * Multiply a vector by a scalar
	 * 
	 * @param v vector to multiply
	 * @param s scalar to multiply by
	 * @return s times v
	 */
	public static Vector3d mult(Vector3d v, double s) {
		return new Vector3d(s*v.x, s*v.y, s*v.z);
	}
	
	/**
	 * Divide all components of a vector by a scalar
	 * 
	 * @param v vector to divide
	 * @param s factor to divide by
	 * @return (1/s) times v
	 */
	public static Vector3d divide(Vector3d v, double s) {
		return mult(v, 1.0/s);
	}

	/**
	 * Provide a string representation of the vector; this allows the vector
	 * to be used anywhere where a string is expected.
	 */
	@Override
	public String toString() {
		return "( " + x + ", " + y + ", " + z + " )";
	}

}
