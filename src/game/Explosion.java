package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import util.ColorList;
import util.Drawable;
import util.Sendable;
import util.Updatable;

public class Explosion implements Drawable, Updatable, Sendable {

	private double timeAtStart;
	private double timeSinceStart;
	private boolean isDone;
//	private boolean chained;
	private double maxSize;
//	private double currSize;
	protected double xc, yc;
	private double[][] places;
	private double timelapse = .4;
	private transient Game game;
		
	public Explosion(double x2, double y2, double size, Game game){
		xc = x2;
		yc = y2;
		places = new double[2][32];
		places[0][0] = xc;
		places[1][0] = yc;
		for(int d = 1; d < places[0].length; d++){
			places[0][d] = xc + (Math.random()*20 - 10);
			places[1][d] = yc + (Math.random()*20 - 10);
		}
		timeAtStart = ((double)System.currentTimeMillis())/1000;
		timeSinceStart = 0;
//		chained = false;
		isDone = false;
		maxSize = size;
//		currSize = 0;
		this.game = game;
	}
	public Explosion(double x2, double y2, Game game){
		xc = x2;
		yc = y2;
		places = new double[2][32];
		places[0][0] = xc;
		places[1][0] = yc;
		for(int d = 1; d < places[0].length; d++){
			places[0][d] = xc + (Math.random()*20 - 10);
			places[1][d] = yc + (Math.random()*20 - 10);
		}
		timeAtStart = ((double)System.currentTimeMillis())/1000;
		timeSinceStart = 0;
//		chained = false;
		isDone = false;
		maxSize = 30;
//		currSize = 0;
		this.game = game;
	}
	public Explosion(Point2D center, Game game){
		xc = center.getX();
		yc = center.getY();
		places = new double[2][32];
		places[0][0] = xc;
		places[1][0] = yc;
		for(int d = 1; d < places[0].length; d++){
			places[0][d] = xc + (Math.random()*20 - 10);
			places[1][d] = yc + (Math.random()*20 - 10);
		}
		timeAtStart = ((double)System.currentTimeMillis())/1000;
		timeSinceStart = 0;
//		chained = false;
		isDone = false;
		maxSize = 30;
//		currSize = 0;
		this.game = game;
	}
	@Override
	public void update(){
		timeSinceStart = ((double)System.currentTimeMillis())/1000 - timeAtStart;
		if (isDone){
			game.removeQueue(this);
		}
	}
	
	@Override
	public void draw(Graphics2D g2) {
		Color initColor = g2.getColor();
		// TODO Auto-generated method stub
		double tc = 1 / timelapse;
		draw(g2, timeSinceStart - 0.2, 1, maxSize - (maxSize / 6));
		draw(g2, timeSinceStart - 0.2, 2, maxSize - (maxSize / 6));
		 if(timeSinceStart <= timelapse){
			//g2.setColor(Color.black);
//			g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
			double bleh =Math.sqrt(Math.sqrt(timeSinceStart * tc));
			g2.setColor(ColorList.getColor((int)(255 - 250 * (timeSinceStart * tc/2))));
			g2.fillOval((int)xc - (int)(.5*bleh * maxSize), 
						(int)yc - (int)(.5*bleh * maxSize), 
						(int)(bleh * maxSize), 
						(int)(bleh * maxSize));
		}
		else if(timeSinceStart <= timelapse * 2){
//			g2.setColor(new Color(255, (int)(255 - 250 * (timeSinceStart * tc/2)), 0));
			g2.setColor(ColorList.getColor((int)(255 - 250 * (timeSinceStart * tc/2))));
			double curr = maxSize - maxSize * (timeSinceStart - timelapse);
			g2.fillOval((int)xc - (int)(.5*curr), 
						(int)yc - (int)(.5*curr), 
						(int)(curr), 
						(int)(curr));
		}
		else if(timeSinceStart >= timelapse * 8){
			isDone = true;
		}
		 g2.setColor(initColor);
	}
	public void draw(Graphics2D g2, double time, int place, double size) {
		// TODO Auto-generated method stub
		double tc = 1 / timelapse;
//		System.out.println(place + ": " + time);
		if(size > 2*maxSize/3){
			draw(g2, time - 0.2, place*2 + 1, size - (maxSize/ 6));
			draw(g2, time - 0.2, place * 2 + 2,  size - (maxSize / 6));
		}
		if(time <= 0){
			
		}
		else if(time <= timelapse){
			//g2.setColor(Color.black);
//			g2.setColor(new Color(255, (int)(255 - 255 * (time * tc)), 0));
			g2.setColor(ColorList.getColor((int)(255 - 255 * (time * tc))));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(places[1][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size));
		}
		else if(time <= timelapse * 2){
//			g2.setColor(new Color(255, (int)(255 - 255 * (time/2 * tc)), 0));
			g2.setColor(ColorList.getColor((int)(255 - 255 * (time/2 * tc))));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(size - size * (time - timelapse))), 
						(int)(places[1][place]) - (int)(.5*(size - size * (time - timelapse))), 
						(int)(size - size * (time - timelapse)), 
						(int)(size - size * (time - timelapse)));
		}
