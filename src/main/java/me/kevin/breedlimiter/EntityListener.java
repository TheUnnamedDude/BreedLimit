package me.kevin.breedlimiter;

import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerEggThrowEvent;
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
                for (Material mat : main.mobinfo.get(living.getType())) {
                    if (mat != null && event.getPlayer().getItemInHand().getType() == mat) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(main.errorMessage);
                    }
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
    
    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        if (!event.isHatching()) {
            if (event.getHatchingType() == EntityType.CHICKEN) {
                if (!canSpawn(event.getPlayer(), EntityType.CHICKEN)) {
                    event.setHatching(false);
                    event.getPlayer().sendMessage(main.errorMessage);
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

    public boolean canSpawn(LivingEntity ent, EntityType entitytype) {
        Integer spawnrate = main.mobrestrictions.get(entitytype);
        
        int count = 0;
        
        for (Entity en : ent.getNearbyEntities(main.radius, main.radius, main.radius)){
            if (en.getType() == entitytype) {
                count ++;
            }
        }

        return spawnrate == null || count <= spawnrate;
    }
}