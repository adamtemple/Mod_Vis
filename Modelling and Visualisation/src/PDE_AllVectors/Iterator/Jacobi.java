package PDE_AllVectors.Iterator;

import PDE_AllVectors.Model.Box;
import PDE_AllVectors.Model.Vector;

public class Jacobi extends Iterator{

	
	
	Vector potential;			// variable to hold potential at each point
	int dim;
	public Jacobi(Box space){
		super(space);
		dim = space.getPotentialDimensions();
	}

	/**
	 * Method to iteratively solve the poisson equation required
	 */
	public void iterate() {
	
		//Is corrected to prevent access to boundary cells
		int tempConverged = 0;
	
		Vector[][][] newPotential = new Vector[space.getSpace().length -2][space.getSpace().length -2][space.getSpace().length -2];
		Vector[][][] newField = new Vector[space.getSpace().length -2][space.getSpace().length -2][space.getSpace().length -2];
		
		//Loop over all cells in the system
		for(int i=1; i< (space.getSpace().length - 1); i++){
			for(int j=1; j< (space.getSpace()[i].length - 1); j++){
				for(int k=1; k< (space.getSpace()[i][j].length - 1); k++){
					//potential = 0.0;
					
					potential = new Vector(dim);
					
					for(int m=0; m < dim; m++){
						double temp = updatePotential(i,j,k,m);
						potential.setComponent(m,temp);
						
					}
					newPotential[i-1][j-1][k-1] = potential; //-1 to account for fact i,j,k starts at 1
				
					if(dim == 1){
						newField[i-1][j-1][k-1] = updateFieldScalarPotential(i,j,k);
					}

					double diff = ( Vector.addVector(space.getCell(i, j, k).getPotential(),
							Vector.scalarProduct(-1.0, potential)) ).getMag();
					//Convergence criterion
					if(diff != 0.0 && diff<= super.accuracy){
						 tempConverged++;
					}
					
				}
			}
		}
		setNumberConverged(tempConverged);
		
		//Apply new potential and field at each point
		
		for(int i=1; i<(space.getSpace().length - 1); i++){
			for(int j=1; j<(space.getSpace()[i].length - 1); j++){
				for(int k=1; k<(space.getSpace()[i][j].length - 1); k++){
					space.getCell(i, j, k).setPotential(newPotential[i-1][j-1][k-1]);
					space.getCell(i, j, k).setField(newField[i-1][j-1][k-1]);
					
				}
			}
		}
	}



	
}//Class brackets
