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

package codedcosmos.hyperdiscord.chat.messages;

import codedcosmos.hyperdiscord.chat.reactions.ReactionBox;
import codedcosmos.hyperdiscord.guild.GuildContext;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public abstract class BookMessage extends DynamicMessage {
	
	protected int currentPage;
	
	// Reactions
	private ReactionBox next;
	private ReactionBox prev;
	
	// sent
	private boolean sent = false;
	
	public BookMessage(GuildContext context) {
		super(context);
		
		currentPage = 0;
	}
	
	@Override
	public Message getNew() {
		return getNew(currentPage);
	}
	
	public abstract Message getNew(int pageNum);
	
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {
		if (!sent) return;
		
		next.onReactionAdd(event);
		prev.onReactionAdd(event);
		
		boolean update = false;
		
		if (prev.isSelected()) {
			currentPage = Math.max(0, currentPage-1);
			update = true;
		}
		if (next.isSelected()) {
			currentPage = Math.min(getNumPages()-1, currentPage+1);
			update = true;
		}
		
		if (update) {
			updateState();
		}
	}
	
	@Override
	public void onReactionRemove(GuildMessageReactionRemoveEvent event) {
		if (!sent) return;
		
		next.onReactionRemove(event);
		prev.onReactionRemove(event);
	}
	
	@Override
	public void postSend(Message message) {
		prev = new ReactionBox(message, "\u2b05");
		next = new ReactionBox(message, "\u27a1");
		
		sent = true;
	}
	
	@Override
	public void postUpdate(Message message) {
		clearReactions();
		
		prev.clearCount();
		next.clearCount();
		
		prev.addToMessageComplete();
		next.addToMessageComplete();
	}
	
	public void clearReactions() {
		if (!sent) return;
		
		message.clearReactions().complete();
	}
	
	public abstract int getNumPages();
}
