package Exam.InteractiveControls;



import javax.swing.JPanel;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFormattedTextField;

import Exam.Controller.InteractivePotts;

public class PottsControlPanel extends JPanel{

	//Reference to the controller object that we are attached to
	private InteractivePotts controller;
	
	

	//User interface objects to show in the panel
	//Names are self explanatory
	private JButton startPauseButton = new JButton("Start");
	private JButton resetButton = new JButton("Reset");

	//CONSTRUCTOR
	
	
	public PottsControlPanel(InteractivePotts attachedTo){
		controller = attachedTo;
	
		//Attach event handlers to the required controls
		
		
		/**
		 * The start/stop button starts the simulation if it is not running
		 * and stops it if it is
		 */
	
		startPauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(controller.isRunning()) controller.pauseSimulation();
				else controller.startSimulation();
			}
		});
		resetButton.setEnabled(false);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(!controller.isRunning()) {
					controller.resetSimulation();
				}
			}
		});

		
		/**
		 * Add controls to the panel. The simplest layout will do for now
		 */
		
		add(startPauseButton);
		add(resetButton);
		
	}//Constructor brackets

	//MESSAGES
	
	/**
	 * This is recieved whenever the simulation displayed by this view has started running
	 * Details what each of the components should do when the simulation starts
	 * 
	 */
	public void simulationStarted(){
		startPauseButton.setText("Pause");
		resetButton.setEnabled(false);

	}
	
	/**
	 * This is recieved whenever the simulation is stopped
	 * Details what each of the components should do when the simulation stops
	 * 
	 */
	public void simulationFinished(){
		startPauseButton.setText("Start");
		resetButton.setEnabled(false);
	}
	
	/**
	 * This is recieved whenever the simulation is paused
	 * Details what each of the components should do when the simulation is paused
	 * 
	 */
	
	public void simulationPaused(){
	startPauseButton.setText("Resume");
	resetButton.setEnabled(true);
	}
	
	/**
	 * This is called whenever the user requests a reset in the simulation
	 * Details what to do to the buttons when the simulation is restarted
	 */
	public void simulationReset(){
		startPauseButton.setText("Start");
		resetButton.setEnabled(false);
	}
	
	
}//class brackets
