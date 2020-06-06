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

package codedcosmos.hyperdiscord.chat.reactions;

import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class ReactionBox implements ReactionReactor {
	
	private int count;
	private String unicode;
	private Message message;
	
	public ReactionBox(Message message, char unicode) {
		init(message, ""+unicode);
	}
	
	public ReactionBox(Message message, String unicode) {
		init(message, unicode);
	}
	
	private void init(Message message, String unicode) {
		this.message = message;
		this.unicode = unicode;
		
		count = 0;
		
		addToMessageComplete();
	}
	
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {
		// Make sure it's the same message
		if (message.getIdLong() != event.getMessageIdLong()) return;
		
		// Check if the event corresponds to the correct reaction
		if (!event.getReactionEmote().getEmoji().equals(unicode)) return;
		
		count++;
	}
	
	@Override
	public void onReactionRemove(GuildMessageReactionRemoveEvent event) {
		// Make sure it's the same message
		if (message.getIdLong() != event.getMessageIdLong()) return;
		
		// Check if the event corresponds to the correct reaction
		if (!event.getReactionEmote().getEmoji().equals(unicode)) return;
		
		count--;
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean isSelected() {
		return (count > 1);
	}
	
	public void addToMessageQueue() {
		message.addReaction(unicode).queue();
	}
	
	public void addToMessageComplete() {
		message.addReaction(unicode).complete();
	}
	
	public void clearCount() {
		count = 0;
	}
}
