package epicapple.anvilinput;

import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class InputManager implements CommandExecutor, Listener {

	private static final String INVENTORY_TITLE = ChatColor.AQUA + "Input";
	private static final String MCCONTAINER_NAME = "minecraft:anvil";

	public InputManager(){
		showText();
	}

	//Command part

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return true;
		}
		execute((Player) sender, args);
		return true;
	}

	private void execute(Player p, String[] args){
		try{
			Location l = p.getLocation();

			net.minecraft.server.v1_8_R1.World nmsWorld = ((CraftWorld)p.getWorld()).getHandle();
			net.minecraft.server.v1_8_R1.BlockPosition blockPosition = new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ());
			net.minecraft.server.v1_8_R1.PlayerInventory nmsPlayerInventory = ((CraftPlayer)p).getHandle().inventory;
			net.minecraft.server.v1_8_R1.EntityPlayer entityHuman = ((CraftPlayer)p).getHandle();

			this.ep = entityHuman;

			ContainerAnvil anvilContainer = new ContainerAnvil(nmsPlayerInventory, nmsWorld, blockPosition, entityHuman);
			anvilContainer.checkReachable = false;

			int counter = entityHuman.nextContainerCounter();

			entityHuman.playerConnection.sendPacket(new PacketPlayOutOpenWindow(counter, MCCONTAINER_NAME, new ChatComponentText(INVENTORY_TITLE)));
			entityHuman.activeContainer = anvilContainer;
			entityHuman.activeContainer.windowId = counter;
			entityHuman.activeContainer.addSlotListener(entityHuman);

			this.anvilContainer = anvilContainer;
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	//Event part

	private ContainerAnvil anvilContainer = null;
	private EntityPlayer ep = null;

	private void showText(){

		if(ep != null){
			AnvilInput.log("d: " + ep.d + ", e: " + ep.e);
		}

		if(anvilContainer != null){
			try{
				Field f = anvilContainer.getClass().getDeclaredField("l");
				f.setAccessible(true);
				String s = (String) f.get(anvilContainer);
				AnvilInput.log(s);

				f = anvilContainer.getClass().getDeclaredField("a");
				f.setAccessible(true);
				AnvilInput.log("a: " + f.get(anvilContainer));

				f = anvilContainer.getClass().getDeclaredField("k");
				f.setAccessible(true);
				AnvilInput.log("k: " + f.get(anvilContainer));
			}catch(Exception ex){}
		}

		Bukkit.getScheduler().runTaskLater(AnvilInput.i(), new Runnable(){
			@Override
			public void run() {
				showText();
			}
		}, 10L);
	}
}
