package MolecularDynamics.InteractiveControls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import MolecularDynamics.Controller.InteractiveParticleSystem;
import MolecularDynamics.Integrator.*;
import MolecularDynamics.Model.*;
import MolecularDynamics.Model.Potential.PolymerInteraction;
import MolecularDynamics.Model.Potential.VanDerWaals;
import MolecularDynamics.Model.Potential.WicksChandlerAnderson;



public class InteractiveSystemOptionsPanel extends JPanel{

	InteractiveParticleSystem controller;

	
	private NumberFormat volFracFormat = NumberFormat.getNumberInstance();
	
	//Labels for probabilities and other variables
	private JLabel temperature, time, timeLabel, volFracLabel, temperatureLabel, targetTempLabel, particleNumberLabel,
					particleNumber;
	private JFormattedTextField volFracInput = new JFormattedTextField(volFracFormat);
	private JFormattedTextField targetTempInput = new JFormattedTextField(volFracFormat);
	
	
	private JRadioButton noseButton = new JRadioButton("Nose-Hoover");
	private JRadioButton noCouplingButton = new JRadioButton("None");
	private ButtonGroup group1 = new ButtonGroup();
	private JLabel couplingLabel = new JLabel("Coupling");
	
	private JRadioButton vdwButton = new JRadioButton("VDW");
	private JRadioButton wcaButton = new JRadioButton("WCA");
	private JRadioButton polymerButton = new JRadioButton("Polymer");
	private ButtonGroup group2 = new ButtonGroup();
	private JLabel potentialLabel = new JLabel("Potential");
	
	public InteractiveSystemOptionsPanel(InteractiveParticleSystem attachedTo){
		controller = attachedTo;
		temperatureLabel = new JLabel("Current Temperature");
		temperature = new JLabel("T = "+controller.getTemperature());
		timeLabel = new JLabel("Time");
		time = new JLabel(""+controller.getTime());
		volFracLabel = new JLabel("Volume Fraction");
		targetTempLabel = new JLabel("Target Temperature");
		particleNumberLabel = new JLabel("Particle Number");
		particleNumber = new JLabel(""+controller.getParticles().getParticleNumber());
		
		volFracInput.setColumns(5);
		volFracInput.setHorizontalAlignment(JTextField.CENTER);
		volFracInput.setValue(controller.getVolumeFraction());
		volFracInput.addPropertyChangeListener("value", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent e){
				double volFrac = ((Number)volFracInput.getValue()).doubleValue();
				//if the entered value is outside the appropirate range, take the current value from the system
				if(volFrac < 0.0 || volFrac > 0.79){
					volFrac = controller.getVolumeFraction();
					volFracInput.setValue(controller.getVolumeFraction());
				}
				controller.setVolumeFraction(volFrac);
				volFracInput.setValue(controller.getVolumeFraction());
			}
		});
		targetTempInput.setColumns(5);
		targetTempInput.setHorizontalAlignment(JTextField.CENTER);
		targetTempInput.setValue(controller.getTargetTemperature());
		targetTempInput.addPropertyChangeListener("value", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent e){
				double targetTemp = ((Number)targetTempInput.getValue()).doubleValue();
				//if the entered value is outside the appropirate range, take the current value from the system
				if(targetTemp < 0.0){
					targetTemp = controller.getTargetTemperature();
					targetTempInput.setValue(controller.getTargetTemperature());
				}
				controller.getParticles().setTargetTemperature(targetTemp);
				targetTempInput.setValue(controller.getTargetTemperature());
			}
		});
		
		
		
		group1.add(noseButton);
		group1.add(noCouplingButton);
		if(controller.getIntegrator().doTempCoupling()){
			noseButton.setSelected(true);
		}else{
			noCouplingButton.setSelected(true);
		}
		
		noseButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
				controller.setIntegrator(new VelocityVerlet_NoseHoover());
			}
		});
		noCouplingButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
					controller.setIntegrator(new VelocityVerlet());
			}
		});
		group2.add(vdwButton);
		group2.add(wcaButton);
		group2.add(polymerButton);
		if(controller.getPotential().isVDW()){
			vdwButton.setSelected(true);
		}else if(controller.getPotential().isWCA()){
			wcaButton.setSelected(true);
		}else if(controller.getPotential().isPolymer()){
			polymerButton.setSelected(true);
		}
		vdwButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
					controller.setPotential(new VanDerWaals());
			}
		});
		wcaButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
					controller.setPotential(new WicksChandlerAnderson());
			}
		});
		polymerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
					controller.setPotential(new PolymerInteraction());
			}
		});
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(temperatureLabel,c);
		c.gridy = 1;
		add(temperature,c);
		c.gridy = 2;
		add(timeLabel,c);
		c.gridy=3;
		c.insets = new Insets(0,15,0,15);
		add(time,c);
		c.gridy=4;
		add(particleNumberLabel,c);
		c.gridy=5;
		add(particleNumber,c);
		c.gridy = 6;
		c.insets = new Insets(50,15,0,15);
		add(targetTempLabel,c);
		c.gridy = 7;
		c.insets = new Insets(0,0,0,0);
		add(targetTempInput,c);
		c.gridy = 8;
		add(volFracLabel,c);
		c.insets = new Insets(0,0,0,0);
		c.gridy = 9;
		add(volFracInput,c);
		c.insets = new Insets(50,15,0,15);
		c.gridy=10;
		add(couplingLabel,c);
		c.insets = new Insets(0,0,0,0);
		c.gridy=11;
		add(noseButton,c);
		c.gridy=12;
		add(noCouplingButton,c);
		c.insets = new Insets(25,15,0,15);
		c.gridy=13;
		add(potentialLabel,c);
		c.insets = new Insets(0,0,0,0);
		c.gridy=14;
		add(vdwButton,c);
		c.gridy=15;
		add(wcaButton,c);
		c.gridy=16;
		add(polymerButton,c);
	}

//Method which is called when the the viewer repaints
	//Allows updating of the values of the states
	public void painting(){
		temperature.setText(new DecimalFormat("#.####").format(controller.getTemperature()));
		time.setText(new DecimalFormat("#.##").format(controller.getTime()));
		particleNumber.setText(""+controller.getParticles().getParticleNumber());
		//System.out.println(controller.getTemperature());
	}

	public void simulationStarted(){
	volFracInput.setEnabled(false);
	}
	public void simulationReset(){
	volFracInput.setEnabled(true);
	}
	public void simulationPaused(){
	volFracInput.setEnabled(true);
	}
	
	
	
}
