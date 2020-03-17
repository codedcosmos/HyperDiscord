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

package codedcosmos.hyperdiscord.bot;

import codedcosmos.hyperdiscord.utils.debug.Log;

import java.util.HashMap;

public class ArgsParser {
	public static HashMap<String, String> parseArgs(String[] args, String... required) throws IllegalArgumentException {
		Log.print("Extracting arguments");
		
		// Parse
		HashMap<String, String> argsMap = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			if (i + 1 >= args.length) break;
			
			String arg1 = args[i];
			String arg2 = args[i + 1];
			
			if (arg1.startsWith("-") && !arg2.startsWith("-")) {
				// Add them
				argsMap.put(arg1.substring(1), arg2);
			}
		}
		
		// Check for required
		for (String item : required) {
			if (!argsMap.containsKey(item)) {
				throw new IllegalArgumentException("Could not find required argument, '" + item + "'\n"
				+ "Specify with -"+item+" ARGS_SETTING");
			}
		}
		
		return argsMap;
	}
}
