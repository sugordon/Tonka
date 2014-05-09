package weapon;

import game.AngleMath;
import game.Tank;
import game.Transform;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class BasicTurret extends Weapon {
	private AffineTransform old;

			
	public BasicTurret(Tank t){
		this.t = t;
		this.ammo = 5;
		this.center = t.getCenter();
		this.turnSpeed = 3;
		this.spread = 2;
		int[] x = {0,	6,	6,	3,	2, -2,	-3,	-6,	-6};
		int[] y = {-3,	-3,	2,	2,	15,	15,	2,	2,	-3};
		weaponShape = new Polygon(x, y, 9);
		//Rotate it to its initial location
		weaponShape = Transform.transform(weaponShape, t.getCenter().getX(), t.getCenter().getY(), Math.toRadians(-90), t.getCenter().getX(), t.getCenter().getY());
		old = new AffineTransform();
	}

	@Override
	public void draw(Graphics2D g2) {
		old = g2.getTransform();
		g2.rotate(Math.toRadians(angle), center.getX(), center.getY());
		g2.draw(weaponShape);
		g2.setTransform(old);
		for (int i = -1;i<=1;i++){
			g2.drawLine(center.x, center.y, (int)(1000 * Math.cos(Math.toRadians(angle + i*this.spread)))+center.x, (int)(1000*Math.sin(Math.toRadians(angle + i*this.spread)))+center.y);
		}
	}
	@Override
	public void clickPoint(Point tgt){
		double tgtAng = Math.atan2(tgt.y - center.y, tgt.x - center.x);
		this.tgtAngle = AngleMath.adjustAngle((int)Math.toDegrees(tgtAng));
	}
	@Override
	public void update(){
		replenishAmmo();
		int diffangle = AngleMath.adjustAngle(tgtAngle - angle);
//		double dtheta = 0;
		if (diffangle > 0){
			angle += turnSpeed;
//			dtheta = turnSpeed;
			if (AngleMath.adjustAngle(tgtAngle - angle) < 0){
//				dtheta = AngleMath.adjustAngle(tgtAngle - angle);
				angle = tgtAngle;
			}
		}
		if (diffangle < 0){
			angle -= turnSpeed;
//			dtheta = -turnSpeed;
			if (AngleMath.adjustAngle(tgtAngle - angle) > 0){
//				dtheta = AngleMath.adjustAngle(tgtAngle - angle);
				angle = tgtAngle;
			}
		}
		double deltax = t.getCenter().getX() - center.getX();
		double deltay = t.getCenter().getY() - center.getY();
		center = t.getCenter();
		weaponShape = Transform.transform(weaponShape, deltax, deltay, 0,
				center.x, center.y);
	}
	
	private void replenishAmmo(){
		
	}

//	private Point getProjectileSpawn() {
//		return new Point((int)(15 * Math.cos(Math.toRadians(angle+90)))+center.x, (int)(15*Math.sin(Math.toRadians(angle+90)))+center.y);
//	}

	@Override
	public Projectile shoot() {
		if (canShoot()){
			Random die = new Random();
			return new BasicMissile(this.center,
					(die.nextInt(2)*2-1)*die.nextDouble()*this.spread + angle, t.getGame());
		}
		return null;
	}

	@Override
	public boolean canShoot() {
		return ammo > 0;
	}
}
