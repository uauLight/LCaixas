package com.uaulight.lcaixas.eventos;

import java.io.File;
import java.util.Random;
import java.util.Set;

import com.uaulight.lcaixas.Main;
import com.uaulight.lcaixas.utils.Caixas;
import com.uaulight.lcaixas.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AbrirCaixa implements Listener {
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null)
            return;
        if (!e.getItem().hasItemMeta())
            return;
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            for (ItemStack item : Caixas.caixas.keySet()) {
                if (e.getItem().isSimilar(item)) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    ItemStack caixa = e.getItem().clone();
                    caixa.setAmount(1);
                    String id = (String)Caixas.caixas.get(caixa);
                    File file = DataManager.getFile(id, "caixas");
                    FileConfiguration config = DataManager.getConfiguration(file);
                    abrirCaixa(config, p);
                    removerCaixa(p);
                    return;
                }
            }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getName().contains("§0ABRINDO CAIXA!"))
            e.setCancelled(true);
    }

    private void abrirCaixa(FileConfiguration config, Player p) {
        p.closeInventory();
        Set<String> ITENS = config.getConfigurationSection("Itens").getKeys(false);
        int nitens = ITENS.size();
        if (nitens < 1) {
            p.sendMessage("§cEsta caixa estava vazia!");
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 45, "§0ABRINDO CAIXA!");
        inv.setItem(22, config.getItemStack("Itens.0"));
        p.openInventory(inv);
        goTask(inv, p, config, nitens);
    }

    private void goTask(final Inventory inv, final Player p, final FileConfiguration config, final int nitens) {
        (new BukkitRunnable() {
            Random rnd = new Random();

            int slot1 = 0;

            int slot2 = 44;

            public void run() {
                if (this.slot1 == 22) {
                    AbrirCaixa.this.finalizarCaixa(p, inv);
                    cancel();
                } else {
                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                    inv.setItem(this.slot1, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)(this.rnd.nextInt(14) + 1)));
                    inv.setItem(this.slot2, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)(this.rnd.nextInt(14) + 1)));
                    inv.setItem(22, config.getItemStack("Itens." + this.rnd.nextInt(nitens)));
                    this.slot1++;
                    this.slot2--;
                }
            }
        }).runTaskTimer((Plugin) Main.aqui, 0L, 2L);
    }

    private void finalizarCaixa(Player p, Inventory inv) {
        ItemStack ganhou = inv.getItem(22);
        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItem(p.getLocation(), ganhou);
        } else {
            p.getInventory().addItem(new ItemStack[] { ganhou });
        }
        p.sendMessage("§aVocê ganhou x" + ganhou.getAmount() + " " + ganhou.getType() + "§a!");
    }

    private void removerCaixa(Player p) {
        ItemStack ap = p.getItemInHand();
        if (ap.getAmount() == 1) {
            p.setItemInHand(new ItemStack(Material.AIR));
        } else {
            ap.setAmount(ap.getAmount() - 1);
            p.setItemInHand(ap);
        }
    }
}

