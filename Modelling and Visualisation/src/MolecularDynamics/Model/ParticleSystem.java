package MolecularDynamics.Model;

import java.util.Random;

import MolecularDynamics.Model.Potential.Potential;


public class ParticleSystem {

	int N; //Num of particles in system
	double [] box; //Dimensions of box the particles is simulated in

	double 	time =0.0,
			timestep = 0.002;
	Random rand = new Random();
	double mass = 1.0, radius = 1.0; //Default mass and radius to use
	
	//Temperature constants for system
	double 	initialT = 1.0,
			temperature = initialT, 
			targetTemperature = initialT;
	
	double volumeFraction = 0.0;
	double PE, KE;
	
	Particle [] particles;
	Potential potential;
	
/////////////////////////////    CONSTRUCTOR		//////////////////////////
	
	public ParticleSystem(int N, double [] boxSize, Potential inPotential){
		//Minimum image convention
		this.N = N;
		potential = inPotential;
		box = new double[boxSize.length];
		
		for(int i=0;i<box.length;i++){box[i] = boxSize[i];} //Set the boxsize
		
		//Set the minimum image size for potential
		for(int i=0;i<box.length;i++){if(box[i] < 2.0* Constants.r_cutoff) box[i] = 2.0* Constants.r_cutoff;} 
		
		//Create array of particles and give particles same velocity in random orientations
		generateParticles();
		generateVelocities();
		
		//Place particles on lattice
		initialiseParticlePositions();
		
		//Initialise the Forces on all the particles
		calculateSystemForces();
		
		//Calculate the volume fraction of the system
		if(box.length == 2) volumeFraction = (N * Math.PI * Constants.sigma * Constants.sigma) / (4.0 * box[0] * box[1]);
		if(box.length == 3) volumeFraction = (N * Math.PI * Constants.sigma * Constants.sigma * Constants.sigma) / (6 * box[0] * box[1] * box[2]);
		
	} // Constructor brackets
	
//////////////////////// END OF CONSTRUCTOR ////////////////////////////	
	
	
	//Method used in the constructor to generator particles
	private void generateParticles(){
		particles = new Particle[N];
		for(int i=0; i<N; i++){
			particles[i] = new Particle(mass, radius, box); //Note, this will only give 2-D case, check Particle constructor
		}
	}
	
	//Method to generate velocities from a Maxwell-Boltzmann dist.
	public void generateVelocities(){
		Random rand = new Random();
		double [] vel = new double[box.length];
		for(int i=0; i<vel.length; i++){
			vel[i] = 0.0;
		}
		Vector totalMomentum = new Vector(vel);
		for(int i = 0; i< N; i++){			//Loop over particles
			double factor = Math.sqrt(targetTemperature*Constants.boltzmannK/particles[i].getMass());
			for(int j=0; j<box.length;j++){ //Loop over dimensions
				vel[j] = rand.nextGaussian() * factor;
			}
			particles[i].setVelocity(new Vector(vel));
			totalMomentum = Vector.addVector(totalMomentum, Vector.scalarProduct(particles[i].getMass(), particles[i].getVelocity()));
		}
		Vector momPerParticle = Vector.scalarProduct((1.0/(double)N), totalMomentum); //Find the momentum per particle of total system momentum
		double sumForTemperature = 0.0;
		for(int i = 0; i< N; i++){	
			Vector changeToVel = Vector.scalarProduct(1.0 / particles[i].getMass(), momPerParticle);
			particles[i].setVelocity(Vector.addVector(particles[i].getVelocity(), Vector.scalarProduct(-1.0, changeToVel)));//Take away small bit of mom from each particle
			sumForTemperature += (particles[i].getMass()) * (particles[i].getVelocity().getMagSquared());
		}
		
		//System.out.println("In initialisation"+sumForTemperature);
		double temperatureFromInitialising = sumForTemperature / (Constants.boltzmannK * box.length * (N - 1));
		updateTemperature(temperatureFromInitialising);
		System.out.println("Initial Temperature: "+temperatureFromInitialising);
	}
	
	private void initialiseParticlePositions(){
		
		if(!potential.isPolymer()) doNormalPositions();
		else doPolymerPositionsPacked();
	
	}//Method bracket
	
