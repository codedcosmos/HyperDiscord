package codedcosmos.hyperdiscord.utils.text;

import java.util.LinkedList;

public class TextSplitter {
	public static String[] split(String inputText, int maxLength) {
		// Init
		String[] lines = inputText.split("\n");
		LinkedList<String> messages = new LinkedList<String>();
		
		// Create batches of < maxLength messages
		String current = "";
		for (String line : lines) {
			if (current.length() + line.length() > maxLength) {
				messages.add(current);
				current = "";
			}
			current += line + "\n";
		}
		messages.add(current);
		
		// Convert to array
		String[] output = new String[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			output[i] = messages.get(i);
		}
		return output;
	}
}
