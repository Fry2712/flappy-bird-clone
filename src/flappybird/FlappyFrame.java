// AUTHOR: Duncan Tilley
// This class creates the window frame that will display the game

package flappybird;

import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FlappyFrame extends Frame {
	
	public FlappyFrame() {
		FlappyGame game = new FlappyGame();
		add(game);
		pack();
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
			
		}) ;
	}
	
}
