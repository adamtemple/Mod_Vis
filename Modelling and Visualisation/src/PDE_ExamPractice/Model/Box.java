package PDE_ExamPractice.Model;

public class Box {

	int N;									//Number of cells in x and y

	Cell [][] fullSpace;					//The 3D simulated space
					//Number of cells in each dimension (needs to be 1 for multiply
											//in loop in constructor
	
	double radius;
	
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
	public Box(int in, double radius){
		
		this.radius = radius;
		N = in;
		/*
		boxSize[0] = boxX;
		boxSize[1] = boxXs;
		for(int i=0;i<numCells.length; i++){
			numCells[i] = (int) (boxSize[i] / dx);
			if(numCells[i] % 2 == 0) numCells[i] += 1;		//Makes sure that there is always an odd number of cells in each row
															//Will always have a cell defined at the centre
			totalCellNumber *= numCells[i];
		}
		*/
		if(N % 2 == 0) N +=1;
		fullSpace = new Cell[N][N];		//+2 to create extra cells that contain boundary conditions
		
		//Set everything to zero everywhere
		for(int i=0; i < fullSpace.length; i++){
			for(int j=0; j < fullSpace[i].length; j++){
					fullSpace[i][j] = new Cell();		//Initially set the charge, potential and field at each point to zero
				}
			}
	}
	
	//Method to set the potential at the boundaries to zero
	public void setPotenialBC(){
		for(int i=0; i < N; i++){
			for(int j=0; j < N; j++){
					
						fullSpace[0][j].setScalarPotential(0.0);
						fullSpace[i][0].setScalarPotential(0.0);
						fullSpace[i][j].setScalarPotential(0.0);
					
						fullSpace[fullSpace.length - 1][j].setScalarPotential(0.0);
						fullSpace[i][fullSpace[i].length - 1].setScalarPotential(0.0);
						fullSpace[i][j].setScalarPotential(0.0);
				
				}
			 }
	}
	
	//Method to create a single unit charge in the centre of the box
	public void setCentralDroplet(){
		//Find the centre cell of the box
		//Or one closest if even numbered
		int mid0 = (fullSpace.length - 2)/ 2;
		int mid1 = (fullSpace[0].length - 2) / 2;
		
		double x, y;
		
		for(int i=0; i< N; i++){
			for(int j=0; j< N; j++){
				x = i - mid0;
				y = j - mid1;
				double mag = Math.sqrt( (x*x) + (y*y) );
				if(mag <= radius) {
					fullSpace[i][j].setScalarPotential(1.0);
				}
			}
		}
	}

	/**
	 * Method to do the periodic boundary conditions
	 * @param in value to do PBC on
	 * @return
	 */
	private int doPBC(int in){
		return ((in+N)%N);
	}
	
	
			//////GETTERS and SETTERS ////
	

/*
	public double[] getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(double[] boxSize) {
		this.boxSize = boxSize;
	}
*/
	public int getTotalCellNumber(){
		return N*N;
	}

	public Cell getCell(int i, int j){
		return fullSpace[doPBC(i)][doPBC(j)];
	}
	
	public Cell[][] getSpace() {
		return fullSpace;
	}

	public void setSpace(Cell[][] space) {
		this.fullSpace = space;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}
	
}//Class brackets
