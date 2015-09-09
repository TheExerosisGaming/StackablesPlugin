package me.exerosis.stackables.commands.commands;

import me.exerosis.stackables.Stackables;
import me.exerosis.stackables.utils.StackablesUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandStackSize implements CommandExecutor, TabCompleter {

	public CommandStackSize() {
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length >= 1) {
			
			Material m;
			if (args.length == 1 && sender instanceof Player && ((Player) sender).getItemInHand() != null
					&& !((Player) sender).getItemInHand().getType().equals(Material.AIR)) {
				m = StackablesUtils.getMaterial(args[0]);
				
			} else {
				m = Material.matchMaterial(args[0]);
			}
			
			if (stackSize(sender, m, Arrays.copyOfRange(args, 1, args.length))) {
				return true;
			} else if (args.length >= 2 || !(sender instanceof Player && ((Player) sender).getItemInHand() != null && !((Player) sender).getItemInHand()
							.getType().equals(Material.AIR))) {
				sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" is not recognized as a Material!");
				return true;
			}
		}

		if (sender instanceof Player == false)
			return false;

		Player p = (Player) sender;

		Material m = p.getItemInHand().getType();

		return stackSize(sender, m, args);
	}

	/**
	 * 
	 * Helps manage the command
	 * 
	 * @param sender
	 * @param material
	 * @param arg
	 * @return Whether or not you should return false on the command.
	 */
	private static boolean stackSize(CommandSender sender, Material material, String... arg) {

		if (material == null || material.equals(Material.AIR)) {
			return false;
		}

		if (arg.length <= 0) {

			sender.sendMessage(ChatColor.DARK_GREEN + StackablesUtils.formatMaterial(material) + "'s" + ChatColor.GREEN + " max stack size is "
					+ ChatColor.DARK_GREEN + material.getMaxStackSize() + ChatColor.GREEN + "!");
			return true;
		}

		int maxStackSize;

		try {
			maxStackSize = Integer.parseInt(arg[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "\"" + arg[0] + "\" is not a integer!");
			return true;
		}

		Integer size = Stackables.setMaxStackSize(material, maxStackSize);

		if (size == null) {
			sender.sendMessage(ChatColor.RED + "Unable to set maxStackSize of " + StackablesUtils.formatMaterial(material) + "!");
		} else
			sender.sendMessage(ChatColor.DARK_GREEN + StackablesUtils.formatMaterial(material) + "'s" + ChatColor.GREEN
					+ " max stack size set to " + ChatColor.DARK_GREEN + size + ChatColor.GREEN + "!");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
