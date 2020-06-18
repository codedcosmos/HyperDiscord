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

import codedcosmos.hyperdiscord.chat.TextSender;
import codedcosmos.hyperdiscord.guild.GuildContext;
import codedcosmos.hyperdiscord.guild.GuildCustomer;
import codedcosmos.hyperdiscord.utils.debug.Log;
import codedcosmos.hyperdiscord.utils.text.OrderedString;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public abstract class CommandListener<C extends GuildContext> extends ListenerAdapter implements GuildCustomer<C> {
	private ArrayList<Command> commands;
	
	private String commandIndicator;
	
	public CommandListener(String packagepath, String commandIndicator) {
		commands = findCommands(packagepath);
		this.commandIndicator = commandIndicator;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			processMessageRecievedEvent(event);
		} catch (Exception e) {
			onCommandExecutionException(e, event);
		}
	}

	public abstract void onCommandExecutionException(Exception e, MessageReceivedEvent event);
	
	public void processMessageRecievedEvent(MessageReceivedEvent event) throws Exception {
		// Just make sure guild is cached
		C context = addGuild(event.getGuild());
		
		// Make sure it's a command
		if (!isCommand(event.getMessage())) return;
		
		// Make sure it's not a bot
		if (event.getAuthor().isBot()) return;
		
		// Update bot channel
		context.updateBotTextChannel(event.getTextChannel());
		
		String[] raw_message = event.getMessage().getContentDisplay().substring(1).split(" ");
		String commandID = raw_message[0];
		
		// Find and run command
		for (Command command : commands) {
			if (command.getClass().getSimpleName().toLowerCase().equals(commandID)) {
				onCommandRun(command);
				command.run(event);
				return;
			}
		}
		
		// No Command Found get suggestions
		ArrayList<OrderedString> suggestions = getSuggestions(commandID);
		
		
		// Sort
		Collections.sort(suggestions, new Comparator<OrderedString>() {
			@Override
			public int compare(OrderedString lhs, OrderedString rhs) {
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				return lhs.getDistance() > rhs.getDistance() ? 1 : (lhs.getDistance() < rhs.getDistance()) ? -1 : 0;
			}
		});
		
		TextSender.send(event.getTextChannel(), "Command '" + commandID + " does not exist, did you mean " + suggestions.get(0).getText() + "?");
	}
	
	protected abstract void onCommandRun(Command command);
	
	public boolean isCommand(Message message) {
		// Ignore messages that don't start with .
		return (message.getContentDisplay().toString().startsWith(commandIndicator));
	}
	
	public ArrayList<Command> findCommands(String packagepath) {
		ArrayList<Command> commands = new ArrayList<Command>();
		
		Reflections reflections = new Reflections(packagepath);
		
		Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
		
		Log.insertLine();
		for (Class clazz : classes) {
			try {
				Constructor constructor = clazz.getConstructor();
				Command command = (Command) constructor.newInstance();
				
				// Command is valid
				Log.print("Loaded command " + command.getName());
				commands.add(command);
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				Log.printErr("Error while loading command '" + clazz.getSimpleName() + "'");
				
				Log.printErr(e);
				continue;
			}
		}
		Log.print("Loaded " + commands.size());
		Log.insertLine();
		
		return commands;
	}
	
	public ArrayList<OrderedString> getSuggestions(String commandID) {
		ArrayList<OrderedString> suggestions = new ArrayList<OrderedString>();
		
		for (Command command : commands) {
			// Add command
			String commandName = command.getClass().getSimpleName().toLowerCase();
			suggestions.add(new OrderedString(commandName, commandID));
			
			// Add Aliases
			for (String alias : command.getAliases()) {
				suggestions.add(new OrderedString(commandName, alias, commandID));
			}
		}
		
		return suggestions;
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public int size() {
		return commands.size();
	}
	
	public Command get(int i) {
		return commands.get(i);
	}
	
	public void appendCommands(CommandListener commandListener) {
		commands.addAll(commandListener.getCommands());
		Log.print("Appended " + commandListener.size() + " commands!");
	}
}
