package io.github.seamo.ability;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaTabCompleter implements TabCompleter {

    private final JavaPlugin plugin;

    public JavaTabCompleter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.asList("fireman", "teleporter", "fang", "exploder");
        }
        return null;
    }
}