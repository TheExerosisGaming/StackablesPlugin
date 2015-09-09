package me.exerosis.stackables.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandEntry implements CommandExecutor, TabCompleter {

	private List<CommandEntry> entrys = new ArrayList<CommandEntry>();
	private List<String> commands = new ArrayList<String>();
	private CommandEntry master = null;

	public CommandEntry() {

	}

	public CommandEntry(String... commands) {
		this(null, commands);
	}

	public CommandEntry(CommandEntry... entrys) {
		this(entrys, null);
	}
	
	public CommandEntry(String[] commands, CommandEntry... entrys) {
		this(entrys, commands);
	}
	
	public CommandEntry(String command, CommandEntry... entrys) {
		this(entrys, new String[]{command});
	}
	
	public CommandEntry(CommandEntry[] entrys, String[] commands) {
		if (entrys != null) {
			for (CommandEntry e : entrys) {
				addCommandEntry(e);
			}
			sortEntrys();
		}
		if (commands != null)
			for (String c : commands) {
				this.commands.add(c);
			}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1)
			for (CommandEntry ce : getEntrys()) {
				if (ce.checkRunCommand(sender, cmd, args[0], Arrays.copyOfRange(args, 1, args.length))) {
					if (ce.getPermission() != null && !sender.hasPermission(ce.getPermission())) {
						sender.sendMessage(cmd.getPermissionMessage());
						return true;
					}
					return ce.onCommand(sender, cmd, args[0], Arrays.copyOfRange(args, 1, args.length));
				}
			}
		if (playerOnly() && sender instanceof Player == false) {
			sender.sendMessage(notPlayerMsg());
			return true;
		}
		if (!disableHelp() && args.length == 1 && args[0].equalsIgnoreCase("help") && sendHelp(sender, cmd))
			return true;
		if (command(sender, cmd, label, args))
			return true;
		else
			return sendHelp(sender, cmd);
	}

	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			sender.sendMessage(ChatColor.RED + "Command '" + getFullUsage(cmd) + args[0] + "' not recognized!");
			return true;
		} else
			return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = tabComplete(sender, cmd, label, args);
		if (list == null)
			list = new ArrayList<String>();

		if (args.length <= 1) {
			List<String> otherList = getSubTabs();
			if (otherList != null)
				list.addAll(otherList);
		}

		if (!list.isEmpty()) {
			List<String> returns = new ArrayList<String>();
			for (String s : list) {
				if (s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
					returns.add(s);
			}
			return returns;
		}

		for (CommandEntry ce : getEntrys()) {
			if (ce.checkRunCommand(sender, cmd, args[0], Arrays.copyOfRange(args, 1, args.length))) {
				return ce.onTabComplete(sender, cmd, args[0], Arrays.copyOfRange(args, 1, args.length));
			}
		}

		return null;
	}

	public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return null;
	}

	public List<String> getTabs() {
		if (!commands.isEmpty())
			return commands;
		return null;
	}

	public List<String> getSubTabs() {
		List<String> set = new ArrayList<String>();

		if (getEntrys() != null)
			for (CommandEntry ce : getEntrys()) {
				if (ce.getTabs() != null)
					set.addAll(ce.getTabs());
			}
		if (!disableHelp())
			set.add("help");
		return set;

	}

	public List<CommandEntry> getEntrys() {
		return entrys;
	}

	public boolean checkRunCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label == null)
			return false;
		List<String> list = getTabs();
		if (list != null && !list.isEmpty()) {
			for (String s : list) {
				if (s.equalsIgnoreCase(label))
					return true;
			}
			return false;
		}
		return true;
	}

	public boolean addCommand(String command) {
		if (command == null)
			return false;
		return commands.add(command);
	}

	public boolean removeCommand(String command) {
		if (command == null)
			return false;
		return commands.remove(command);
	}

	public boolean addCommandEntry(CommandEntry entry) {
		if (entry == null)
			return false;
		if (getMaster() != null && getMaster() != entry) {
			Bukkit.getLogger().warning("Unable to add " + entry.getCommands() + " as it is my Master!");
			return false;
		}
		boolean b = entrys.add(entry);
		entry.setMaster(this);
		sortEntrys();
		return b;
	}

	public void sortEntrys() {
		Collections.sort(entrys, new Comparator<CommandEntry>() {
			public int compare(CommandEntry entryA, CommandEntry entryB) {
				return Math.max(entryA.getPriority().getSlot(), entryB.getPriority().getSlot());
			}
		});
	}

	public boolean removeCommandEntry(CommandEntry entry) {
		if (entry == null)
			return false;
		return entrys.remove(entry);
	}

	public CommandPriority getPriority() {
		return CommandPriority.NORMAL;
	}

	public String getDescription() {
		return null;
	}

	public String getCommandUsage() {
		List<String> tabs = getTabs();
		if (tabs != null)
			return tabs.get(0);
		return null;
	}

	public String getUsage() {
		return null;
	}

	public String getFullUsage(Command cmd) {
		String usage = "";
		if (getCommandUsage() != null)
			usage = usage + " " + getCommandUsage();
		if (getUsage() != null)
			usage = usage + " " + getUsage();
		CommandEntry master = getMaster();
		while (master != null) {
			if (master.getCommandUsage() != null)
				usage = master.getCommandUsage() + " " + usage;
			master = master.getMaster();
		}
		usage = "/" + cmd.getLabel() + " " + usage;
		return usage;

	}

	public boolean sendHelp(CommandSender sender, Command cmd) {
		if (getFullUsage(cmd) != null || getDescription() != null) {
			if (getFullUsage(cmd) != null)
				sender.sendMessage(getFullUsage(cmd));

			if (getDescription() != null)
				sender.sendMessage(ChatColor.GRAY + "- " + getDescription());
			return true;
		}
		return false;
	}

	public CommandEntry getMaster() {
		return master;
	}

	public void setMaster(CommandEntry master) {
		this.master = master;
	}

	public String getPermission() {
		return null;
	}

	public List<String> getCommands() {
		return commands;
	}

	public boolean disableHelp() {
		return false;
	}
	
	public void enableForCommand(PluginCommand pluginCommand) {
		Validate.notNull(pluginCommand, "The command is NULL!");
		pluginCommand.setExecutor(this);
		pluginCommand.setTabCompleter(this);
	}
	
	public boolean playerOnly() {
		return false;
	}
	
	public String notPlayerMsg() {
		return ChatColor.RED + "Only players can preform this command!";
	}
	
	public enum CommandPriority {
		LOWEST(0), LOW(1), NORMAL(2), HIGH(3), HIGHEST(4);
		private final int slot;

		private CommandPriority(int slot) {
			this.slot = slot;
		}

		public int getSlot() {
			return this.slot;
		}
	}
}