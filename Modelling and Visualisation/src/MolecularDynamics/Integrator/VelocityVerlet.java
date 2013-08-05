package MolecularDynamics.Integrator;

import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Vector;

public class VelocityVerlet extends Integrator{

	
	public VelocityVerlet(){}

	public boolean doTempCoupling(){
		return false;
	}
	
	/**
	 * Method which is actually used to carry out the integration of the particle system in time
	 */
	public void integrate(ParticleSystem particles) {
		int N = particles.getParticleNumber();
		
		double dt = particles.getTimestep();
		double dt2 = dt*dt;
		
		updatePositions(particles, N, dt, dt2);
		updateVelocities(particles, N, dt, dt2);
		particles.calculateSystemForces();
		updateVelocities(particles, N, dt, dt2);
		
		particles.iterateTime();
		particles.updateCurrent_Temperature_KE();
	}
	
	private void updatePositions(ParticleSystem particles, int N, double dt, double dt2){
		for(int i=0; i<N; i++){
			
			Vector velTerm = Vector.scalarProduct(dt, particles.getParticle(i).getVelocity());
			Vector accTerm = Vector.scalarProduct( (dt2 / 2.0), particles.getParticle(i).getAcceleration());
			
			Vector combined = Vector.addVector(velTerm, accTerm);
		
			particles.getParticle(i).setPosition(Vector.addVector(particles.getParticle(i).getPosition(),combined));
			particles.doPBC(i); //incase the particles have moved outside the box
	
		}
	}
	
	
	private void updateVelocities(ParticleSystem particles, int N, double dt, double dt2){
		for(int i=0; i<N; i++){
			
			Vector accTerm = Vector.scalarProduct(dt/ 2.0, particles.getParticle(i).getAcceleration());
			
			Vector combined = Vector.addVector(particles.getParticle(i).getVelocity(), accTerm);
			
			particles.getParticle(i).setVelocity(combined);	//Set velocity updates the kinetic energy of each particle
		
		}
		
		
	}


}
