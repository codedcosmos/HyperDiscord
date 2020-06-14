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

public class OrderedString {
	private int distance;
	private String text;
	
	public OrderedString(String text, String compare) {
		distance = TextUtils.distance(text, compare);
		this.text = text;
	}
	
	public OrderedString(String text, String comparel, String comparer) {
		distance = TextUtils.distance(comparel, comparer);
		this.text = text;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public String getText() {
		return text;
	}
}
