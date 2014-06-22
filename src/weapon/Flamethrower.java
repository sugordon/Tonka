package weapon;

import game.Tank;
import game.Transform;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class Flamethrower extends Weapon{
	public Flamethrower(Tank t, Point2D center, double atot, double dtot){
		super(t, 2, center, 60, 2, 25, atot, dtot);
		//Needs model, currently using that of MachineGun
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
	
	public Flamethrower(Tank t, double atot, double dtot) {
		this(t, t.getCenter(), atot, dtot);
	}
	
	public Flamethrower(Point2D t, double atot, double dtot){
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
//			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
//			missiles.add(new BasicMissile(this.getCenter(),
//					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(), 1, this, this.getTank().getGame()));
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(3);
			missiles.add(new FlameMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),
					1, this, this.getTank().getGame(), 110));
			missiles.add(new FlameMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),
					1, this, this.getTank().getGame(), 70));
			missiles.add(new FlameMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),
					1, this, this.getTank().getGame(), 20));
			return missiles;
		}
		return null;
	}
}
