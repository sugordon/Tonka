package weapon;

import game.Block;
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
import java.io.Serializable;

import util.Collidable;
import util.Drawable;

public class FlameMissile extends Projectile {
	private int time;
	private int timerDelay;
	private int timetolive;
	protected double deltax, deltay;
	
	public FlameMissile(Point2D center, double theta, double damage, Weapon weapon, Game game, int timetolive){
		this((int)center.getX(), (int)center.getY(), 9, damage, theta, weapon, game, timetolive);
	}

	public FlameMissile(int xstart, int ystart, double velocity, double damage, double theta, Weapon weapon, Game game, int timetolive){
		super(new Polygon(), xstart, ystart, velocity, damage, theta, weapon, game);
		this.timetolive = timetolive;
		int sides = 5;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(this.center.getX() + 4 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(this.center.getY() + 4 * Math.sin(Math.PI/(sides/2) * i));
		}
		this.setProjectileShape(new Polygon(xarr,yarr,sides));
		deltax = Math.cos(Math.toRadians(theta))*velocity;
		deltay = Math.sin(Math.toRadians(theta))*velocity;
		setProjectileShape(Transform.transform(getProjectileShape(), 0, 0, Math.toRadians(theta), xstart, ystart));
	}
	public void update(){
		time++;
		if(time>timetolive) {
			game.removeQueue(this);
		}
		setProjectileShape(Transform.transform(getProjectileShape(), deltax, deltay, 0, this.center.getX(), this.center.getY()));
//		projectileShape = translate.createTransformedShape(projectileShape);
		double xcenter = center.getX() + deltax;
		double ycenter = center.getY() + deltay;
		if(deltax>0)
			deltax-=.1;
		else
			deltax+=.1;
		if(deltay>0)
			deltay-=.1;
		else
			deltay+=.1;
//		deltax -= Math.abs(deltax)/deltax*.1;
//		deltay -= Math.abs(deltay)/deltay*.1;
		center.setLocation(xcenter, ycenter);
		for (Collidable c : game.getCollisions(this)){
			c.collision(this);
		}
	}

	@Override
	public Drawable getProxyClass() {
		return new ProxyFlame(this.getProjectileShape());
	}
	public Shape getShape(){
		return getProjectileShape();
	}
	public Shape getDestroyed(){
		int sides = 10;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for (int i = 0;i<sides;i++){
			xarr[i] = (int)(((Math.random()*2)+2)*Math.cos(Math.toRadians(i*360/sides)) + this.center.getX() + deltax/20);
			yarr[i] = (int)(((Math.random()*2)+2)*Math.sin(Math.toRadians(i*360/sides)) + this.center.getY() + deltay/20);
		}
		Polygon destroyed = new Polygon(xarr, yarr, sides);
		return destroyed;
//		int[] xarr = {};
//		int[] yarr = {};
//		Polygon destroyed = new Polygon(xarr, yarr, 0);
//		return destroyed;
	}
	@Override
	public void draw(Graphics2D g2){
		getProxyClass().draw(g2);
	}
	@Override
	public void collision(Collidable c) {
		if (c instanceof Block){
			if (timerDelay == 0){
				timerDelay = time;
			}
			if (timerDelay + 1 <= time){
				game.removeQueue(this);
				deltax = 0;
				deltay = 0;
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
class ProxyFlame implements Drawable, Serializable {
	private final Shape shape;
	
	public ProxyFlame(Shape s){
		this.shape = s;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.red);
		g2.fill(shape);
	}
	
}
