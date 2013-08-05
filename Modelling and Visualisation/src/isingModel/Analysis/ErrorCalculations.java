package isingModel.Analysis;

import java.util.Random;

public class ErrorCalculations {

	//NEED TO REVIEW THESE METHODS FOR THE SPECIFIC PURPOSES I.E. METHODS OF CALCULATING VALUE MAY BE WRONG
	
	
	/**
	 * Method to do the bootstrapping method to calculate errors on a quantity
	 * For heat capacities, the c array should be energy measurements
	 * TODO Need to adapt to change the different quanities
	 * i.e. need method like calculateCv(double [] c) etc. 
	 */
	public static double bootstrap(double [] c, int repeats){
		//REVIEW
		int n = c.length;
		double [] temp = new double[n];
		double [] bootstrapResults = new double[repeats];
		
		Random rand = new Random();		//Random num gen. to randomly sample the array
		
		double error;
		double results = 0;	//Values for the average of the averaged quantity
		double results2 = 0;
		
		//Loop for how many times we want to calculate average C
		for(int i=0; i < repeats; i++){
			
			double value2 = 0; 
			double value = 0;
			for(int j=0; j < temp.length; j++){ //Loop around the temporary array
				temp[j] = c[rand.nextInt(n)];
				value += temp[j];
				value2 += temp[j] * temp[j];
			}
			double av = value / temp.length;
			double av2 = value2 / temp.length;
			bootstrapResults[i] = av2 - (av * av);	//Now contains values of Heat capacity, susceptibility, etc.
			results += bootstrapResults[i];
			results2 += bootstrapResults[i] * bootstrapResults[i];
		}
		
		error = Math.sqrt( (results2 / bootstrapResults.length ) - 
								( (results / bootstrapResults.length) * (results / bootstrapResults.length))
						);		
	return error;
	}
	
	/**
	 * Method to do the jackknife sampling method to calculate the error on an averaged quantity
	 * @param c
	 * @return
	 */
	public static double jackknife(double [] c){
		//REVIEW
		int n = c.length;
		//double [] temp = new double[n];
		
		double tempSum =0;
		double tempSum2 = 0;
		for(int i=0; i < n; i++){
			tempSum += c[i];
			tempSum2 += c[i] * c[i];
		}
		
		double averageValue = Math.sqrt( tempSum2 / n - ( (tempSum/n)*(tempSum/n) ) );	//i.e. calc of cV or chi
		
		double errorSum = 0;
		for(int i=0; i < n; i++){
			double sum = 0;
			double sum2 = 0;
			for(int j=0; j < n; j++){
				if(j != i){
					sum += c[j];
					sum2 += c[j] * c[j];
				}
			}
			double tempValue = Math.sqrt( sum2 / (n-1) - ( (sum /(n-1) ) * ( sum / (n-1) )   ) ) ; //Since one value is left out each time
			tempValue = (tempValue - averageValue) * (tempValue - averageValue); //reuse variable
			errorSum += tempValue;
		}
		double error =  Math.sqrt(errorSum);
		return error;
	}
	
}
