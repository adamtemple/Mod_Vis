package PDE_NoVectors.Model;

public class Cell {

	double charge, scalarPotential;
	
	Vector field1, field2, vectorPotential, vectorCharge;
	
	public Cell(){

		charge = 0.0;
		scalarPotential = 0.0;
		double [] temp2 = {0.0,0.0,0.0};
		field1 = new Vector(temp2);
		field2 = new Vector(temp2);
		vectorPotential = new Vector(temp2);
		vectorCharge = new Vector(temp2);
	}
	/*
	public Cell(double charge, double potential, Vector eField){
		this.charge = charge;
		this.potential = potential;
		this.field = eField;
		
	}
*/
	///// GETTERS and SETTERS //////

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public double getScalarPotential() {
		return scalarPotential;
	}

	public void setScalarPotential(double potential) {
		this.scalarPotential = potential;
	}

	public Vector getField1() {
		return field1;
	}

	public void setField1(Vector field) {
		this.field1 = field;
	}

	public Vector getField2() {
		return field2;
	}

	public void setField2(Vector field2) {
		this.field2 = field2;
	}

	public Vector getVectorPotential() {
		return vectorPotential;
	}

	public void setVectorPotential(Vector current) {
		this.vectorPotential = current;
	}

	public Vector getVectorCharge() {
		return vectorCharge;
	}

	public void setVectorCharge(Vector vectorCharge) {
		this.vectorCharge = vectorCharge;
	}

	
	
	
}
