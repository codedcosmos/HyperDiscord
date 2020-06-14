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

package codedcosmos.hyperdiscord.command.prebuilt;

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.command.Command;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public abstract class HelpMessageGenerator {
	
	public Message buildMessage(MessageReceivedEvent event, int maxPerPage, char commandStart) {
		return buildMessage(event.getMessage().getContentDisplay(), maxPerPage, commandStart);
	}
	
	public Message buildMessage(String message, int maxPerPage, char commandStart) {
		int maxPages = getMaxPages(maxPerPage);
		int page = extractPage(message, maxPages);
		
		return buildMessage(page, maxPerPage, commandStart);
	}
	
	public Message buildMessage(int page, int maxPerPage, char commandStart) {
		int maxPages = getMaxPages(maxPerPage);
		
		// Create message builder and embed builder
		MessageBuilder builder = new MessageBuilder();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		
		// Invalid number checker
		if (page < 0 || page > maxPages) {
			builder.setContent("Invalid page number, must be between 1 and " + maxPages);
			return builder.build();
		}
		
		// Set base content
		String messageContent = "";
		
		messageContent += "\n";
		messageContent += "Help  -  page " + (page+1) + "/" + maxPages;
		messageContent += "\n\n";
		
		builder.setContent(messageContent);
		
		// Prepare and iterate
		int index = page * maxPerPage;
		
		ArrayList<Command> commands = getCommands();
		
		int o = 0;
		for (int i = index; i < index+maxPerPage && i < commands.size(); i++) {
			Command command = commands.get(i);
			
			String commandContent = "";
			commandContent += command.getHelp() + "\n\n";
			commandContent += "Usage:" + "\n";
			for (String usage : command.getStynax()) {
				commandContent += commandStart + command.getName() + " " + usage + "\n";
			}
			commandContent += "\n";
			
			embedBuilder.addField("~=~ __**"+command.getName()+"**__ ~=~", commandContent, true);
			
			o++;
		}
		while (o < maxPerPage) {
			o++;
			embedBuilder.addBlankField(true);
		}
		
		// Return
		builder.setEmbed(embedBuilder.build());
		return builder.build();
	}
	
	public int extractPage(String message, int max) {
		String[] lines = message.split(" ");
		if (lines.length < 2) return 1;
		
		try {
			int page = Integer.parseInt(lines[1]);
			if (page < 1) return -1;
			if (page > max) return -1;
			
			return page;
		} catch (NumberFormatException e) {
			// Do nothing
			return -1;
		}
	}
	
	public int getMaxPages(int maxPerPage) {
		double commands = getCommands().size();
		double commandsPerPage = maxPerPage;
		double divInt = Math.ceil(commands/commandsPerPage);
		
		int total = Math.max((int)divInt, 1);
		
		return total;
	}
	
	public abstract ArrayList<Command> getCommands();
}
