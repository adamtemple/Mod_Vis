package isingModel.Analysis;


import isingModel.Controller.InteractiveIsing;

import java.io.*;
public class IsingOutput {

	PrintWriter out;
	PrintWriter outChi;
	PrintWriter outCv;
	InteractiveIsing controller;
	String filename;
	
	
	public IsingOutput(InteractiveIsing attachedTo) throws IOException{
		controller = attachedTo;
		filename = controller.getSystemSize()+"x"+controller.getSystemSize()+"_"+controller.getTemperature()+"K_new.xvg";
		out = new PrintWriter(new FileWriter(filename));
		outChi = new PrintWriter(new FileWriter("Chi_"+filename));
		outCv = new PrintWriter(new FileWriter("Cv_"+filename));
	}
	
	public String getFilename(){
		return filename;
	}
	
	/**
	 * Method to do the file output for this output object
	 */
	public void doOutput(){
		out.write(controller.getTime()+"\t"+controller.getMagnetisation()+"\n");
		out.flush();
	}
	
	public void doEquilibriumOutput(){
		outChi.write(controller.getTemperature()+"\t"+controller.getChi()+"\n");
		outCv.write(controller.getTemperature()+"\t"+controller.getCv()+"\n");
		outChi.flush();
		outCv.flush();
	}
	
	public void closeOutput(){
		System.out.println("Closing file output stream");
		out.close();
		outChi.close();
		outCv.close();
	}
	
}
