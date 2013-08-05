package PDE_NoVectors.Iterator;

import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Model.Vector;


public class Jacobi extends Iterator{

	
	

	public Jacobi(Box space){
		super(space);
	}
	public boolean isGauss(){
		return false;
	}
	public boolean isJacobi(){
		return true;
	}
	/**
	 * Method to iteratively solve the poisson equation required
	 */
	public void iterateScalarPotential() {
	
		//Is corrected to prevent access to boundary cells
		double[][][] newPotential = new double[space.getSpace().length -2][space.getSpace()[0].length -2][space.getSpace()[0][0].length -2];
		Vector[][][] newField = new Vector[space.getSpace().length -2][space.getSpace()[0].length -2][space.getSpace()[0][0].length -2];
		
		//Loop over all cells in the system
		for(int i=1; i< (space.getSpace().length - 1); i++){
			for(int j=1; j< (space.getSpace()[i].length - 1); j++){
				for(int k=1; k< (space.getSpace()[i][j].length - 1); k++){
					
					newPotential[i-1][j-1][k-1] = updateMethods.updateScalarPotential(space, i,j,k); 

					newField[i-1][j-1][k-1] = Vector.scalarProduct(-1.0, updateMethods.divScalarField(space, i,j,k));

					//Convergence criterion
					double diff =  newPotential[i-1][j-1][k-1] - space.getCell(i, j, k).getScalarPotential();
					checkCellConvergence(diff);

				}
			}
		}
		doConvergenceCalculation();
		
		//Apply new potential and field at each point
		for(int i=1; i<(space.getSpace().length - 1); i++){
			for(int j=1; j<(space.getSpace()[i].length - 1); j++){
				for(int k=1; k<(space.getSpace()[i][j].length - 1); k++){
					space.getCell(i, j, k).setScalarPotential(newPotential[i-1][j-1][k-1]);
					space.getCell(i, j, k).setField1(newField[i-1][j-1][k-1]);
				}
			}
		}
	}

	@Override
	public void iterateVectorPotential() {
		
		//Is corrected to prevent access to boundary cells
		Vector[][][] newPotential = new Vector[space.getSpace().length -2][space.getSpace().length -2][space.getSpace().length -2];
		Vector[][][] newField = new Vector[space.getSpace().length -2][space.getSpace().length -2][space.getSpace().length -2];
		
		//Loop over all cells in the system
		for(int i=1; i< (space.getSpace().length - 1); i++){
			for(int j=1; j< (space.getSpace()[i].length - 1); j++){
				for(int k=1; k< (space.getSpace()[i][j].length - 1); k++){
					
					newPotential[i-1][j-1][k-1] = updateMethods.updateVectorPotential(space, i,j,k); 

					newField[i-1][j-1][k-1] =  updateMethods.curlVectorField(space, i,j,k);

					//Convergence criterion
					Vector difference = Vector.subtractVector(newPotential[i-1][j-1][k-1], space.getCell(i, j, k).getVectorPotential());
					double diff = difference.getMag();
					checkCellConvergence(diff);

				}
			}
		}
		doConvergenceCalculation();
		
		//Apply new potential and field at each point
		for(int i=1; i<(space.getSpace().length - 1); i++){
			for(int j=1; j<(space.getSpace()[i].length - 1); j++){
				for(int k=1; k<(space.getSpace()[i][j].length - 1); k++){
					space.getCell(i, j, k).setVectorPotential(newPotential[i-1][j-1][k-1]);
					space.getCell(i, j, k).setField2(newField[i-1][j-1][k-1]);
				}
			}
		}
		
	}
	@Override
	public boolean isDoOverRelaxation() {
		return false;
	}
	@Override
	public void setDoOverRelaxation(boolean doOverRelaxation) {
		//DOES NOTHING
	}


	
}//Class brackets
