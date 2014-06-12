package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Drawable;
import util.KeyInput;

public class ClientFrame {
	public static void main(String[] args) {
		new ClientFrame();
	}

	private JFrame frame;
	private Timer ti;
	private Dimension windowSize;
	private Client client;
	private HashSet<Drawable> drawables;
	private TankProxy myTank;
	private JPanel selectPanel;
	private Listener li;

	public static final int TIMESTEP = 17;

	public ClientFrame() {
		String ip = JOptionPane.showInputDialog("IP?");
		if (ip == null) {
			ip = "localhost";
		}
		client = new Client(ip, 34556);
		client.startThread();
		
		frame = new JFrame();
		windowSize = new Dimension(1280, 720);
		li = new Listener();
		frame.addKeyListener(li);
//		selectPanel = new SelectPanel();
//		frame.add(selectPanel);
//		selectPanel.addMouseListener(li);
//		selectPanel.addMouseMotionListener(li);
		JPanel mainDraw = new MainDraw();
		frame.add(mainDraw);
		mainDraw.addMouseListener(li);
		mainDraw.addMouseMotionListener(li);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ti = new Timer(TIMESTEP, li);
		ti.start();
	}

	@SuppressWarnings("serial")
	public class MainDraw extends JPanel {
		public MainDraw() {
			this.setPreferredSize(windowSize);
			this.setVisible(true);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform old = g2.getTransform();
			if (myTank != null) {
				g2.translate(-myTank.getX() + Game.windowSize.getWidth()/2,
						-myTank.getY() + Game.windowSize.getHeight()/2);
			} else {
				g2.setTransform(old);
			}
			if (drawables != null){
				for (Drawable d : drawables){
					d.draw(g2);
				}
			}
			g2.setTransform(old);
			
			//Draw the minimap
			g2.translate(Game.windowSize.getWidth()-256, 0);
			g2.scale(1.0/5, 1.0/5);
			if (drawables != null) {
				for (Drawable d : drawables) {
					d.draw(g2);
				}
			}
			g2.setTransform(old);
		}
	}
	
	public class SelectPanel extends JPanel {
		public SelectPanel() {
			this.setPreferredSize(windowSize);
			this.setVisible(true);
			JSpinner jspinner = new JSpinner();
			jspinner.setVisible(true);
			jspinner.setValue(1);
			jspinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					JSpinner slider = (JSpinner)arg0.getSource();
					int value = (int) slider.getValue();
					if (value <= 0) {
						value = 1;
						slider.setValue(1);
					}
				}
				
			});
			this.add(new JLabel("Team"));
			this.add(jspinner);
		}
	}

	public class Listener implements KeyListener, ActionListener,
			MouseListener, MouseMotionListener {
		
		private int down, right;
		private Point clickPoint = new Point();
		private boolean shoot;

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_W) {
				down = -1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_S) {
				down = 1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_D) {
				right = 1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_A) {
				right = -1;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_P) {
				if (ti.isRunning()) {
					ti.stop();
				} else {
					ti.start();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_A
					|| arg0.getKeyCode() == KeyEvent.VK_D) {
				right = 0;
			}
			if (arg0.getKeyCode() == KeyEvent.VK_W
					|| arg0.getKeyCode() == KeyEvent.VK_S) {
				down = 0;
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
//			game.update(down, right, clickpoint);
//			if (drawables != null && drawables.isEmpty() == false) {
//				frame.remove(selectPanel);
//				JPanel mainDraw = new MainDraw();
//				frame.add(mainDraw);
//				mainDraw.addMouseListener(li);
//				mainDraw.addMouseMotionListener(li);
//				mainDraw.setVisible(true);
//				mainDraw.requestFocus();
//				frame.pack();
//			}
			drawables = client.getGame();
			int index = client.getIndex();
			myTank = null;
			for (Drawable d : drawables) {
				if (d instanceof TankProxy) {
					if (((TankProxy)d).getTeam() == index) {
//						System.out.println(((TankProxy)d).getTeam());
						myTank = ((TankProxy)d);
					}
				}
			}
			if (myTank != null) {
				int dx = (int) -(-myTank.getX() + Game.windowSize.getWidth()/2);
				int dy = (int) -(-myTank.getY() + Game.windowSize.getHeight()/2);
				clickPoint.translate(dx, dy);
			}
			client.sendInputs(new KeyInput(down, right, clickPoint, shoot));
			frame.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			shoot = true;
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			shoot = false;
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			clickPoint = arg0.getPoint();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			clickPoint = arg0.getPoint();
		}

	}
}
