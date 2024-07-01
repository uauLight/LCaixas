package com.uaulight.lcaixas.comandos;

import com.uaulight.lcaixas.utils.DataManager;
import org.bukkit.inventory.Inventory;
import java.io.File;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

public class ComandoCriarcaixa implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("criarcaixa")) {
            if (!(s instanceof Player)) {
                s.sendMessage("§cO console nao pode utilizar este comando!");
                return false;
            }
            if (args.length != 1) {
                s.sendMessage("§cComando incorreto, use /criarcaixa <id>");
                return false;
            }
            String caixa = args[0].toLowerCase();
            if (args[0].length() > 4) {
                s.sendMessage("§cID invalido! Por favor, use no máximo 4 digitos.");
                return false;
            }
            File file = DataManager.getFile(String.valueOf(caixa), "caixas");
            if (file.exists()) {
                s.sendMessage("§aUma caixa ja foi criada com o id '" + caixa + "', por favor escolha outro id.");
                return false;
            }
            Player p = (Player)s;
            Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)p, 36, "§0Criar Caixa §n" + caixa);
                    p.openInventory(inv);
        }
        return false;
    }
}