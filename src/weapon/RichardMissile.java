package weapon;

import game.Game;
import game.SinglePlayer;
import game.Transform;

import java.awt.geom.Point2D;

import util.Collidable;

public class RichardMissile extends BasicMissile{
	private int lifespan;
	public RichardMissile(Point2D center, double theta, double damage,
			Weapon weapon, Game game) {
		super((int)(center.getX()), (int)(center.getY()), 1, damage, theta, weapon, game);
		lifespan = 100;
	}
	
	public void update(){
		if (lifespan>0){
			lifespan--;
		} else {
			game.removeQueue(this);
			return;
		}
		damage = damage * 1.01;
		//I'm not changing this
		if (deltax < 50)
			deltax = deltax * 1.04;
		if (deltay < 50)
			deltay = deltay * 1.04;
		super.update();
	}
}
