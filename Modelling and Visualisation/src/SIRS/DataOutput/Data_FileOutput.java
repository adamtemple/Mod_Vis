package SIRS.DataOutput;

import isingModel.Controller.InteractiveIsing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import SIRS.Model.SIRS_System;

/**
 * This class handles the data output to file for a variety of different variables
 * Different outputs are created through the factory methods createXX. This allows methods to be added as required
 * Separate PrinterWriters are used due to the relatively low number of output variables and for clarity,
 * although may want to reuse the same one in future.
 */

public class Data_FileOutput {

	PrintWriter infected_out, averageI_out, contour, prob;
	String filename1, filename2, filename3, filename4;
	boolean file1 = false, file2 = false, file3 = false, file4= false; //booleans to say whether the output is being done
	
	public Data_FileOutput() throws IOException{}
	
// ######################## Creating output steams #########################################	
/**
 * Methods to create the different output streams
 * @throws IOException 
 */
	public void createInfectedOutput(SIRS_System in) throws IOException{
		filename1 = "I_v_Time_("+in.getSystemSize()+"x"+in.getSystemSize()+").xvg";
		infected_out = new PrintWriter(new FileWriter(filename1));
		file1 = true;
	}
	public void createAverageOutput(SIRS_System in) throws IOException{
		filename2 = "averageI_v_Time_("+in.getSystemSize()+"x"+in.getSystemSize()+").xvg";
		averageI_out = new PrintWriter(new FileWriter(filename2));
		file2 = true;
	}
	public void createContourOutput(SIRS_System in)throws IOException{
		filename3 = "phase_plot_("+in.getSystemSize()+"x"+in.getSystemSize()+").xvg";
		contour = new PrintWriter(new FileWriter(filename3));
		file3 = true;
	}
	public void createProbabilityOutput(SIRS_System in, int temp)throws IOException{
		filename4 = "averageI_v_P"+temp+"_"+in.getSystemSize()+"x"+in.getSystemSize()+".xvg";
		prob = new PrintWriter(new FileWriter(filename4));
		file4 = true;
	}

//############################## Methods for data output ##############################
	/**
	 * Methods to do the output for each case
	 * @param infected
	 * @param time
	 */
	public void doInfectedOutput(double infected, int time){
		infected_out.write(time+"\t"+infected+"\n");
		infected_out.flush();
	}
	
	public void doAverageOutput(double average, int time){
		averageI_out.write(time+"\t"+average+"\n");
		averageI_out.flush();
	}
	public void doContourOutput(double x, double y, double value){
		contour.write(x+"\t"+y+"\t"+value+"\n");
		contour.flush();
	}
	public void doProbabilityOutput(double infected, double probability){
		prob.write(probability+"\t"+infected+"\n");
		prob.flush();
	}
	
	//Method to give new line between data sets
	public void newLine(){
		if(file1==true)infected_out.write("\n");
		if(file2==true)averageI_out.write("\n");
		if(file3==true)contour.write("\n");
		if(file4==true)prob.write("\n");
	}
	
	//Method to close the output properly
	public void closeOutput(){
		System.out.println("Closing file output stream");
		if(file1==true)infected_out.close();
		if(file2==true)averageI_out.close();
		if(file3==true)contour.close();
		if(file4==true)prob.close();
	}
	
}
