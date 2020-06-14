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

import codedcosmos.hyperdiscord.chat.reactions.ReactionReactor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class GuildContext implements ReactionReactor {
	
	// Guild
	protected Guild guild;
	
	// Chat
	private TextChannel botTextChannel;
	
	public GuildContext(Guild guild) {
		this.guild = guild;
		this.botTextChannel = guild.getTextChannels().get(0);
	}
	
	// Update
	public void updateBotTextChannel(TextChannel botTextChannel) {
		this.botTextChannel = botTextChannel;
	}
	
	// Method
	public boolean matches(long guildid) {
		return (guild.getIdLong() == guildid);
	}
	
	public boolean matches(GuildContext context) {
		return (guild.getIdLong() == context.getIdLong());
	}
	
	// Getters
	public TextChannel getBotTextChannel() {
		return botTextChannel;
	}
	
	public String getName() {
		return guild.getName();
	}
	
	public long getIdLong() {
		return guild.getIdLong();
	}
	
	public Guild getGuild() {
		return guild;
	}
}
