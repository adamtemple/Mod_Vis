package SIRS.GraphicControls;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
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
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * This class is the options panel for the simulation. It allows varying of the parameters of the simulation using various means.
 * @author s0831683
 *
 */

public class InteractiveSIRSOptionsPanel extends JPanel{
	
	InteractiveSIRS controller;
	private NumberFormat probabilityFormat = NumberFormat.getNumberInstance();
	private NumberFormat sizeFormat = NumberFormat.getIntegerInstance();
	private static final int SLIDER_FACTOR = 1000;		//Allows more accurate representation of the slider values
														//as these must be of integer value
	
	//Input text fields for probabilities
	private JFormattedTextField p1Input = new JFormattedTextField(probabilityFormat),
								p2Input = new JFormattedTextField(probabilityFormat),
								p3Input = new JFormattedTextField(probabilityFormat),
								sizeInput = new JFormattedTextField(sizeFormat),
								immuneInput = new JFormattedTextField(probabilityFormat);
	
	//Labels for probabilities and other variables
	private JLabel 	p1Label = new JLabel("P(S->I)"),
					p2Label = new JLabel("P(I->R)"),
					p3Label = new JLabel("P(R->S)"),
					numOfS, numOfI, numOfR, numOfImmune,
					sizeLabel = new JLabel("Size of system (NxN)"),
					immuneLabel = new JLabel("Immunity Fraction");
	//Sliders for the probabilities
	private JSlider p1Slider, 
					p2Slider, 
					p3Slider;
	
