/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 27, 2014, 1:52:33 AM (GMT)]
 */
package vazkii.botania.client.gui;

import net.minecraft.client.gui.screen.Screen;

// todo 1.13
public class GuiBotaniaConfig extends /*GuiConfig*/ Screen {

	public GuiBotaniaConfig(Screen parentScreen) {
		super(parentScreen.getTitle());
		// super(parentScreen, new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), LibMisc.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}

}
