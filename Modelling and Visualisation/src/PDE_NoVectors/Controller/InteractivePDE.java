package PDE_NoVectors.Controller;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import PDE_NoVectors.InteractiveControls.InteractiveSystemControlPanel;
import PDE_NoVectors.InteractiveControls.InteractiveSystemOptionsPanel;
import PDE_NoVectors.Iterator.*;
import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Viewers.InteractiveSystemViewer;


public class InteractivePDE extends JFrame{


	
	private int N;							//Particle Number
	private Box space;
	private Iterator iterator;
	private int iterations = 0;
	
	private Thread background = null;
	
	InteractiveSystemViewer viewer; 
	InteractiveSystemControlPanel controls; 
	InteractiveSystemOptionsPanel options; 
	
	private boolean pause = false, reset = false; 	//booleans containing the current state of the simulation
	
	// CONSTRUCTor
	/*
	public InteractivePDE(double x, double y, double z){
		//Create the frame (window)
		super("PDE Solver");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		iterations = 0;
		setN();
		space = new Box(x,y,z);
		iterator = new Jacobi(space); 		//Jacobi by default
		
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
	*/
	public InteractivePDE(int in){
		//Create the frame (window)
		super("PDE Solver");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		iterations = 0;
		space = new Box(in, in, in);
		N = in;
			//CANNOT USE SETSYSTEM here as viewer has not been initialised
		space.setCentralUnitCharge();
		
		iterator = new GaussSeidel(space); 		//Jacobi by default
		
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
	public void setSystem(int in){
		space = new Box(in, in, in);
		N = in;
		space.setCentralUnitCharge();
		//space.setTwoCentralWires();
		iterator.updateSpace(space);
		iterations = 0;
		viewer.repaint();	//repaint the visualisation
		options.painting();	//repaint the options panel
	}
	
	public void setN(int in){
		setSystem(in);
	}
	
	public int getN(){
		return N;
	}
	public void setIterator(Iterator iterator){
		this.iterator = iterator;
	}
	public Iterator getIterator(){
		return iterator;
	}
	public Box getSpace(){
		return space;
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
			setSystem(N);
			
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
				while(!isInterrupted() && !iterator.isConverged()){		//Allows interruption or stops when converged
					iterator.iterateScalarPotential();
					iterations++;
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

	public int getIterations() {
		return iterations;
	}
	
	
}
