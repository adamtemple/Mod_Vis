package PDE_AllVectors.Iterator;

import PDE_AllVectors.Model.Box;
import PDE_AllVectors.Model.Vector;

public abstract class Iterator {

	public Box space;
	public double accuracy = 0.00000001;
	
	public int numberConverged = 0;
	
	
	public Iterator(Box space){
		this.space = space;
	}
	
	//Method to update the the system until accuracy required is reached
	public abstract void iterate();
	
	protected double updatePotential(int i, int j, int k, int m){
	return	(1.0/6.0)*(  space.getCell(i+1, j, k).getPotential(m) + space.getCell(i-1, j, k).getPotential(m)
				+space.getCell(i, j+1, k).getPotential(m) + space.getCell(i, j-1, k).getPotential(m)
				+ space.getCell(i, j, k+1).getPotential(m) + space.getCell(i, j, k-1).getPotential(m)
				+ (space.getDx() * space.getDx() * space.getCell(i, j, k).getCharge(m))  );
	}
	
	protected Vector updateFieldScalarPotential(int i, int j, int k){
		
		double xField = -1.0 * ( space.getCell(i+1, j, k).getPotential(0) - space.getCell(i-1, j, k).getPotential(0) )
						/ ( 2.0 * space.getDx());
		double yField = -1.0 * ( space.getCell(i, j+1, k).getPotential(0) - space.getCell(i, j-1, k).getPotential(0) )
						/ ( 2.0 * space.getDx());
		double zField = -1.0 * ( space.getCell(i, j, k+1).getPotential(0) - space.getCell(i, j, k-1).getPotential(0) )
						/ ( 2.0 * space.getDx());
		
		return new Vector(new double[]{xField,yField,zField});
		
	}
	
	
	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public void setNumberConverged(int in){
		numberConverged = in;
	}
	

	
	public boolean isConverged(){
		
		double frac = (double)numberConverged / (double)space.getTotalCellNumber();
		System.out.println("Convergence fraction "+frac);
		if(frac == 1.0) return true;
		else{
			return false;
		}
		
	}
	
}
