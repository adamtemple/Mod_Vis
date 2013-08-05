package MolecularDynamics.Model;


public class Particle {

	double mass, radius;
	Vector position, velocity, acceleration;
	double PE  = 0.0, KE =0.0;
	
	public Particle(double mass, double radius, Vector pos, Vector vel, Vector acc){
		this.mass = mass;
		this.radius = radius;
		position = pos;
		velocity = vel;
		setKE(0.5 * mass * vel.getMagSquared());
		acceleration = acc;
	}
	public Particle(double mass, double radius, double [] box){
		double [] temp = new double[box.length];
		for(int i=0;i<temp.length;i++){
			temp[i] = 0.0;
		}
		Vector init = new Vector(temp);
		this.mass = mass;
		this.radius = radius;
		position = init;
		velocity = init;
		acceleration = init;
	}
	

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getRadius() {
		return mass;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
		KE = 0.5 * mass * velocity.getMagSquared();
	}

	public Vector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}
	
	public void setForce(Vector force){
		acceleration = Vector.scalarProduct((1.0/mass), force);
	}
	public void addForce(Vector force){
		acceleration = Vector.addVector(acceleration, Vector.scalarProduct((1.0/mass),force));
	}
	public void restartForce(){
		double [] temp = new double[acceleration.getSize()];
		for(int i=0; i<temp.length; i++){
			temp[i] = 0.0;
		}
		acceleration = new Vector(temp);
		PE =0.0;
	}

	
	public double getKE(){
		return KE;
	}
	private void setKE(double in){
		KE = in;
	}
	public double getPE(){
		return PE;
	}
	public void addToPE(double in){
		PE += in;
	}
//	public void setKE(double in){
//		KE = in;
//	}
	
}
