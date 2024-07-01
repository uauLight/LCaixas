package com.uaulight.lcaixas.comandos;

import com.uaulight.lcaixas.utils.Caixas;
import com.uaulight.lcaixas.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ComandoEditarcaixa implements Listener, CommandExecutor
{
    public boolean onCommand(final CommandSender s, final Command cmd, final String lbl, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("editarcaixa")) {
            if (!(s instanceof Player)) {
                s.sendMessage("§cO console nao pode utilizar este comando!");
                return false;
            }
            if (args.length < 2) {
                s.sendMessage("§cComando incorreto, use: ");
                s.sendMessage("§c/editarcaixa <id> itens §8-§7 Para editar os itens.");
                s.sendMessage("§c/editarcaixa <id> icone §8-§7 Para editar o icone da caixa.");
                s.sendMessage("§c/editarcaixa <id> nome <nome> §8-§7 Para editar o nome da caixa.");
                s.sendMessage("§c/editarcaixa <id> removelore §8-§7 Para remover a lore da caixa.");
                s.sendMessage("§c/editarcaixa <id> addlore <frase> §8-§7 Para adicionar uma linha de lore.");
                return false;
            }
            final String caixa = args[0].toLowerCase();
            final File file = DataManager.getFile(caixa, "caixas");
            if (!file.exists()) {
                s.sendMessage("§cA caixa '" + caixa + "' n\u00e3o existe!");
                ComandoCaixas.ListCaixas(s);
                return false;
            }
            final FileConfiguration config = DataManager.getConfiguration(file);
            if (args[1].equalsIgnoreCase("itens")) {
                final Player p = (Player)s;
                final Set<String> ITENS = (Set<String>)config.getConfigurationSection("Itens").getKeys(false);
                final int n = ITENS.size();
                final Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)p, 36, "§0Editar Caixa §n" + caixa);
                for (int i = 0; n > i; ++i) {
                    final ItemStack item = config.getItemStack("Itens." + i);
                    if (item != null) {
                        inv.setItem(i, item);
                    }
                }
                p.openInventory(inv);
                return false;
            }
            if (args[1].equalsIgnoreCase("nome")) {
                if (args.length < 3) {
                    s.sendMessage("§cComando incorreto, use /editarcaixa <id> nome <nome>");
                    return false;
                }
                String nome = "";
                for (int j = 2; j < args.length; ++j) {
                    nome = String.valueOf(nome) + args[j] + " ";
                }
                config.set("Nome", (Object)nome.replace("&", "§"));
                try {
                    config.save(file);
                    s.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                    Caixas.carregarCaixas();
                }
                catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§cNao foi possivel salvar o arquivo da caixa '" + caixa + "'.");
                }
                return false;
            }
            else {
                if (args[1].equalsIgnoreCase("icone")) {
                    this.abrirMenuEditarIcone((Player)s, caixa);
                    return false;
                }
                if (args[1].equalsIgnoreCase("removelore")) {
                    config.set("Lore", (Object)null);
                    try {
                        config.save(file);
                        s.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                        Caixas.carregarCaixas();
                    }
                    catch (IOException e2) {
                        Bukkit.getConsoleSender().sendMessage("§cNao foi possivel salvar o arquivo da caixa '" + caixa + "'.");
                    }
                    return false;
                }
                if (args[1].equalsIgnoreCase("addlore")) {
                    if (args.length < 3) {
                        s.sendMessage("§cComando incorreto, use /editarcaixa <id> addlore <frase>");
                        return false;
                    }
                    final List<String> lore = new ArrayList<String>();
                    lore.addAll(config.getStringList("Lore"));
                    String novaLinha = "";
                    for (int k = 2; k < args.length; ++k) {
                        novaLinha = String.valueOf(novaLinha) + args[k] + " ";
                    }
                    lore.add(novaLinha.replace("&", "§"));
                    config.set("Lore", (Object)lore);
                    try {
                        config.save(file);
                        s.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                        Caixas.carregarCaixas();
                    }
                    catch (IOException e3) {
                        Bukkit.getConsoleSender().sendMessage("§cNao foi possivel salvar o arquivo da caixa '" + caixa + "'.");
                    }
                    return false;
                }
                else {
                    s.sendMessage("§cComando incorreto, use: ");
                    s.sendMessage("§c/editarcaixa <id> itens §8-§7 Para editar os itens.");
                    s.sendMessage("§c/editarcaixa <id> icone §8-§7 Para editar o icone da caixa.");
                    s.sendMessage("§c/editarcaixa <id> nome <nome> §8-§7 Para editar o nome da caixa.");
                    s.sendMessage("§c/editarcaixa <id> removelore §8-§7 Para remover a lore da caixa.");
                    s.sendMessage("§c/editarcaixa <id> addlore <frase> §8-§7 Para adicionar uma linha de lore.");
                }
            }
        }
        return false;
    }

    private void abrirMenuEditarIcone(final Player p, final String caixa) {
        final File file = DataManager.getFile(caixa, "caixas");
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "§0Icone da Caixa §n" + caixa);
        final ItemStack atual = DataManager.getConfiguration(file).getItemStack("Icone");
        final ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
        final ItemMeta atualMeta = atual.getItemMeta();
        final ItemMeta vidroMeta = vidro.getItemMeta();
        atualMeta.setDisplayName("§a§lIcone atual da caixa");
        vidroMeta.setDisplayName("§8-/-");
        atual.setItemMeta(atualMeta);
        vidro.setItemMeta(vidroMeta);
        for (int i = 0; i < 27; ++i) {
            inv.setItem(i, vidro);
        }
        inv.setItem(12, atual);
        inv.setItem(14, (ItemStack)null);
        p.openInventory(inv);
    }
}