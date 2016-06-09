// AUTHOR: Duncan Tilley
// This class is responsible for updating and displaying the game state

package flappybird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class FlappyGame extends Panel {
	
	private Timer animator;
	private int bgOffset = 0;
	private FlappyChar bird;
	private BufferedImage background;
	private BufferedImage banner;
	final private Vector<FlappyPipe> pipes;
	private int scrollSpeed;
	private int score;
	
	private boolean running;
	private boolean showBanner;
	private boolean updateGame;
	
	final FlappyGame game = this;
	
	private final static int SLEEP_TIME = 30;
	private final static int ACTUAL_WIDTH = 600;
	private final static int ACTUAL_HEIGHT = 800;
        private final static float JUMP_HEIGHT = 26F;
	
	public FlappyGame() {
		setFocusable(true);
		setMinimumSize(new Dimension(ACTUAL_WIDTH, ACTUAL_HEIGHT));
		setMaximumSize(new Dimension(ACTUAL_WIDTH, ACTUAL_HEIGHT));
		setPreferredSize(new Dimension(ACTUAL_WIDTH, ACTUAL_HEIGHT));
		animator = new Timer(SLEEP_TIME, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateGame();
				paint(game.getGraphics());
			}
			
		});
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent kevt) {
				if (kevt.getKeyCode() == KeyEvent.VK_SPACE) {
					if (!running) {
						game.restart();
						return;
					}
					if (showBanner) {
						game.start();
					}
					bird.setYVelocity(-JUMP_HEIGHT);
				}
			}
			
		});
		try {
			background = ImageIO.read(new File("res/flappy_back.png"));
		} catch (IOException ex) {
			background = (BufferedImage) createImage(ACTUAL_WIDTH, ACTUAL_HEIGHT);
		}
		try {
			banner = ImageIO.read(new File("res/tap_to_play.png"));
		} catch (IOException ex) {
			banner = (BufferedImage) createImage(ACTUAL_WIDTH, ACTUAL_HEIGHT);
		}
		scrollSpeed = 8;
		bird = new FlappyChar(this);
		pipes = new Vector<>();
		running = true;
		updateGame = false;
		showBanner = true;
		animator.start();
	}
	
	@Override
	public void paint(Graphics g) {
		BufferedImage image = (BufferedImage) this.createImage(600, 800);;
		try {
			Graphics2D dg = (Graphics2D)image.getGraphics();
			dg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			dg.drawImage(background, bgOffset, 0, null);
			dg.drawImage(background, bgOffset + 1200, 0, null);
			dg.drawImage(bird.getSprite(), bird.getX(), bird.getY(), null);
			for (FlappyPipe cur : pipes) {
				dg.drawImage(cur.getSprite(), cur.getX(), 0, null);
			}
			if (showBanner) {
				dg.drawImage(banner, 0, 0, null);
			} else {
				dg.setColor(Color.GRAY);
				dg.setFont(new Font("Consolas", Font.BOLD, 52));
				dg.drawString("" + score, getActualWidth()/2, 700);
				dg.setColor(Color.WHITE);
				dg.setFont(new Font("Consolas", Font.PLAIN, 52));
				dg.drawString("" + score, getActualWidth()/2, 700);
			}
			g.drawImage(image, 0, 0, game);
		} catch (Exception e) {}
	}
	
	public void restart() {
		scrollSpeed = 8;
		score = 0;
		bird = new FlappyChar(game);
		pipes.removeAllElements();
		running = true;
		showBanner = true;
	}
	
	public void start() {
		updateGame = true;
		showBanner = false;
		pipes.add(new FlappyPipe(game, 150));
		pipes.add(new FlappyPipe(game, 800));
	}
	
	private void updateGame() {
		if (!running) {
			return;
		}
		bgOffset -= scrollSpeed;
		if (bgOffset <= -(background.getWidth())) {
			bgOffset = 0;
		}
		if (!updateGame) {
			return;
		}
		bird.update();
		for (FlappyPipe cur : pipes) {
			cur.update();
		}
		try {
			if (pipes.firstElement().getX() < -(pipes.firstElement().getWidth() + 450)) {
				pipes.remove(0);
				pipes.add(new FlappyPipe(this, 0));
			}
		} catch (Exception e) {}
		if (bird.shouldGetPoint(pipes)) {
			score++;
		}
		if (bird.testCollisions(pipes)) {
			// end of game
			scrollSpeed = 0;
			running = false;
			updateGame = false;
			showBanner = false;
		}
	}
	
	public int getActualWidth() {
		return ACTUAL_WIDTH;
	}
	
	public int getActualHeight() {
		return ACTUAL_HEIGHT;
	}
	
	public int getSpeed() {
		return scrollSpeed;
	}
	
}
