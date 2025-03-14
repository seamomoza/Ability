package io.github.seamo.ability;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;


public final class Ability extends JavaPlugin implements Listener, CommandExecutor {


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("psy").setExecutor(this);
        getCommand("psy").setTabCompleter(new JavaTabCompleter(this));
        getCommand("psychics").setExecutor(this);
        getCommand("psychics").setTabCompleter(new JavaTabCompleter(this));
        getCommand("fireman").setExecutor(this);
        getCommand("fireman").setTabCompleter(new JavaTabCompleter(this));
        getCommand("teleporter").setExecutor(this);
        getCommand("teleporter").setTabCompleter(new JavaTabCompleter(this));
        getCommand("fang").setExecutor(this);
        getCommand("fang").setTabCompleter(new JavaTabCompleter(this));
        getCommand("exploder").setExecutor(this);
        getCommand("exploder").setTabCompleter(new JavaTabCompleter(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            Player target = getServer().getPlayer(args[0]);
            if (target != null) {
                switch (args[1].toLowerCase()) {
                    case "psy":
                    case "psychics":
                        giveItems(target, "basic");
                        break;
                    case "fireman":
                        giveItems(target, "fireman");
                        break;
                    case "teleporter":
                        giveItems(target, "teleporter");
                        break;
                    case "fang":
                        giveItems(target, "fang");
                        break;
                    case "exploder":
                        giveItems(target, "exploder");
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Unknown ability.");
                        return false;
                }
                sender.sendMessage(ChatColor.GREEN + "아이템이 " + target.getName() + "에게 지급되었습니다!");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
                return false;
            }
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (command.getName().toLowerCase()) {
                case "fireman":
                    giveItems(player, "fireman");
                    break;
                case "teleporter":
                    giveItems(player, "teleporter");
                    break;
                case "fang":
                    giveItems(player, "fang");
                    break;
                case "exploder":
                    giveItems(player, "exploder");
                    break;
                default:
                    return false;
            }
            player.sendMessage(ChatColor.GREEN + "기초 소지 템 지급!");
            return true;
        }
        return false;
    }

    private void giveItems(Player player, String ability) {
        switch (ability) {
            case "basic":
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                player.getInventory().addItem(new ItemStack(Material.BREAD, 32));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS));
                break;
            case "fireman":
                player.getInventory().addItem(createNamedItem(Material.FIRE_CHARGE, "§2능력 완드"));
                break;
            case "teleporter":
                player.getInventory().addItem(createNamedItem(Material.ENDER_PEARL, "§2능력 완드"));
                break;
            case "fang":
                player.getInventory().addItem(createNamedItem(Material.STICK, "§2능력 완드"));
                break;
            case "exploder":
                player.getInventory().addItem(createNamedItem(Material.BLAZE_ROD, "§2능력 완드"));
                break;
        }
    }

    @EventHandler
    public void onEnderPearlUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.ENDER_PEARL || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§2능력 완드"))
            return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection().normalize();
            player.teleport(player.getLocation().add(direction.multiply(1)));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            player.getWorld().spawnParticle(Particle.WITCH, player.getLocation(), 10);
        }
    }

    @EventHandler
    public void onPlayerUseFireball(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.FIRE_CHARGE || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§2능력 완드"))
            return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection().normalize();

            loc.add(direction);
            loc.getWorld().spawn(loc, Fireball.class);
        }
    }

    @EventHandler
    public void onPlayerUseStick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.STICK || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§2능력 완드"))
            return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection().normalize();
            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    if (count >= 50) {
                        cancel();
                        return;
                    }

                    loc.add(direction);
                    loc.getWorld().spawn(loc, EvokerFangs.class);
                    count++;
                }
            }.runTaskTimer(this, 0L, 1L);
        }
    }

    @EventHandler
    public void onPlayerUseBlaze(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.BLAZE_ROD || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§2능력 완드"))
            return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection().normalize();
            new BukkitRunnable() {
                Location particleLoc = loc.clone();
                int distance = 0;

                @Override
                public void run() {
                    if (distance > 50) { // 최대 거리 제한
                        cancel();
                        return;
                    }

                    if (!particleLoc.getBlock().isPassable()) { // 벽에 닿으면 터짐
                        explode(particleLoc);
                        cancel();
                        return;
                    }

                    List<Entity> nearbyEntities = particleLoc.getWorld().getNearbyEntities(particleLoc, 0.5, 0.5, 0.5).stream()
                            .filter(entity -> entity instanceof Player == false)
                            .toList();

                    if (!nearbyEntities.isEmpty()) { // 엔티티에 닿으면 터짐
                        explode(particleLoc);
                        cancel();
                        return;
                    }

                    particleLoc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, particleLoc, 5, 0.1, 0.1, 0.1, 0.05);
                    particleLoc.add(direction);
                    distance++;
                }
            }.runTaskTimer(this, 0L, 1L);
        }
    }

    private void explode(Location location) {
        location.getWorld().createExplosion(location, 2f);
    }

    private ItemStack createNamedItem(Material material, String name) {
        return createNamedItem(material, name, 1);
    }

    private ItemStack createNamedItem(Material material, String name, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}