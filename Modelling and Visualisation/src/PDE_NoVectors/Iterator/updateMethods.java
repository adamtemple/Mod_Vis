package PDE_NoVectors.Iterator;

import PDE_NoVectors.Model.Box;
import PDE_NoVectors.Model.Vector;

public class updateMethods {

	/**
	 * Method to update the scalar potential in each cell
	 * @param space
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	
	public static double updateScalarPotential(Box space, int i, int j, int k){
		return	(1.0/6.0)*(  space.getCell(i+1, j, k).getScalarPotential() + space.getCell(i-1, j, k).getScalarPotential()
				+space.getCell(i, j+1, k).getScalarPotential() + space.getCell(i, j-1, k).getScalarPotential()
				+ space.getCell(i, j, k+1).getScalarPotential() + space.getCell(i, j, k-1).getScalarPotential()
				+ (space.getDx() * space.getDx() * space.getCell(i, j, k).getCharge())  );
	}
	
	
	public static Vector updateVectorPotential(Box space, int i, int j, int k){
		
		Vector potential = new Vector(new double[]{0.0,0.0,0.0});
		for(int m=0; m<3; m++){
			potential.setComponent(m, (1.0/6.0)*(  space.getCell(i+1, j, k).getVectorPotential().getComponent(m) + space.getCell(i-1, j, k).getVectorPotential().getComponent(m)
					+space.getCell(i, j+1, k).getVectorPotential().getComponent(m) + space.getCell(i, j-1, k).getVectorPotential().getComponent(m)
					+ space.getCell(i, j, k+1).getVectorPotential().getComponent(m) + space.getCell(i, j, k-1).getVectorPotential().getComponent(m)
					+ (space.getDx() * space.getDx() * space.getCell(i, j, k).getVectorCharge().getComponent(m))  )
					);
		}
			return potential;
	}
	
	public static Vector divScalarField(Box space, int i, int j, int k){
		
		double xField = ( space.getCell(i+1, j, k).getScalarPotential() - space.getCell(i-1, j, k).getScalarPotential() )
						/ ( 2.0 * space.getDx());
		double yField =  ( space.getCell(i, j+1, k).getScalarPotential() - space.getCell(i, j-1, k).getScalarPotential() )
						/ ( 2.0 * space.getDx());
		double zField =  ( space.getCell(i, j, k+1).getScalarPotential() - space.getCell(i, j, k-1).getScalarPotential() )
						/ ( 2.0 * space.getDx());
		
		return new Vector(new double[]{xField,yField,zField});
		
	}
	
	public static Vector curlVectorField(Box space, int i, int j, int k){
		double x1 = space.getCell(i, j+1, k).getVectorPotential().getComponent(2) - space.getCell(i, j-1, k).getVectorPotential().getComponent(2);
		double x2 = space.getCell(i, j, k+1).getVectorPotential().getComponent(1) - space.getCell(i, j, k-1).getVectorPotential().getComponent(1);
		
		double y1 = space.getCell(i, j, k+1).getVectorPotential().getComponent(0) - space.getCell(i, j, k-1).getVectorPotential().getComponent(0);
		double y2 = space.getCell(i+1, j, k).getVectorPotential().getComponent(2) - space.getCell(i-1, j, k).getVectorPotential().getComponent(2);
		
		double z1 = space.getCell(i+1, j, k).getVectorPotential().getComponent(1) - space.getCell(i-1, j, k).getVectorPotential().getComponent(1);
		double z2 = space.getCell(i, j+1, k).getVectorPotential().getComponent(0) - space.getCell(i, j-1, k).getVectorPotential().getComponent(0);
		
		double factor = 2.0 * space.getDx();
		
		return new Vector(new double[]{ (x1-x2)/factor , (y1-y2)/factor, (z1-z2)/factor });
	}
	
	
}