	private void doPolymerPositionsPacked(){
		//Find number of lattice sites
		int latticeSites = 1;
		for(int i=0; i<box.length; i++){
			latticeSites = (int)(latticeSites * box[i]);
		}
		System.out.println("Number of lattice sites"+latticeSites);
		//Do check for lattice sites and number of potential particles
		if(latticeSites < (int)N*radius){
			System.out.println("Error in number of initial lattice sites");
			System.out.println(latticeSites+"\t"+N+"\t"+radius);
		}
		double x = 0;
		double y = 0;
		boolean forward = true;
		for(int i=0; i<N; i++){
			double [] pos = new double[box.length];
			Vector position;
			double distance = 1.0;
			
			
			if(forward && (x + distance)  > box[0]){
				System.out.println("distance "+(x+distance)+"box length"+box[0]);
				forward = !forward;  //Acts as XOR switch, to change from true to false or false to true
				y += distance;
			}else if(!forward && (x - distance)  < 0){
				System.out.println("distance "+(x-distance)+"box length"+box[0]);
				forward = !forward;  //Acts as XOR switch, to change from true to false or false to true
				y += distance;
			}
			if(forward)x += distance;
			else x -= distance;
				
			pos[0] = x;
			pos[1] = y;
			if(box.length == 3) pos[2] = 0;
		
			position = new Vector(pos);
			particles[i].setPosition(position);
		}
	}

	private void doPolymerPositionsUnpacked(){
		//Find number of lattice sites
		int latticeSites = 1;
		for(int i=0; i<box.length; i++){
			latticeSites = (int)(latticeSites * box[i]);
		}
		System.out.println("Number of lattice sites"+latticeSites);
		//Do check for lattice sites and number of potential particles
		if(latticeSites < (int)N*radius){
			System.out.println("Error in number of initial lattice sites");
			System.out.println(latticeSites+"\t"+N+"\t"+radius);
		}
		double x = 0;
		double y = 0;
		boolean forward = true;
		boolean up = false;
		for(int i=0; i<N; i++){
			double [] pos = new double[box.length];
			Vector position;
			double distance = 1.0;
			
			if(forward && !up && (x + distance)  > box[0]){
				up = true;
			}else if(forward && up && (y + distance > box[1])){
				forward = false;
				up = false;
			}
			else if(!forward && !up && (x-distance) < 0 ){
				up = true;
			}
			
			/*
			if(forward && !up && (x + distance)  > box[0]){
				System.out.println("distance "+(x+distance)+"box length"+box[0]);
				forward = !forward;  //Acts as XOR switch, to change from true to false or false to true
				y += distance;
			}else if(!forward && !up && (x - distance)  < 0){
				System.out.println("distance "+(x-distance)+"box length"+box[0]);
				forward = !forward;  //Acts as XOR switch, to change from true to false or false to true
				y += distance;
			}else
			*/
			
			if(forward && !up)x += distance;
			else if(!forward && !up) x -= distance;
			else if(forward && up) y += distance;
			else if(!forward && up) y -= distance;
				
			pos[0] = x;
			pos[1] = y;
			if(box.length == 3) pos[2] = 0;
		
			position = new Vector(pos);
			particles[i].setPosition(position);
			System.out.println("position "+position);
		}
	}
	
