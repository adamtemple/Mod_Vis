package PDE_ExamPractice.Iterator;

import PDE_ExamPractice.Model.Box;
import PDE_ExamPractice.Model.Vector;



public abstract class Iterator {

	public Box space;
	public double accuracy = 1E-10;
	
	public int numberConverged = 0;
	
	boolean isStillChanging = true;
	
	public Iterator(Box space){
		this.space = space;
	}
	
	//Method to update the the system until accuracy required is reached
	public abstract void iterateScalarPotential();
	
	//public abstract void iterateVectorPotential();

	protected double updateScalarPotential(int i, int j){
		return	(1.0/4.0)*(  space.getCell(i+1, j).getScalarPotential() + space.getCell(i-1, j).getScalarPotential()
					+space.getCell(i, j+1).getScalarPotential() + space.getCell(i, j-1).getScalarPotential()
					+ (space.getDx() * space.getDx() * space.getCell(i,j).getScalarPotential() * (1.0 - space.getCell(i, j).getScalarPotential())  ) );
		}
	/*
	protected Vector updateVectorPotential(int i, int j){
		
		Vector potential = new Vector(new double[]{0.0,0.0});
		for(int m=0; m<3; m++){
			potential.setComponent(m, (1.0/6.0)*(  space.getCell(i+1, j).getVectorPotential().getComponent(m) + space.getCell(i-1, j).getVectorPotential().getComponent(m)
					+space.getCell(i, j+1).getVectorPotential().getComponent(m) + space.getCell(i, j-1).getVectorPotential().getComponent(m)
					+ space.getCell(i, j).getVectorPotential().getComponent(m) + space.getCell(i, j).getVectorPotential().getComponent(m)
					+ (space.getDx() * space.getDx() * space.getCell(i, j).getVectorCharge().getComponent(m))  )
					);
		}
			return potential;
	}
*/

	protected Vector divScalarField(int i, int j){
		
		double xField = ( space.getCell(i+1, j).getScalarPotential() - space.getCell(i-1, j).getScalarPotential() )
						/ ( 2.0 * space.getDx());
		double yField =  ( space.getCell(i, j+1).getScalarPotential() - space.getCell(i, j-1).getScalarPotential() )
						/ ( 2.0 * space.getDx());
		
		
		return new Vector(new double[]{xField,yField});
		
	}
	
	protected Vector curlVectorField(int i, int j){
		double x1 = space.getCell(i, j).getVectorPotential().getComponent(2) - space.getCell(i, j-1).getVectorPotential().getComponent(2);
		double x2 = space.getCell(i, j).getVectorPotential().getComponent(1) - space.getCell(i, j).getVectorPotential().getComponent(1);
		
		double y1 = space.getCell(i, j).getVectorPotential().getComponent(0) - space.getCell(i, j).getVectorPotential().getComponent(0);
		double y2 = space.getCell(i+1, j).getVectorPotential().getComponent(2) - space.getCell(i-1, j).getVectorPotential().getComponent(2);
		
	
		
		double factor = 2.0 * space.getDx();
		
		return new Vector(new double[]{ (x1-x2)/factor , (y1-y2)/factor });
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

	public void setIsStillChanging(boolean in){
		isStillChanging = in;
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
