package isingModel.Controller;


import isingModel.Analysis.IsingOutput;
import isingModel.InteractiveControls.IsingControlPanel;
import isingModel.InteractiveControls.IsingOptionsPanel;
import isingModel.Model.Ising;
import isingModel.Model.Dynamics.Dynamics;
import isingModel.Model.Dynamics.Glauber;
import isingModel.Model.Dynamics.Kawasaki;
import isingModel.Viewers.InteractiveIsingViewer;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;


public class InteractiveIsing extends JFrame {

	//Current simulation state
	private Ising ising = null;
	
	private int N = 200;	//Default System Size
	private double T = 1.0;	//Default System Temperature
	private Dynamics iterator;
	
	//These will be the control panel with buttons and the viewer itself
	private InteractiveIsingViewer viewer;
	private IsingControlPanel controls;
	private IsingOptionsPanel options;
	
	//File output object
	private IsingOutput output; 
		
	//Background thread to run the simulation in
	private Thread background = null;
	
	//Boolean to control whether to restart or resume the running simulation
	// and whether the simulation is paused
	//Probably already available with the background isRunning method but
	//Look at again
	private boolean reset = false;
	private boolean paused = false;
	private boolean doOutput = false;
	private boolean inEquilibrium = false;
	
	//Variable to keep track of the number of update iterations taken place
	//is reset when simulation is restarted
	private int time = 0;

	//Constructor
	
	/**
	 * Creates and displays the window contained the view of the system
	 * and controls that allow it to be manipulated by the user
	 */
	public InteractiveIsing(){
		//Create the frame (window)
		super("Ising Model");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		if(doOutput){
			try{
				output = new IsingOutput(this);
			}catch(IOException e){
				System.out.println("Failed to create output stream");
			}
		}
		ising = new Ising(N,T);
		iterator = Dynamics.createDynamics(ising,0);	//Defaulted to Glauber dynamics
		
		viewer = new InteractiveIsingViewer(this);
		controls = new IsingControlPanel(this);
		options = new IsingOptionsPanel(this);
		
		getContentPane().add(viewer, BorderLayout.CENTER);
		getContentPane().add(controls,BorderLayout.SOUTH);
		getContentPane().add(options,BorderLayout.WEST);
		pack();
		
		
		//Open the window
		setVisible(true);
		

		
		
		//
	    // Add window listener by implementing WindowAdapter class to the 
	    // frame instance. To handle the close event we just need to implement
	    // the windowClosing() method.
		// Handles the file output stream closing when the window is closed
	    //
	    addWindowListener(new WindowAdapter() {
	    	@Override
	        public void windowClosing(WindowEvent e) {
	    	
	    	if(inEquilibrium){
	    		ising.doFinalCalc();
	    	}
	    	if(doOutput) {
	    		output.closeOutput();	//Close the fileoutput stream
	    	}
	    	System.out.println("Chi: "+ising.getChi()+"\t"+"Cv: "+ising.getCv());			
	        System.exit(0);
	        }
	    });
		
	    
	}//Constructor brackets
	
	
	// Accessor methods, allow access to methods in the Ising class
	
	/**
	 * Obtain the current state of the simulation, should be done in thread-safe manner
	 * using synchronized to ensure consistency
	 */
	public void getIsingState(){
		//If the simulation is running, update local copy of the state of the system
		if(background != null){
			synchronized(ising){
				N = ising.getN();
				T = ising.getT();
			}
		}
	}
	
	public int getSystemSize(){
		return N;
	}
	
	public double getTemperature(){
		return T;
	}
	
	public double getChi(){
		return ising.getChi();
	}
	public double getCv(){
		return ising.getCv();
	}
	public double getEnergy(){
		return ising.getE();
	}
	public double getMagnetisation(){
		return ising.getM();
	}
	public double getAverageEnergy(){
		return ising.getAvE();
	}
	//Synchronized as called by viewer in paintComponent method
	public int getSpinState(int i, int j){
		synchronized(ising){
			return ising.getSpinState(i, j);
		}
	}
	
	public int getTime(){
		return time;
	}
	/**
	 * Set the temperature of the system
	 * Currently, this method only takes effect when the simulation is not running.
	 * Only accepts positive values for temperature
	 * @param temp Temperature
	 */
	public void setTemperature(double temp){
		//if(background != null) return;
		if(temp >= 0) {
			T = temp;
			ising.setT(T);
			System.out.println("Changing temperature to "+T);
		}
	}
	/**
	 * Set the size of the system
	 * @param input new size of system
	 */
	public void setSystemSize(int input){
		//if(background != null) return;
		if(N>0) {
			N = input;
			ising = new Ising(N,T);
			viewer.setPreferredSize(new Dimension(N,N));
			viewer.repaint();
		}
	}
	
