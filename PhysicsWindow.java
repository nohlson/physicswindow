package graphx;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.ActionEvent;

public class PhysicsWindow extends JComponent implements ActionListener
{
	
    private int ballX = 0;
    private int ballY = 200;
    private int startY = 200;
    private double timestep = .05;
    private double acceleration = -9.8;
    private double yVelocity = 50;
    private double xVelocity = 10;
    private double positionY;
    private double positionX;
    private double diff;
	private double prevBallX;
	protected double prevBallY;
    private static int windowSizeWidth = 1200;
    private static int windowSizeHeight = 600;
    private static double velocityDecay = 1.2; //bouncy-ness
    private static Timer tm;
    private int[][] lines = {{50, 302, 423, 200}, {100, 350, 423, 100}};
    

    
    public void paint(Graphics g)  
    {          

        super.paintComponent(g); 
        
        g.drawString("Velocity:", 5, 10);
        g.drawString(String.valueOf((int) Math.round(yVelocity)) , 5, 20);
        g.drawString("Position    dP", 5, 30);
        g.drawString(String.valueOf((int) Math.round(ballY)), 5, 40);
        g.drawString(String.valueOf((int) Math.round(positionY)), 75, 40);
        
        for (int i = 0; i < lines.length; i++) {
        	 g.drawLine(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
        }

        
        
        
        g.fillOval(ballX, ballY, 5, 5);
       
        
    }
    
    private double dotProduct(double uXComponent, double uYComponent, 
    		double vXcomponent, double vYComponent) {
    	//TODO use dotproduct and inverse cosine to return radian theta between two vectors
    	return 0.0;
    }
    
    private void collisionDetection() {
    	for (int i = 0; i < lines.length; i++) {
    		//between x
    		int minX;
    		int maxX;
    		if (lines[i][0] <= lines[i][2]) {
    			maxX = lines[i][2];
    			minX = lines[i][0];
    		} else {
    			maxX = lines[i][0];
    			minX = lines[i][2];
    		}
    		//between Y
    		int minY;
    		int maxY;
    		if (lines[i][1] <= lines[i][3]) {
    			minY = lines[i][1];
    			maxY = lines[i][3];
    		} else {
    			minY = lines[i][3];
    			maxY = lines[i][1];
    		}
    		////
    		//    	System.out.println(Math.abs(ballX - maxX));
    		double slope = (double)(maxY - minY) / (double)(maxX - minX);
    		slope = slope * -1;
    		int yIntercept = (int) Math.round((double) lines[i][1] - (slope * lines[i][0]));

    		if (ballX <= maxX && ballX >= minX) {
    			if (Math.abs((ballX * slope) - ballY + yIntercept) < 10) {
    				// TODO do dot product and recalculate constituent velocity parameters
    				System.out.println("collision");
    				yVelocity = -1 * (yVelocity / velocityDecay);
    			}
    		}
    	}
    }


    public void actionPerformed(ActionEvent e) {
    	new Thread() {
    		

			public void run() {
    			positionY += timestep * (yVelocity + timestep * acceleration / 2); //dt * t for y (includes gravity)
    			positionX += timestep * xVelocity; // dt*t
    			
    			yVelocity += timestep * acceleration;
    			//xVelocity = xVelocity (implied)
    			
//    			diff = startY + positionY; // TODO replace this collision detection with new
//    			if (diff < 0 ) {
//    				positionY = Math.abs(diff) - startY;
//    				yVelocity = -1 * (yVelocity / velocityDecay);
//    			}
    			
    			
    			prevBallY = ballY; 
    			prevBallX = ballX;
    			
    			ballY = (int) Math.round((-1 * positionY) + startY); //due to gravity y component is tracked from startY position
    			ballX = (int) Math.round(positionX);
    			
    			collisionDetection();
    			
    			
    		}
    	}.start();

        repaint();
    }
    

    public static void main(String[] args)
    {
        PhysicsWindow panel = new PhysicsWindow();                            
        JFrame application = new JFrame();
        application.add(panel);
        application.pack();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setLocationRelativeTo(null);
        
        application.setSize(windowSizeWidth, windowSizeHeight);            
        application.setVisible(true);
        tm  = new Timer(10, panel);
        tm.start();
    }
}
