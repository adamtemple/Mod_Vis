package MolecularDynamics.InteractiveControls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import MolecularDynamics.Controller.InteractiveParticleSystem;

public class InteractiveSystemControlPanel extends JPanel{

	private InteractiveParticleSystem controller;
	private JButton startPauseButton = new JButton("Start"),
					resetButton = new JButton("Reset");
	
	public InteractiveSystemControlPanel(InteractiveParticleSystem attachedTo){
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
}
