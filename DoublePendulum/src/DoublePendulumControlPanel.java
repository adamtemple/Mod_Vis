import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A control panel that provides controls for the user to set the mass ratio, change the integration timestep
 * and start and stop the simulation.
 * 
 * These controls call methods in the controller object of type <code>InteractiveDoublePendulum</code> which
 * is expected to perform the appropriate action.
 * 
 * @author Richard Blythe
 *
 */
public class DoublePendulumControlPanel extends JPanel {
	
	// A reference to the controller object that we are attached to
	private InteractiveDoublePendulum controller;
	
	// Number formatters to display currently set values of parameters in a sensible way
	private DecimalFormat roundToTwoDP = new DecimalFormat("0.00");
	private DecimalFormat scientificFormat = new DecimalFormat("0.##E0");
	
	// User interface objects to show in the panel
	private JLabel massRatioLabel = new JLabel("Mass ratio (1.00)");
	private JSlider massRatioSlider = new JSlider(0, 50, 10);  // Select second mass from 0kg to 5kg in units of 0.1kg
	private JLabel timeStepLabel = new JLabel("Time step");
	private NumberField timeStepField = new NumberField(1.0, scientificFormat);
	private JButton startStopButton = new JButton("Start");

	//   CONSTRUCTOR
	
	/**
	 * Create a control panel to start/stop the simulation and alter the mass ratio and integration timestep
	 * 
	 * 
	 * @param attachedTo controller object to interact with
	 */
	public DoublePendulumControlPanel(InteractiveDoublePendulum attachedTo) {
		controller = attachedTo;
				
		// Attach event handlers to the controls
		
		// The start/stop button starts the simulation if it is not running, and stops it if it is
		startStopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controller.isRunning()) controller.stopSimulation();
				else controller.startSimulation();
			}			
		});
		
		// The mass ratio slider sets the mass of the second bob
		massRatioSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Convert the slider position to the appropriate decimal value
				double val = (double)massRatioSlider.getValue()/(double)10;
				// set the mass ratio
				controller.setMass(1, val);
				// update the label to reflect the change
				massRatioLabel.setText("Mass ratio (" + roundToTwoDP.format(val) + ")");
			}
		});
		
		// The time step field allows the length of the time step to be altered
		timeStepField.setValue(controller.getTimeStep());
		timeStepField.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Note, we need to cast the generic Number object to a Double here
				controller.setTimeStep((Double)timeStepField.getValue());
			}
		});
		
		// Add controls to the panel - the simplest layout (FlowLayout) will do for us here
		// NOTE: if you need a more complicated layout, look into the GridBagLayout class
		add(massRatioLabel);
		add(massRatioSlider);
		add(timeStepLabel);
		add(timeStepField);
		add(startStopButton);
	}

	//  MESSAGES
	
	/**
	 * This is received whenever the simulation displayed by this view has started running
	 */
	public void simulationStarted() {
		// Change the start/stop button to reflect the fact it now stops the simulation
		startStopButton.setText("Stop");
		// Dim the other controls
		massRatioSlider.setEnabled(false);
		timeStepField.setEnabled(false);
	}

	/**
	 * This is received whenever the simulation displayed by this view has stopped running
	 */
	public void simulationFinished() {
		// Change the start/stop button to reflect the fact it now starts the simulation
		startStopButton.setText("Start");
		// Enable the other controls
		massRatioSlider.setEnabled(true);
		timeStepField.setEnabled(true);
	}
	
}
