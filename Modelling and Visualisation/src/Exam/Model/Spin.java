package Exam.Model;
import java.lang.Math;


public class Spin{

	//The current spin state of the spin in this cell
	int spin = 0;


	
	public Spin(int n){
			if(n != 0 && n!=1 && n!=2){
	System.out.println("Value for the spin is not valid");
	
			}else{
				spin = n;
			}
	}
	public Spin(){
		double rand = 3*Math.random();
		if(rand <= 1){
		spin = 0;		
		}else if(rand>1 && rand < 2){	
		spin = 1;
		}else if(rand > 2){
			spin = 2;
		}
	}
	
	public int getState(){
		return spin;
	}
	public void setState(int n){
		if(n != 0 && n!=1 && n!=2){
			System.out.println("Value for the spin is not valid "+n);
		}else{
			spin = n;
		}
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
	public boolean isEqual(Spin in){
		if(this.getState() == in.getState())return true;
		else return false;
				
	}
}//class brackets
	
