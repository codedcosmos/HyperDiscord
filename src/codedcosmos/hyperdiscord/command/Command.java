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

package codedcosmos.hyperdiscord.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
	public String getName();
	
	public String getHelp();
	public String[] getStynax();
	public void run(MessageReceivedEvent event) throws Exception;
	
	public String[] getAliases();
}
