package MolecularDynamics.Model.Potential;

import MolecularDynamics.Model.Constants;
import MolecularDynamics.Model.Particle;
import MolecularDynamics.Model.Vector;

public class PolymerInteraction extends Potential{

	
	
	public PolymerInteraction(){}
	
	//Required for options panel
	public boolean isVDW(){
		return false;
	}
	public boolean isWCA(){
		return false;
	}
	public boolean isPolymer(){
		return true;
	}
	
	public Vector calculateForce(Particle a, Particle b, double[] box) {
		
		Vector dist = Vector.calcParticleDistance(a.getPosition(), b.getPosition(), box); //Method includes minimum image
		double distMag = dist.getMag();
	
		double r2 = distMag*distMag;
		double r8 = r2*r2*r2*r2;
		double r14 = (r8*r8) / r2;
		
		
		//VDW component of force
		double force1 = -24.0 * Constants.epsilon * ( (Constants.sigma6/r8) - (2.0 * Constants.sigma12/r14) ); //using full distance vector rather than unit to get even powers

		double force2 =  Constants.polymerK / (1 - r2 / (Constants.polymerR *Constants.polymerR) );
		double force = force1 + force2;
		
		return Vector.scalarProduct(force,dist);	//Done as force has been modified to use even powers and avoid use of unit vector
	}
	
	public double calculatePotential(Particle a, Particle b, double[] box){
		Vector dist = Vector.calcParticleDistance(a.getPosition(), b.getPosition(), box); //Method includes minimum image
		double distMag = dist.getMag();
		double r2 = distMag*distMag;
		double r6 = r2*r2*r2;
		double r12 = r6 * r6;
		double potential1 = 4.0 * Constants.epsilon * ( (Constants.sigma12/r12) - (Constants.sigma6/r6) );
		
		double R2 = Constants.polymerR * Constants.polymerR;
		
		double potential2 = -(  (Constants.polymerK * R2) / 2.0 ) * Math.log(1 - r2 / R2 ); 
		
		double potential = potential1 + potential2;
		
		return potential;
	}

}
