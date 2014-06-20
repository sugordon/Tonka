package game;


import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.Polygon;

public class Map {
	
	private ArrayList<Block> blocks;
	private Game game;
	
	public Map(Game game){
		this.game = game;
		blocks = new ArrayList<Block>();
	}
	
	public Map(ArrayList<Block> blocker){
		blocks = new ArrayList<Block>();
		addBlock(blocker);
	}
	
	public void addBlock(ArrayList<Block> blocker){
		blocks.addAll(blocker);
	}
	
	public void addBlock(Block blocke){
		blocks.add(blocke);
	}
	
	public ArrayList<Block> showBlocks(){
		return blocks;
	}
	
	public void basicMap(){
		addBlock(new Block(new Rectangle(0,0,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,700,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,0,20,720), false, game));
		addBlock(new Block(new Rectangle(1260,0,20,720), false, game));
		addBlock(new Block(new Rectangle(300, 200, 75, 350), true, game));
		addBlock(new Block(new Rectangle(800, 200, 75, 350), true, game));

	}
	public void paintBall() {
		addBlock(new Block(new Rectangle(0,0,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,700,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,0,20,720), false, game));
		addBlock(new Block(new Rectangle(1260,0,20,720), false, game));
		//cans
		int sides = 20;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(300 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(220 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), false, game));
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(950 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(220 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), false, game));for(int i = 0; i < sides; i++){
			xarr[i] = (int)(420 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(440 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), false, game));for(int i = 0; i < sides; i++){
			xarr[i] = (int)(770 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(440 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), false, game));
		//bunkers
		addBlock(new Block(new Rectangle(220,300,40,100), false, game));
		addBlock(new Block(new Rectangle(1000,300,40,100), false, game));
		//X
		addBlock(new Block(new Rectangle(575,250,50,150), false, game));
		addBlock(new Block(new Rectangle(525,300,150,50), false, game));
		//snake
		addBlock(new Block(new Rectangle(140,500,20,60), false, game));
		
		addBlock(new Block(new Rectangle(230,550,200,20), false, game));
		addBlock(new Block(new Rectangle(230,550,30,70), false, game));
		addBlock(new Block(new Rectangle(400,550,30,70), false, game));
		
		addBlock(new Block(new Rectangle(530,550,200,20), false, game));
		addBlock(new Block(new Rectangle(530,550,30,70), false, game));
		addBlock(new Block(new Rectangle(700,550,30,70), false, game));
		
		addBlock(new Block(new Rectangle(830,550,200,20), false, game));
		addBlock(new Block(new Rectangle(830,550,30,70), false, game));
		addBlock(new Block(new Rectangle(1000,550,30,70), false, game));

		addBlock(new Block(new Rectangle(1100,500,20,60), false, game));
		//doritos
		int dorito = 60;
		for(int a=0; a<3; a++) {
			int[] doritox = {200+a*150, 260+a*150, 200+a*150};
			int[] doritoy = {dorito, dorito+40, dorito+80};
			addBlock(new Block(new Polygon(doritox, doritoy, 3), false, game));
		}
		for(int a=0; a<3; a++) {
			int[] doritox = {700+a*150, 640+a*150, 700+a*150};
			int[] doritoy = {dorito, dorito+40, dorito+80};
			addBlock(new Block(new Polygon(doritox, doritoy, 3), false, game));
		}
	}
	public void destructiblepaintBall() {
		addBlock(new Block(new Rectangle(0,0,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,700,1280,20), false, game));
		addBlock(new Block(new Rectangle(0,0,20,720), false, game));
		addBlock(new Block(new Rectangle(1260,0,20,720), false, game));
		//cans
		int sides = 20;
		int[] xarr = new int[sides];
		int[] yarr = new int[sides];
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(300 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(220 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), true, game));
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(950 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(220 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), true, game));
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(420 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(440 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), true, game));
		for(int i = 0; i < sides; i++){
			xarr[i] = (int)(770 + 25 * Math.cos(Math.PI/(sides/2) * i));
			yarr[i] = (int)(440 + 25 * Math.sin(Math.PI/(sides/2) * i));
		}
		addBlock(new Block(new Polygon(xarr,yarr,sides), true, game));
		//bunkers
		addBlock(new Block(new Rectangle(220,300,40,100), true, game));
		addBlock(new Block(new Rectangle(1000,300,40,100), true, game));
		//X
		addBlock(new Block(new Rectangle(575,250,50,150), true, game));
		addBlock(new Block(new Rectangle(525,300,150,50), true, game));
		//snake
		addBlock(new Block(new Rectangle(140,500,20,60), true, game));
		
		addBlock(new Block(new Rectangle(230,550,200,20), true, game));
		addBlock(new Block(new Rectangle(230,550,30,70), true, game));
		addBlock(new Block(new Rectangle(400,550,30,70), true, game));
		
		addBlock(new Block(new Rectangle(530,550,200,20), true, game));
		addBlock(new Block(new Rectangle(530,550,30,70), true, game));
		addBlock(new Block(new Rectangle(700,550,30,70), true, game));
		
		addBlock(new Block(new Rectangle(830,550,200,20), true, game));
		addBlock(new Block(new Rectangle(830,550,30,70), true, game));
		addBlock(new Block(new Rectangle(1000,550,30,70), true, game));

		addBlock(new Block(new Rectangle(1100,500,20,60), true, game));
		//doritos
		int dorito = 60;
		for(int a=0; a<3; a++) {
			int[] doritox = {200+a*150, 260+a*150, 200+a*150};
			int[] doritoy = {dorito, dorito+40, dorito+80};
			addBlock(new Block(new Polygon(doritox, doritoy, 3), true, game));
		}
		for(int a=0; a<3; a++) {
			int[] doritox = {700+a*150, 640+a*150, 700+a*150};
			int[] doritoy = {dorito, dorito+40, dorito+80};
			addBlock(new Block(new Polygon(doritox, doritoy, 3), true, game));
		}
	}
}
