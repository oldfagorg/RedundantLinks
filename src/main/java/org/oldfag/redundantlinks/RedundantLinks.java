package org.oldfag.redundantlinks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author John200410
 */
public final class RedundantLinks extends JavaPlugin implements Listener {

	/**
	 * Constants for regex url check, credit to https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
	 */
	private final static String HTTP_REGEX_CHECK = "(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
	private final static String REGEX_CHECK = "[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)";

	/**
	 * Variables
	 */
	private int radius;

	@Override
	public void onEnable() {
		//create config if it doesn't exist
		saveDefaultConfig();

		//get the radius from the config
		this.radius = getConfig().getInt("radius");

		//register chat event
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	private void onChat(AsyncPlayerChatEvent event) {
		final World playerWorld = event.getPlayer().getWorld();

		//check if player is in overworld
		if(!playerWorld.getName().equals("world")) {
			return;
		}
		final Location location = event.getPlayer().getLocation();

		//check distance from the world spawn
		if(location.distance(playerWorld.getSpawnLocation()) > radius) {
			return;
		}

		//check if message contains a link
		final String msg = event.getMessage();

		//loop through each word
		for (String string : msg.split(" ")) {
			//check if it is a link
			if(string.matches(REGEX_CHECK) || string.matches(HTTP_REGEX_CHECK)) {
				event.setCancelled(true);
			}
		}
	}

}
