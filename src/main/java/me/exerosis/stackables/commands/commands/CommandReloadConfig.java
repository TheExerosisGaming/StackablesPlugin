package me.exerosis.stackables.commands.commands;

import me.exerosis.stackables.commands.CommandEntry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.exerosis.stackables.Stackables;

public class CommandReloadConfig extends CommandEntry {

	public CommandReloadConfig() {
		super("loadConfig", "reloadConfig", "config", "rl", "reload");
	}
	
	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		Stackables.loadConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "Config reloaded!");
		return true;
	}
	
}
