package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class Longbow extends Weapon {
	
	public Longbow(Tank t, Point2D center, double atot, double dtot){
		super(t, 7, center, 12, 25, 1, atot, dtot);
		int[] x = { 0,  6, 6, 3,  2, -2, -3, -6, -6};
		int[] y = {-8, -5, 2, 4, 15, 15,  4,  2, -5};
		setWeaponShape(new Polygon(x, y, 9));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}
	
	public Longbow(Tank t, double atot, double dtot){
		this(t, t.getCenter(), atot, dtot);
	}
	
	@Override
	public void update(){
		super.update();
	}
	
//	public void moveTo(Point2D next){
//		double deltax = next.getX() - getCenter().getX();
//		double deltay = next.getY() - getCenter().getY();
//		setCenter(new Point2D.Double(getCenter().getX() + deltax, getCenter().getY() + deltay));
//		setWeaponShape(Transform.transform(getWeaponShape(), deltax, deltay, 0,
//				getCenter().getX(), getCenter().getY()));
//		
//	}

//	private Point getProjectileSpawn() {
//		return new Point((int)(15 * Math.cos(Math.toRadians(angle+90)))+center.x, (int)(15*Math.sin(Math.toRadians(angle+90)))+center.y);
//	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == 0){
				this.addTimer(new Timer(180, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new Arrow(this.getCenter(), (die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(), 7, this, this.getTank().getGame()));
			return missiles;
		}
		return null;
	}

	@Override
	public void updateSpread() {
		
	}
}