	public InteractiveSIRSOptionsPanel(InteractiveSIRS attachedTo){
		controller = attachedTo;
		//Set up sliders and labels
		p1Slider = new JSlider(JSlider.VERTICAL, 0, SLIDER_FACTOR, (int)(controller.getProb(0)*SLIDER_FACTOR));
		p2Slider = new JSlider(JSlider.VERTICAL, 0, SLIDER_FACTOR, (int)(controller.getProb(1)*SLIDER_FACTOR));
		p3Slider = new JSlider(JSlider.VERTICAL, 0, SLIDER_FACTOR, (int)(controller.getProb(2)*SLIDER_FACTOR));
		numOfS = new JLabel("S:\t"+controller.getSusceptible());
		numOfI = new JLabel("I:\t"+controller.getInfected());
		numOfR = new JLabel("R:\t"+controller.getRecovered());
		numOfImmune = new JLabel("Immune = "+controller.getImmune());
		
		//ACTIONS
		//Set up probabilities input
		doProbabilityBoxes(p1Input,p1Slider,0);
		doProbabilityBoxes(p2Input,p2Slider,1);
		doProbabilityBoxes(p3Input,p3Slider,2);
		doSliders(p1Slider,p1Input,0);
		doSliders(p2Slider,p2Input,1);
		doSliders(p3Slider,p3Input,2);

		sizeInput.setColumns(5);
		sizeInput.setValue(new Integer(controller.getSystemSize()));
		sizeInput.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				int size = ((Number)sizeInput.getValue()).intValue();
				if(size <= 0){
					size = controller.getSystemSize(); //If good value not input, revert to current system size
					sizeInput.setValue(new Integer(controller.getSystemSize()));
				}
				controller.setSystemSize(size);
			}
		});
		
		immuneInput.setColumns(5);
		immuneInput.setHorizontalAlignment(JTextField.CENTER);
		immuneInput.setValue(controller.getImmuneFraction());

		immuneInput.addPropertyChangeListener("value", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent e){
				double immunity = ((Number)immuneInput.getValue()).doubleValue();
				//if the entered value is outside the appropirate range, take the current value from the system
				if(immunity < 0.0 || immunity >1.0){
					immunity = controller.getImmuneFraction();
					immuneInput.setValue(new Double(controller.getImmuneFraction()));
				}
				//Resets the system if the new immune fraction is smaller than current one
				//if(immunity < controller.getImmuneFraction())
				//Current, resets the system everytime a new immune fraction is set
				controller.setSystemSize(controller.getSystemSize());
				controller.setImmuneFraction(immunity);
			}
		});

		//Setting layout of panel, positioning items on a grid.
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//Labels
		c.gridx = 0;
		c.gridy = 0;
		add(p1Label,c);
		c.gridx = 1;
		add(p2Label,c);
		c.gridx = 2;
		add(p3Label,c);
		//TextBoxes
		c.gridx = 0;
		c.gridy = 1;
		add(p1Input,c);
		c.gridx = 1;
		add(p2Input,c);
		c.gridx = 2;
		add(p3Input,c);
		//Sliders
		c.gridx = 0;
		c.gridy = 2;
		add(p1Slider,c);
		c.gridx = 1;
		add(p2Slider,c);
		c.gridx = 2;
		add(p3Slider,c);
		c.gridx = 1;
		c.gridy = 3;
		add(numOfS,c);
		c.gridy=4;
		add(numOfI,c);
		c.gridy = 5;
		add(numOfR,c);
		c.gridy = 7;
		add(sizeLabel,c);
		c.gridy = 8;
		add(sizeInput,c);
		c.gridy=9;
		add(immuneLabel,c);
		c.gridy=10;
		add(immuneInput,c);
		c.gridy=11;
		add(numOfImmune,c);
	}
	
	//Method to create the text boxes for the probabilities
	private void doProbabilityBoxes(final JFormattedTextField in,final JSlider slider, final int probIndex){
		in.setColumns(5);
		in.setHorizontalAlignment(JTextField.CENTER);
		in.setValue(controller.getProb(probIndex));
		in.addPropertyChangeListener("value", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent e){
				double prob = ((Number)in.getValue()).doubleValue();
				//if the entered value is outside the appropirate range, take the current value from the system
				if(prob < 0.0 || prob >1.0){
					prob = controller.getProb(probIndex);
					in.setValue(new Double(controller.getProb(probIndex)));
				}
				controller.setProb(probIndex,prob);
				slider.setValue((int)(prob*SLIDER_FACTOR));
			}
		});
	}
	
	//Method to create the sliders for the probabilities
	private void doSliders(final JSlider in,final JFormattedTextField textField, final int probIndex){
		in.setMajorTickSpacing((int) (0.1*SLIDER_FACTOR));
		in.setMinorTickSpacing((int) (0.5*SLIDER_FACTOR));
		in.setPaintTicks(true);
		in.setPreferredSize(new Dimension(50,500));
		Hashtable labelTable = new Hashtable();
		//Label table allows different labels for each point, to give 0.0 to 1.0 labels on the sliders
		for(int i=0;i<11;i++){
			labelTable.put( new Integer((int)(SLIDER_FACTOR*(0.1*i))),new JLabel(""+((double)i/10)));
		}
	
		in.setLabelTable( labelTable );

		in.setPaintLabels(true);
		
		//Add listener to change value of probabilities in simulation when slider is changed
		//Also changes textbox
		in.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				if(!in.getValueIsAdjusting()){
					int sliderValue = in.getValue();
					if(sliderValue == 0) controller.setProb(probIndex, (double)sliderValue);
					double probValue = (double)sliderValue / (double)SLIDER_FACTOR;
					controller.setProb(probIndex, probValue);
					textField.setValue(controller.getProb(probIndex));
				}
			}
		});
	}
	//Method which is called when the the viewer repaints
	//Allows updating of the values of the states
	public void painting(){
		numOfS.setText("S:\t"+controller.getSusceptible());
		numOfI.setText("I:\t"+controller.getInfected());
		numOfR.setText("R:\t"+controller.getRecovered());
		numOfImmune.setText("Immune = "+controller.getImmune());
	}

	public void simulationStarted(){
	sizeInput.setEnabled(false); 
	}
	public void simulationReset(){
	
	}
	public void simulationPaused(){
	
	}
} //Class brackets
