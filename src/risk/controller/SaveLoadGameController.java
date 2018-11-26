package risk.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import risk.game.Game;

/**
 * This controller is responsible for saving current game and loading a saved game.
 */
public class SaveLoadGameController {

	/**
	 * Saves the serializable object Game in a file.
	 * @param fileName a string that is to be the name of the file to be saved.
	 * @param game the Game object to be serialized and saved.
	 */
	public void saveGame(String fileName, Game game) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(game);
			outStream.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Reads a file and deserialize the Game object from it.
	 * @param fileName a string that is the name of the file to be read.
	 * @return a Game object deserialized from the file.
	 */
	public Game loadGame(String fileName) {
		Game game = null;
		
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			game = (Game) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("Game class not found");
			c.printStackTrace();
			return null;
		}
		
		return game;
	}
}
