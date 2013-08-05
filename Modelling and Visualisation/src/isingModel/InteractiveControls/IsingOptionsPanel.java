package isingModel.InteractiveControls;

import isingModel.Controller.InteractiveIsing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;


public class IsingOptionsPanel extends JPanel{

	//Reference to the controller object that we are attached to
	private InteractiveIsing controller;
	
	
	private NumberFormat temperatureFormat = NumberFormat.getNumberInstance();
	private NumberFormat sizeFormat = NumberFormat.getIntegerInstance();
	
	//User interface objects to show in the panel
	//Names are self explanatory
	private JFormattedTextField systemSizeInput = new JFormattedTextField(sizeFormat);
	private JLabel systemSizeLabel = new JLabel("System size (NxN)");
	private JFormattedTextField temperatureInput = new JFormattedTextField(temperatureFormat);
	private JLabel temperatureLabel = new JLabel("Temperature");
	
	private JRadioButton glauberButton = new JRadioButton("Glauber");
	private JRadioButton kawasakiButton = new JRadioButton("Kawasaki");
	private ButtonGroup group1 = new ButtonGroup();
	private JLabel DynamicsLabel = new JLabel("Dynamics");
	
	private JRadioButton fileOutNo = new JRadioButton("No");
	private JRadioButton fileOutYes = new JRadioButton("Yes");
	private ButtonGroup group2 = new ButtonGroup();
	private JLabel fileOutLabel = new JLabel("File Output");
	
	private JToggleButton chiButton = new JToggleButton("In Equilibrium");

	private JLabel totalE, totalM;

	public IsingOptionsPanel(InteractiveIsing attachedTo){
		controller = attachedTo;
		
		//Attach event handlers to the required controls
		totalE = new JLabel("E : "+controller.getEnergy());
		totalM = new JLabel("M : "+controller.getMagnetisation());
		
		/**
		 * Set up of the field to edit the system size
		 */
		//Set this to display the system size
		systemSizeInput.setColumns(5);
		systemSizeInput.setHorizontalAlignment(JTextField.CENTER);
		systemSizeInput.setValue(new Integer(controller.getSystemSize()));
		//Change the value held here when the text changes
		systemSizeInput.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				int size = ((Number)systemSizeInput.getValue()).intValue();
				if(size <= 0){
					size = controller.getSystemSize(); //If good value not input, revert to current system size
					systemSizeInput.setValue(new Integer(controller.getSystemSize()));
				}
				controller.setSystemSize(size);
			}
		});
		
		/**
		 * Set up of the field to edit the temperature of the system
		 */
		//Set this to display the system size
		temperatureInput.setColumns(5);
		temperatureInput.setHorizontalAlignment(JTextField.CENTER);
		temperatureInput.setValue(new Double(controller.getTemperature()));
		
		//Change the value held here when the text changes
		temperatureInput.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				double temp = ((Number)temperatureInput.getValue()).doubleValue();
				if(temp < 0.0){
					temp = controller.getTemperature();
					temperatureInput.setValue(new Double(controller.getTemperature()));
				}
				controller.setTemperature(temp);
			}
		});
		
		/**
		 * Code to set up the choice of dynamics via buttons
		 */
		//Group together the radio buttons so only one can be selected
		group1.add(glauberButton);
		group1.add(kawasakiButton);
		//Default is to use is glauber
		glauberButton.setSelected(true);
		glauberButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
					controller.setDynamics(0);
			}
		});
		kawasakiButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
					controller.setDynamics(1);
				
			}
		});
		
		/**
		 * Code to set up the choice of whether to do file output via buttons
		 */
		group2.add(fileOutNo);
		group2.add(fileOutYes);
		fileOutNo.setSelected(true);
		fileOutYes.setSelected(false);
		fileOutYes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(!controller.isRunning()){
					controller.setFileOutput(true);
				}
			}
		});
		fileOutNo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(!controller.isRunning()){
					controller.setFileOutput(false);
				}
			}
		});
	
		/**
		 * Code to set-up the manual equilibration signal button
		 */
		chiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(!controller.isRunning()){
					if(chiButton.isSelected()){
						controller.setInEquilibrium(true);
						chiButton.setSelected(true);
					}else{
						chiButton.setSelected(false);
						controller.setInEquilibrium(false);
						controller.doFinalCalc();
					}
				}
				//chiButton.setPressedIcon();
			}
		});

		/**
		 * Add controls to the panel. The simplest layout will do for now
		 */
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(systemSizeLabel,c);
		c.gridy = 1;
		add(systemSizeInput,c);
		c.gridy = 2;
		add(temperatureLabel,c);
		c.gridy=3;
		c.insets = new Insets(0,5,0,5);
		add(temperatureInput,c);
		c.gridy=4;
		c.insets = new Insets(30,0,0,0);
		add(DynamicsLabel,c);
		c.gridy=5;
		c.insets = new Insets(0,0,0,0);
		add(glauberButton,c);
		c.gridy = 6;
		add(kawasakiButton,c);
		c.gridy = 7;
		c.insets = new Insets(30,0,0,0);
		add(fileOutLabel,c);
		c.gridy = 8;
		c.insets = new Insets(0,0,0,0);
		add(fileOutNo,c);
		c.gridy = 9;
		add(fileOutYes,c);
		c.insets = new Insets(30,5,0,5);
		c.gridy=10;
		add(chiButton,c);
		c.insets = new Insets(0,0,0,0);
		c.gridy=11;
		add(totalE,c);
		c.gridy=12;
		add(totalM,c);
	}

//Method which is called when the the viewer repaints
	//Allows updating of the values of the states
	public void painting(){
		totalE.setText("E : "+controller.getEnergy());
		totalM.setText("M : "+controller.getMagnetisation());
			}

	public void simulationStarted(){
		systemSizeInput.setEnabled(false);
		temperatureInput.setEnabled(false);
		chiButton.setEnabled(false);
		fileOutNo.setEnabled(false);
		fileOutYes.setEnabled(false);
		//glauberButton.setEnabled(false);
		//kawasakiButton.setEnabled(false);
		
	}
	public void simulationReset(){

	}
	public void simulationPaused(){
		
		temperatureInput.setEnabled(true);
		glauberButton.setEnabled(true);
		kawasakiButton.setEnabled(true);
		chiButton.setEnabled(true);
		fileOutNo.setEnabled(true);
		fileOutYes.setEnabled(true);
	}
	
	
	
}
