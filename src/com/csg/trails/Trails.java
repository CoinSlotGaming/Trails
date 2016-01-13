package com.csg.trails;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import de.inventivegames.particle.ParticleEffect;

public class Trails
extends JavaPlugin
{

	public static Inventory trail = Bukkit.createInventory(null, 54, "Trails");
	ItemStack one = new ItemStack(Material.REDSTONE);
	ItemStack two = new ItemStack(Material.NOTE_BLOCK);
	ItemStack three = new ItemStack(Material.WATER_BUCKET);
	static ItemStack fifty = new ItemStack(Material.STAINED_GLASS, 1, (short)7);

	int b = 0;
	int x = 0;
	int y = 0;

	HashMap<Block, Location> old = new HashMap<Block, Location>();

	public void onEnable(){
		getLogger().info("Loading Trails...");
		getServer().getPluginManager().registerEvents(new EventManager(), this);

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				for (Player p : Bukkit.getOnlinePlayers()) {
					if(EventManager.trail.get(p) != null) {
						if (EventManager.trail.get(p).equals("rainbow")) {
							try
							{
								playParticles(p, "red_dust", 0, 0, 0, 0, 1, 100);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						} else if (EventManager.trail.get(p).equals("notes")) {
							try
							{
								playParticles(p, "note", 0, 2.2f, 0, 0, 1, 1);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						} else if (EventManager.trail.get(p).equals("splash")) {
							try
							{
								playParticles(p, "splash", 0, 2.2f, 0, 0, 1, 10);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
						else if (EventManager.trail.get(p).equals("hearts")) {
							x++;

							try
							{
								if(x == 5) {
									playParticles(p, "heart", 0, 2.2f, 0, 0, 1, 1);
									x = 0;
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}

						}
						else if (EventManager.trail.get(p).equals("rainblocks")) {
							y++;
							try
							{
								if(y == 5) {
									Block blockUnder = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
									Location l = blockUnder.getLocation();
									if(blockUnder.getType() != Material.STAINED_GLASS)
									old.put(blockUnder, l);
									blockUnder.setType(Material.STAINED_GLASS);
									
									if(blockUnder.getType() != Material.AIR && blockUnder.getType() != Material.SNOW
											&& blockUnder.getType() != Material.CARPET) {
										if(b == 0) {
											blockUnder.setData((byte) 14);
										}
										else if(b == 1) {
											blockUnder.setData((byte) 1);
										}
										else if(b == 2) {
											blockUnder.setData((byte) 4);
										}
										else if(b == 3) {
											blockUnder.setData((byte) 5);
										}
										else if(b == 4) {
											blockUnder.setData((byte) 3);
										}
										else if(b == 5) {
											blockUnder.setData((byte) 10);
											b = 0;
										}
										b++;
										y = 0;
										new BukkitRunnable()
										{
											public void run()
											{
												for(Block b : old.keySet()) {
													old.get(b).getBlock().setType(b.getType());
													old.get(b).getBlock().setData(b.getData());
												}
												
											}
										}.runTaskLater(Bukkit.getPluginManager().getPlugin("Trails"), 20L);
										
									}
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}, 0, 1);
	}

	public void onDisable()
	{
		getLogger().info("Shutting down Trails...");
		getServer().clearRecipes();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player)sender;
		if ((commandLabel.equalsIgnoreCase("trail"))) {
			if (args.length == 0)
			{
				ItemMeta rMeta = one.getItemMeta();
				rMeta.setDisplayName(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Rainbow");
				one.setItemMeta(rMeta);
				trail.setItem(0, one);

				ItemMeta nMeta = two.getItemMeta();
				nMeta.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Notes");
				two.setItemMeta(nMeta);
				trail.setItem(1, two);

				ItemMeta bMeta = three.getItemMeta();
				bMeta.setDisplayName(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + "Splash");
				three.setItemMeta(bMeta);
				trail.setItem(2, three);

				ItemStack four = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
				ItemMeta heartMeta = four.getItemMeta();
				heartMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Heart");
				four.setItemMeta(heartMeta);
				trail.setItem(3, four);

				ItemStack five = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
				ItemMeta rBMeta = five.getItemMeta();
				rBMeta.setDisplayName(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Rainbow Blocks (rainblocks)");
				five.setItemMeta(rBMeta);
				trail.setItem(4, five);

				ItemMeta removeMeta = fifty.getItemMeta();
				removeMeta.setDisplayName(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD.toString() + "Remove");
				fifty.setItemMeta(removeMeta);
				trail.setItem(49, fifty);

				p.openInventory(trail);
			}
			else if (args.length == 1)
			{
				String a = args[0];
				if (a.equalsIgnoreCase("rainbow") ||
						a.equalsIgnoreCase("notes") ||
						a.equalsIgnoreCase("bubbles") ||
						a.equalsIgnoreCase("hearts") ||
						a.equalsIgnoreCase("rainblocks")) {
					EventManager.trail.put(p, a);
				} 
				else if (a.equalsIgnoreCase("remove")) {
					EventManager.trail.put(p, "none");
				}
			}
			else
			{
				sender.sendMessage("Invalid usage! Usage: /trail or /trail (trail)");
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public static void playParticles(Player player, String particleEnum, float offX, float offY, float offZ, float Dir, float Speed, int Count) {
		try
		{
			ParticleEffect.valueOf(particleEnum.toUpperCase()).sendToPlayers(Bukkit.getOnlinePlayers(), new Location(player.getWorld(), player.getLocation().getX() + offX,  player.getLocation().getY() + offY, player.getLocation().getZ() + offZ), Dir, Dir, Dir, Speed, Count);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}