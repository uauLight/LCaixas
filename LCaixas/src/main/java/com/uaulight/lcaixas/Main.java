package com.uaulight.lcaixas;

import com.uaulight.lcaixas.comandos.ComandoCriarcaixa;
import com.uaulight.lcaixas.comandos.ComandoGivecaixa;
import com.uaulight.lcaixas.comandos.ComandoEditarcaixa;
import com.uaulight.lcaixas.comandos.ComandoDelcaixa;
import org.bukkit.command.CommandExecutor;
import com.uaulight.lcaixas.comandos.ComandoCaixas;
import org.bukkit.plugin.PluginManager;
import com.uaulight.lcaixas.eventos.AbrirCaixa;
import org.bukkit.plugin.Plugin;
import com.uaulight.lcaixas.eventos.ComandosListener;
import org.bukkit.Bukkit;
import com.uaulight.lcaixas.utils.DataManager;
import org.bukkit.event.HandlerList;
import com.uaulight.lcaixas.utils.Caixas;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    public static Main aqui;

    public void onEnable() {
        this.instanceMain();
        this.gerarConfigs();
        this.registrarEventos();
        this.registrarComandos();
        Caixas.carregarCaixas();
    }

    public void onDisable() {
        HandlerList.unregisterAll((Listener)this);
    }

    public void instanceMain() {
        Main.aqui = this;
    }

    public void gerarConfigs() {
        this.saveDefaultConfig();
        DataManager.createFolder("caixas");
    }

    public void registrarEventos() {
        final PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents((Listener)new ComandosListener(), (Plugin)this);
        pm.registerEvents((Listener)new AbrirCaixa(), (Plugin)this);
    }

    public void registrarComandos() {
        this.getCommand("caixas").setExecutor((CommandExecutor)new ComandoCaixas());
        this.getCommand("delcaixa").setExecutor((CommandExecutor)new ComandoDelcaixa());
        this.getCommand("editarcaixa").setExecutor((CommandExecutor)new ComandoEditarcaixa());
        this.getCommand("givecaixa").setExecutor((CommandExecutor)new ComandoGivecaixa());
        this.getCommand("criarcaixa").setExecutor((CommandExecutor)new ComandoCriarcaixa());
    }
}
