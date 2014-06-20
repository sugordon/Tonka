package weapon;

import game.Block;
import game.Explosion;
import game.Game;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import util.Collidable;

public class Arrow extends Projectile{
	private int time;
	private int timerDelay;
	protected double deltax, deltay;
	
	public Arrow(Point2D center, double theta, double damage, Weapon weapon, Game game){
		this((int)center.getX(), (int)center.getY(), 14, damage, theta, weapon, game);
	}

	public Arrow(int xstart, int ystart, double velocity, double damage, double theta, Weapon weapon, Game game){
		super(new Polygon(), xstart, ystart, velocity, damage, theta, weapon, game);
		int[] x = {22, 22, 32, 22, 22, 1, -2, -6, -4, -6, -2, 1};
		int[] y = {2, 5, 0, -5, -2, -2, -3, -3, 0, 3, 3, 2};
		for(int a=0; a<x.length; a++) {
			x[a]+=xstart;
			y[a]+=ystart;
		}
		
		this.setProjectileShape(new Polygon(x,y,12));
		deltax = Math.cos(Math.toRadians(theta))*velocity;
		deltay = Math.sin(Math.toRadians(theta))*velocity;
		setProjectileShape(Transform.transform(getProjectileShape(), 0, 0, Math.toRadians(theta), xstart, ystart));
	}
	public void update(){
		time++;
		setProjectileShape(Transform.transform(getProjectileShape(), deltax, deltay, 0, this.center.getX(), this.center.getY()));
//		projectileShape = translate.createTransformedShape(projectileShape);
		double xcenter = center.getX() + deltax;
		double ycenter = center.getY() + deltay;
		center.setLocation(xcenter, ycenter);
		for (Collidable c : game.getCollisions(this)){
			c.collision(this);
		}
	}
	public Shape getShape(){
		return getProjectileShape();
	}
	public Shape getDestroyed(){
		int sides = 20;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for (int i = 0;i<sides;i++){
			xarr[i] = (int)(((Math.random()*1)+1)*Math.cos(Math.toRadians(i*360/sides)) + this.center.getX() + deltax/20);
			yarr[i] = (int)(((Math.random()*1)+1)*Math.sin(Math.toRadians(i*360/sides)) + this.center.getY() + deltay/20);
		}
		Polygon destroyed = new Polygon(xarr, yarr, sides);
		return destroyed;
//		int sides = 10;
//		int[] xarr = new int[sides];
//		int[] yarr = new int[sides];
//		for(int i = 0; i < sides; i++){
//			xarr[i] = (int)(this.center.getX() + 7 * Math.cos(Math.PI/(sides/2) * i));
//			yarr[i] = (int)(this.center.getY() + 7 * Math.sin(Math.PI/(sides/2) * i));
//		}
//		return new Polygon(xarr,yarr,sides);
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Block){
			if (timerDelay == 0){
				timerDelay = time;
			}
			if (timerDelay + 1 <= time){
				game.removeQueue(this);
				Block b = new Block(this.getShape(), true, game);
				game.addQueue(b);
//				game.addQueue(new Explosion(this.center, game));
			}
		}
		if (c instanceof Tank && !c.equals(this.getWeapon().getTank())){
			if (((Tank) c).getTeam() != this.getWeapon().getTank().getTeam()) {
				((Tank) c).takeDamage((int) this.damage);
			}
			game.removeQueue(this);
//			game.addQueue(new Explosion(this.center, game));
		}
	}

	@Override
	public boolean isColliding(Collidable c) {
		if (c.equals(this) || c.equals(getWeapon().getTank())) {
			return false;
		}
		if (c.getBoundingBox().intersects((Rectangle2D) this.getBoundingBox())){
			Area tankArea = new Area(this.getShape());
			tankArea.intersect(new Area(c.getShape()));
			if (!tankArea.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public double getVar(){
		return this.center.getX();
	}
}
