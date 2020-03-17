/*
 * HyperDiscord by codedcosmos
 *
 * HyperDiscord is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * HyperDiscord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License 3 for more details.
 * You should have received a copy of the GNU General Public License 3
 * along with HyperDiscord.  If not, see <https://www.gnu.org/licenses/>.
 */

package codedcosmos.hyperdiscord.utils.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	// Static
	private static int longestLength = 12;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
	private static Date date = new Date();
	
	private static LogCache cache = new LogCache(5);
	
	// Print
	public static void print(Object... line) {
		String completedLine = getCompletedLine(line);
		
		System.out.println(completedLine);
	}
	
	public static void printErr(Object... line) {
		String completedLine = getCompletedLine(line);
		
		System.err.println(completedLine);
		cache.add(completedLine);
	}
	
	// Insert line
	public static void insertLine() {
		System.out.println("");
	}
	
	public static void insertLine(int number) {
		for (int i = 0; i < number; i++) {
			insertLine();
		}
	}
	
	// Get text
	private static String getCompletedLine(Object... line) {
		date.setTime(System.currentTimeMillis());
		String currentDate = dateFormat.format(date);
		
		String threadname = Thread.currentThread().getName();
		
		String text = "["+currentDate+"] ["+threadname+"] ";
		
		while (longestLength > text.length()) {
			text += " ";
		}
		
		return text + ": " + getFormated(line);
	}
	
	public static String getFormated(Object... line) {
		String text = "";
		
		for (Object t : line) {
			text += getFormated(t) + " ";
		}
		
		return text;
	}
	
	public static String getFormated(Object object) {
		if (object == null) {
			return "null";
		}
		
		if (object instanceof Throwable) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			((Throwable)object).printStackTrace(pw);
			return sw.toString();
		}
		
		return object.toString();
	}
	
	public static String[] getCache() {
		return cache.getCache();
	}
}