//		else if(time >= timelapse * 8){
//			isDone = true;
//		}
	}
	
	/*public boolean timeToChain(){
		timeSinceStart = (double)System.currentTimeMillis()/1000 - timeAtStart;
		System.out.println(timeSinceStart);
		if(timeSinceStart > timelapse/2 && !chained){
			chained = true;
			return true;
		}
		return false;
	}
	
	public Explosion chain(){
		if(maxSize > 14){
			return new Explosion(xc + (Math.random()*20 - 10), yc + (Math.random()*20 - 10), maxSize - 4);
		}
		else{
			return null;
		}
	}
	*/
	public boolean done(){
		return isDone;
	}
	@Override
	public Drawable getProxyClass() {
		return new ProxyExplosion(timeAtStart, timeSinceStart, isDone, maxSize, xc, yc, places, timelapse);
	}
}

class ProxyExplosion implements Serializable, Drawable {
//	private double timeAtStart;
	private double timeSinceStart;
	private double maxSize;
	private double xc;
	private double yc;
	private double[][] places;
	private double timelapse;
	
	public ProxyExplosion(double timeStart, double timeSince, boolean done, double maxSize,
			double xc, double yc, double[][] places, double timelapse) {
//		this.timeAtStart = timeStart;
		this.timeSinceStart = timeSince;
		this.maxSize = maxSize;
		this.xc = xc;
		this.yc = yc;
		this.places = places;
		this.timelapse = timelapse;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		Color initColor = g2.getColor();
		// TODO Auto-generated method stub
		double tc = 1 / timelapse;
		draw(g2, timeSinceStart - 0.2, 1, maxSize - (maxSize / 6));
		draw(g2, timeSinceStart - 0.2, 2, maxSize - (maxSize / 6));
		 if(timeSinceStart <= timelapse){
			g2.setColor(ColorList.getColor((int)(255 - 250 * (timeSinceStart * tc/2))));
			g2.fillOval((int)xc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)yc - (int)(.5*(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize)), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize), 
						(int)(Math.sqrt(Math.sqrt(timeSinceStart * tc)) * maxSize));
		}
		else if(timeSinceStart <= timelapse * 2){
			g2.setColor(ColorList.getColor((int)(255 - 250 * (timeSinceStart * tc/2))));
			g2.fillOval((int)xc - (int)(.5*(maxSize - maxSize * (timeSinceStart - timelapse))), 
						(int)yc - (int)(.5*(maxSize - maxSize * (timeSinceStart - timelapse))), 
						(int)(maxSize - maxSize * (timeSinceStart - timelapse)), 
						(int)(maxSize - maxSize * (timeSinceStart - timelapse)));
		}
		 g2.setColor(initColor);
	}
	public void draw(Graphics2D g2, double time, int place, double size) {
		double tc = 1 / timelapse;
		if(size > 2*maxSize/3){
			draw(g2, time - 0.2, place*2 + 1, size - (maxSize/ 6));
			draw(g2, time - 0.2, place * 2 + 2,  size - (maxSize / 6));
		}
		if(time <= 0){
			
		}
		else if(time <= timelapse){
			g2.setColor(ColorList.getColor((int)(255 - 255 * (time * tc))));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(places[1][place]) - (int)(.5*(Math.sqrt(Math.sqrt(time * tc)) * size)), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size), 
						(int)(Math.sqrt(Math.sqrt(time * tc)) * size));
		}
		else if(time <= timelapse * 2){
			g2.setColor(ColorList.getColor((int)(255 - 255 * (time/2 * tc))));
			g2.fillOval((int)(places[0][place]) - (int)(.5*(size - size * (time - timelapse))), 
						(int)(places[1][place]) - (int)(.5*(size - size * (time - timelapse))), 
						(int)(size - size * (time - timelapse)), 
						(int)(size - size * (time - timelapse)));
		}
	}
	
}