	/**
	 * Method to set the dynamics used in the system. 1 picks Kawasaki, all over numbers picks Glauber
	 * @param in 1 for Kawasaki, all other ints for Glauber
	 */
	public void setDynamics(int in){
		//if(background != null) return; 		//Keeping this will not allow dynamics updating
		if(in == 1) iterator = new Kawasaki(ising);
		else iterator = new Glauber(ising);
	}
	/**
	 * Chose whether to do the file input or not. Also handles the exceptions associated with buffere creation
	 */
	public void setFileOutput(boolean in){
		doOutput = in;
		if(doOutput){
			try{
				output = new IsingOutput(this);
				System.out.println("Data output to file "+output.getFilename());
			}catch(IOException e){
				System.out.println("Failed to create output stream");
			}
		}
	}
	/**
	 * Manually signal if the system is in equilibrium
	 * @param in true if system is in equilibrium, false if not. Defaulted to false
	 */
	public void setInEquilibrium(boolean in){
		inEquilibrium = in;
		ising.setInEquilibrium(in);
		if(in)System.out.println("Calculating Equilibrium values from "+time);
		else System.out.println("Not calculating Equilibrium value");
	}
	/**
	 * Signal to carry out the final calculation for chi and cv
	 */
	public void doFinalCalc(){
		ising.doFinalCalc();
	}

	/**
	 * Method to set the seed for tha random number generator used in the iterator
	 * @param seed The seed for the iterator to repeat experiments
	 */
	
	public void setSeed(long seed){
		if(background != null) return;
		iterator.setSeed(seed);
	}
	
			
	/**
	 * Determine whether the simulation is running or not
	 * @return true if the simulation is running, false if it is not
	 */
	public boolean isRunning(){
		return background != null;
	}
	
	/**
	 * Determine whether the simulation is paused or not
	 * @return true if pause, false if not
	 */
	public boolean isPaused(){
		return paused;
	}
	//Method to tell the options panel the viewer is painting.
	//Allows updating of the sliders for probabilities
	//This is only called in repaint method
	public void painting(){
		options.painting();
	}
	
	//Actions
	/**
	 * Starts running a simulation instance.
	 * If there is already one running, it does nothing
	 */
	public void startSimulation(){
		//Return straight away if there is a background thread running already
		if(background != null) return;
		
		//Set up a new simulation with the desired initial conditions
		//These may have changed since the simulation last ran
		
		//If just paused or starting sim for first time then do not create a new instance of the ising class,
		//only update the temperature
		if(reset){
			ising = new Ising(N,T);
			time = 0;
		}
				
		//Run it in a new background thread
		background = new Thread(){
			@Override
			//This is what is run on the new thread that has been created
			public void run(){
				javax.swing.SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run(){
					options.simulationStarted();
					controls.simulationStarted();
					viewer.simulationStarted();
				}
			});
				
		//Repeatedly iterate the system using dynamics of choice until thread is interrupted
		//isInterrupted() is method for threads
		while(!isInterrupted()){
			
			iterator.update(ising);
			if(doOutput) output.doOutput();
			if(inEquilibrium && doOutput) output.doEquilibriumOutput();
			time++;
			//System.out.println("time = "+time);
			
		}
		
		//Update the local copy of the system with that of the ising object
		getIsingState();
		
		//Communicate with the view objects on the Swing thread
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				// Remove the reference to the background thread (note, we do this
				// on the Swing thread to avoid concurrency problems)
				background = null;
				//Inform the view objects the simulation has finished
				//TODO implement this
				if(reset){
					options.simulationReset();
					viewer.simulationFinished();
					controls.simulationFinished();
				}else{
					options.simulationPaused();
					viewer.simulationPaused();
					controls.simulationPaused();
				}
			}		
		});
	
	}
		
		};//thread brackets	
		background.start();
		
	}//startSimulation brackets
	
	/**
	 * What to do when the simulation stops
	 */
	public void stopSimulation(){
		//Checks to see if there is a background thread running
		if(background == null) return;
		//Send message to this background thread to stop it when it can
		background.interrupt();
		reset = true;
	}
	/**
	 * Method allows a pause in the simulation. The simulation can then be
	 * resumed or completely restarted
	 */
	public void pauseSimulation(){
		if(background == null) return;
		background.interrupt();
		reset = false;
		paused = true;
	}
	/**
	 * Method to reset the simulation. This generates a new simulation state and resets all control panels
	 * Some command line prompts are also given to outline what is going on
	 */
	public void resetSimulation(){
		reset = true;
		controls.simulationReset();
		System.out.println("Resetting simulation with new parameters N="+getSystemSize()+" and T="+getTemperature());
		startSimulation();
		background.interrupt();
		reset = false;
		System.out.println("Ready to restart");
		
	}

}//Class brackets
