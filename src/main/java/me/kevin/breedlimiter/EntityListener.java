package me.kevin.breedlimiter;

import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityListener implements Listener {
    BreedLimiterPlugin main;

    public EntityListener(BreedLimiterPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Ageable
                && event.getRightClicked() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)event.getRightClicked();
            if (!canSpawn(living)) {
                Material mat = main.mobinfo.get(living.getType());
                if (mat != null && event.getPlayer().getItemInHand().getType() == mat) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(main.errorMessage);
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.isCancelled()) {
            if (event.getEntity() instanceof Villager) {
                if (!canSpawn(event.getEntity())) {
                    if(event.getSpawnReason() == SpawnReason.BREEDING) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public boolean canSpawn(LivingEntity ent) {
        Integer spawnrate = main.mobrestrictions.get(ent.getType());
        
        int count = 0;
        
        for (Entity en : ent.getNearbyEntities(main.radius, main.radius, main.radius)){
            if (en.getType() == ent.getType()) {
                count ++;
            }
        }

        return spawnrate == null || count <= spawnrate;
    }
}