package epicapple.anvilinput;

import org.bukkit.plugin.java.JavaPlugin;

public class AnvilInput extends JavaPlugin {

	private static AnvilInput instance;

	public void onEnable(){
		instance = this;

		InputManager inputManager = new InputManager();
		this.getCommand("input").setExecutor(inputManager);
		this.getServer().getPluginManager().registerEvents(inputManager, this);
	}

	public static AnvilInput i(){
		return instance;
	}

	public static void log(Object message){
		instance.getLogger().info(message.toString());
	}
}
