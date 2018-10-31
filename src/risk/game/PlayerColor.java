package risk.game;

import java.awt.Color;

public class PlayerColor {

	private static final int size = 6;
	private static int index = 0;
	
	private static Color[] colors = new Color[] {Color.red,
													Color.blue,
													Color.yellow,
													Color.green,
													Color.pink,
													Color.cyan};
	
	public static Color nextColor() {
		if (index >= size) {
			return null;
		} else {
			return colors[index++];
		}
	}
}
