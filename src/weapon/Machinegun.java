package weapon;

import game.Tank;
import game.Transform;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class Machinegun extends Weapon {

	public Machinegun(Tank t, Point2D center, double atot, double dtot){
		super(t, 3, center, 30, 1, 7, atot, dtot);
		int[] x = {0, 6, 6, 2,  2,  0, 0};
		int[] y = {-4, -4, 4, 4, 20, 20, 4};
		setWeaponShape(new Polygon(x, y, 7));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}
	
	public Machinegun(Tank t, double atot, double dtot) {
		this(t, t.getCenter(), atot, dtot);
	}
	
	public Machinegun(Point2D t, double atot, double dtot){
		this(null, t, atot, dtot);
	}
	@Override
	public void updateSpread() {

	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == this.getMAXAMMO()-1){
				this.addTimer(new Timer(250, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),
					2.5, this, this.getTank().getGame()));
			return missiles;
		}
		return null;
	}

}
