package PDE_AllVectors.Model;

public class Cell {

	Vector charge, potential;
	
	Vector field;
	
	public Cell(int length){
		double[] temp = new double[length];
		for(int i=0;i<length;i++){
			temp[i] = 0.0;
		}
		charge = new Vector(temp);
		potential = new Vector(temp);
		
		double [] temp2 = {0.0,0.0,0.0};
		field = new Vector(temp2);
	}
	/*
	public Cell(double charge, double potential, Vector eField){
		this.charge = charge;
		this.potential = potential;
		this.field = eField;
		
	}
*/
	///// GETTERS and SETTERS //////
	
	public Vector getCharge() {
		return charge;
	}
	public double getCharge(int n){
		return charge.getComponent(n);
	}

	public void setCharge(int n, double chargeIn) {
		charge.setComponent(n, Constants.chargeScale* chargeIn);
	}

	public Vector getField() {
		return field;
	}

	public void setField(Vector eField) {
		this.field = eField;
	}

	public Vector getPotential() {
		return potential;
	}
	public double getPotential(int n){
		return potential.getComponent(n);
	}

	public void setPotential(int n, double potentialIn) {
		potential.setComponent(n, potentialIn);
	}
	public void setPotential(Vector in){
		potential = in;
	}
	
	
	
}
