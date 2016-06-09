// AUTHOR: Duncan Tilley
// This class defines the behaviour and attributes of the main character

package flappybird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

public class FlappyChar {
	
	private int x;
	private int y; 
	private float yVelocity;
	private BufferedImage sprite;
	private int bottom;
	private int gameSpeed;
	
	private static final float GRAVITY_CONSTANT = 9.8F;
	private static final float SCALE = 0.25F;
	private static final float MAX_VELOCITY = 40F;
	
	public FlappyChar(FlappyGame game) {
		y = (game.getActualHeight() / 2) - 16; // image is 32x32, thus the
		x = (game.getActualWidth() / 2) - 16;  // 16 enables the image offset
		yVelocity = 0;
		gameSpeed = game.getSpeed();
		bottom = game.getActualHeight();
		try {
			sprite = ImageIO.read(new File("res/bird.png"));
		} catch (IOException e) {
			sprite = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
			// manually draw sprite
			Graphics g = sprite.getGraphics();
			g.setColor(Color.ORANGE);
			g.fillOval(2, 16, 60, 32);
		}
	}
	
	public void update() {
		y += yVelocity;
		yVelocity += (GRAVITY_CONSTANT * SCALE);
		if (yVelocity > MAX_VELOCITY) {
			yVelocity = MAX_VELOCITY;
		}
		if (y < 0) {
			y = 0;
			yVelocity = 0F;
		}
		if ((y + 64) > bottom) {
			y = bottom - 64;
		}
	}
	
	public void setYVelocity(float vel) {
		yVelocity = vel;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public float getYVelocity() {
		return yVelocity;
	}
	
	public boolean testCollisions(Vector<FlappyPipe> pipes) {
		if ((y + 64) >= bottom) {
			return true;
		}
		for (FlappyPipe cur : pipes) {
			// if between the pipe
			if (((x + 64) >= cur.getX()) && ((x) <= cur.getX() + cur.getWidth())) {
				// test for top collision
				if (y < cur.getTopMask().getHeight()) {
					return true;
				}
				// test for bottom collision
				if ((y + 64) > (bottom - cur.getBotMask().getHeight())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean shouldGetPoint(Vector<FlappyPipe> pipes) {
		for (FlappyPipe cur : pipes) {
			if ((x > cur.getX()) && (x < cur.getX() + gameSpeed))  {
				return true;
			}
		}
		return false;
	}
	 
	public BufferedImage getSprite() {
		BufferedImage tempSpr = sprite;
		AffineTransform trans = new AffineTransform();
		trans.setToRotation(Math.toRadians(yVelocity*2), 32, 32);
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
		tempSpr = op.filter(tempSpr, null);
		return tempSpr;
	}
	
}
