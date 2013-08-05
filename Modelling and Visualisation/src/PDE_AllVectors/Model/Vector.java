package PDE_AllVectors.Model;


public class Vector {

		int N;		//Size of vector
		double [] vector;
		
		/**
		 * Constructors
		 * @param a
		 * @param b
		 */
		public Vector(double [] in){
			vector = new double[in.length];
			N = in.length;
			for(int i=0; i<N; i++){
				vector[i] = in[i];	
			}		
		}
		public Vector(int n) {
			vector = new double[n];
			N = n;
			for(int i=0; i<vector.length; i++){
				vector[i] = 0.0;
			}
		}
		
		public int getSize(){
			return N;
		}
		
		public void setComponent(int n, double in) {
			//if(n > N-1) return;
			vector[n] = in;
		}
		
		public double getComponent(int n) {
			return vector[n];
		}
		
		public double getMag(){
			double temp = 0.0;
			for(int i=0; i<N; i++){
				temp += (vector[i] * vector[i]);
			}
			return Math.sqrt(temp);
		}
		
		//Same as above without sqrt
		public double getMagSquared(){
			double temp = 0.0;
			for(int i=0; i<N; i++){
				temp += (vector[i] * vector[i]);
			}
			return temp;
		}
		
		//Adds two vectors of any size
		//Can be coupled with scalarProduct to also do subtraction
		public static Vector addVector(Vector a, Vector b) {
			double [] temp = new double[a.getSize()];
			for(int i=0; i<a.getSize(); i++){
				temp[i] = a.getComponent(i) + b.getComponent(i);
			}
			return new Vector(temp);
		}
		
		public static double dotProduct(Vector a, Vector b) {
			double temp = 0.0;
			for(int i=0; i<a.getSize(); i++){
				temp += a.getComponent(i)* b.getComponent(i);
			}
			return temp;
		}
		
		public static Vector scalarProduct(double in, Vector a) {
			double [] temp = new double[a.getSize()];
			for(int i=0; i<a.getSize(); i++){
				temp[i] = in * a.getComponent(i);
			}
			return new Vector(temp);
		}
		
		//Calculates the distance between two particles in a box of a specified size
		//This also implements code to check the minimum image convention
		public static Vector calcParticleDistance(Vector a, Vector b, double [] box){
			double [] dist = new double[a.getSize()];
			for(int i=0; i<a.getSize();i++){
				dist[i] = b.getComponent(i) - a.getComponent(i);
				
				//System.out.println(dist[i]);
			}
			Vector distance = new Vector(dist);
			int [] k = new int [box.length];
			for(int i=0; i<k.length; i++){
				k[i] = (int) (2*distance.getComponent(i) / box[i]);
				if(k[i] != 0) distance.setComponent(i, distance.getComponent(i) + (-k[i] * box[i]) );
			}
			//int kX = (int) (2*distance.getComponent(0) / x);
			//int kY = (int) (2*distance.getComponent(1) / y);
			//if(kX != 0) distance.setComponent(0, distance.getComponent(0) + -kX*x);
			//if(kY != 0) distance.setComponent(1, distance.getComponent(1) + -kY*y);
			return distance;
		}
		public static Vector calcUnitVector(Vector a){
			double [] unit = new double[a.getSize()];
			for(int i=0; i<a.getSize();i++){
				unit[i] = a.getComponent(i) / a.getMag();
			}
			return new Vector(unit);
		}
		
		public String toString(){
			String line = "[";
			for(int i=0; i<N; i++){
				line = line+vector[i]+", ";
			}
			line = line+"]";
			return line;
		}
		
		public boolean isEqual(Vector in){
			if(N != in.getSize()) return false;
			for(int i=0;i<N;i++){
				if(vector[i] != in.getComponent(i)) return false;
			}
			return true;
		}
}
	


