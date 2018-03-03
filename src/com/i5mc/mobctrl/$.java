package com.i5mc.mobctrl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class $ extends JavaPlugin implements Listener {

    private Map<EntityType, MobCtrlInf> all;
    private Gson json;

    public void onEnable() {
        saveDefaultConfig();

        json = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        loadAll();
        Bukkit.getPluginManager().registerEvents(this, this);

        PluginHelper.addExecutor(this, "mobctrl", "mobctrl.admin", this::mobctrl);
    }

    private void mobctrl(CommandSender who, List<String> input) {
        loadAll();
        who.sendMessage(ChatColor.GREEN + "Okay!");
    }

    private void loadAll() {
        all = new EnumMap<>(EntityType.class);

        List<Map<?, ?>> list = getConfig().getMapList("list");
        list.forEach(l -> {
            MobCtrlInf inf = json.fromJson(json.toJsonTree(l), MobCtrlInf.class);
            if (inf.getMobType() == null) {
                return;
            }

            inf.setHandle(l);
            all.put(inf.getMobType(), inf);
        });

    }

    @EventHandler
    public void handle(CreatureSpawnEvent event) {
        MobCtrlInf inf = all.get(event.getEntityType());
        if (inf == null) {
            return;
        }

        LivingEntity mob = event.getEntity();
        if (inf.getHandle().containsKey("max_health")) {
            double max = inf.getMaxHealth();
            if (!(mob.getMaxHealth() == max)) {
                mob.setMaxHealth(max);
                if (!(mob.getMaxHealth() == mob.getHealth())) {
                    mob.setHealth(max);
                }
            }
        }
    }
}
