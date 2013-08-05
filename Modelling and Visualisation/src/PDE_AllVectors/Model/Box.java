package PDE_AllVectors.Model;

public class Box {

	double[] boxSize = new double[]{0.0,0.0,0.0};

	Cell [][][] fullSpace;			//The 3D simulated space
	int [] numCells = new int[]{0,0,0}; 	//Array that counts the number of cells in each direction
	int totalCellNumber = 1;				//Number of cells in each dimension (needs to be 1 for multiply
											//in loop in constructor
	
	int potentialDimensions;
	double dx = 0.05;	//The discretised size in time and space
	
	/**
	 * MUST BE 3D
	 * 
	 * Cells of interest are from 1 up to numCells in each dimension
	 * 
	 * @param boxX
	 * @param boxY
	 * @param boxZ
	 */
	public Box(double boxX, double boxY,  double boxZ, int potentialDimensions){
		
		this.potentialDimensions = potentialDimensions;
		
		boxSize[0] = boxX;
		boxSize[1] = boxY;
		boxSize[2] = boxZ;
		for(int i=0;i<numCells.length; i++){
			numCells[i] = (int) (boxSize[i] / dx);
			totalCellNumber *= numCells[i];
		}
		
		fullSpace = new Cell[numCells[0]+2][numCells[1]+2][numCells[2]+2];		//+2 to create extra cells that contain boundary conditions
		
		//Set everything to zero everywhere
		for(int i=0; i < fullSpace.length; i++){
			for(int j=0; j < fullSpace[i].length; j++){
				for(int k=0; k < fullSpace[i][j].length; k++){
					fullSpace[i][j][k] = new Cell(potentialDimensions);		//Initially set the charge, potential and field at each point to zero
				}
			}
		}	
	}
	
	//Method to set the potential at the boundaries to zero
	public void setPotenialBC(){
		for(int i=0; i < fullSpace.length; i++){
			for(int j=0; j < fullSpace[i].length; j++){
				for(int k=0; k < fullSpace[i][j].length; k++){
					for(int m=0; m< potentialDimensions; m++){
						fullSpace[0][j][k].setPotential(m,0.0);
						fullSpace[i][0][k].setPotential(m,0.0);
						fullSpace[i][j][0].setPotential(m,0.0);
					}
					for(int m=0; m< potentialDimensions; m++){
						fullSpace[fullSpace.length - 1][j][k].setPotential(m,0.0);
						fullSpace[i][fullSpace[i].length - 1][k].setPotential(m,0.0);
						fullSpace[i][j][fullSpace[i][j].length - 1].setPotential(m,0.0);
					}
					
					
				}
			 }
		}
	}
	
	//Method to create a single unit charge in the centre of the box
	public void setCentralUnitCharge(){
		//Find the centre cell of the box
		//Or one closest if even numbered
		int mid0 = numCells[0] / 2;
		int mid1 = numCells[1] / 2;
		int mid2 = numCells[2] / 2;
		for(int m=0; m< potentialDimensions; m++){
			fullSpace[mid0][mid1][mid2].setCharge(m,1.0);
		}

	}
	
	public void setCentralQuadrapole(){
		int mid0 = numCells[0] / 2;
		int mid1 = numCells[1] / 2;
		int mid2 = numCells[2] / 2;
		/*for(int i=0; i < cellLength; i++){
			space[i][mid][mid].setCharge(1000.0);
		}*/
		for(int m=0; m< potentialDimensions; m++){
			fullSpace[mid0][mid1][mid2].setCharge(m,-1.0);
			fullSpace[mid0+5][mid1][mid2].setCharge(m,1.0);
			fullSpace[mid0][mid1+5][mid2].setCharge(m,1.0);
			fullSpace[mid0+5][mid1+5][mid2].setCharge(m,-1.0);
		}	
	}
	
	public void setCentralWire(){
		int mid = numCells[0] / 2;
			for(int k=1; k < fullSpace.length - 2; k++ ){
					fullSpace[mid][mid][k].setCharge(2,1.0); //every cell in line down centre has z-component of 
															//"charge" i.e. current set to 1
			
				}
	}
	

			//////GETTERS and SETTERS ////
	
	public int getPotentialDimensions() {
		return potentialDimensions;
	}

	public void setPotentialDimensions(int potentialDimensions) {
		this.potentialDimensions = potentialDimensions;
	}

	public double[] getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(double[] boxSize) {
		this.boxSize = boxSize;
	}

	public int[] getNumCells() {
		return numCells;
	}

	public void setNumCells(int[] numCells) {
		this.numCells = numCells;
	}

	public int getTotalCellNumber() {
		return totalCellNumber;
	}

	public void setTotalCellNumber(int totalCellNumber) {
		this.totalCellNumber = totalCellNumber;
	}

	public Cell getCell(int i, int j, int k){
		return fullSpace[i][j][k];
	}
	
	public Cell[][][] getSpace() {
		return fullSpace;
	}

	public void setSpace(Cell[][][] space) {
		this.fullSpace = space;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}
	
}//Class brackets
