package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	protected Minecraft mc;
	private MouseFilter field_21903_bJ = new MouseFilter();
	private MouseFilter field_21904_bK = new MouseFilter();
	private MouseFilter field_21902_bL = new MouseFilter();

	public EntityPlayerSP(Minecraft minecraft1, World world2, Session session3, int i4) {
		super(world2);
		this.mc = minecraft1;
		this.dimension = i4;
		if(session3 != null && session3.username != null && session3.username.length() > 0) {
			this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + session3.username + ".png";
		}

		this.username = session3.username;
	}

	public void moveEntity(double d1, double d3, double d5) {
		super.moveEntity(d1, d3, d5);
	}

	public void updatePlayerActionState() {
		super.updatePlayerActionState();
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
	}

	public void onLivingUpdate() {
		if(!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
			this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
		}

		this.prevTimeInPortal = this.timeInPortal;
		if(this.inPortal) {
			if(!this.worldObj.multiplayerWorld && this.ridingEntity != null) {
				this.mountEntity((Entity)null);
			}

			if(this.mc.currentScreen != null) {
				this.mc.displayGuiScreen((GuiScreen)null);
			}

			if(this.timeInPortal == 0.0F) {
				this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
			}

			this.timeInPortal += 0.0125F;
			if(this.timeInPortal >= 1.0F) {
				this.timeInPortal = 1.0F;
				if(!this.worldObj.multiplayerWorld) {
					this.timeUntilPortal = 10;
					this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
					this.mc.usePortal();
				}
			}

			this.inPortal = false;
		} else {
			if(this.timeInPortal > 0.0F) {
				this.timeInPortal -= 0.05F;
			}

			if(this.timeInPortal < 0.0F) {
				this.timeInPortal = 0.0F;
			}
		}

		if(this.timeUntilPortal > 0) {
			--this.timeUntilPortal;
		}

		this.movementInput.updatePlayerMoveState(this);
		if(this.movementInput.sneak && this.ySize < 0.2F) {
			this.ySize = 0.2F;
		}

		this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
		this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
		super.onLivingUpdate();
	}

	public void resetPlayerKeyState() {
		this.movementInput.resetKeyState();
	}

	public void handleKeyPress(int i1, boolean z2) {
		this.movementInput.checkKeyForMovementInput(i1, z2);
		
		if(!z2) {
			if(i1 == Keyboard.KEY_N) {
				purity.no_fall = !purity.no_fall;
			} else if(i1 == Keyboard.KEY_X){
				purity.speed = !purity.speed;
			} else if(i1 == Keyboard.KEY_U) {
				purity.no_knockback = !purity.no_knockback;
			} else if(i1 == Keyboard.KEY_P) {
				purity.step = !purity.step;
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.score = nBTTagCompound1.getInteger("Score");
	}

	public void closeScreen() {
		super.closeScreen();
		this.mc.displayGuiScreen((GuiScreen)null);
	}

	public void displayGUIEditSign(TileEntitySign tileEntitySign1) {
		this.mc.displayGuiScreen(new GuiEditSign(tileEntitySign1));
	}

	public void displayGUIChest(IInventory iInventory1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, iInventory1));
	}

	public void displayWorkbenchGUI(int i1, int i2, int i3) {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, i1, i2, i3));
	}

	public void displayGUIFurnace(TileEntityFurnace tileEntityFurnace1) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, tileEntityFurnace1));
	}

	public void displayGUIDispenser(TileEntityDispenser tileEntityDispenser1) {
		this.mc.displayGuiScreen(new GuiDispenser(this.inventory, tileEntityDispenser1));
	}

	public void onItemPickup(Entity entity1, int i2) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity1, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void sendChatMessage(String string1) {
	}

	public boolean isSneaking() {
		return this.movementInput.sneak && !this.sleeping;
	}

	public void setHealth(int i1) {
		int i2 = this.health - i1;
		if(i2 <= 0) {
			this.health = i1;
			if(i2 < 0) {
				this.heartsLife = this.heartsHalvesLife / 2;
			}
		} else {
			this.field_9346_af = i2;
			this.prevHealth = this.health;
			this.heartsLife = this.heartsHalvesLife;
			this.damageEntity(i2);
			this.hurtTime = this.maxHurtTime = 10;
		}

	}

	public void respawnPlayer() {
		this.mc.respawn(false, 0);
	}

	public void func_6420_o() {
	}

	public void addChatMessage(String string1) {
		this.mc.ingameGUI.addChatMessageTranslate(string1);
	}

	public void addStat(StatBase statBase1, int i2) {
		if(statBase1 != null) {
			if(statBase1.func_25067_a()) {
				Achievement achievement3 = (Achievement)statBase1;
				if(achievement3.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(achievement3.parentAchievement)) {
					if(!this.mc.statFileWriter.hasAchievementUnlocked(achievement3)) {
						this.mc.guiAchievement.queueTakenAchievement(achievement3);
					}

					this.mc.statFileWriter.readStat(statBase1, i2);
				}
			} else {
				this.mc.statFileWriter.readStat(statBase1, i2);
			}

		}
	}

	private boolean isBlockTranslucent(int i1, int i2, int i3) {
		return this.worldObj.isBlockNormalCube(i1, i2, i3);
	}

	protected boolean pushOutOfBlocks(double d1, double d3, double d5) {
		int i7 = MathHelper.floor_double(d1);
		int i8 = MathHelper.floor_double(d3);
		int i9 = MathHelper.floor_double(d5);
		double d10 = d1 - (double)i7;
		double d12 = d5 - (double)i9;
		if(this.isBlockTranslucent(i7, i8, i9) || this.isBlockTranslucent(i7, i8 + 1, i9)) {
			boolean z14 = !this.isBlockTranslucent(i7 - 1, i8, i9) && !this.isBlockTranslucent(i7 - 1, i8 + 1, i9);
			boolean z15 = !this.isBlockTranslucent(i7 + 1, i8, i9) && !this.isBlockTranslucent(i7 + 1, i8 + 1, i9);
			boolean z16 = !this.isBlockTranslucent(i7, i8, i9 - 1) && !this.isBlockTranslucent(i7, i8 + 1, i9 - 1);
			boolean z17 = !this.isBlockTranslucent(i7, i8, i9 + 1) && !this.isBlockTranslucent(i7, i8 + 1, i9 + 1);
			byte b18 = -1;
			double d19 = 9999.0D;
			if(z14 && d10 < d19) {
				d19 = d10;
				b18 = 0;
			}

			if(z15 && 1.0D - d10 < d19) {
				d19 = 1.0D - d10;
				b18 = 1;
			}

			if(z16 && d12 < d19) {
				d19 = d12;
				b18 = 4;
			}

			if(z17 && 1.0D - d12 < d19) {
				d19 = 1.0D - d12;
				b18 = 5;
			}

			float f21 = 0.1F;
			if(b18 == 0) {
				this.motionX = (double)(-f21);
			}

			if(b18 == 1) {
				this.motionX = (double)f21;
			}

			if(b18 == 4) {
				this.motionZ = (double)(-f21);
			}

			if(b18 == 5) {
				this.motionZ = (double)f21;
			}
		}

		return false;
	}
}
