package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import util.Collidable;
import util.Drawable;
import util.KeyInput;
import util.Sendable;
import util.Updatable;
import weapon.Projectile;
import weapon.Weapon;

public class Tank implements Drawable, Collidable, Updatable, Sendable {
	private double hp;
	private double maxHP;
	private double speed;
	private double turnSpeed;
	private double theta;
	private double prevtheta;
	private ArrayList<Weapon> myWeapons;
	private Shape tankShape;
	private double xcenter;
	private double ycenter;
	private Color color;
	private AI ai;
	private Game game;
	private final int team;
	
	public Tank(double x, double y, int team, Game game){
		this(x,y,new ArrayList<Weapon>(), team, game);
	}
	
	public Tank(double x, double y, ArrayList<Weapon> weapons, int team, Game game){
		this.theta = 0;
		this.prevtheta = 0;
		this.xcenter = x;
		this.ycenter = y;
		this.team = team;
		this.myWeapons = weapons;
		this.maxHP = 100;
		hp = maxHP;
		this.speed = 3;
		this.turnSpeed = 5;
		setColor(Color.green);
		int width = 15+(int)(Math.random()*10);
		int height = 30+(int)(Math.random()*10);
		tankShape = new Rectangle((int)x-height/2, (int)y-width/2, height,width);
		this.game = game;
		this.tankShape = Transform.transform(getShape(), 0, 0, Math.toRadians(180), xcenter, ycenter);
		this.setTheta(getTheta()+180);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(tankShape);
		g2.setColor(Color.black);
		g2.draw(tankShape);
		g2.setColor(Color.red);
		g2.fillRect((int)xcenter - 25, (int)ycenter - 30, (int)(this.hp/this.maxHP * 50), 5);
		g2.setColor(Color.black);
		g2.drawRect((int)xcenter - 25, (int)ycenter - 30, 50, 5);
		//g2.drawString("HP" + this.hp, (int)xcenter, (int)ycenter);
		g2.setColor(color);
		for (Weapon w : myWeapons){
			w.draw(g2);
		}
//		g2.drawString(Double.toString(this.getTheta()), (int)this.getCenter().getX() + 20, (int)this.getCenter().getY());
	}
	
	public void addWeapon(Weapon w){
		myWeapons.add(w);
	}
	
	public void shoot(Point shootPoint){
		for (Weapon w : myWeapons){
			game.addQueue(w.shoot());
		}
	}
	
	//If keyinput is a null, then it is an AI tank
	public void movement(KeyInput i){
		if (i == null) {
			i = ai.getInputs();
		}
		this.movement(i.getDown(), i.getRight(), i.getClickPoint(), i.isShoot());
	}
	
	public void movement(int down, int right, Point clickpoint, boolean shoot){
		prevtheta = getTheta();
		double originalTheta = getTheta();
		double tgtTheta = this.determineTheta(right);
		double tempSpeed = speed;
		if (down != 0 && right != 0){
			tempSpeed = tempSpeed/Math.sqrt(2);
		}
		double xvel = tempSpeed * Math.cos(Math.toRadians(tgtTheta)) * down;
		double yvel = tempSpeed * Math.sin(Math.toRadians(tgtTheta)) * down;
		xcenter += xvel;
		ycenter += yvel;
		
		tankShape = Transform.transform(tankShape, xvel, yvel,
				Math.toRadians(tgtTheta-getTheta()), xcenter, ycenter);
		setTheta(tgtTheta);
		
		//rotates weapons along with the tank//
//		for(int a=0; a<this.getWeapons().size(); a++) {
//			Weapon w = getWeapons().get(a);
//			w.setAngle((int) (w.getAngle()+tgtTheta-originalTheta));
//		}
		
		HashSet<Collidable> collisions = game.getCollisions(this);
		boolean blocked = false;
		for (Collidable c : collisions){
			c.collision(this);
			if (c instanceof Block || c instanceof Tank){
				blocked = true;
			}
		}
		if (blocked){
			xcenter -= xvel;
			ycenter -= yvel;
			tankShape = Transform.transform(tankShape, -xvel, -yvel, Math.toRadians(originalTheta - getTheta()), xcenter, ycenter);
			setTheta(originalTheta);
			
			//unrotates weapons along with the tank//
//			for(int a=0; a<this.getWeapons().size(); a++) {
//				Weapon w = getWeapons().get(a);
//				w.setAngle((int)(w.getAngle()-tgtTheta+originalTheta));
//			}
			
		}
		
		for (Weapon w : myWeapons){
			w.clickPoint(clickpoint);
			w.update();
		}
		
		if (shoot){
			this.shoot(clickpoint);
		}
	}
	private double determineTheta(int right){
		return getTheta() + right * turnSpeed;
	}

	public double getTheta() {
		return theta;
	}
	
	public double getPrevTheta(){
		return prevtheta;
	}

	public void setTheta(double theta) {
		this.theta = (int)AngleMath.adjustAngle(theta);
	}

	@Override
	public void collision(Collidable c) {
		if (c instanceof Projectile){
			c.collision(this);
		}
	}

	@Override
	public Shape getShape() {
		return this.tankShape;
	}

	@Override
	public Rectangle getBoundingBox() {
		return this.tankShape.getBounds();
	}
	
	public void addAI(AI ai){
		this.ai = ai;
	}
	
	public boolean isAI(){
		return ai != null;
	}

	public Point2D.Double getCenter(){
		return new Point2D.Double(xcenter, ycenter);
	}

	@Override
	public boolean isColliding(Collidable c) {
		if (c.equals(this)){
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
	
	public double getHp(){
		return hp;
	}
	
	public void takeDamage(double dmg){
		this.hp -= dmg;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color c){
		color = c;
	}
	
	public Game getGame(){
		return game;
	}

	
	public void removeWeapon(Weapon w){
		this.myWeapons.remove(w);
	}
	
	@Override
	public void update() {
		if(getHp() <= maxHP/3){
			game.addQueue(new Explosion(getCenter(), game));
		}
		if(getHp() <= 0){
			game.removeQueue(this);
			game.addQueue(new Explosion(getCenter().getX(), getCenter().getY(), 60, game));
		}
	}
	@Override
	public String toString(){
		return this.getCenter().toString();
	}
	public ArrayList<Weapon> getWeapons(){
		return this.myWeapons;
	}
	
	public int getTeam() {
		return team;
	}

	@Override
	public Drawable getProxyClass() {
		return new TankProxy(this.getColor(), this.getShape(),
				this.xcenter, this.ycenter, this.getHp(), this.getWeapons(), this.getTeam());
	}
}
