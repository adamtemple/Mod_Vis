package MolecularDynamics.Model.Potential;

import MolecularDynamics.Model.Particle;
import MolecularDynamics.Model.Vector;

public abstract class Potential {

	
	public Potential(){
		
	}

	/**
	 * Method which is implemented by subclasses to return the force
	 * between two particles in a specified box according to the 
	 * chosen potential
	 * @param a
	 * @param b
	 * @param box
	 * @return
	 */
	public abstract Vector calculateForce(Particle a, Particle b, double [] box);
	public abstract double calculatePotential(Particle a, Particle b, double [] box);
	public abstract boolean isVDW();
	public abstract boolean isWCA();
	public abstract boolean isPolymer();
}
