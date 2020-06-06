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

package codedcosmos.hyperdiscord.utils.text;

import java.util.ArrayList;
import java.util.Random;

public class TextUtils {
	public static int distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		// i == 0
		int [] costs = new int [b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}
	
	public static String getRandom(ArrayList<String> choices) {
		Random random = new Random();
		
		return choices.get(random.nextInt(choices.size()));
	}
	
	public static String convertToMMSS(long time) {
		long timeInSeconds = time/1000;
		
		int hours = (int) timeInSeconds / 3600;
		int remainder = (int) timeInSeconds - hours * 3600;
		int minutes = remainder / 60;
		remainder = remainder - minutes * 60;
		int seconds = remainder;
		
		String hoursStr = makeDoubleDigit(Integer.toString(hours));
		String minutesStr = makeDoubleDigit(Integer.toString(minutes));
		String secondsStr = makeDoubleDigit(Integer.toString(seconds));
		
		if (hours > 0) {
			return hoursStr+":"+minutesStr+":"+secondsStr;
		} else {
			return minutesStr+":"+secondsStr;
		}
	}
	
	public static String makeDoubleDigit(String text) {
		if (text.length() == 0) {
			return "00";
		} else if (text.length() == 1) {
			return "0"+text;
		} else {
			return text;
		}
	}
}
