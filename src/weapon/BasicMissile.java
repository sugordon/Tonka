package weapon;

import game.Block;
import game.Collidable;
import game.Game;
import game.Tank;
import game.Transform;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BasicMissile extends Projectile{
	private int time;
	private int timerDelay;
	private final double deltax, deltay;
	
	public BasicMissile(Point2D center, double theta, Game game, Weapon weapon){
		this((int)center.getX(), (int)center.getY(), 9, 10, theta, game, weapon);
	}

	public BasicMissile(int xstart, int ystart, double velocity, double damage, double theta, Game game, Weapon weapon){
		super(new Polygon(), xstart, ystart, velocity, damage, theta, game);
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = xstart+15;
		y[0] = ystart;
		x[1] = xstart-5;
		y[1] = ystart+3;
		x[2] = xstart-5;
		y[2] = ystart-3;
		this.setProjectileShape(new Polygon(x,y,3));
		this.setGame(game);
		this.setWeapon(weapon);
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
		for (Collidable c : getGame().getCollisions(this)){
			c.collision(this);
		}
	}
	@Override
	public void draw(Graphics2D g2) {
		g2.draw(getProjectileShape());
//		g2.drawOval((int)this.center.getX()-5, (int)this.center.getY()-5, 10, 10);
	}
	public Shape getShape(){
		return getProjectileShape();
	}
	public Shape getDestroyed(){
		int sides = 20;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for (int i = 0;i<sides;i++){
			xarr[i] = (int)(((Math.random()*4)+4)*Math.cos(Math.toRadians(i*360/sides)) + this.center.getX() + deltax/20);
			yarr[i] = (int)(((Math.random()*4)+4)*Math.sin(Math.toRadians(i*360/sides)) + this.center.getY() + deltay/20);
		}
		Polygon destroyed = new Polygon(xarr, yarr, sides);
		return destroyed;
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Block){
			if (timerDelay == 0){
				timerDelay = time;
			}
			if (timerDelay + 1 <= time){
				getGame().removeQueue(this);
			}
		}
		if (c instanceof Tank && !c.equals(this.getWeapon().getTank())){
			((Tank) c).setHp((int) (((Tank)c).getHp()-this.damage));
			getGame().removeQueue(this);
		}
	}

	@Override
	public Shape getBoundingBox() {
		return this.getProjectileShape().getBounds();
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
