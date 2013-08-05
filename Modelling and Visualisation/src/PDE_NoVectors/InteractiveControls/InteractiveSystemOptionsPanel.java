package PDE_NoVectors.InteractiveControls;

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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import PDE_NoVectors.Iterator.*;

import PDE_NoVectors.Controller.InteractivePDE;


public class InteractiveSystemOptionsPanel extends JPanel{
	

	InteractivePDE controller;

	private JLabel iterations, convergence, iterationsLabel, convergenceLabel, systemSize, accuracyLabel;
	private NumberFormat doubleFormat = NumberFormat.getNumberInstance();
	private NumberFormat sizeFormat = NumberFormat.getIntegerInstance();
	private NumberFormat accuracyFormat = new DecimalFormat("0.###E0");
	
	//Labels for probabilities and other variables
	private JFormattedTextField sizeInput = new JFormattedTextField(sizeFormat);
	private JFormattedTextField accuracyInput = new JFormattedTextField(accuracyFormat);
	
	//private JFormattedTextField targetTempInput = new JFormattedTextField(volFracFormat);
	
	
	private JRadioButton 	jacobiButton = new JRadioButton("Jacobi"),
	 						gaussNoRelaxButton = new JRadioButton("Gauss-Seidel: No Relaxation"),
	 						gaussRelaxButton = new JRadioButton("Gauss-Seidel: Relaxation");
	private ButtonGroup group1 = new ButtonGroup();
	private JLabel iterator = new JLabel("Iterator");
	
	
	public InteractiveSystemOptionsPanel(InteractivePDE attachedTo){
		controller = attachedTo;
		systemSize = new JLabel("System Size");
		iterationsLabel = new JLabel("Iterations");
		convergenceLabel = new JLabel("Convergence Fraction");
		iterations = new JLabel(""+controller.getIterations());
		convergence = new JLabel(""+controller.getIterator().getConvergenceFraction());
		accuracyLabel = new JLabel("Accuracy");
		
		sizeInput.setColumns(5);
		sizeInput.setHorizontalAlignment(JTextField.CENTER);
		sizeInput.setValue(new Integer(controller.getN()));
		sizeInput.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				int size = ((Number)sizeInput.getValue()).intValue();
				if(size <= 0){
					size = controller.getN(); //If good value not input, revert to current system size
					sizeInput.setValue(new Integer(controller.getN()));
				}
				controller.setN(size);
			}
		});
		accuracyInput.setColumns(5);
		accuracyInput.setHorizontalAlignment(JTextField.CENTER);
		accuracyInput.setValue(controller.getIterator().getAccuracy());
		accuracyInput.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				double accuracy = ((Number)sizeInput.getValue()).doubleValue();
				if(accuracy < 0){
					accuracy = controller.getIterator().getAccuracy(); //If good value not input, revert to current system size
					sizeInput.setValue(new Double(controller.getIterator().getAccuracy()));
				}
				controller.getIterator().setAccuracy(accuracy);
			}
		});
		
		group1.add(jacobiButton);
		group1.add(gaussNoRelaxButton);
		group1.add(gaussRelaxButton);
		if(controller.getIterator().isJacobi()){
			jacobiButton.setSelected(true);
		}else{
			if(controller.getIterator().isGauss() && !controller.getIterator().isDoOverRelaxation()){
				gaussNoRelaxButton.setSelected(true);
			}else{
				gaussRelaxButton.setSelected(true);
			}
		}
		
		jacobiButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
				controller.setIterator(new Jacobi(controller.getSpace()));
			}
		});
		gaussNoRelaxButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
				controller.setIterator(new GaussSeidel(controller.getSpace()));
				controller.getIterator().setDoOverRelaxation(false);
			}
		});
		gaussRelaxButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if(!controller.isRunning()) 
				controller.setIterator(new GaussSeidel(controller.getSpace()));
				controller.getIterator().setDoOverRelaxation(true);
			}
		});

		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(iterationsLabel,c);
		c.gridy = 1;
		add(iterations,c);
		c.gridy = 2;
		add(convergenceLabel,c);
		c.gridy = 3;
		add(convergence,c);
		c.insets = new Insets(50,15,0,15);
		c.gridy = 4;
		add(systemSize,c);
		c.gridy=5;
		c.insets = new Insets(0,15,0,15);
		add(sizeInput,c);
		c.gridy = 6;
		add(accuracyLabel,c);
		c.gridy=7;
		add(accuracyInput,c);
		
		c.gridy=8;
		c.insets = new Insets(50,15,0,15);
		add(iterator,c);
		c.insets = new Insets(0,0,0,0);
		c.gridy=9;
		add(jacobiButton,c);
		c.gridy =10;
		
		add(gaussNoRelaxButton,c);
		c.gridy = 11;
		c.insets = new Insets(0,0,0,0);
		add(gaussRelaxButton,c);
	
		/*
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
		*/
	}

//Method which is called when the the viewer repaints
	//Allows updating of the values of the states
	public void painting(){
		iterations.setText(new DecimalFormat("#.#").format(controller.getIterations()));
		convergence.setText(new DecimalFormat("#.###").format(controller.getIterator().getConvergenceFraction()));
	}

	public void simulationStarted(){
		sizeInput.setEnabled(false);
		accuracyInput.setEnabled(false);
	//volFracInput.setEnabled(false);
	}
	public void simulationReset(){
		sizeInput.setEnabled(true);
		accuracyInput.setEnabled(true);
	//volFracInput.setEnabled(true);
	}
	public void simulationPaused(){
		sizeInput.setEnabled(true);
		accuracyInput.setEnabled(true);
	//volFracInput.setEnabled(true);
	}

	
	
	
}
