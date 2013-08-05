package SIRS.GraphicControls;

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
import SIRS.Controller.InteractiveSIRS;
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
public class InteractiveSIRSControlPanel extends JPanel {

	private InteractiveSIRS controller;
	private JButton startPauseButton = new JButton("Start"),
					resetButton = new JButton("Reset");
	
	public InteractiveSIRSControlPanel(InteractiveSIRS attachedTo){
		controller = attachedTo;
		//CONTROLS
		
		//Stop/start buttons
		startPauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(controller.isRunning()) controller.pauseSimulation();
				else controller.startSimulation();
			}
		});
		resetButton.setEnabled(false);			//Done so the simulation cannot be reset before it has started
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(!controller.isRunning())controller.resetSimulation();
				
			}
		});
		add(startPauseButton);
		add(resetButton);
		
	}
	
//####################### Situational methods ######################
	//Used in the Controller object to notify the control panel of the state of the simulation
	public void simulationStarted(){
		startPauseButton.setText("Pause");
		resetButton.setEnabled(false);
	}
	public void simulationPaused(){
		startPauseButton.setText("Resume");
		resetButton.setEnabled(true);
	}
	
	public void simulationReset(){
		startPauseButton.setText("Start");
		resetButton.setEnabled(false);
		
	}
}//Class brackets
