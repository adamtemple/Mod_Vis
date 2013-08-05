package MolecularDynamics.Controller;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import MolecularDynamics.Integrator.Integrator;
import MolecularDynamics.InteractiveControls.InteractiveSystemOptionsPanel;
import MolecularDynamics.InteractiveControls.InteractiveSystemControlPanel;
import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Potential.Potential;
import MolecularDynamics.Viewers.InteractiveSystemViewer;


import java.awt.BorderLayout;


public class InteractiveParticleSystem extends JFrame{

	
	private int N;							//Particle Number
	private ParticleSystem particles;			//System
	private double [] box;						//Box size
	private Integrator integrator;				//Updater object
	private Potential potential;				//Current potential being used
	
	private Thread background = null;
	
	InteractiveSystemViewer viewer; 
	InteractiveSystemControlPanel controls; 
	InteractiveSystemOptionsPanel options; 
	
	private boolean pause = false, reset = false; 	//booleans containing the current state of the simulation
	
	// CONSTRUCTor
	public InteractiveParticleSystem(int N, double [] boxIn, Integrator in, Potential inPot){
		//Create the frame (window)
		super("Particle System");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		potential = inPot;
		setIntegrator(in);
		setBox(boxIn);
		setParticles(new ParticleSystem(N, box, inPot));
		//setPotential(inPot);
		N = particles.getParticleNumber();
		this.N = N;
		//Adding the JPanels for options, controls and the sim itself
		viewer = new InteractiveSystemViewer(this);
		controls = new InteractiveSystemControlPanel(this);//new InteractiveSIRSControlPanel(this);
		options = new InteractiveSystemOptionsPanel(this);
		getContentPane().add(viewer,BorderLayout.CENTER);
		getContentPane().add(controls,BorderLayout.SOUTH);
		getContentPane().add(options,BorderLayout.WEST);
		pack();
		setVisible(true);
	}
	
	public void setBox(double [] in){
		box = in;
	}
	public double[] getBox(){
		return box;
	}
	
	public void setIntegrator(Integrator in){
		integrator = in;
	}
	public Integrator getIntegrator(){
		return integrator;
	}
	
	public Potential getPotential(){
		return particles.getPotential();
	}
	public void setPotential(Potential in){
		particles.setPotential(in);
		potential = in;
	}
	
	public double getTemperature() {
		return particles.getTemperature();
	}

	public double getTargetTemperature() {
		return particles.getTargetTemperature();
	}

	public double getTime() {
		return particles.getTime();
	}
	
	public void setParticles(ParticleSystem particles) {
		this.particles = particles;
	}

	public ParticleSystem getParticles() {
		return particles;
	}

	//public void setSystemSize(int n) {
	//	N = n;
	//}

	public int getSystemSize() {
		return particles.getParticleNumber();
	}
	
	public void setVolumeFraction(double in){
		if(background != null) return;
		particles = ParticleSystem.setVolumeFraction(particles,in);
		N = particles.getParticleNumber();
		viewer.repaint();
	}
	
	public double getVolumeFraction() {
		return particles.getVolumeFraction();
	}
	/**
	 * Determine whether the simulation is running or not
	 * @return
	 */
	public boolean isRunning(){
		return background != null;
	}
	//Method to tell the options panel the viewer is painting.
	//Allows updating of the sliders for probabilities
	//This is only called in repaint method
	public void painting(){
		options.painting();
	}
	
// #####################  ACTIONS ####################################
	
	public void startSimulation(){
		//Return straight away if thread is already running
		if(background != null) return;
		//Create new instance of system, as may have changed
		if(reset){
			particles = new ParticleSystem(N, box, potential);
		}
		//Run in new thread
		background = new Thread(){
			@Override
			//What is to be run on the new thread
			public void run(){
				//Communicate to Swing thread
				javax.swing.SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run(){
						controls.simulationStarted();
						viewer.simulationStarted();
						options.simulationStarted();
					}
				});
		//Repeatedly iterate the system until thread is interrupted
				while(!isInterrupted()){
					integrator.integrate(particles);
				}
		//Communicate with the view objects on the Swing thread
				javax.swing.SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run(){
						// Remove the reference to the background thread (note, we do this
						// on the Swing thread to avoid concurrency problems)
						background = null;
						// Inform the view objects that the simulation has finished
						if(reset){
							viewer.simulationReset();
							controls.simulationReset();
							options.simulationReset();
						}else{
							viewer.simulationPaused();
							controls.simulationPaused();
							options.simulationPaused();
						}
					}
				});
			}
		};
		background.start();
	}//startSimulation brackets
	
	public void stopSimulation(){
		if(background == null) return;
		background.interrupt();
	}
	public void pauseSimulation(){
		if(background == null) return;
		background.interrupt();
		reset = false;
		pause = true;
	}
	public void resetSimulation(){
		reset = true;
		pause = false;
		startSimulation();
		background.interrupt();
		reset = false;
		System.out.println("Ready to restart");
	}





	
	
}//Class brackets
