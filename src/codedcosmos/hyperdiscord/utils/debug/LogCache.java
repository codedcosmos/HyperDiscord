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

import java.util.Iterator;

public class LogCache {
	public static String[] lines;
	public static int index = 0;

	public static int maxIndex = 0;

	public LogCache(int maxSize) {
		init(maxSize);
	}

	public LogCache() {
		init(100);
	}

	private void init(int maxSize) {
		lines = new String[maxSize];
	}

	public void add(String line) {
		if (index == lines.length) index = 0;

		lines[index] = line;
		index++;

		if (maxIndex < lines.length) {
			maxIndex++;
		}
	}

	public Iterator<String> getIterator() {
		return new Iterator<String>() {
			private int i = index;
			private int o = 0;

			@Override
			public String next() {
				if (!hasNext()) {
					return null;
				}

				i++;
				o++;

				return lines[(i-1)%(maxIndex)];
			}

			@Override
			public boolean hasNext() {
				return o < maxIndex;
			}
		};
	}
	
	
	public String[] getCache() {
		return lines;
	}
}
