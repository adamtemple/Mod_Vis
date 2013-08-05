package PDE_ExamPractice.Iterator;

import PDE_ExamPractice.Model.Box;
import PDE_ExamPractice.Model.Vector;

public class FisherIterator extends Iterator{


	public FisherIterator(Box space) {
		super(space);
	}

	double potential;			// variable to hold potential at each point
	int dim;

	/**
	 * Method to iteratively solve the poisson equation required
	 */
	/**
	 * Method to iteratively solve the Gaussian equation required
	 */
	public void iterateScalarPotential() {

		int tempConverged = 0;
		
		//Loop over all cells in the system. boundary conditions sorted by cells at edges
		for(int i=0; i < space.getN(); i++){
			for(int j=0; j < space.getN(); j++){
				
					
					double previous = space.getCell(i, j).getScalarPotential();
				
////////			Update the potential and fields in each direction		////////
					potential = updateScalarPotential(i,j);
					
					space.getCell(i, j).setScalarPotential(potential);
					//space.getCell(i, j).setField1(Vector.scalarProduct(-1.0, divScalarField(i,j) ) );
					
					//Convergence criterion
					double diff = previous - space.getCell(i, j).getScalarPotential();
					diff = Math.sqrt(diff*diff);								

					if(diff <= super.accuracy){
						tempConverged++;
					}
				}
			}
		
		setNumberConverged(tempConverged);
	}
/*
	@Override
	public void iterateVectorPotential() {

		int tempConverged = 0;
		//Loop over all cells in the system. boundary conditions sorted by cells at edges
		for(int i=1; i < (space.getSpace().length - 1); i++){
			for(int j=1; j < (space.getSpace()[i].length - 1); j++){
			
					
						Vector previous = space.getCell(i, j).getVectorPotential();
						
////////				Update the potential and fields in each direction		////////
						
						Vector potential = updateVectorPotential(i,j);
						
						space.getCell(i, j).setVectorPotential(potential);
					
						space.getCell(i, j).setField2( curlVectorField(i,j) );
						
						//System.out.println(space.getCell(i, j, k).getField2().getMag());
						
						//Convergence criterion
						Vector difference = Vector.subtractVector(previous, space.getCell(i, j).getVectorPotential());
						double diff = difference.getMag();	

						if(diff != 0.0 && diff <= super.accuracy)tempConverged++;
					
			
				}
			}
		
		setNumberConverged(tempConverged);
		
	}
*/
	
	
}//Class brackets
