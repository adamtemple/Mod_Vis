package MolecularDynamics.Model;

/**
 * Class which contains the constants required for an MD system
 * @author s0831683
 *
 */
public class Constants {

	public static double sigma = 1.0;
	public static double epsilon = -1.0;
	public static double sigma6 = sigma * sigma * sigma * sigma * sigma * sigma;
	public static double sigma12 = sigma6 * sigma6;
	
	//public static double boltzmannK= 1.3806503E-23;
	public static double boltzmannK = 1.0;
	
	public static double r_cutoff = Math.pow(2.0, (1.0/6.0)) * sigma;
	
	public static double polymerR = 1.6 * sigma;
	public static double polymerK = 1;
	
	public static void setPolymerK(double in){
		polymerK = in;
	}

}
