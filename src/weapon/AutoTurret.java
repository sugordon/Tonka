package weapon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import util.Drawable;
import util.Timer;
import util.Timer.Action;

import game.Tank;
import game.Transform;

public class AutoTurret extends Weapon {
	private boolean fire;
	private final int diameter = 500;
	private boolean mouseHeld;
	private int time = -1;

	public AutoTurret(Tank t, double atot, double dtot) {
		this(t, t.getCenter(), atot, dtot);
	}
	
	public AutoTurret(Tank t, Point2D center, double atot, double dtot) {
//		super(t, 3, center, 5, 17, 2, atot, dtot);
		super(t, 3, center, 20, 3, 0, atot, dtot);
		int[] x = { 1,  4,  6,  6,  4,  1, 1, -1, -1, -4, -6, -6, -4, -1};
		int[] y = {-2, -3, -5, -3, -1,  0, 7,  7,  0, -1, -3, -5, -3, -2};
		setWeaponShape(new Polygon(x, y, 14));
		double xcenter = t.getCenter().getX() + dtot * Math.cos(Math.toRadians(t.getTheta() + atot));
		double ycenter = t.getCenter().getY() + dtot * Math.sin(Math.toRadians(t.getTheta() + atot));
		this.setCenter(new Point2D.Double(xcenter, ycenter));
		//Rotate it to its initial location
		setWeaponShape(Transform.transform(getWeaponShape(), xcenter, ycenter, Math.toRadians(-90),
				xcenter, ycenter));
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.drawOval((int)this.getCenter().getX()-diameter/2, (int)this.getCenter().getY()-diameter/2,
				diameter, diameter);
		if (mouseHeld || this.getTank().isAI()) {
			g2.setColor(Color.blue);
		} else {
			g2.setColor(Color.red);
		}
		g2.fillOval((int)this.getCenter().getX()-2, (int)this.getCenter().getY()-2, 4, 4);
		g2.setColor(Color.black);
	}

	@Override
	public void clickPoint(Point2D tgt) {
		double minDist = Integer.MAX_VALUE;
		Tank minTank = null;
		for (Tank t : this.getTank().getGame().getTanks()) {
			if (t.getTeam() != this.getTank().getTeam()) {
				double dist = t.getCenter().distance(this.getTank().getCenter());
				if (dist < minDist) {
					minTank = t;
					minDist = dist;
				}
			}
		}
		if (minDist < diameter/2) {
			fire = true;
		} else {
			fire = false;
		}
		if (minTank != null) {
			super.clickPoint(minTank.getCenter());
		}
	}

	@Override
	public ArrayList<Projectile> shoot() {
		if (time == -1) {
			mouseHeld = !mouseHeld;
			time = 0;
		}
		if (time > 10) {
			time = -1;
		}
		return null;
	}
	
	private ArrayList<Projectile> missile() {
		this.setCanFire(false);
		this.setAmmo(getAmmo()-1);
		this.addTimer(new Timer((int) this.getFirerate(), Action.FIRE));
		if (getAmmo() == this.getMAXAMMO()-1){
			this.addTimer(new Timer(300, Action.AMMO));
		}
		Random die = new Random();
		ArrayList<Projectile> missiles = new ArrayList<Projectile>(1);
		missiles.add(new BasicMissile(this.getCenter(),
				(die.nextInt(2)*2-1)*die.nextDouble()*this.getSpread() + getAngle(),
				5, this, this.getTank().getGame()));
		return missiles;
	}

	@Override
	public boolean canShoot() {
		return fire && super.canShoot() && (mouseHeld || this.getTank().isAI());
	}

	@Override
	public void update() {
		if (time != -1){
			time++;
		}
		super.update();
		if (canShoot()) {
			this.getTank().getGame().addQueue(this.missile());
		}
	}

	@Override
	public void updateSpread() {
		
	}
	@Override
	public Drawable getProxyClass() {
		return new AutoProxy(this.getAngle(), this.diameter, this.getCenter().getX(),
				this.getCenter().getY(), this.getWeaponShape(),
				this.getAmmo(), mouseHeld || this.getTank().isAI());
	}
}

/*
 * Because we override the draw method, we have to create a new proxyclass to draw it
 * The proxyclass should be immuntable, so make all the data members private.
 */

class AutoProxy implements Serializable, Drawable {
	private final double theta;
	private final double xcenter;
	private final double ycenter;
	private final Shape shape;
	private final int ammo;
	private final int diameter;
	private final boolean isOn;
	
	public AutoProxy(double theta, int diameter, double xcenter, double ycenter, Shape s, int ammo,
			boolean isOn){
		this.theta = theta;
		this.xcenter = xcenter;
		this.ycenter = ycenter;
		this.shape = s;
		this.ammo = ammo;
		this.diameter = diameter;
		this.isOn = isOn;
	}

	@Override
	public void draw(Graphics2D g2) {
		AffineTransform old = g2.getTransform();
		g2.rotate(Math.toRadians(theta), xcenter, ycenter);
		g2.setColor(Color.black);
		g2.fill(shape);
		g2.setTransform(old);
		g2.drawString(Integer.toString(ammo), (int)xcenter, (int)ycenter-10);
		g2.drawOval((int)xcenter-diameter/2, (int)ycenter-diameter/2,
				diameter, diameter);
		if (isOn) {
			g2.setColor(Color.blue);
		} else {
			g2.setColor(Color.red);
		}
		g2.fillOval((int)xcenter-2, (int)ycenter-2, 4, 4);
		g2.setColor(Color.black);
	}
}
	
