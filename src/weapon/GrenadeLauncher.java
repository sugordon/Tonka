package weapon;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import util.Timer;
import util.Timer.Action;

import game.AngleMath;
import game.Tank;
import game.Transform;

public class GrenadeLauncher extends Weapon {
	private double distance;
	
	public GrenadeLauncher(Tank t, double turnSpeed, Point2D center, int ammo,
			double firerate, double spread, double dtot, double atot) {
		super(t, turnSpeed, center, ammo, firerate, spread);
		int[] x = { 0,  6, 6, 3, 5,  3,  1, -1, -3, -5, -3, -6, -6};
		int[] y = {-8, -5, 2, 4, 7, 12, 11, 11, 12,  7,  4,  2, -5};
		setWeaponShape(new Polygon(x, y, 13));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}

	public GrenadeLauncher(Tank t, double dtot, double atot) {
		this(t,4,t.getCenter(), 1, 5, 0, dtot, atot);
	}

	@Override
	public void clickPoint(Point2D tgt){
		super.clickPoint(tgt);
		this.distance = this.getCenter().distance(tgt);
	}
	
	@Override
	public ArrayList<Projectile> shoot(){
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == this.getMAXAMMO()-1){
				this.addTimer(new Timer(75, Action.AMMO));
			}
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new Grenade(this.getCenter(), distance, 20, this.getAngle(),
					this, this.getTank().getGame()));
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			return missiles;
		}
		return null;
	}

	@Override
	public void updateSpread() {

	}

}
