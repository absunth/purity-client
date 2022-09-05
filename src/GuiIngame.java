package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	public String field_933_a = null;
	private int updateCounter = 0;
	private String recordPlaying = "";
	private int recordPlayingUpFor = 0;
	private boolean field_22065_l = false;
	public float damageGuiPartialTime;
	float prevVignetteBrightness = 1.0F;

	public GuiIngame(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public void renderGameOverlay(float f1, boolean z2, int i3, int i4) {
		ScaledResolution scaledResolution5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int i6 = scaledResolution5.getScaledWidth();
		int i7 = scaledResolution5.getScaledHeight();
		FontRenderer fontRenderer8 = this.mc.fontRenderer;
		this.mc.entityRenderer.func_905_b();
		GL11.glEnable(GL11.GL_BLEND);
		if(Minecraft.isFancyGraphicsEnabled()) {
			this.renderVignette(this.mc.thePlayer.getEntityBrightness(f1), i6, i7);
		}

		ItemStack itemStack9 = this.mc.thePlayer.inventory.armorItemInSlot(3);
		if(!this.mc.gameSettings.thirdPersonView && itemStack9 != null && itemStack9.itemID == Block.pumpkin.blockID) {
			this.renderPumpkinBlur(i6, i7);
		}

		float f10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * f1;
		if(f10 > 0.0F) {
			this.renderPortalOverlay(f10, i6, i7);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer inventoryPlayer11 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(i6 / 2 - 91, i7 - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(i6 / 2 - 91 - 1 + inventoryPlayer11.currentItem * 20, i7 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		this.drawTexturedModalRect(i6 / 2 - 7, i7 / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
		boolean z12 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			z12 = false;
		}

		int i13 = this.mc.thePlayer.health;
		int i14 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int i15;
		int i16;
		int i17;
		if(this.mc.playerController.shouldDrawHUD()) {
			i15 = this.mc.thePlayer.getPlayerArmorValue();

			int i18;
			for(i16 = 0; i16 < 10; ++i16) {
				i17 = i7 - 32;
				if(i15 > 0) {
					i18 = i6 / 2 + 91 - i16 * 8 - 9;
					if(i16 * 2 + 1 < i15) {
						this.drawTexturedModalRect(i18, i17, 34, 9, 9, 9);
					}

					if(i16 * 2 + 1 == i15) {
						this.drawTexturedModalRect(i18, i17, 25, 9, 9, 9);
					}

					if(i16 * 2 + 1 > i15) {
						this.drawTexturedModalRect(i18, i17, 16, 9, 9, 9);
					}
				}

				byte b28 = 0;
				if(z12) {
					b28 = 1;
				}

				int i19 = i6 / 2 - 91 + i16 * 8;
				if(i13 <= 4) {
					i17 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(i19, i17, 16 + b28 * 9, 0, 9, 9);
				if(z12) {
					if(i16 * 2 + 1 < i14) {
						this.drawTexturedModalRect(i19, i17, 70, 0, 9, 9);
					}

					if(i16 * 2 + 1 == i14) {
						this.drawTexturedModalRect(i19, i17, 79, 0, 9, 9);
					}
				}

				if(i16 * 2 + 1 < i13) {
					this.drawTexturedModalRect(i19, i17, 52, 0, 9, 9);
				}

				if(i16 * 2 + 1 == i13) {
					this.drawTexturedModalRect(i19, i17, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
				i16 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				i17 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - i16;

				for(i18 = 0; i18 < i16 + i17; ++i18) {
					if(i18 < i16) {
						this.drawTexturedModalRect(i6 / 2 - 91 + i18 * 8, i7 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(i6 / 2 - 91 + i18 * 8, i7 - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPushMatrix();
		GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		for(i15 = 0; i15 < 9; ++i15) {
			i16 = i6 / 2 - 90 + i15 * 20 + 2;
			i17 = i7 - 16 - 3;
			this.renderInventorySlot(i15, i16, i17, f1);
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		if(this.mc.thePlayer.func_22060_M() > 0) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			i15 = this.mc.thePlayer.func_22060_M();
			float f27 = (float)i15 / 100.0F;
			if(f27 > 1.0F) {
				f27 = 1.0F - (float)(i15 - 100) / 10.0F;
			}

			i17 = (int)(220.0F * f27) << 24 | 1052704;
			this.drawRect(0, 0, i6, i7, i17);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		String string23;
		if(this.mc.gameSettings.showDebugInfo) {
			GL11.glPushMatrix();
			fontRenderer8.drawStringWithShadow("purity b1.7.3 (" + this.mc.debug + ")", 2, 2, 0xFFFFFF);
			long j24 = Runtime.getRuntime().maxMemory();
			long j29 = Runtime.getRuntime().totalMemory();
			long j30 = Runtime.getRuntime().freeMemory();
			long j21 = j29 - j30;
			string23 = "mem usage: " + j21 / 1024L / 1024L + "MB/" + j24 / 1024L / 1024L + "MB";
			this.drawString(fontRenderer8, string23, i6 - fontRenderer8.getStringWidth(string23) - 2, 2, 14737632);
			string23 = "allocated: " + j29 * 100L / j24 + "% (" + j29 / 1024L / 1024L + "MB)";
			this.drawString(fontRenderer8, string23, i6 - fontRenderer8.getStringWidth(string23) - 2, 12, 14737632);
			this.drawString(fontRenderer8, "x: " + this.mc.thePlayer.posX, 2, 64, 14737632);
			this.drawString(fontRenderer8, "y: " + this.mc.thePlayer.posY, 2, 72, 14737632);
			this.drawString(fontRenderer8, "z: " + this.mc.thePlayer.posZ, 2, 80, 14737632);
			this.drawString(fontRenderer8, "seed: " + this.mc.theWorld.getRandomSeed(), 2, 112, 14737632);
			GL11.glPopMatrix();
		} else if(this.mc.inGameHasFocus) {
			fontRenderer8.drawStringWithShadow("purity " + purity.version, 2, 2, 0xFFFFFF);
			fontRenderer8.drawStringWithShadow("no knockback [u]", 2, 12, (purity.no_knockback ? 52224 : 0xFFFFFF));
			fontRenderer8.drawStringWithShadow("no fall [n]", 2, 22, (purity.no_fall ? 52224 : 0xFFFFFF));
			fontRenderer8.drawStringWithShadow("speed [x]", 2, 32, (purity.speed ? 52224 : 0xFFFFFF));
			fontRenderer8.drawStringWithShadow("step [p]", 2, 42, (purity.step ? 52224 : 0xFFFFFF));
		}

		if(this.recordPlayingUpFor > 0) {
			float f25 = (float)this.recordPlayingUpFor - f1;
			i16 = (int)(f25 * 256.0F / 20.0F);
			if(i16 > 255) {
				i16 = 255;
			}

			if(i16 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(i6 / 2), (float)(i7 - 48), 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				i17 = 0xFFFFFF;
				if(this.field_22065_l) {
					i17 = Color.HSBtoRGB(f25 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
				}

				fontRenderer8.drawString(this.recordPlaying, -fontRenderer8.getStringWidth(this.recordPlaying) / 2, -4, i17 + (i16 << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}

		byte b26 = 10;
		boolean z31 = false;
		if(this.mc.currentScreen instanceof GuiChat) {
			b26 = 20;
			z31 = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(i7 - 48), 0.0F);

		for(i17 = 0; i17 < this.chatMessageList.size() && i17 < b26; ++i17) {
			if(((ChatLine)this.chatMessageList.get(i17)).updateCounter < 200 || z31) {
				double d32 = (double)((ChatLine)this.chatMessageList.get(i17)).updateCounter / 200.0D;
				d32 = 1.0D - d32;
				d32 *= 10.0D;
				if(d32 < 0.0D) {
					d32 = 0.0D;
				}

				if(d32 > 1.0D) {
					d32 = 1.0D;
				}

				d32 *= d32;
				int i20 = (int)(255.0D * d32);
				if(z31) {
					i20 = 255;
				}

				if(i20 > 0) {
					byte b33 = 2;
					int i22 = -i17 * 9;
					string23 = ((ChatLine)this.chatMessageList.get(i17)).message;
					this.drawRect(b33, i22 - 1, b33 + 320, i22 + 8, i20 / 2 << 24);
					GL11.glEnable(GL11.GL_BLEND);
					fontRenderer8.drawStringWithShadow(string23, b33, i22, 0xFFFFFF + (i20 << 24));
				}
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderPumpkinBlur(int i1, int i2) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
		Tessellator tessellator3 = Tessellator.instance;
		tessellator3.startDrawingQuads();
		tessellator3.addVertexWithUV(0.0D, (double)i2, -90.0D, 0.0D, 1.0D);
		tessellator3.addVertexWithUV((double)i1, (double)i2, -90.0D, 1.0D, 1.0D);
		tessellator3.addVertexWithUV((double)i1, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator3.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderVignette(float f1, int i2, int i3) {
		f1 = 1.0F - f1;
		if(f1 < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(f1 - this.prevVignetteBrightness) * 0.01D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		Tessellator tessellator4 = Tessellator.instance;
		tessellator4.startDrawingQuads();
		tessellator4.addVertexWithUV(0.0D, (double)i3, -90.0D, 0.0D, 1.0D);
		tessellator4.addVertexWithUV((double)i2, (double)i3, -90.0D, 1.0D, 1.0D);
		tessellator4.addVertexWithUV((double)i2, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator4.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void renderPortalOverlay(float f1, int i2, int i3) {
		if(f1 < 1.0F) {
			f1 *= f1;
			f1 *= f1;
			f1 = f1 * 0.8F + 0.2F;
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
		float f4 = (float)(Block.portal.blockIndexInTexture % 16) / 16.0F;
		float f5 = (float)(Block.portal.blockIndexInTexture / 16) / 16.0F;
		float f6 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16.0F;
		float f7 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16.0F;
		Tessellator tessellator8 = Tessellator.instance;
		tessellator8.startDrawingQuads();
		tessellator8.addVertexWithUV(0.0D, (double)i3, -90.0D, (double)f4, (double)f7);
		tessellator8.addVertexWithUV((double)i2, (double)i3, -90.0D, (double)f6, (double)f7);
		tessellator8.addVertexWithUV((double)i2, 0.0D, -90.0D, (double)f6, (double)f5);
		tessellator8.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)f4, (double)f5);
		tessellator8.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderInventorySlot(int i1, int i2, int i3, float f4) {
		ItemStack itemStack5 = this.mc.thePlayer.inventory.mainInventory[i1];
		if(itemStack5 != null) {
			float f6 = (float)itemStack5.animationsToGo - f4;
			if(f6 > 0.0F) {
				GL11.glPushMatrix();
				float f7 = 1.0F + f6 / 5.0F;
				GL11.glTranslatef((float)(i2 + 8), (float)(i3 + 12), 0.0F);
				GL11.glScalef(1.0F / f7, (f7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float)(-(i2 + 8)), (float)(-(i3 + 12)), 0.0F);
			}

			itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack5, i2, i3);
			if(f6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack5, i2, i3);
		}
	}

	public void updateTick() {
		if(this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		for(int i1 = 0; i1 < this.chatMessageList.size(); ++i1) {
			++((ChatLine)this.chatMessageList.get(i1)).updateCounter;
		}

	}

	public void clearChatMessages() {
		this.chatMessageList.clear();
	}

	public void addChatMessage(String string1) {
		while(this.mc.fontRenderer.getStringWidth(string1) > 320) {
			int i2;
			for(i2 = 1; i2 < string1.length() && this.mc.fontRenderer.getStringWidth(string1.substring(0, i2 + 1)) <= 320; ++i2) {
			}

			this.addChatMessage(string1.substring(0, i2));
			string1 = string1.substring(i2);
		}

		this.chatMessageList.add(0, new ChatLine(string1));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}

	public void setRecordPlayingMessage(String string1) {
		this.recordPlaying = "Now playing: " + string1;
		this.recordPlayingUpFor = 60;
		this.field_22065_l = true;
	}

	public void addChatMessageTranslate(String string1) {
		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		String string3 = stringTranslate2.translateKey(string1);
		this.addChatMessage(string3);
	}
}
