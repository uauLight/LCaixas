package com.uaulight.lcaixas.comandos;

import com.uaulight.lcaixas.utils.DataManager;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

public class ComandoGivecaixa implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givecaixa")) {
            if (!(s instanceof Player)) {
                int quantidade;
                if (args.length != 3) {
                    s.sendMessage("§cComando incorreto, use /givecaixa <id> [quantia] [player]");
                    return false;
                }
                String str = args[0].toLowerCase();
                File file1 = DataManager.getFile(str, "caixas");
                if (!file1.exists()) {
                    s.sendMessage("§cA caixa '" + str + "' não existe!");
                    ComandoCaixas.ListCaixas(s);
                    return false;
                }
                FileConfiguration fileConfiguration = DataManager.getConfiguration(file1);
                try {
                    quantidade = Integer.valueOf(args[1]).intValue();
                } catch (NumberFormatException e) {
                    s.sendMessage("§cQuantidade invalida!");
                    return false;
                }
                ItemStack ItemCaixa = caixa(fileConfiguration);
                ItemCaixa.setAmount(quantidade);
                if (args[2].equalsIgnoreCase("all")) {
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        PlayerInventory playerInventory1 = player1.getInventory();
                        playerInventory1.addItem(new ItemStack[] { ItemCaixa });
                    }
                    s.sendMessage("§a"+ quantidade + "caixa(s) '" + str + "' enviada(s) para todos os players do server.");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    s.sendMessage("§cEste player não está online no momento ou não existe!");
                    return false;
                }
                PlayerInventory playerInventory = player.getInventory();
                playerInventory.addItem(new ItemStack[] { ItemCaixa });
                s.sendMessage("§a" + quantidade + "caixa(s) '" + str + "' enviada(s) com sucesso para " + player.getName() + ".");
                return false;
            }
            if (args.length < 1 || args.length > 3) {
                s.sendMessage("§cComando incorreto, use /givecaixa <id> [quantia] [player]");
                return false;
            }
            String caixa = args[0].toLowerCase();
            File file = DataManager.getFile(caixa, "caixas");
            if (!file.exists()) {
                s.sendMessage("§cA caixa '" + caixa + "' não existe!");
                ComandoCaixas.ListCaixas(s);
                return false;
            }
            FileConfiguration config = DataManager.getConfiguration(file);
            if (args.length == 2) {
                int quantidade;
                try {
                    quantidade = Integer.valueOf(args[1]).intValue();
                } catch (NumberFormatException e) {
                    s.sendMessage("§cQuantidade invalida!");
                    return false;
                }
                Player player = (Player) s;
                PlayerInventory playerInventory = player.getInventory();
                ItemStack ItemCaixa = caixa(config);
                ItemCaixa.setAmount(quantidade);
                playerInventory.addItem(new ItemStack[] { ItemCaixa });
                s.sendMessage("§a"+ quantidade + "caixa(s) '" + caixa + "' enviada(s) para o seu inventário.");
                return false;
            }
            if (args.length == 3) {
                int quantidade;
                try {
                    quantidade = Integer.valueOf(args[1]).intValue();
                } catch (NumberFormatException e) {
                    s.sendMessage("§cQuantidade invalida!");
                    return false;
                }
                ItemStack ItemCaixa = caixa(config);
                ItemCaixa.setAmount(quantidade);
                if (args[2].equalsIgnoreCase("all")) {
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        PlayerInventory playerInventory1 = player1.getInventory();
                        playerInventory1.addItem(new ItemStack[] { ItemCaixa });
                    }
                    s.sendMessage("§a"+ quantidade + "caixa(s) '" + caixa + "' enviada(s) para todos os players do server.");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    s.sendMessage("§cEste player não está online no momento ou não existe!");
                    return false;
                }
                PlayerInventory playerInventory = player.getInventory();
                playerInventory.addItem(new ItemStack[] { ItemCaixa });
                s.sendMessage("§a" + quantidade + "caixa(s) '" + caixa + "' enviada(s) com sucesso para " + player.getName() + ".");
                return false;
            }
            Player p = (Player)s;
            PlayerInventory inv = p.getInventory();
            inv.addItem(new ItemStack[] { caixa(config) });
            s.sendMessage("§c1 caixa '" + caixa + "' enviada para o seu inventário.");
        }
        return false;
    }

    private ItemStack caixa(FileConfiguration config) {
        ItemStack caixa = config.getItemStack("Icone");
        ItemMeta meta = caixa.getItemMeta();
        meta.setDisplayName(config.getString("Nome"));
        List<String> lore = config.getStringList("Lore");
        if (!lore.isEmpty())
            meta.setLore(lore);
        caixa.setItemMeta(meta);
        return caixa;
    }
}