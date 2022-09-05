package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP {
	public NetClientHandler sendQueue;
	private int field_9380_bx = 0;
	private boolean field_21093_bH = false;
	private double oldPosX;
	private double field_9378_bz;
	private double oldPosY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private boolean field_9382_bF = false;
	private boolean wasSneaking = false;
	private int field_12242_bI = 0;

	public EntityClientPlayerMP(Minecraft minecraft1, World world2, Session session3, NetClientHandler netClientHandler4) {
		super(minecraft1, world2, session3, 0);
		this.sendQueue = netClientHandler4;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		return false;
	}

	public void heal(int i1) {
	}

	public void onUpdate() {
		if(this.worldObj.blockExists(MathHelper.floor_double(this.posX), 64, MathHelper.floor_double(this.posZ))) {
			super.onUpdate();
			this.func_4056_N();
		}
	}

	public void func_4056_N() {
		if(this.field_9380_bx++ == 20) {
			this.sendInventoryChanged();
			this.field_9380_bx = 0;
		}

		boolean z1 = this.isSneaking();
		if(z1 != this.wasSneaking) {
			if(z1) {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
			}

			this.wasSneaking = z1;
		}

		double d2 = this.posX - this.oldPosX;
		double d4 = this.boundingBox.minY - this.field_9378_bz;
		double d6 = this.posY - this.oldPosY;
		double d8 = this.posZ - this.oldPosZ;
		double d10 = (double)(this.rotationYaw - this.oldRotationYaw);
		double d12 = (double)(this.rotationPitch - this.oldRotationPitch);
		boolean z14 = d4 != 0.0D || d6 != 0.0D || d2 != 0.0D || d8 != 0.0D;
		boolean z15 = d10 != 0.0D || d12 != 0.0D;
		if(this.ridingEntity != null) {
			if(z15) {
				this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.motionX, -999.0D, -999.0D, this.motionZ, (purity.no_fall ? true : this.onGround)));
			} else {
				this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, (purity.no_fall ? true : this.onGround)));
			}

			z14 = false;
		} else if(z14 && z15) {
			this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, (purity.no_fall ? true : this.onGround)));
			this.field_12242_bI = 0;
		} else if(z14) {
			this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, (purity.no_fall ? true : this.onGround)));
			this.field_12242_bI = 0;
		} else if(z15) {
			this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, (purity.no_fall ? true : this.onGround)));
			this.field_12242_bI = 0;
		} else {
			this.sendQueue.addToSendQueue(new Packet10Flying((purity.no_fall ? true : this.onGround)));
			if(this.field_9382_bF == this.onGround && this.field_12242_bI <= 200) {
				++this.field_12242_bI;
			} else {
				this.field_12242_bI = 0;
			}
		}

		this.field_9382_bF = this.onGround;
		if(z14) {
			this.oldPosX = this.posX;
			this.field_9378_bz = this.boundingBox.minY;
			this.oldPosY = this.posY;
			this.oldPosZ = this.posZ;
		}

		if(z15) {
			this.oldRotationYaw = this.rotationYaw;
			this.oldRotationPitch = this.rotationPitch;
		}

	}

	public void dropCurrentItem() {
		this.sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
	}

	private void sendInventoryChanged() {
	}

	protected void joinEntityItemWithWorld(EntityItem entityItem1) {
	}

	public void sendChatMessage(String string1) {
		if(string1.startsWith(".")) {
			if(string1.equals(".spawntp")) {
				this.sendQueue.addToSendQueue(new Packet11PlayerPosition(Double.NaN, Double.NaN, Double.NaN, Double.NaN, true));
			} else if(string1.equals(".test")) {
				this.sendQueue.addToSendQueue(new Packet3Chat(string1));
			}
		} else {
			this.sendQueue.addToSendQueue(new Packet3Chat(string1));
		}
	}

	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
	}

	public void respawnPlayer() {
		this.sendInventoryChanged();
		this.sendQueue.addToSendQueue(new Packet9Respawn((byte)this.dimension));
	}

	protected void damageEntity(int i1) {
		this.health -= i1;
	}

	public void closeScreen() {
		this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
		this.inventory.setItemStack((ItemStack)null);
		super.closeScreen();
	}

	public void setHealth(int i1) {
		if(this.field_21093_bH) {
			super.setHealth(i1);
		} else {
			this.health = i1;
			this.field_21093_bH = true;
		}

	}

	public void addStat(StatBase statBase1, int i2) {
		if(statBase1 != null) {
			if(statBase1.field_27088_g) {
				super.addStat(statBase1, i2);
			}

		}
	}

	public void func_27027_b(StatBase statBase1, int i2) {
		if(statBase1 != null) {
			if(!statBase1.field_27088_g) {
				super.addStat(statBase1, i2);
			}

		}
	}
}
