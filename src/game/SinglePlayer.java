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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SinglePlayer {
	public static void main(String[] args) {
		new SinglePlayer();
	}

	private JFrame frame;
	private Timer ti;
	private Dimension windowSize;
	private Game game;

	public static final int TIMESTEP = 17;

	public SinglePlayer() {
		game = new Game(1);
		frame = new JFrame();
		windowSize = new Dimension(1280, 720);
		frame.setSize(windowSize);
		Listener li = new Listener();
		frame.addKeyListener(li);
		JPanel mainDraw = new MainDraw();
		mainDraw.setPreferredSize(windowSize);
		frame.add(mainDraw);
		frame.pack();
		mainDraw.addMouseListener(li);
		mainDraw.addMouseMotionListener(li);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ti = new Timer(TIMESTEP, li);
		ti.start();
	}

	@SuppressWarnings("serial")
	public class MainDraw extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			game.draw(g2);
		}
	}

	public class Listener implements KeyListener, ActionListener,
			MouseListener, MouseMotionListener {
		private int down, right;
		private Point clickpoint;
		private boolean shoot;

		public Listener() {
			clickpoint = new Point();
		}

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
			game.update(down, right, clickpoint, shoot);
			frame.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			shoot = true;
			// clickpoint = arg0.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			shoot = false;
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			clickpoint = arg0.getPoint();
		}

	}

}