package SIRS.Controller;

import javax.swing.JFrame;

import SIRS.GraphicControls.InteractiveSIRSControlPanel;
import SIRS.GraphicControls.InteractiveSIRSOptionsPanel;
import SIRS.Model.*;
import SIRS.Model.Updater.Updater;
import SIRS.Viewers.InteractiveSIRSViewer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class InteractiveSIRS extends JFrame{
	
	private int N = 400;						//System Size
	private SIRS_System agents;					//The model the controller sits on top of
	private double [] prob = {0.5,0.5,0.5};		//Default probabilities
	private double immuneFraction = 0.0;
	private Updater updater = new Updater();	//The updater object
	
	private Thread background = null;
	
	InteractiveSIRSViewer viewer; //= new InteractiveSIRSViewer(this);
	InteractiveSIRSControlPanel controls; //= new InteractiveSIRSControlPanel(this);
	InteractiveSIRSOptionsPanel options; //= new InteractiveSIRSOptionsPanel(this);
	
	private boolean pause = false, reset = false; 	//booleans containing the current state of the simulation
	
	// CONSTRUCTor
	public InteractiveSIRS(){
		//Create the frame (window)
		super("SIRS Model");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		agents = new SIRS_System(N, prob);
		setImmuneFraction(immuneFraction);
		//Adding the JPanels for options, controls and the sim itself
		viewer = new InteractiveSIRSViewer(this);
		controls = new InteractiveSIRSControlPanel(this);
		options = new InteractiveSIRSOptionsPanel(this);
		getContentPane().add(viewer,BorderLayout.CENTER);
		getContentPane().add(controls,BorderLayout.SOUTH);
		getContentPane().add(options,BorderLayout.WEST);
		pack();
		setVisible(true);
	}
	
// ######################### METHODS #####################################
	
	public int getSystemSize(){return N;}
	
	public void setSystemSize(int input){
		//if(background != null) return;
		if(N>0){
			N = input;
			agents = new SIRS_System(N, prob);
		}
		viewer.setPreferredSize(new Dimension(N,N));
		viewer.repaint();
	}
	public Agent getAgent(int x,int y){
		synchronized(this){
			return agents.getAgent(x, y);
		}
	}
	public double getProb(int in){	return prob[in];}
	public void setProb(int n, double input){
		agents.setProb(n,input);
		prob[n] = input;
	}
	public int getSusceptible(){synchronized(agents){return agents.getSusceptible();}}
	public int getInfected(){synchronized(agents){return agents.getInfected();}}
	public int getRecovered(){synchronized(agents){return agents.getRecovered();}}
	public int getImmune(){synchronized(agents){return agents.getImmune();}}
	public double getImmuneFraction(){synchronized(agents){return agents.getImmuneFraction();}}
	
	public void setImmuneFraction(double fraction){
		immuneFraction = fraction;
		agents.setImmuneFraction(immuneFraction);
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
			agents = new SIRS_System(N, prob);
			setImmuneFraction(immuneFraction);
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
					updater.update(agents);
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
		System.out.println("Ready to restart");
	}
	
	
}//Class brackets
