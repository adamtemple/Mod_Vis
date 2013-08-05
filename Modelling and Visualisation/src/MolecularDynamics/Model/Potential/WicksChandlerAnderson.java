package MolecularDynamics.Model.Potential;

import MolecularDynamics.Model.Constants;
import MolecularDynamics.Model.Particle;
import MolecularDynamics.Model.Vector;

public class WicksChandlerAnderson extends Potential{
	
	//Constructor
	public WicksChandlerAnderson(){};
	
	//Required for options panel
	public boolean isVDW(){
		return false;
	}
	public boolean isWCA(){
		return true;
	}
	public boolean isPolymer(){
		return false;
	}
	
	public Vector calculateForce(Particle a, Particle b, double[] box) {
		
		
		Vector dist = Vector.calcParticleDistance(a.getPosition(), b.getPosition(), box); //Method includes minimum image
		double distMag = dist.getMag();
		
	
		double force = 0.0;
			//Do WCA potential
			if(distMag < Constants.r_cutoff){
				double r2 = distMag*distMag;
				double r8 = r2*r2*r2*r2;
				double r14 = (r8*r8) / r2;
				force = -24.0 * Constants.epsilon * ( (Constants.sigma6/r8) - (2.0 * Constants.sigma12/r14) ); //using full distance vector rather than unit to get even powers

			}
	return Vector.scalarProduct(force,dist);	//Done as force has been modified to use even powers and avoid use of unit vector
	}
	
	public double calculatePotential(Particle a, Particle b, double[] box){
		Vector dist = Vector.calcParticleDistance(a.getPosition(), b.getPosition(), box); //Method includes minimum image
		double distMag = dist.getMag();
		
		double potential = 0.0;
		if(distMag < Constants.r_cutoff){
			double r2 = distMag*distMag;
			double r6 = r2*r2*r2;
			double r12 = r6 * r6;
		potential = 4.0 * Constants.epsilon * ( (Constants.sigma12/r12) - (Constants.sigma6/r6) ) + Constants.epsilon;
		}
		return potential;
	}

}//Class Brackets
