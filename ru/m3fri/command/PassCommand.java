package ru.m3fri.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import ru.m3fri.Loader;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;

public class PassCommand extends Command {

	private final Plugin plugin;
	private final Config settings;

	public PassCommand(String name, Plugin plugin, File dataFolder) {
		super(name, "Выдать проходку игроку", "/pass <имя>");
		this.settings = new Config(new File(dataFolder, "settings.yml"), 2);
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		if (!sender.hasPermission(settings.getString("pass-perm", "pass.give")) && !sender.isOp()) {
			sender.sendMessage(TextFormat.colorize(settings.getString("pass-no", "У тебя нет прав на использование этой команды")));
			return true;
		}

		if (!label.equalsIgnoreCase("pass")) {
			sender.sendMessage(TextFormat.colorize(settings.getString("pass-usage", "Использование: /pass <имя>")));
			return false;
		}

		if (args.length != 1) {
			sender.sendMessage(TextFormat.colorize(settings.getString("pass-usage", "Использование: /pass <имя>")));
			return false;
		}

		String playerName = args[0];

		((Loader) plugin).addPass(playerName);
		sender.sendMessage(TextFormat.colorize(settings.getString("pass-done", "Проходка успешно выдана игроку {player}").replace("{player}", playerName)));
		return true;
	}
}
