package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import util.Timer;
import util.Timer.Action;

public class BasicTurret extends Weapon {
	public BasicTurret(Tank t){
		super(t, 3, t.getCenter(), 5, 10, 2);
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		setWeaponShape(new Polygon(x, y, 9));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), t.getCenter().getX(), t.getCenter().getY(), Math.toRadians(-90), t.getCenter().getX(), t.getCenter().getY()));
	}
	@Override
	public void update(){
		super.update();
	}

//	private Point getProjectileSpawn() {
//		return new Point((int)(15 * Math.cos(Math.toRadians(angle+90)))+center.x, (int)(15*Math.sin(Math.toRadians(angle+90)))+center.y);
//	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (canShoot()){
			this.setCanFire(false);
			this.setAmmo(getAmmo()-1);
			this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
			if (getAmmo() == 4){
				this.addTimer(new Timer(150, Action.AMMO));
			}
			Random die = new Random();
			ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
			missiles.add(new BasicMissile(this.getCenter(),
					(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),this, this.getTank().getGame()));
			return missiles;
		}
		return null;
	}

	@Override
	public void replenishAmmo() {
		this.setAmmo(5);
	}

	@Override
	public void updateSpread() {
		
	}
}
