package me.exerosis.stackables.commands.commands;

import me.exerosis.stackables.Stackables;
import me.exerosis.stackables.commands.CommandEntry;
import me.exerosis.stackables.utils.StackablesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandSaveMaxStackSize extends CommandEntry {

    public CommandSaveMaxStackSize() {
        super("SaveStackSizes", "ToggleSaveStacksizes", "SaveMaxStackSizes", "ToggleSaveMaxStackSizes", "Save", "Toggle", "autoSave");
    }

    @Override
    public List<String> getSubTabs() {
        return Arrays.asList("true", "false");
    }

    @Override
    public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
        boolean b = false;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + Stackables.SAVE_STACK_SIZES + ChatColor.GREEN + " is set to " + ChatColor.DARK_GREEN
                    + StackablesUtils.capitalize(Stackables.getPlugin().getConfig().getBoolean(Stackables.SAVE_STACK_SIZES) + "")
                    + ChatColor.GREEN + "!");
            return true;
        }
        else if (args.length >= 1)
            if (args[0].toLowerCase().startsWith("t"))
                b = true;

        Stackables.setAutoSave(b);
        sender.sendMessage(ChatColor.DARK_GREEN + Stackables.SAVE_STACK_SIZES + ChatColor.GREEN + " set to " + ChatColor.DARK_GREEN
                + StackablesUtils.capitalize(b + "") + ChatColor.GREEN + "!");
        return true;
    }
}