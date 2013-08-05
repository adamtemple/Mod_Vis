package MolecularDynamics.Integrator;

import MolecularDynamics.Model.ParticleSystem;

public abstract class Integrator {

	
	public Integrator(){}
	
	/**
	 * Method to integrate the system in time according
	 * to the chosen integration scheme
	 */
	public abstract void integrate(ParticleSystem particles);
	public abstract boolean doTempCoupling();


}


