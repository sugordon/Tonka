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

public class Pulsar extends Weapon {
	
	public Pulsar(Tank t, Point2D center, double atot, double dtot){
		super(t, 0, center, 4, 30, 0, atot, dtot);
//		int[] x = { 0,  6, 6, 3,  2, -2, -3, -6, -6};
//		int[] y = {-8, -5, 2, 4, 15, 15,  4,  2, -5};
//		setWeaponShape(new Polygon(x, y, 9));
		int sides = 18;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		int bigradius = 10, smallradius = 5;
		int asdf = 0;
		int radius = bigradius;
		for(int i = 0; i < sides; i++) {
			if(asdf==0) {
				if(radius==bigradius) {
					radius = smallradius;
					asdf = 1;
				} else {
					radius = bigradius;
					asdf = 2;
				}
			}
			asdf--;
			xarr[i] = (int)(radius * Math.cos(i*2*Math.PI/(sides) ));
			yarr[i] = (int)(radius * Math.sin(i*2*Math.PI/(sides) ));
		}
		setWeaponShape(new Polygon(xarr, yarr, sides));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}
	
	public Pulsar(Tank t, double atot, double dtot){
		this(t, t.getCenter(), atot, dtot);
	}
	
//	public Pulsar(Point2D t, double atot, double dtot){
//		this(null, t, atot, dtot);
//	}
//	
//	public Pulsar(double x, double y){
//		this(null, new Point2D.Double(x, y), 0, 0);
//	}
	
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
				this.addTimer(new Timer(50, Action.AMMO));
			}
//			Random die = new Random();
			int num = 17;
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(num);
			for(int a=0; a<num; a++) {
				missiles.add(new BasicMissile(this.getCenter(), a*360/num + getAngle(), 8, this, this.getTank().getGame()));
			}
			return missiles;
		}
		return null;
	}

	@Override
	public void updateSpread() {
		
	}
}
