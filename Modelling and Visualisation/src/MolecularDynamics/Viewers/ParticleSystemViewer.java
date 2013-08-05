package MolecularDynamics.Viewers;
import java.awt.Color;
import java.awt.Graphics;


import javax.swing.JComponent;

import MolecularDynamics.Model.ParticleSystem;
import MolecularDynamics.Model.Vector;

public class ParticleSystemViewer extends JComponent{


		private ParticleSystem particles;
		double [] box;
		  

		private int N;
		

		public ParticleSystemViewer(ParticleSystem in) {
			particles = in;
			N = particles.getParticleNumber();
			box = particles.getBox();
			setOpaque(true);
			setBackground(Color.WHITE);
			
		}
		
		
		
		/**
		 * This is the method in which all the drawing happens. It is called automatically by Swing when the component needs to
		 * be drawn or updated.
		 * 
		 * @param g current graphics context (an object that provides the drawing methods)
		 */
		@Override
		protected void paintComponent(Graphics g) {
			// First, get the dimensions of the component
				int width = getWidth(), height = getHeight();
				
				
				
			// Clear the background if we are an opaque component
			if(isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(0, 0, width, height);
			}
			//Take size of component and make rectangles of this size 
			//for each spin

			//System.out.println("width "+width+" Height "+height+" N "+N);
			double xScaling = (width)/box[0];
			double yScaling = (height)/box[1];
			//int ballWidth = (int)(xScaling), ballHeight = (int)(yScaling);
			//g.setColor(Color.BLACK);
			//g.drawRect(0, 0, width - 50, height -50);
			
			
			synchronized(particles){
			for(int i=0;i<N;i++){
				int ballWidth = (int)(xScaling), ballHeight = (int)(yScaling);
				g.setColor(Color.RED);
				if(i==0) g.setColor(Color.BLUE);
				Vector position = particles.getParticle(i).getPosition();
				int xPos = (int) width - (int)(xScaling*position.getComponent(0));
				int yPos = (int) height - (int)(yScaling*position.getComponent(1));
				if(box.length == 3.0){
					double zScaling  = (position.getComponent(2) - box[2]/2.0) / box[2]/2.0;
					if(zScaling < 0.0) zScaling = zScaling*(-1.0);
					if(zScaling >1.0) System.out.println("zScaling is incorrect");
					ballWidth *= zScaling;
					ballHeight *= zScaling;
				}
				
				xPos = xPos - ballWidth/2;
				yPos = yPos - ballHeight/2;
				
				g.fillOval(xPos, yPos , ballWidth, ballHeight);
				
				//This code allows the balls to smoothly move across the boundary conditions
				if(xPos+ballWidth > width)g.fillOval(xPos-width, yPos, ballWidth, ballHeight);
				if(yPos+ballHeight > height)g.fillOval(xPos, yPos-height, ballWidth, ballHeight);
				
				if(xPos-ballWidth < width)g.fillOval(xPos+width, yPos, ballWidth, ballHeight);
				if(yPos-ballHeight < height)g.fillOval(xPos, yPos+height, ballWidth, ballHeight);
				
				//Code to draw lines for polymer
				if(particles.getPotential().isPolymer() && i!= particles.getParticleNumber() - 1){
					g.setColor(Color.BLACK);
					Vector position2 = particles.getParticle(i+1).getPosition();
				
					int xPos2 = (int)width - (int)(xScaling*position2.getComponent(0));
					int yPos2 = (int)height - (int)(yScaling*position2.getComponent(1));
					
					//xPos2 = xPos2 + ballWidth/2;
					//yPos2 = yPos2 + ballHeight/2;
					
					g.drawLine(xPos+ballWidth/2, yPos + ballWidth/2, xPos2 , yPos2);
					//g.drawRect(xPos2, yPos2, ballWidth, ballHeight);
				}
				
				
			particles.notifyAll();
			}
		}
		
		
	}//paintComponent brackets


	
	
	
}
