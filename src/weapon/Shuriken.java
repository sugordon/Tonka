package weapon;

import game.Block;
import game.Game;
import game.Tank;
import game.Transform;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import util.Collidable;

public class Shuriken extends Projectile{

	private double currRotation;
	private int time;
	private int timerDelay;
	private double deltax, deltay;
	
	
	public Shuriken(Point2D center, double theta, double damage, Weapon weapon, Game game){
		this((int)center.getX(), (int)center.getY(), 9, damage, theta, weapon, game);
	}

	public Shuriken(int xstart, int ystart, double velocity, double damage, double theta, Weapon weapon, Game game){
		super(new Polygon(), xstart, ystart, velocity, damage, theta, weapon, game);
		int[] x = {xstart + 6, xstart + 1, xstart, xstart - 1, xstart - 6, xstart - 1, xstart, xstart + 1};
		int[] y = {ystart, ystart + 1, ystart + 6, ystart + 1, ystart, ystart - 1, ystart - 6, ystart - 1};
		this.setProjectileShape(new Polygon(x,y,8));
		this.currRotation = 0;
		deltax = Math.cos(Math.toRadians(theta))*velocity;
		deltay = Math.sin(Math.toRadians(theta))*velocity;
		setProjectileShape(Transform.transform(getProjectileShape(), 0, 0, Math.toRadians(theta), xstart, ystart));
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		time++;
		currRotation += 2;
		setProjectileShape(Transform.transform(getProjectileShape(), deltax, deltay, .1, this.center.getX(), this.center.getY()));
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
	
	@Override
	public Shape getDestroyed() {
//		int sides = 20;
//		int[] xarr = new int[sides];
//		int[] yarr = new int[sides];
//		for (int i = 0;i<sides;i++){
//			xarr[i] = (int)(((Math.random()*0)+4)*Math.cos(Math.toRadians(i*360/sides)) + this.center.getX() + deltax/20);
//			yarr[i] = (int)(((Math.random()*0)+4)*Math.sin(Math.toRadians(i*360/sides)) + this.center.getY() + deltay/20);
//		}
//		Polygon destroyed = new Polygon(xarr, yarr, sides);
		return this.getShape();
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Block){
			if (timerDelay == 0){
				timerDelay = time;
			}
			if (timerDelay + 1 <= time){
				game.removeQueue(this);
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


}
