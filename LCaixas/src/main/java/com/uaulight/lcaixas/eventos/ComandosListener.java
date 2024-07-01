package com.uaulight.lcaixas.eventos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uaulight.lcaixas.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.uaulight.lcaixas.utils.Caixas;

public class ComandosListener implements Listener {

        @EventHandler
        public void InventoryClose(InventoryCloseEvent e) {
                if (e.getInventory().getName().contains("§0Criar Caixa §n")) {
                        Inventory inv = e.getInventory();
                Player p = (Player)e.getPlayer();
                criarCaixa(inv, p);
                return;
        }
    if (e.getInventory().getName().contains("§0Editar Caixa §n")) {
                                            Player p = (Player)e.getPlayer();
        Inventory inv = e.getInventory();
        editarCaixaItens(inv, p);
      return;
}
    if (e.getInventory().getName().contains("§0Icone da Caixa §n")) {
            Player p = (Player)e.getPlayer();
            Inventory inv = e.getInventory();
            editarCaixaIcone(inv, p);
            return;
    }
        }

@EventHandler
public void InventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getName().contains("§0Icone da Caixa §n") &&
        e.getRawSlot() != 14 && e.getRawSlot() < 27)
        e.setCancelled(true);
        }

private void criarCaixa(Inventory inv, Player p) {
        ItemStack[] itens = inv.getContents();
        int j = 0;
        String caixa = inv.getName().substring(16, inv.getName().length());
        File file = DataManager.getListFiles(caixa, "caixas");
        FileConfiguration config = DataManager.getConfiguration(file);
        DataManager.createFile(file);
        List<String> lore = new ArrayList<>();
        lore.add("§aLore padrão - Caixa §n" + caixa);
        lore.add("§eEdite a caixa usando /editarcaixa!");
        config.set("Icone", iconePadrao(caixa));
        config.set("Nome", "§a§lCaixa §n"+ caixa);
        config.set("Lore", lore);
        config.createSection("Itens");
        byte b;
        int i;
        ItemStack[] arrayOfItemStack1;
        for (i = (arrayOfItemStack1 = itens).length, b = 0; b < i; ) {
        ItemStack item = arrayOfItemStack1[b];
        if (item != null) {
        config.set("Itens." + j, item);
        j++;
        }
        b++;
        }
        try {
        config.save(file);
        p.sendMessage("§aCaixa '" + caixa + "' criada com sucesso.");
        Caixas.carregarCaixas();
        } catch (IOException ex) {
        Bukkit.getConsoleSender().sendMessage("§cNão foi possivel salvar o arquivo da caixa '" + caixa + "'.");
        }
        }

private void editarCaixaItens(Inventory inv, Player p) {
        ItemStack[] itens = inv.getContents();
        int j = 0;
        String caixa = inv.getName().substring(17, inv.getName().length());
        File file = DataManager.getListFiles(caixa, "caixas");
        FileConfiguration config = DataManager.getConfiguration(file);
        config.set("Itens", null);
        config.createSection("Itens");
        byte b;
        int i;
        ItemStack[] arrayOfItemStack1;
        for (i = (arrayOfItemStack1 = itens).length, b = 0; b < i; ) {
        ItemStack item = arrayOfItemStack1[b];
        if (item != null) {
        config.set("Itens." + j, item);
        j++;
        }
        b++;
        }
        try {
        config.save(file);
        p.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
        Caixas.carregarCaixas();
        } catch (IOException ex) {
        Bukkit.getConsoleSender().sendMessage("§cNão foi possivel salvar o arquivo da caixa '" + caixa + "'.");
        }
        }

private void editarCaixaIcone(Inventory inv, Player p) {
        String caixa = inv.getName().substring(19, inv.getName().length());
        File file = DataManager.getListFiles(caixa, "caixas");
        FileConfiguration config = DataManager.getConfiguration(file);
        ItemStack icone = inv.getItem(14);
        if (icone == null || icone.getType().equals(Material.AIR)) {
        p.sendMessage("§c Icone invalido, a caixa não foi editada.");
        } else {
        icone.setAmount(1);
        config.set("Icone", icone);
        try {
        config.save(file);
        p.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
        Caixas.carregarCaixas();
        } catch (IOException ex) {
        Bukkit.getConsoleSender().sendMessage("§cNão foi possivel salvar o arquivo da caixa '" + caixa + "'.");
        }
        }
        }

private ItemStack iconePadrao(String id) {
        ItemStack icone = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = icone.getItemMeta();
        meta.setDisplayName("§aCaixa §2" + id);
        icone.setItemMeta(meta);
        return icone;
        }
        }