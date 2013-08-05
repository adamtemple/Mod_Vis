package PDE_NoVectors.Iterator;

import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Model.Vector;


public class GaussSeidel extends Iterator{

	double potential, w = 1.2;	//Relaxation term
	int dim;
	
	boolean doOverRelaxation = false;
	public GaussSeidel(Box space){
		super(space);
		
	}
	
	public GaussSeidel(Box space, boolean in, double w){
		super(space);
		doOverRelaxation = in;
		this.w = w;
	}

	public boolean isGauss(){
		return true;
	}
	public boolean isJacobi(){
		return false;
	}
	/**
	 * Method to iteratively solve the Gaussian equation required
	 */
	public void iterateScalarPotential() {

		//Loop over all cells in the system. boundary conditions sorted by cells at edges
		for(int i=1; i < (space.getSpace().length - 1); i++){
			for(int j=1; j < (space.getSpace()[i].length - 1); j++){
				for(int k=1; k < (space.getSpace()[i][j].length - 1); k++){
					
					double previous = space.getCell(i, j, k).getScalarPotential();
				
////////			Update the potential and fields in each direction		////////
					if(doOverRelaxation)potential = (1 - w) * previous + w * updateMethods.updateScalarPotential(space, i,j,k);
					else 				potential = updateMethods.updateScalarPotential(space, i,j,k);
					
					space.getCell(i, j, k).setScalarPotential(potential);
				
					space.getCell(i, j, k).setField1(Vector.scalarProduct(-1.0, updateMethods.divScalarField(space, i,j,k) ) );
					
					//Convergence criterion
					double diff = previous - space.getCell(i, j, k).getScalarPotential();
					checkCellConvergence(diff);	
				}
			}
		}

		doConvergenceCalculation();
	}

	@Override
	public void iterateVectorPotential() {

		//Loop over all cells in the system. boundary conditions sorted by cells at edges
		for(int i=1; i < (space.getSpace().length - 1); i++){
			for(int j=1; j < (space.getSpace()[i].length - 1); j++){
				for(int k=1; k < (space.getSpace()[i][j].length - 1); k++){
					
						Vector previous = space.getCell(i, j, k).getVectorPotential();
						
////////				Update the potential and fields in each direction		////////
						Vector potential; 
					
						if(doOverRelaxation)potential = Vector.addVector(Vector.scalarProduct((1 - w),  previous) , Vector.scalarProduct(
								w, updateMethods.updateVectorPotential(space, i,j,k)));
						else 				potential = updateMethods.updateVectorPotential(space, i,j,k);
						
						space.getCell(i, j, k).setVectorPotential(potential);
					
						space.getCell(i, j, k).setField2( updateMethods.curlVectorField(space, i,j,k) );
						
						//System.out.println(space.getCell(i, j, k).getField2().getMag());
						
						//Convergence criterion
						Vector difference = Vector.subtractVector(previous, space.getCell(i, j, k).getVectorPotential());
						double diff = difference.getMag();	
						checkCellConvergence(diff);
					
				}
			}
		}
		doConvergenceCalculation();
		
	}

	public boolean isDoOverRelaxation() {
		return doOverRelaxation;
	}

	public void setDoOverRelaxation(boolean doOverRelaxation) {
		this.doOverRelaxation = doOverRelaxation;
	}
	//Method to set the SOR constant value
	public void setW(double in){
		w = in;
	}


	
	
}//Class brackets
