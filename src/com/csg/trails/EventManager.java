package com.csg.trails;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventManager
implements Listener
{

	static HashMap<Player, String> trail = new HashMap<Player, String>();
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player p = (Player)event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Material type = clicked.getType();
		Inventory inventory = event.getInventory();
		int d = clicked.getDurability();
		
		if (inventory.getName().equals(Trails.trail.getName())) {
			if(type != Material.AIR) {
				if (type == Material.REDSTONE)
				{
					trail.put(p, "rainbow");
				}
				else if (type == Material.NOTE_BLOCK)
				{
					trail.put(p, "notes");
				}
				else if (type == Material.WATER_BUCKET)
				{
					trail.put(p, "splash");
				}
				else if ((clicked.getType() == Material.STAINED_GLASS) && (d == 7))
				{
					trail.put(p, "none");
				}
				else if ((clicked.getType() == Material.STAINED_CLAY) && (d == 14))
				{
					trail.put(p, "hearts");
				}
				else if (clicked.getType() == Material.STAINED_GLASS && (d == 14))
				{
					trail.put(p, "rainblocks");
				}
				event.setCancelled(true);
				p.closeInventory();
			}
			}
		
	}
}