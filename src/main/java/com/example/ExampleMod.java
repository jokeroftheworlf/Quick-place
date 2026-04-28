package com.example.quickplace;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import org.lwjgl.glfw.GLFW;

public class QuickPlaceMod implements ClientModInitializer {

    private static KeyBinding key;
    private int stage = 0;

    @Override
    public void onInitializeClient() {

        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.quickplace.activate",
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.quickplace"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (key.wasPressed()) {
                stage = 1;
            }

            run(client);
        });
    }

    private void run(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;

        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof BlockHitResult blockHit)) return;

        switch (stage) {

            case 1 -> {
                player.getInventory().selectedSlot = 2; // slot 3
                client.interactionManager.interactBlock(player, Hand.MAIN_HAND, blockHit);
                stage = 2;
            }

            case 2 -> {
                player.getInventory().selectedSlot = 1; // slot 2
                client.interactionManager.interactBlock(player, Hand.MAIN_HAND, blockHit);
                stage = 0;
            }
        }
    }
}
