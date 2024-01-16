package com.aetherteam.aether.event.listeners.abilities;

import com.aetherteam.aether.event.hooks.AbilityHooks;
import com.aetherteam.aether.item.accessories.abilities.ShieldOfRepulsionAccessory;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.ProjectileImpactEvent;
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class AccessoryAbilityListener {
    /**
     * @see AbilityHooks.AccessoryHooks#damageZaniteRing(LivingEntity, LevelAccessor, BlockState, BlockPos)
     * @see AbilityHooks.AccessoryHooks#damageZanitePendant(LivingEntity, LevelAccessor, BlockState, BlockPos)
     */
    public static void onBlockBreak(BlockEvents.BreakEvent event) {
        Player player = event.getPlayer();
        LevelAccessor level = event.getLevel();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        if (!event.isCanceled()) {
            AbilityHooks.AccessoryHooks.damageZaniteRing(player, level, state, pos);
            AbilityHooks.AccessoryHooks.damageZanitePendant(player, level, state, pos);
        }
    }

    /**
     * @see AbilityHooks.AccessoryHooks#handleZaniteRingAbility(LivingEntity, float)
     * @see AbilityHooks.AccessoryHooks#handleZanitePendantAbility(LivingEntity, float)
     */
    public static void onMiningSpeed(PlayerEvents.BreakSpeed event) {
        Player player = event.getEntity();
        if (!event.isCanceled()) {
            event.setNewSpeed(AbilityHooks.AccessoryHooks.handleZaniteRingAbility(player, event.getNewSpeed()));
            event.setNewSpeed(AbilityHooks.AccessoryHooks.handleZanitePendantAbility(player, event.getNewSpeed()));
        }
    }

    /**
     * Makes the wearer invisible to other mobs' targeting if wearing an Invisibility Cloak.
     * @see com.aetherteam.aether.event.hooks.AbilityHooks.AccessoryHooks#preventTargeting(LivingEntity, Entity)
     * @see com.aetherteam.aether.event.hooks.AbilityHooks.AccessoryHooks#recentlyAttackedWithInvisibility(LivingEntity, Entity)
     */
    public static double onTargetSet(LivingEntity livingEntity, Entity lookingEntity, double originalMultiplier) {
        if (AbilityHooks.AccessoryHooks.preventTargeting(livingEntity, lookingEntity) && !AbilityHooks.AccessoryHooks.recentlyAttackedWithInvisibility(livingEntity, lookingEntity)) {
            return 0.0;
        }
        if (AbilityHooks.AccessoryHooks.recentlyAttackedWithInvisibility(livingEntity, lookingEntity)) {
            return originalMultiplier;
        }
        return originalMultiplier;
    }

    /**
     * @see ShieldOfRepulsionAccessory#deflectProjectile(ProjectileImpactEvent, HitResult, Projectile)
     */
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        HitResult hitResult = event.getRayTraceResult();
        Projectile projectile = event.getProjectile();
        ShieldOfRepulsionAccessory.deflectProjectile(event, hitResult, projectile); // Has to take event due to the event being canceled within a lambda and also mid-behavior.
    }

    /**
     * @see AbilityHooks.AccessoryHooks#preventMagmaDamage(LivingEntity, DamageSource)
     */
    public static boolean onEntityHurt(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        AbilityHooks.AccessoryHooks.setAttack(damageSource);
        if (AbilityHooks.AccessoryHooks.preventMagmaDamage(livingEntity, damageSource)) {
            return true;
        }
        return false;
    }

    public static void init() {
        BlockEvents.BLOCK_BREAK.register(AccessoryAbilityListener::onBlockBreak);
        PlayerEvents.BREAK_SPEED.register(AccessoryAbilityListener::onMiningSpeed);
        LivingEntityEvents.VISIBILITY.register(AccessoryAbilityListener::onTargetSet);
        ProjectileImpactEvent.PROJECTILE_IMPACT.register(AccessoryAbilityListener::onProjectileImpact);
        LivingEntityEvents.ATTACK.register(AccessoryAbilityListener::onEntityHurt);
    }
}
