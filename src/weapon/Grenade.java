package weapon;

import game.Block;
import game.Explosion;
import game.Game;
import game.Tank;
import game.Transform;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;

import util.Collidable;

public class Grenade extends Projectile {
	private double deltax;
	private double deltay;
	private final double distance;
	private final Point2D init;
	private boolean remove;
	
	public Grenade(Point2D center, double distance, double damage, double theta, Weapon weapon, Game game) {
		super(null, (int)center.getX(), (int)center.getY(), 5, damage, theta, weapon, game);
		int sides = 10;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(this.center.getX() + 4 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(this.center.getY() + 4 * Math.sin(Math.PI/(sides/2) * i));
		}
		this.setProjectileShape(new Polygon(xarr,yarr,sides));
		deltax = 6*Math.cos(Math.toRadians(theta));
		deltay = 6*Math.sin(Math.toRadians(theta));
		this.distance = distance;
		this.init = center;
		this.remove = false;
	}

	@Override
	public void update() {
		if (this.remove){
			game.removeQueue(this);
			game.addQueue(new Explosion(this.center.getX(), this.center.getY(), 60, game));
		}
		setProjectileShape(Transform.transform(getProjectileShape(), deltax, deltay, 0, this.center.getX(), this.center.getY()));
//		projectileShape = translate.createTransformedShape(projectileShape);
		double xcenter = center.getX() + deltax;
		double ycenter = center.getY() + deltay;
		center.setLocation(xcenter, ycenter);
		for (Collidable c : game.getCollisions(this)){
			c.collision(this);
		}
		if (this.init.distance(this.center) > distance){
			this.setProjectileShape(this.getExploded());
			this.remove = true;
		}
	}

	@Override
	public Shape getDestroyed() {
		this.setProjectileShape(this.getExploded());
		return this.getProjectileShape();
	}
	
	public Shape getExploded() {
		int sides = 10;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(this.center.getX() + 50 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(this.center.getY() + 50 * Math.sin(Math.PI/(sides/2) * i));
		}
		return new Polygon(xarr,yarr,sides);
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Tank && !c.equals(this.getWeapon().getTank())) {
			if (this.center.distance(init) >= 30){
				((Tank) c).takeDamage((int) this.damage);
				game.addQueue(new Explosion(this.center.getX(), this.center.getY(), 60, game));
			}
			game.removeQueue(this);
		}
		if (c instanceof Block){
			game.removeQueue(this);
			game.addQueue(new Explosion(this.center.getX(), this.center.getY(), 60, game));
		}
	}

}