package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import util.Collidable;
import util.Drawable;
import util.KeyInput;
import util.Sendable;
import util.Updatable;
import weapon.*;
import weapon.Weapon.WeaponList;

public class Game implements Drawable {
	public static Dimension windowSize = new Dimension(1280, 720);
	
	private HashSet<Collidable> collidables;
	private HashSet<Drawable> drawables;
	private HashSet<Updatable> updatables;
	private Map map;
	private CollisionMap collisions;
//	private ThreadHandler thHand;
	
	/*
	 * Tanks are special because their update method needs params
	 */
	private ArrayList<Tank> allTanks;
	private HashMap<Integer, Tank> playerTanks;
	
	//Prevent Concurrent Modification Errors
	private HashSet<Object> removeQue;
	private HashSet<Object> addQue;
	
	private static ArrayList<Object[]> randWeap;
	
	public Game(int playerNum, double frequency){
		collidables = new HashSet<Collidable>();
		drawables = new HashSet<Drawable>();
		updatables = new HashSet<Updatable>();
		allTanks = new ArrayList<Tank>();
		playerTanks = new HashMap<Integer, Tank>();
		removeQue = new HashSet<Object>();
		addQue = new HashSet<Object>();
		collisions = new CollisionMap();
//		thHand = new ThreadHandler();
		map = new Map(this);
		map.cubez();
		for(Block b: map.showBlocks()){
			addObject(b);
		}
		
		for (int i = 0;i<playerNum;i++){
			Tank t = new Tank(100,100 + 40*i, i + 1, this);
			this.addRandomWeapons(t, frequency);
//			t.takeDamage(-1000);
//			t.addWeapon(new Flamethrower(t, 0, 0));
			addObject(t);
			for (int j = 0;j<4;j++) {
				Tank enemy = new Tank(1100 + 50*i, 100*j + 50, 0, this);
				enemy.setColor(new Color(150 + (int)(Math.random()*80),  (int)(Math.random()*40),  (int)(Math.random()*40)));
				this.addRandomWeapons(enemy, frequency);
				enemy.addAI(new AI(enemy, this));
				boolean colliding = false;
				for (Collidable c : collidables){
					if (enemy.isColliding(c) || enemy.isColliding(c)){
						colliding = true;
						break;
					}
				}
				if (colliding == false){
					this.addQueue(enemy);
				}
			}
		}
		
//		//Stress testing
//		for (int i = 9;i<13;i++){
//			for (int j = 6;j<13;j++){
//				Tank enemy = new Tank(j*100, 50*i, this);
//				enemy.addWeapon(new BasicTurret(enemy, 0, 10));
//				enemy.addAI(new AI(enemy, this));
//				boolean colliding = false;
//				for (Collidable c : collidables){
//					if (enemy.isColliding(c) || enemy.isColliding(c)){
//						colliding = true;
//						break;
//					}
//				}
//				if (colliding == false){
//					this.addQueue(enemy);
//				}
//			}
//		}
	}
	public Game(double freqeuncy) {
		this(1,freqeuncy);
	}
	private void addRandomWeapons(Tank t, double probability) {
		ArrayList<Object[]> weaps = Game.getRandWeaps();
		//Normalizez the weapon values
		double sum = 0.0;
		for (Object[] o : weaps) {
			sum += (Double)o[1];
		}
		if (sum == 0) {
			for (Object[] o : weaps) {
				o[1] = new Double(100/weaps.size());
			}
		}
		if (sum != 100) {
			for (Object[] o : weaps) {
				o[1] = new Double((Double)o[1]/sum);
			}
		}
		double num = Math.random();
		double prob = 0.0;
		for (Object[] o : weaps) {
			prob += (double)o[1];
			if (prob > num) {
				t.addWeapon(WeaponList.getWeapon((WeaponList) o[0], t,
						0, 0));
				break;
			}
		}
		
		int pos = 0;
		if (Math.random() < probability) {
			num = Math.random();
			prob = 0.0;
			for (Object[] o : weaps) {
				prob += (double)o[1];
				if (prob > num) {
					t.addWeapon(WeaponList.getWeapon((WeaponList) o[0], t,
							0, t.getBoundingBox().height/2-(pos++)*12));
					break;
				}
			}
		}
	}
	// Test method, draw whatever you want on the panel
	private Shape test;
	public void setTestDraw(Shape s){
		test = s;
	}
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.black);
		for (Drawable d : drawables){
			d.draw(g2);
		}
		if (test != null){
			g2.draw(test);
		}
	}
	public void tick(){
//		long start = System.currentTimeMillis();

		for (Updatable u : updatables) {
			u.update();
		}
		//Updates everything with the thread Handler. This part is multithreaded
//		thHand.update(updatables);
//		long end = System.currentTimeMillis();
		//Updates all AI tanks
		for (Tank t : allTanks){
			if (t.isAI()){
//				System.out.println(t.getCenter());
				t.movement(null);
			}
		}
		//Adding and removing
		for (Object o : addQue){
			addObject(o);
		}
		addQue.clear();
		for (Object o : removeQue){
			removeObject(o);
		}
		removeQue.clear();
		collisions.updateCollidables(collidables);
	}
	public boolean isFinished(){
		return playerTanks.size() == 0;
	}
	//Single player update
	public void update(int down, int right, Point clickpoint, boolean shoot){
//		System.out.println(playerTanks);
		if (isFinished() == false){
			playerTanks.get(1).movement(down, right, clickpoint, shoot);
		}
		this.tick();
	}
	//MultiPlayer update
	public void update(KeyInput i, int player){
		if (playerTanks.containsKey(player+1)) {
			playerTanks.get(player+1).movement(i);
		}
	}
	//Called by other collidables to see who is colliding with the frame
	public HashSet<Collidable> getCollisions(Collidable init){
		return collisions.getCollisions(init);
//		HashSet<Collidable> collisions = new HashSet<Collidable>();
//		for (Collidable c : collidables){
//			if (c.isColliding(init)){
//				collisions.add(c);
//			}
//		}
//		return collisions;
	}
	private void addObject(Object o){
		if (o instanceof Collidable){
			collidables.add((Collidable) o);
		}
		if (o instanceof Drawable){
			drawables.add((Drawable) o);
		}
		if (o instanceof Updatable){
			updatables.add((Updatable) o);
		}
		if (o instanceof Tank){
			allTanks.add((Tank) o);
			if (!((Tank)o).isAI()){
				playerTanks.put(((Tank) o).getTeam(), (Tank)o);
			}
		}
	}
	
	private void removeObject(Object o){
		if (o instanceof Collidable){
			collidables.remove((Collidable) o);
		}
		if (o instanceof Drawable){
			drawables.remove((Drawable) o);
		}
		if (o instanceof Updatable){
			updatables.remove((Updatable) o);
		}
		if (o instanceof Tank){
			allTanks.remove((Tank) o);
			playerTanks.remove(((Tank) o).getTeam());
		}
	}
	public void addQueue(Object o){
		if (o == null) {
			return;
		}
		if (o instanceof Collection){
			addQue.addAll((Collection<? extends Object>) o);
		} else {
			addQue.add(o);
		}
	}
	public void removeQueue(Object o){
		if (o == null) {
			return;
		}
		removeQue.add(o);
	}
	public ArrayList<Tank> getTanks(){
		return new ArrayList<Tank>(allTanks);
	}
	public ArrayList<Block> getBlocks(){
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (Collidable c : collidables){
			if (c instanceof Block){
				blocks.add((Block) c);
			}
		}
		return blocks;
	}
	
	public HashMap<Integer, Tank> getPlayerTanks() {
		return playerTanks;
	}
	
	public HashSet<Drawable> getSend() {
		HashSet<Drawable> sends = new HashSet<Drawable>();
		for (Drawable d : drawables){
			if (d instanceof Sendable && d instanceof Explosion == false){
				sends.add(((Sendable)d).getProxyClass());
			}
		}
		return sends;
	}
	
	public int getSize(){
		return updatables.size();
	}
	
	public int getNumPlayers() {
		return playerTanks.size();
	}
	// *+*
	/*
	 * You need to add new weapons here. Remember to fill in the probabilities
	 */
	public static ArrayList<Object[]> getRandWeaps() {
		if (randWeap == null) {
			//Create the random weapon Stuff
			randWeap = new ArrayList<Object[]>();

			Object[] shotgun = 		{WeaponList.Shotgun, .25};
			Object[] machinegun = 	{WeaponList.Machinegun, .25};
			Object[] basic = 		{WeaponList.BasicTurret, .2};
			Object[] auto = 		{WeaponList.AutoTurret, .15};
			Object[] grenade = 		{WeaponList.GrenadeLauncher, .1};
			Object[] shuriken =     {WeaponList.ShurikenLauncher, 0.1};
			Object[] richard = 		{WeaponList.RichardWeapon, 0.01};
			Object[] flamethrower = {WeaponList.Flamethrower, 0.1};
			Object[] pulsar = 		{WeaponList.Pulsar, 0.1};
			Object[] longbow = 		{WeaponList.Longbow, 0.1};
			
			randWeap.add(shotgun);
			randWeap.add(machinegun);
			randWeap.add(basic);
			randWeap.add(auto);
			randWeap.add(grenade);
			randWeap.add(shuriken);
			randWeap.add(richard);
			randWeap.add(flamethrower);
			randWeap.add(pulsar);
			randWeap.add(longbow);
		}
		return randWeap;
	}
}
