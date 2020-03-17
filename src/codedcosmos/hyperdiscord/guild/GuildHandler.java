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

package codedcosmos.hyperdiscord.guild;

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.utils.debug.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

import java.util.ArrayList;

public abstract class GuildHandler<C extends GuildContext> {
	private ArrayList<C> contexts;
	
	public GuildHandler() {
		contexts = new ArrayList<C>(5);
	}
	
	public C getContextBy(long id) {
		for (C context : contexts) {
			if (context.matches(id)) return context;
		}
		
		Log.printErr("No guild of id " + id);
		
		return null;
	}
	
	public C addGuild(Guild guild) {
		for (C context : contexts) {
			if (context.matches(guild.getIdLong())) return context;
		}
		
		C context = createNew(guild);
		addGuild(context);
		return context;
	}
	
	public void addGuild(C context) {
		for (C contextB : contexts) {
			if (context.matches(contextB)) return;
		}
		
		Log.print("Added Guild " + context.getName());
		contexts.add(context);
	}
	
	public C getContextBy(Guild guild) {
		return getContextBy(guild.getIdLong());
	}
	
	public C getContextBy(MessageReceivedEvent event) {
		return getContextBy(event.getGuild());
	}
	
	public boolean isCached(MessageReceivedEvent event, C guild) {
		if (guild == null) {
			TextSender.send(event,"Error Guild '" + event.getGuild().getName() + "' is not cached in server");
			return true;
		} return false;
	}
	
	public void sendReactionAddEvent(GuildMessageReactionAddEvent event) {
		for (C guild : contexts) {
			guild.onReactionAdd(event);
		}
	}
	
	public void sendReactionRemoveEvent(GuildMessageReactionRemoveEvent event) {
		for (C guild : contexts) {
			guild.onReactionRemove(event);
		}
	}
	
	public ArrayList<C> getGuilds() {
		return contexts;
	}
	
	public abstract C createNew(Guild guild);
}
