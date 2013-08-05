package MolecularDynamics.Integrator;

import MolecularDynamics.Model.Constants;
import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Vector;

public class VelocityVerlet_NoseHoover extends Integrator{
	
	double noseTerm = 0.0;
	double relaxationTerm =  0.1;
	public VelocityVerlet_NoseHoover(){}
	
	public boolean doTempCoupling(){
		return true;
	}
	
		public void integrate(ParticleSystem particles) {
			int N = particles.getParticleNumber();
			
			double dt = particles.getTimestep();
			double dt2 = dt*dt;
			
			updatePositions(particles, N, dt, dt2);	//Also updates NoseTerm
			updateVelocities1(particles, N, dt, dt2);	//Updates NoseTerm for a second time
			particles.calculateSystemForces();
			updateVelocities2(particles, N, dt, dt2);	//Uses NoseTerm as it should for the last step
			
			particles.iterateTime();
			particles.updateCurrent_Temperature_KE();
		}
		
		private void updatePositions(ParticleSystem particles, int N, double dt, double dt2){
			double sumForNose = 0.0;
			for(int i=0; i<N; i++){
				
				Vector velTerm = Vector.scalarProduct(dt, particles.getParticle(i).getVelocity());
				Vector accTerm = Vector.scalarProduct( (dt2 / 2.0), particles.getParticle(i).getAcceleration());
				Vector noseAccTerm = Vector.scalarProduct( (dt2 / 2.0),  Vector.scalarProduct(noseTerm, particles.getParticle(i).getVelocity() ));
				
				Vector totalAccTerm = Vector.addVector(accTerm, Vector.scalarProduct(-1.0, noseAccTerm));
				Vector combined = Vector.addVector(velTerm, totalAccTerm);
			
				particles.getParticle(i).setPosition(Vector.addVector(particles.getParticle(i).getPosition(),combined));
				particles.doPBC(i); //incase the particles have moved outside the box
				
				sumForNose += particles.getParticle(i).getKE();
			}
			//Update the nose coupling factor
			noseTerm = noseTerm +( ( dt / (2.0 * relaxationTerm))  *  
					( sumForNose - (0.5 * particles.getBox().length * (N + 1) * particles.getTargetTemperature() * Constants.boltzmannK)) );
				
		}
		
		
		private void updateVelocities1(ParticleSystem particles, int N, double dt, double dt2){
			double sumForNose = 0.0;
			for(int i=0; i<N; i++){
				
				Vector accTerm1 = Vector.scalarProduct(dt/ 2.0, particles.getParticle(i).getAcceleration());
				Vector accTerm2 = Vector.scalarProduct( ( (dt/2.0) * noseTerm ) , particles.getParticle(i).getVelocity() );
				
				Vector combined = Vector.addVector(accTerm1, Vector.scalarProduct(-1.0, accTerm2));
				Vector newVelocity = Vector.addVector(particles.getParticle(i).getVelocity(), combined);
				particles.getParticle(i).setVelocity(newVelocity);	//Set velocity updates the kinetic energy of each particle
			
				sumForNose += particles.getParticle(i).getKE();
			}
			//Update the nose Coupling factor
			noseTerm = noseTerm +( ( dt / (2.0 * relaxationTerm))  *  
					( sumForNose - (0.5 * particles.getBox().length * (N + 1) * particles.getTargetTemperature() * Constants.boltzmannK)) );
			
		}

		
		private void updateVelocities2(ParticleSystem particles, int N, double dt, double dt2){
			for(int i=0; i<N; i++){
				
				Vector accTerm = Vector.scalarProduct(dt/ 2.0, particles.getParticle(i).getAcceleration());
				double noseScalingTerm = 1.0 /(1.0 + (dt / 2.0)*noseTerm );
				
				Vector newVelocity = Vector.scalarProduct(noseScalingTerm, Vector.addVector(particles.getParticle(i).getVelocity(), accTerm));
				particles.getParticle(i).setVelocity(newVelocity);	//Set velocity updates the kinetic energy of each particle
			}
		}
		
		public double getNoseTerm() {
			return noseTerm;
		}

		public void setNoseTerm(double noseTerm) {
			this.noseTerm = noseTerm;
		}

		public double getRelaxationTerm() {
			return relaxationTerm;
		}

		public void setRelaxationTerm(double relaxionTerm) {
			this.relaxationTerm = relaxionTerm;
		}

		
		
}