	private void doNormalPositions(){
		//Find number of lattice sites
		int latticeSites = 1;
		for(int i=0; i<box.length; i++){
			latticeSites = (int)(latticeSites * box[i]);
		}
		if(latticeSites < (int)N*radius){
			System.out.println("Error in number of initial lattice sites");
			System.out.println(latticeSites+"\t"+N+"\t"+radius);
		}
		for(int i=0; i<N; i++){	//Loop over all particles
			double [] pos = new double[box.length];
			Vector position;
			boolean okPosition = false; //Boolean that notes if particle has already been placed there
			while(okPosition == false){	//Loop until position that has not been filled before is found
				//Create a random position vector in box
				for(int j=0; j<box.length; j++){
					Random rand = new Random();
					pos[j] = rand.nextInt((int)box[j]); //generates a random position vector with integer coords within box
					position = new Vector(pos);
					particles[i].setPosition(position);
				}
					//Check this vector has not been used before
					//If it has, generated another one
					boolean test = true;
					for(int k=0; k < i; k++){
						if(particles[i].getPosition().isEqual(particles[k].getPosition()) == true) {
							test = false;
							//System.out.println("Breaking");
							break;
							}else{
								test = true;
							}
					}
					okPosition = test;
			}//While loop
		}//Loop over particles
	}
	
	
	//Method to calculate all the force on each particle in the system
	//Need to ensure force vector is zero before using this method
	public void calculateSystemForces(){
		double tempPE = 0.0;
		//Loop to set forces to zero to allow additive method below
		for(int i=0; i<N; i++){
			particles[i].restartForce(); //Also sets PE to zero to allow recalculation with force
		}
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				if(j > i){ //So that interactions are not double counted
					Vector force;
					
					//If n.n. in chain, then does full polymer interaction
					//If not, then polymer constant is zero and only vdw interaction is done
					if(potential.isPolymer()){
						if(j == i+1)	Constants.setPolymerK(10 * temperature * Constants.boltzmannK / (Constants.sigma * Constants.sigma));
						else			Constants.setPolymerK(0.0);	//Makes polymer chain contribution zero
						//System.out.println("PolymerK "+Constants.polymerK+" for i "+i+" j "+j);
					}
					
					force = potential.calculateForce(particles[i], particles[j], box);
					double pairPotential = potential.calculatePotential(particles[i], particles[j], box);
					particles[i].addForce(force);
					particles[i].addToPE(pairPotential);
			
					particles[j].addForce(Vector.scalarProduct(-1.0, force));
					particles[j].addToPE(pairPotential);
				
				}
			}
			tempPE += particles[i].getPE();
		}
		updatePE(tempPE);
	}
	
	
	//Method to use the periodic boundary conditions
	public void doPBC(int in){
		double [] pos = new double[box.length];
		for(int i=0;i<box.length;i++){
			pos[i] = particles[in].getPosition().getComponent(i);
			pos[i] = (pos[i]+box[i])%box[i];
		}
		particles[in].setPosition(new Vector(pos));
		//if(( ==0) p;
		//if(yPos > y) particles[i].setPosition(new Vector(new double[]{xPos,yPos-y}));
	}
	
	//Method to generate system based on a required volume fraction
	public static ParticleSystem setVolumeFraction(ParticleSystem particles, double in){
		if(in > 1.0) in = particles.getVolumeFraction();
		ParticleSystem newSystem = particles;
		if(particles.getBox().length == 2){
			int newN =(int)((4.0 * particles.getBox()[0] * particles.getBox()[1] * in) / (Math.PI * Constants.sigma * Constants.sigma));
			newSystem = new ParticleSystem(newN, particles.getBox(), particles.getPotential());
		}
		if(particles.getBox().length == 3){
			int newN =(int)((6.0 * particles.getBox()[0] * particles.getBox()[1] * particles.getBox()[2] * in) / (Math.PI * Constants.sigma * Constants.sigma * Constants.sigma));
			newSystem = new ParticleSystem(newN, particles.getBox(), particles.getPotential());
		}
		System.out.println("newN = "+newSystem.getParticleNumber());
		return newSystem;
	}

	//Method to calculate the current temperature in the system
	public void updateCurrent_Temperature_KE(){
		double sumT =0.0;
		double sumKE = 0.0;
		for(int i=0; i<N; i++){
			sumT += particles[i].getMass() * particles[i].getVelocity().getMagSquared();
			sumKE += particles[i].getKE();
		}
		double temp = sumT / (Constants.boltzmannK * box.length * (N - 1));
		updateTemperature(temp);
		updateKE(sumKE);
	}
	
	/////////////// Basic GETTERS and SETTERS //////////////
	
	public Potential getPotential(){
		return potential;
	}
	public void setPotential(Potential in){
		potential = in;
	}
	
	public int getParticleNumber(){
		return N;
	}
	public Particle getParticle(int n){
		return particles[n];
	}
	public double getTimestep(){
		return timestep;
	}
	public void setTimestep(double in){
		timestep = in;
	}
	public double getTime(){
		return time;
	}
	public void iterateTime(){
		time += timestep;
	}
	public void updateTemperature(double in){
		temperature = in;
	}
	public double getTemperature(){
		return temperature;
	}
	public double getTargetTemperature(){
		return targetTemperature;
	}
	public void setTargetTemperature(double in) {
		targetTemperature = in;
		System.out.println("Setting target temperature to "+targetTemperature);
	}
	public double getBox(int n){
		return box[n];
	}
	public double [] getBox(){
		return box;
	}
	public void updateKE(double in){
		KE = in;
	}
	public void updatePE(double in){
		PE = in;
	}
	public double getTotalE(){
		return PE+KE;
	}
	public double getPE(){
		return PE;
	}
	public double getKE(){
		return KE;
	}

	public double getVolumeFraction() {
		return volumeFraction;
	}

	public void setParticles(Particle [] in){
		if(in.length != particles.length) return;
		for(int i=0;i<in.length;i++){
			particles[i] = in[i];
		}
	}


	
}//Class brackets
