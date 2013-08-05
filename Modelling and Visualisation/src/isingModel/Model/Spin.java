package isingModel.Model;
import java.lang.Math;


public class Spin{

	//The current spin state of the spin in this cell
	int spin = 0;
	//Locations in grid of this spin object
	int i, j;

	
	public Spin(int n){
			if((n * n) != 1){
	System.out.println("Value for the spin is not 1 or -1");
	
			}else{
				spin = n;
			}
	}
	public Spin(int i, int j){
		this.i =i;
		this.j=j;
		double ran = Math.random();
		if(ran <= 0.5){
		spin = 1;		
		}else{	
		spin = -1;
		}
	}
		
	public int getState(){
		return spin;
	}
	public void setState(int n){
		if( (n * n) != 1){
			System.out.println("n = "+n);
			System.out.println("Value for the spin is not 1 or -1, setState");
		}else{
			spin = n;
		}
	}
	public void flipSpin(){
		spin = spin*(-1);
	}
	
	/**
	 * Method to check whether this spin and the argument are the same
	 * If so, returns 1, else -1.
	 * 
	 * This is used in the energy methods to calculated the local energy contribution.
	 * There is an energy cost for unlike spins, -1, but energy gain for like spins, 1.
	 * Note in the ising formula the use of -J, making -1 an overall positive contribution to energy 
	 * (i.e. raising it -> is a cost) while 1 is overal negative, and therefore lowers the total energy 
	 * @param in
	 * @return
	 */
	public int isEqual(Spin in){
		if(this.getState() == in.getState())return 1;
		else return -1;
				
	}
}//class brackets
	
