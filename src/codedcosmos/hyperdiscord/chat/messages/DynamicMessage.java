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

import codedcosmos.hyperdiscord.chat.reactions.ReactionReactor;
import codedcosmos.hyperdiscord.guild.GuildContext;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public abstract class DynamicMessage implements ReactionReactor {

	private boolean exists = false;
	protected Message message;
	private long messageIdLong;

	private GuildContext context;

	public DynamicMessage(GuildContext context) {
		this.context = context;
	}

	public abstract Message getNew();
	public abstract void postSend(Message message);
	public abstract void postUpdate(Message message);

	public void send() {
		if (exists()) delete();
		message = context.getBotTextChannel().sendMessage(getNew()).complete();
		messageIdLong = this.message.getIdLong();
		exists = true;
		postSend(message);
	}

	public void updateState() {
		if (!exists()) return;
		Message message = getNew();
		this.message.editMessage(message).queue();
		postUpdate(this.message);
	}

	public void delete() {
		exists = false;
		message.delete().queue();
	}

	public void resend(String state) {
		delete();
		exists = true;
		send();
	}

	public boolean exists() {
		return exists;
	}
	
	public void checkForDeletion(MessageDeleteEvent event) {
		if (event.getMessageIdLong() == messageIdLong) {
			exists = false;
		}
	}
	
	@Override
	public void onReactionAdd(GuildMessageReactionAddEvent event) {}
	
	@Override
	public void onReactionRemove(GuildMessageReactionRemoveEvent event) {}
}
