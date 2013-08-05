package PDE_AllVectors.Iterator;

import PDE_AllVectors.Model.Box;
import PDE_AllVectors.Model.Vector;

public class GaussSeidel extends Iterator{

	
	Box space;
	Vector potential;
	int dim;
	
	public GaussSeidel(Box space){
		super(space);
		this.space = space;
		dim = space.getPotentialDimensions();
		potential = new Vector(dim);	
	}

	/**
	 * Method to iteratively solve the Gaussian equation required
	 */
	public void iterate() {

		int tempConverged = 0;
		
		//Loop over all cells in the system. boundary conditions sorted by cells at edges
		for(int i=1; i < (space.getSpace().length - 1); i++){
			for(int j=1; j < (space.getSpace()[i].length - 1); j++){
				for(int k=1; k < (space.getSpace()[i][j].length - 1); k++){
					
					Vector previous = space.getCell(i, j, k).getPotential();
					potential = new Vector(dim);
				
////////			Update the potential and fields in each direction		////////
					for(int m=0; m < dim; m++){
						double temp = (1.0/6.0) * (space.getCell(i+1, j, k).getPotential(m) + space.getCell(i-1, j, k).getPotential(m)
									+space.getCell(i, j+1, k).getPotential(m) + space.getCell(i, j-1, k).getPotential(m)
									+ space.getCell(i, j, k+1).getPotential(m) + space.getCell(i, j, k-1).getPotential(m)
									+ (space.getDx() * space.getDx() * space.getCell(i, j, k).getCharge(m))  );
						potential.setComponent(m, temp);
					}

					
					space.getCell(i, j, k).setPotential(potential);
					
					if(space.getPotentialDimensions() == 1){
						space.getCell(i, j, k).setField(updateFieldScalarPotential(i,j,k));
					}
					
					Vector difference = Vector.addVector(previous,
													Vector.scalarProduct(-1.0, potential));
					
					double diff = difference.getMag();
					//Convergence criterion
					if(diff != 0.0 && diff <= super.accuracy){
						tempConverged++;
						//System.out.println("Converged"+(previous.getCell(i, j, k).getPotential() - space.getCell(i, j, k).getPotential()));
					}
				}
			}
		}
		setNumberConverged(tempConverged);
	}

	
	
	
}//Class brackets
