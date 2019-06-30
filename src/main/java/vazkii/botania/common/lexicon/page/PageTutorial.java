/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 16, 2015, 1:39:10 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import java.awt.Desktop;
import java.net.URI;

public class PageTutorial extends PageText {

	// Turn this on once we have an up to date video
	private static final boolean VIDEO_ENABLED = true;

	private Button buttonText, buttonVideo;

	public PageTutorial(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void onOpened(IGuiLexiconEntry gui) {
		buttonText = new Button(gui.getLeft() + 20, gui.getTop() + gui.getHeight() - 40, 50, 20, I18n.format("botaniamisc.tutorialText"), b -> {
			GuiLexicon.startTutorial();
			Minecraft.getInstance().displayGuiScreen(new GuiLexicon());
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("botaniamisc.tutorialStarted").setStyle(new Style().setColor(TextFormatting.GREEN)));
		});
		if(VIDEO_ENABLED)
			buttonVideo = new Button(gui.getLeft() + 75, gui.getTop() + gui.getHeight() - 40, 50, 20, I18n.format("botaniamisc.tutorialVideo"), b -> {
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=D75Aad-5QgQ"));
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			});

		gui.getButtonList().add(buttonText);
		if(VIDEO_ENABLED)
			gui.getButtonList().add(buttonVideo);
	}

	@Override
	public void onClosed(IGuiLexiconEntry gui) {
		gui.getButtonList().remove(buttonText);
		if(VIDEO_ENABLED)
			gui.getButtonList().remove(buttonVideo);
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		super.renderScreen(gui, mx, my);

		if(!VIDEO_ENABLED)
			PageText.renderText(buttonText.x + buttonText.getWidth() + 4, buttonText.y - 14, 65, 100, "botaniamisc.noVideo");
	}
}
