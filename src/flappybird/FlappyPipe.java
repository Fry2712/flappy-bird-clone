package flappybird;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FlappyPipe {
	
	private int x;
	private int speed;
	private BufferedImage sprite;
	private int startWidth;
	private Dimension topCollMask;
	private Dimension botCollMask;
	private int gapStart;
	
	private static final int VERTICAL_GAP = 280;
	private static final int MASK_WIDTH = 150;
	
	public FlappyPipe(FlappyGame game, int startOffset) {
		startWidth = (game.getActualWidth()) + startOffset;
		x = startWidth;
		speed = game.getSpeed();
		gapStart = (int)Math.round(Math.random() * (game.getActualHeight() - VERTICAL_GAP - 84) + 20);
		//int gapStart = 50;
		topCollMask = new Dimension(MASK_WIDTH, gapStart);
		botCollMask = new Dimension(MASK_WIDTH, game.getActualHeight() - (gapStart + VERTICAL_GAP));
		try {
			sprite = new BufferedImage(164, game.getActualHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = sprite.getGraphics();
			g.drawImage(ImageIO.read(new File("res/pole_down.png")), 0, gapStart - 666, null);
			g.drawImage(ImageIO.read(new File("res/pole_up.png")), 0, gapStart + VERTICAL_GAP,
					null);
		} catch (IOException e) {
			sprite = new BufferedImage(164, game.getActualHeight(), 0);
			// manually draw the sprite
		}
	}
	
	public void update() {
		x -= speed;
	}
	
	public int getWidth() {
		return MASK_WIDTH;
	}
	
	public int getX() {
		return x;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public Dimension getTopMask() {
		return topCollMask;
	}
	
	public Dimension getBotMask() {
		return botCollMask;
	}
	
}
