package me.kevin.breedlimiter;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class BreedLimiterPlugin extends JavaPlugin {
    double radius = 16;
    HashMap<EntityType, Integer> mobrestrictions = new HashMap<EntityType, Integer>();
    HashMap<EntityType, Material[]> mobinfo = new HashMap<EntityType, Material[]>();
    EntityType[] breedable = {EntityType.COW, EntityType.SHEEP, EntityType.PIG,
            EntityType.VILLAGER, EntityType.CHICKEN, EntityType.HORSE};
    String errorMessage = "This area is too crowded to breed!";
    @Override
    public void onEnable() {
        mobinfo.put(EntityType.COW, new Material[]{Material.WHEAT});
        mobinfo.put(EntityType.SHEEP, new Material[]{Material.WHEAT});
        mobinfo.put(EntityType.PIG, new Material[]{Material.CARROT_ITEM});
        mobinfo.put(EntityType.CHICKEN, new Material[]{Material.SEEDS});
        mobinfo.put(EntityType.HORSE, new Material[]{Material.GOLDEN_CARROT, Material.GOLDEN_APPLE});
        
        super.onEnable();
        FileConfiguration config = getConfig();

        config.addDefault("radius", radius);
        config.addDefault("errormessage", errorMessage);
        for (EntityType ent : breedable) {
            if (config.contains(ent.getName())) {
                int spawnLimit = config.getInt(ent.getName());
                mobrestrictions.put(ent, spawnLimit >= 0 ? spawnLimit : null);
            } else { 
                config.addDefault(ent.getName(), -1);
            }
        }

        config.options().copyDefaults(true);
        saveConfig();


        radius = config.getDouble("radius");
        errorMessage = config.getString("errormessage");

        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
    }
}
