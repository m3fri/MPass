package ru.m3fri;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import ru.m3fri.command.PassCommand;

import java.io.File;
import java.util.Arrays;

public class Loader extends PluginBase implements Listener {

	private Config passesConfig;
	private Config settings;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.passesConfig = new Config(getDataFolder() + "/passes.yml", Config.YAML);
		this.settings = new Config(new File(getDataFolder(), "config.yml"), Config.YAML);
		this.getServer().getCommandMap().register("pass", new PassCommand("pass", this, this.getDataFolder()));

		this.getLogger().info("§fПлагин §eуспешно §fвключился! Автор: §am3fri");

		String author = this.getDescription().getAuthors().get(0);
		if (!author.equalsIgnoreCase("m3fri")) {
			this.getLogger().warning("§c§lИмя автора плагина в §eplugin.yml §fбыло изменено! Верните §bm3fri, §fчтобы плагин заработал");
			this.getServer().getPluginManager().disablePlugin(this);
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		int deviceOS = player.getLoginChainData().getDeviceOS();
		String disallowedDevices = settings.getString("disallowed-devices", "7");
		if (Arrays.asList(disallowedDevices.split(",")).contains(Integer.toString(deviceOS)) && !hasPass(player.getName())) {
			player.kick(settings.getString("kick-message", "Купите проходку!"), false);
			event.setCancelled(true);
		}
	}


	private boolean hasPass(String playerName) {
		return passesConfig.exists(playerName.toLowerCase());
	}

	public void addPass(String playerName) {
		passesConfig.set(playerName.toLowerCase(), true);
		passesConfig.save();
	}
}
