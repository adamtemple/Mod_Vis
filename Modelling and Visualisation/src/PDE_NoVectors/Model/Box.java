package PDE_NoVectors.Model;

public class Box {

	double[] boxSize = new double[]{0.0,0.0,0.0};

	Cell [][][] fullSpace;					//The 3D simulated space
	int totalCellNumber = 1;				//Number of cells in each dimension (needs to be 1 for multiply
											//in loop in constructor
	
	
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
	
	public Box(double boxX, double boxY,  double boxZ){
		
		int [] numCells = new int[]{0,0,0}; 	//Array that counts the number of cells in each direction
		boxSize[0] = boxX;
		boxSize[1] = boxY;
		boxSize[2] = boxZ;
		for(int i=0;i<numCells.length; i++){
			numCells[i] = (int) (boxSize[i] / dx);
			totalCellNumber *= numCells[i];		//Need to change if only looking in 2D
		}
		
		fullSpace = new Cell[numCells[0]+2][numCells[1]+2][numCells[2]+2];		//+2 to create extra cells that contain boundary conditions
		
		//Set everything to zero everywhere
		for(int i=0; i < fullSpace.length; i++){
			for(int j=0; j < fullSpace[i].length; j++){
				for(int k=0; k < fullSpace[i][j].length; k++){
					fullSpace[i][j][k] = new Cell();		//Initially set the charge, potential and field at each point to zero
				}
			}
		}	
	}	
	
	public Box(int x, int y, int z){
		totalCellNumber = x*y;
		
		if(z != 0) totalCellNumber *= z;
		dx = 1;
		if(z !=0)fullSpace = new Cell[x+2][y+2][z+2];
		else fullSpace = new Cell[x+2][y+2][z+2];
		
		//Set everything to zero everywhere
		System.out.println(""+x+"x"+y+"x"+z+"\t Total Cell number "+totalCellNumber);
		for(int i=0; i < fullSpace.length; i++){
			for(int j=0; j < fullSpace[i].length; j++){
				for(int k=0; k < fullSpace[i][j].length; k++){
					fullSpace[i][j][k] = new Cell();		//Initially set the charge, potential and field at each point to zero
				}
			}
		}
		//setCentralUnitCharge();
	}
	
	//Method to set the potential at the boundaries to zero
	public void setPotenialBC(){
		for(int i=0; i < fullSpace.length; i++){
			for(int j=0; j < fullSpace[i].length; j++){
				for(int k=0; k < fullSpace[i][j].length; k++){
					
						fullSpace[0][j][k].setScalarPotential(0.0);
						fullSpace[i][0][k].setScalarPotential(0.0);
						fullSpace[i][j][0].setScalarPotential(0.0);
					
						fullSpace[fullSpace.length - 1][j][k].setScalarPotential(0.0);
						fullSpace[i][fullSpace[i].length - 1][k].setScalarPotential(0.0);
						fullSpace[i][j][fullSpace[i][j].length - 1].setScalarPotential(0.0);
				
				}
			 }
		}
	}
	
	//Method to create a single unit charge in the centre of the box
	public void setCentralUnitCharge(){
		//Find the centre cell of the box
		//Or one closest if even numbered
		int mid0 = (fullSpace.length - 2)/ 2;
		int mid1 = (fullSpace[0].length - 2) / 2;
		int mid2 = (fullSpace[0][0].length - 2) / 2;
		
		fullSpace[mid0][0+10][mid2].setCharge(1.0);
	}
	
	public void setCentralQuadrapole(){
		int mid0 = (fullSpace.length - 2)/ 2;
		int mid1 = (fullSpace[0].length - 2) / 2;
		int mid2 = (fullSpace[0][0].length - 2) / 2;
	
			fullSpace[mid0][mid1][mid2].setCharge(-1.0);
			fullSpace[mid0+5][mid1][mid2].setCharge(1.0);
			fullSpace[mid0][mid1+5][mid2].setCharge(1.0);
			fullSpace[mid0+5][mid1+5][mid2].setCharge(-1.0);
		}

	
	public void setCentralWire(){
		int mid0 = (fullSpace.length - 2)/ 2;
		int mid1 = (fullSpace[0].length - 2) / 2;
		//int mid0 = numCells[0] / 2;
		//int mid1 = numCells[1] / 2;
		
		for(int k=1; k< (fullSpace.length - 1); k++){
			//Set z component of current in each of the central cells to 1
			fullSpace[mid0][mid1][k].setVectorCharge(new Vector(new double[]{0.0,0.0,1.0}));
		}
	}
	public void setTwoCentralWires(){
		int mid0 = (fullSpace.length - 2)/ 2;
		int mid1 = (fullSpace[0].length - 2) / 2;
		//int mid0 = numCells[0] / 2;
		//int mid1 = numCells[1] / 2;
		
		for(int k=1; k< (fullSpace.length - 1); k++){
			//Set z component of current in each of the central cells to 1
			fullSpace[mid0][mid1][k].setVectorCharge(new Vector(new double[]{0.0,0.0,1.0}));
			fullSpace[mid0][mid1+5][k].setVectorCharge(new Vector(new double[]{0.0,0.0,-1.0}));
		}
	}
	
	
	
			//////GETTERS and SETTERS ////
	


	public double[] getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(double[] boxSize) {
		this.boxSize = boxSize;
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
