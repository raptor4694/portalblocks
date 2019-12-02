package com.raptor.portalblocks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(PortalBlocksMod.MODID)
public class PortalSounds {
	
	public static final SoundEvent button_up = null;
	public static final SoundEvent button_down = null;
	public static final SoundEvent button_press = null;
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(new SoundEvent[] {
				createSoundEvent("button_up"),
				createSoundEvent("button_down"),
				createSoundEvent("button_press")
		});
	}
	
	private static SoundEvent createSoundEvent(String name) {
		ResourceLocation id = PortalBlocksMod.getResource(name);
		return new SoundEvent(id).setRegistryName(id);
	}
	
}
