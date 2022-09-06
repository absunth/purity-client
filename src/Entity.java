package net.minecraft.src;

import java.util.List;
import java.util.Random;

public abstract class Entity {
	private static int nextEntityID = 0;
	public int entityId = nextEntityID++;
	public double renderDistanceWeight = 1.0D;
	public boolean preventEntitySpawning = false;
	public Entity riddenByEntity;
	public Entity ridingEntity;
	public World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	public boolean onGround = false;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	public boolean isCollided = false;
	public boolean beenAttacked = false;
	public boolean isInWeb;
	public boolean field_9293_aM = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	protected float fallDistance = 0.0F;
	private int nextStepDistance = 1;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	public float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	public float entityCollisionReduction = 0.0F;
	protected Random rand = new Random();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	public int fire = 0;
	protected int maxAir = 300;
	protected boolean inWater = false;
	public int heartsLife = 0;
	public int air = 300;
	private boolean isFirstUpdate = true;
	public String skinUrl;
	public String cloakUrl;
	protected boolean isImmuneToFire = false;
	protected DataWatcher dataWatcher = new DataWatcher();
	public float entityBrightness = 0.0F;
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk = false;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	public int serverPosX;
	public int serverPosY;
	public int serverPosZ;
	public boolean ignoreFrustumCheck;

	public Entity(World world1) {
		this.worldObj = world1;
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.dataWatcher.addObject(0, (byte)0);
		this.entityInit();
	}

	protected abstract void entityInit();

	public DataWatcher getDataWatcher() {
		return this.dataWatcher;
	}

	public boolean equals(Object object1) {
		return object1 instanceof Entity ? ((Entity)object1).entityId == this.entityId : false;
	}

	public int hashCode() {
		return this.entityId;
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			while(this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}
	}

	public void setEntityDead() {
		this.isDead = true;
	}

	protected void setSize(float f1, float f2) {
		this.width = f1;
		this.height = f2;
	}

	protected void setRotation(float f1, float f2) {
		this.rotationYaw = f1 % 360.0F;
		this.rotationPitch = f2 % 360.0F;
	}

	public void setPosition(double d1, double d3, double d5) {
		this.posX = d1;
		this.posY = d3;
		this.posZ = d5;
		float f7 = this.width / 2.0F;
		float f8 = this.height;
		this.boundingBox.setBounds(d1 - (double)f7, d3 - (double)this.yOffset + (double)this.ySize, d5 - (double)f7, d1 + (double)f7, d3 - (double)this.yOffset + (double)this.ySize + (double)f8, d5 + (double)f7);
	}

	public void func_346_d(float f1, float f2) {
		float f3 = this.rotationPitch;
		float f4 = this.rotationYaw;
		this.rotationYaw = (float)((double)this.rotationYaw + (double)f1 * 0.15D);
		this.rotationPitch = (float)((double)this.rotationPitch - (double)f2 * 0.15D);
		if(this.rotationPitch < -90.0F) {
			this.rotationPitch = -90.0F;
		}

		if(this.rotationPitch > 90.0F) {
			this.rotationPitch = 90.0F;
		}

		this.prevRotationPitch += this.rotationPitch - f3;
		this.prevRotationYaw += this.rotationYaw - f4;
	}

	public void onUpdate() {
		this.onEntityUpdate();
	}

	public void onEntityUpdate() {
		if(this.ridingEntity != null && this.ridingEntity.isDead) {
			this.ridingEntity = null;
		}

		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.isFirstUpdate) {
				float f1 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2F) * 0.2F;
				if(f1 > 1.0F) {
					f1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				float f2 = (float)MathHelper.floor_double(this.boundingBox.minY);

				int i3;
				float f4;
				float f5;
				for(i3 = 0; (float)i3 < 1.0F + this.width * 20.0F; ++i3) {
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)f4, (double)(f2 + 1.0F), this.posZ + (double)f5, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
				}

				for(i3 = 0; (float)i3 < 1.0F + this.width * 20.0F; ++i3) {
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)f4, (double)(f2 + 1.0F), this.posZ + (double)f5, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.worldObj.multiplayerWorld) {
			this.fire = 0;
		} else if(this.fire > 0) {
			if(this.isImmuneToFire) {
				this.fire -= 4;
				if(this.fire < 0) {
					this.fire = 0;
				}
			} else {
				if(this.fire % 20 == 0) {
					this.attackEntityFrom((Entity)null, 1);
				}

				--this.fire;
			}
		}

		if(this.handleLavaMovement()) {
			this.setOnFireFromLava();
		}

		if(this.posY < -64.0D) {
			this.kill();
		}

		if(!this.worldObj.multiplayerWorld) {
			this.setEntityFlag(0, this.fire > 0);
			this.setEntityFlag(2, this.ridingEntity != null);
		}

		this.isFirstUpdate = false;
	}

	protected void setOnFireFromLava() {
		if(!this.isImmuneToFire) {
			this.attackEntityFrom((Entity)null, 4);
			this.fire = 600;
		}

	}

	protected void kill() {
		this.setEntityDead();
	}

	public boolean isOffsetPositionInLiquid(double d1, double d3, double d5) {
		AxisAlignedBB axisAlignedBB7 = this.boundingBox.getOffsetBoundingBox(d1, d3, d5);
		List list8 = this.worldObj.getCollidingBoundingBoxes(this, axisAlignedBB7);
		return list8.size() > 0 ? false : !this.worldObj.getIsAnyLiquid(axisAlignedBB7);
	}

	public void moveEntity(double d1, double d3, double d5) {
		if(this.noClip) {
			this.boundingBox.offset(d1, d3, d5);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			this.ySize *= 0.4F;
			double d7 = this.posX;
			double d9 = this.posZ;
			if(this.isInWeb) {
				this.isInWeb = false;
				d1 *= 0.25D;
				d3 *= (double)0.05F;
				d5 *= 0.25D;
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			double d11 = d1;
			double d13 = d3;
			double d15 = d5;
			AxisAlignedBB axisAlignedBB17 = this.boundingBox.copy();
			boolean z18 = this.onGround && this.isSneaking();
			if(z18) {
				double d19;
				for(d19 = 0.05D; d1 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(d1, -1.0D, 0.0D)).size() == 0; d11 = d1) {
					if(d1 < d19 && d1 >= -d19) {
						d1 = 0.0D;
					} else if(d1 > 0.0D) {
						d1 -= d19;
					} else {
						d1 += d19;
					}
				}

				for(; d5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, d5)).size() == 0; d15 = d5) {
					if(d5 < d19 && d5 >= -d19) {
						d5 = 0.0D;
					} else if(d5 > 0.0D) {
						d5 -= d19;
					} else {
						d5 += d19;
					}
				}
			}

			List list35 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d1, d3, d5));

			for(int i20 = 0; i20 < list35.size(); ++i20) {
				d3 = ((AxisAlignedBB)list35.get(i20)).calculateYOffset(this.boundingBox, d3);
			}

			this.boundingBox.offset(0.0D, d3, 0.0D);
			if(!this.field_9293_aM && d13 != d3) {
				d5 = 0.0D;
				d3 = 0.0D;
				d1 = 0.0D;
			}

			boolean z36 = this.onGround || d13 != d3 && d13 < 0.0D;

			int i21;
			for(i21 = 0; i21 < list35.size(); ++i21) {
				d1 = ((AxisAlignedBB)list35.get(i21)).calculateXOffset(this.boundingBox, d1);
			}

			this.boundingBox.offset(d1, 0.0D, 0.0D);
			if(!this.field_9293_aM && d11 != d1) {
				d5 = 0.0D;
				d3 = 0.0D;
				d1 = 0.0D;
			}

			for(i21 = 0; i21 < list35.size(); ++i21) {
				d5 = ((AxisAlignedBB)list35.get(i21)).calculateZOffset(this.boundingBox, d5);
			}

			this.boundingBox.offset(0.0D, 0.0D, d5);
			if(!this.field_9293_aM && d15 != d5) {
				d5 = 0.0D;
				d3 = 0.0D;
				d1 = 0.0D;
			}

			this.stepHeight = (purity.step ? 1.0F : 0.5F);

			double d23;
			int i28;
			double d37;
			if(this.stepHeight > 0.0F && z36 && (z18 || this.ySize < 0.05F) && (d11 != d1 || d15 != d5)) {
				d37 = d1;
				d23 = d3;
				double d25 = d5;
				d1 = d11;
				d3 = (double)this.stepHeight;
				d5 = d15;
				AxisAlignedBB axisAlignedBB27 = this.boundingBox.copy();
				this.boundingBox.setBB(axisAlignedBB17);
				list35 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d11, d3, d15));

				for(i28 = 0; i28 < list35.size(); ++i28) {
					d3 = ((AxisAlignedBB)list35.get(i28)).calculateYOffset(this.boundingBox, d3);
				}

				this.boundingBox.offset(0.0D, d3, 0.0D);
				if(!this.field_9293_aM && d13 != d3) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				}

				for(i28 = 0; i28 < list35.size(); ++i28) {
					d1 = ((AxisAlignedBB)list35.get(i28)).calculateXOffset(this.boundingBox, d1);
				}

				this.boundingBox.offset(d1, 0.0D, 0.0D);
				if(!this.field_9293_aM && d11 != d1) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				}

				for(i28 = 0; i28 < list35.size(); ++i28) {
					d5 = ((AxisAlignedBB)list35.get(i28)).calculateZOffset(this.boundingBox, d5);
				}

				this.boundingBox.offset(0.0D, 0.0D, d5);
				if(!this.field_9293_aM && d15 != d5) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				}

				if(!this.field_9293_aM && d13 != d3) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				} else {
					d3 = (double)(-this.stepHeight);

					for(i28 = 0; i28 < list35.size(); ++i28) {
						d3 = ((AxisAlignedBB)list35.get(i28)).calculateYOffset(this.boundingBox, d3);
					}

					this.boundingBox.offset(0.0D, d3, 0.0D);
				}

				if(d37 * d37 + d25 * d25 >= d1 * d1 + d5 * d5) {
					d1 = d37;
					d3 = d23;
					d5 = d25;
					this.boundingBox.setBB(axisAlignedBB27);
				} else {
					double d41 = this.boundingBox.minY - (double)((int)this.boundingBox.minY);
					if(d41 > 0.0D) {
						this.ySize = (float)((double)this.ySize + d41 + 0.01D);
					}
				}
			}

			// credits to https://glass-repo.net/repo/mod/mpentityfix
			if(!this.worldObj.multiplayerWorld || this instanceof EntityPlayer || !(this instanceof EntityLiving)) {
				this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
				this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
				this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			}

			this.isCollidedHorizontally = d11 != d1 || d15 != d5;
			this.isCollidedVertically = d13 != d3;
			this.onGround = d13 != d3 && d13 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			this.updateFallState(d3, this.onGround);
			if(d11 != d1) {
				this.motionX = 0.0D;
			}

			if(d13 != d3) {
				this.motionY = 0.0D;
			}

			if(d15 != d5) {
				this.motionZ = 0.0D;
			}

			d37 = this.posX - d7;
			d23 = this.posZ - d9;
			int i26;
			int i38;
			int i39;
			if(this.canTriggerWalking() && !z18 && this.ridingEntity == null) {
				this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d37 * d37 + d23 * d23) * 0.6D);
				i38 = MathHelper.floor_double(this.posX);
				i26 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
				i39 = MathHelper.floor_double(this.posZ);
				i28 = this.worldObj.getBlockId(i38, i26, i39);
				if(this.worldObj.getBlockId(i38, i26 - 1, i39) == Block.fence.blockID) {
					i28 = this.worldObj.getBlockId(i38, i26 - 1, i39);
				}

				if(this.distanceWalkedModified > (float)this.nextStepDistance && i28 > 0) {
					++this.nextStepDistance;
					StepSound stepSound29 = Block.blocksList[i28].stepSound;
					if(this.worldObj.getBlockId(i38, i26 + 1, i39) == Block.snow.blockID) {
						stepSound29 = Block.snow.stepSound;
						this.worldObj.playSoundAtEntity(this, stepSound29.func_1145_d(), stepSound29.getVolume() * 0.15F, stepSound29.getPitch());
					} else if(!Block.blocksList[i28].blockMaterial.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, stepSound29.func_1145_d(), stepSound29.getVolume() * 0.15F, stepSound29.getPitch());
					}

					Block.blocksList[i28].onEntityWalking(this.worldObj, i38, i26, i39, this);
				}
			}

			i38 = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
			i26 = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
			i39 = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
			i28 = MathHelper.floor_double(this.boundingBox.maxX - 0.001D);
			int i40 = MathHelper.floor_double(this.boundingBox.maxY - 0.001D);
			int i30 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001D);
			if(this.worldObj.checkChunksExist(i38, i26, i39, i28, i40, i30)) {
				for(int i31 = i38; i31 <= i28; ++i31) {
					for(int i32 = i26; i32 <= i40; ++i32) {
						for(int i33 = i39; i33 <= i30; ++i33) {
							int i34 = this.worldObj.getBlockId(i31, i32, i33);
							if(i34 > 0) {
								Block.blocksList[i34].onEntityCollidedWithBlock(this.worldObj, i31, i32, i33, this);
							}
						}
					}
				}
			}

			boolean z42 = this.isWet();
			if(this.worldObj.isBoundingBoxBurning(this.boundingBox.func_28195_e(0.001D, 0.001D, 0.001D))) {
				this.dealFireDamage(1);
				if(!z42) {
					++this.fire;
					if(this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(z42 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

	protected boolean canTriggerWalking() {
		return true;
	}

	protected void updateFallState(double d1, boolean z3) {
		if(z3) {
			if(this.fallDistance > 0.0F) {
				this.fall(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		} else if(d1 < 0.0D) {
			this.fallDistance = (float)((double)this.fallDistance - d1);
		}

	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	protected void dealFireDamage(int i1) {
		if(!this.isImmuneToFire) {
			this.attackEntityFrom((Entity)null, i1);
		}

	}

	protected void fall(float f1) {
		if(this.riddenByEntity != null) {
			this.riddenByEntity.fall(f1);
		}

	}

	public boolean isWet() {
		return this.inWater || this.worldObj.canBlockBeRainedOn(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean isInWater() {
		return this.inWater;
	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).func_28195_e(0.001D, 0.001D, 0.001D), Material.water, this);
	}

	public boolean isInsideOfMaterial(Material material1) {
		double d2 = this.posY + (double)this.getEyeHeight();
		int i4 = MathHelper.floor_double(this.posX);
		int i5 = MathHelper.floor_float((float)MathHelper.floor_double(d2));
		int i6 = MathHelper.floor_double(this.posZ);
		int i7 = this.worldObj.getBlockId(i4, i5, i6);
		if(i7 != 0 && Block.blocksList[i7].blockMaterial == material1) {
			float f8 = BlockFluid.getPercentAir(this.worldObj.getBlockMetadata(i4, i5, i6)) - 0.11111111F;
			float f9 = (float)(i5 + 1) - f8;
			return d2 < (double)f9;
		} else {
			return false;
		}
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	public boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
	}

	public void moveFlying(float f1, float f2, float f3) {
		float f4 = MathHelper.sqrt_float(f1 * f1 + f2 * f2);
		if(f4 >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}
			f4 = f3 / f4;
			f1 *= f4;
			f2 *= f4;
			float f5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float f6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			if(purity.speed && this.onGround) {
				this.motionX += (double)((f1 * f6 - f2 * f5) * 1.5F);
				this.motionZ += (double)((f2 * f6 + f1 * f5) * 1.5F);
			} else {
				this.motionX += (double)(f1 * f6 - f2 * f5);
				this.motionZ += (double)(f2 * f6 + f1 * f5);
			}
		}
	}

	public float getEntityBrightness(float f1) {
		int i2 = MathHelper.floor_double(this.posX);
		double d3 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
		int i5 = MathHelper.floor_double(this.posY - (double)this.yOffset + d3);
		int i6 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.checkChunksExist(MathHelper.floor_double(this.boundingBox.minX), MathHelper.floor_double(this.boundingBox.minY), MathHelper.floor_double(this.boundingBox.minZ), MathHelper.floor_double(this.boundingBox.maxX), MathHelper.floor_double(this.boundingBox.maxY), MathHelper.floor_double(this.boundingBox.maxZ))) {
			float f7 = this.worldObj.getLightBrightness(i2, i5, i6);
			if(f7 < this.entityBrightness) {
				f7 = this.entityBrightness;
			}

			return f7;
		} else {
			return this.entityBrightness;
		}
	}

	public void setWorld(World world1) {
		this.worldObj = world1;
	}

	public void setPositionAndRotation(double d1, double d3, double d5, float f7, float f8) {
		this.prevPosX = this.posX = d1;
		this.prevPosY = this.posY = d3;
		this.prevPosZ = this.posZ = d5;
		this.prevRotationYaw = this.rotationYaw = f7;
		this.prevRotationPitch = this.rotationPitch = f8;
		this.ySize = 0.0F;
		double d9 = (double)(this.prevRotationYaw - f7);
		if(d9 < -180.0D) {
			this.prevRotationYaw += 360.0F;
		}

		if(d9 >= 180.0D) {
			this.prevRotationYaw -= 360.0F;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(f7, f8);
	}

	public void setLocationAndAngles(double d1, double d3, double d5, float f7, float f8) {
		this.lastTickPosX = this.prevPosX = this.posX = d1;
		this.lastTickPosY = this.prevPosY = this.posY = d3 + (double)this.yOffset;
		this.lastTickPosZ = this.prevPosZ = this.posZ = d5;
		this.rotationYaw = f7;
		this.rotationPitch = f8;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public float getDistanceToEntity(Entity entity1) {
		float f2 = (float)(this.posX - entity1.posX);
		float f3 = (float)(this.posY - entity1.posY);
		float f4 = (float)(this.posZ - entity1.posZ);
		return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public double getDistanceSq(double d1, double d3, double d5) {
		double d7 = this.posX - d1;
		double d9 = this.posY - d3;
		double d11 = this.posZ - d5;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}

	public double getDistance(double d1, double d3, double d5) {
		double d7 = this.posX - d1;
		double d9 = this.posY - d3;
		double d11 = this.posZ - d5;
		return (double)MathHelper.sqrt_double(d7 * d7 + d9 * d9 + d11 * d11);
	}

	public double getDistanceSqToEntity(Entity entity1) {
		double d2 = this.posX - entity1.posX;
		double d4 = this.posY - entity1.posY;
		double d6 = this.posZ - entity1.posZ;
		return d2 * d2 + d4 * d4 + d6 * d6;
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
	}

	public void applyEntityCollision(Entity entity1) {
		if(entity1.riddenByEntity != this && entity1.ridingEntity != this && !purity.no_knockback) {
			double d2 = entity1.posX - this.posX;
			double d4 = entity1.posZ - this.posZ;
			double d6 = MathHelper.abs_max(d2, d4);
			if(d6 >= (double)0.01F) {
				d6 = (double)MathHelper.sqrt_double(d6);
				d2 /= d6;
				d4 /= d6;
				double d8 = 1.0D / d6;
				if(d8 > 1.0D) {
					d8 = 1.0D;
				}

				d2 *= d8;
				d4 *= d8;
				d2 *= (double)0.05F;
				d4 *= (double)0.05F;
				d2 *= (double)(1.0F - this.entityCollisionReduction);
				d4 *= (double)(1.0F - this.entityCollisionReduction);
				this.addVelocity(-d2, 0.0D, -d4);
				entity1.addVelocity(d2, 0.0D, d4);
			}

		}
	}

	public void addVelocity(double d1, double d3, double d5) {
		this.motionX += d1;
		this.motionY += d3;
		this.motionZ += d5;
	}

	protected void setBeenAttacked() {
		this.beenAttacked = true;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.setBeenAttacked();
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public void addToPlayerScore(Entity entity1, int i2) {
	}

	public boolean isInRangeToRenderVec3D(Vec3D vec3D1) {
		double d2 = this.posX - vec3D1.xCoord;
		double d4 = this.posY - vec3D1.yCoord;
		double d6 = this.posZ - vec3D1.zCoord;
		double d8 = d2 * d2 + d4 * d4 + d6 * d6;
		return this.isInRangeToRenderDist(d8);
	}

	public boolean isInRangeToRenderDist(double d1) {
		double d3 = this.boundingBox.getAverageEdgeLength();
		d3 *= 64.0D * this.renderDistanceWeight;
		return d1 < d3 * d3;
	}

	public String getEntityTexture() {
		return null;
	}

	public boolean addEntityID(NBTTagCompound nBTTagCompound1) {
		String string2 = this.getEntityString();
		if(!this.isDead && string2 != null) {
			nBTTagCompound1.setString("id", string2);
			this.writeToNBT(nBTTagCompound1);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setTag("Pos", this.newDoubleNBTList(new double[]{this.posX, this.posY + (double)this.ySize, this.posZ}));
		nBTTagCompound1.setTag("Motion", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
		nBTTagCompound1.setTag("Rotation", this.newFloatNBTList(new float[]{this.rotationYaw, this.rotationPitch}));
		nBTTagCompound1.setFloat("FallDistance", this.fallDistance);
		nBTTagCompound1.setShort("Fire", (short)this.fire);
		nBTTagCompound1.setShort("Air", (short)this.air);
		nBTTagCompound1.setBoolean("OnGround", this.onGround);
		this.writeEntityToNBT(nBTTagCompound1);
	}

	public void readFromNBT(NBTTagCompound nBTTagCompound1) {
		NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Pos");
		NBTTagList nBTTagList3 = nBTTagCompound1.getTagList("Motion");
		NBTTagList nBTTagList4 = nBTTagCompound1.getTagList("Rotation");
		this.motionX = ((NBTTagDouble)nBTTagList3.tagAt(0)).doubleValue;
		this.motionY = ((NBTTagDouble)nBTTagList3.tagAt(1)).doubleValue;
		this.motionZ = ((NBTTagDouble)nBTTagList3.tagAt(2)).doubleValue;
		if(Math.abs(this.motionX) > 10.0D) {
			this.motionX = 0.0D;
		}

		if(Math.abs(this.motionY) > 10.0D) {
			this.motionY = 0.0D;
		}

		if(Math.abs(this.motionZ) > 10.0D) {
			this.motionZ = 0.0D;
		}

		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)nBTTagList2.tagAt(0)).doubleValue;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)nBTTagList2.tagAt(1)).doubleValue;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)nBTTagList2.tagAt(2)).doubleValue;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)nBTTagList4.tagAt(0)).floatValue;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)nBTTagList4.tagAt(1)).floatValue;
		this.fallDistance = nBTTagCompound1.getFloat("FallDistance");
		this.fire = nBTTagCompound1.getShort("Fire");
		this.air = nBTTagCompound1.getShort("Air");
		this.onGround = nBTTagCompound1.getBoolean("OnGround");
		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(this.rotationYaw, this.rotationPitch);
		this.readEntityFromNBT(nBTTagCompound1);
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound nBTTagCompound1);

	protected abstract void writeEntityToNBT(NBTTagCompound nBTTagCompound1);

	protected NBTTagList newDoubleNBTList(double... d1) {
		NBTTagList nBTTagList2 = new NBTTagList();
		double[] d3 = d1;
		int i4 = d1.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			double d6 = d3[i5];
			nBTTagList2.setTag(new NBTTagDouble(d6));
		}

		return nBTTagList2;
	}

	protected NBTTagList newFloatNBTList(float... f1) {
		NBTTagList nBTTagList2 = new NBTTagList();
		float[] f3 = f1;
		int i4 = f1.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			float f6 = f3[i5];
			nBTTagList2.setTag(new NBTTagFloat(f6));
		}

		return nBTTagList2;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public EntityItem dropItem(int i1, int i2) {
		return this.dropItemWithOffset(i1, i2, 0.0F);
	}

	public EntityItem dropItemWithOffset(int i1, int i2, float f3) {
		return this.entityDropItem(new ItemStack(i1, i2, 0), f3);
	}

	public EntityItem entityDropItem(ItemStack itemStack1, float f2) {
		EntityItem entityItem3 = new EntityItem(this.worldObj, this.posX, this.posY + (double)f2, this.posZ, itemStack1);
		entityItem3.delayBeforeCanPickup = 10;
		this.worldObj.entityJoinedWorld(entityItem3);
		return entityItem3;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}

	public boolean isEntityInsideOpaqueBlock() {
		for(int i1 = 0; i1 < 8; ++i1) {
			float f2 = ((float)((i1 >> 0) % 2) - 0.5F) * this.width * 0.9F;
			float f3 = ((float)((i1 >> 1) % 2) - 0.5F) * 0.1F;
			float f4 = ((float)((i1 >> 2) % 2) - 0.5F) * this.width * 0.9F;
			int i5 = MathHelper.floor_double(this.posX + (double)f2);
			int i6 = MathHelper.floor_double(this.posY + (double)this.getEyeHeight() + (double)f3);
			int i7 = MathHelper.floor_double(this.posZ + (double)f4);
			if(this.worldObj.isBlockNormalCube(i5, i6, i7)) {
				return true;
			}
		}

		return false;
	}

	public boolean interact(EntityPlayer entityPlayer1) {
		return false;
	}

	public AxisAlignedBB getCollisionBox(Entity entity1) {
		return null;
	}

	public void updateRidden() {
		if(this.ridingEntity.isDead) {
			this.ridingEntity = null;
		} else {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.onUpdate();
			if(this.ridingEntity != null) {
				this.ridingEntity.updateRiderPosition();
				this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

				for(this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
				}

				while(this.entityRiderYawDelta < -180.0D) {
					this.entityRiderYawDelta += 360.0D;
				}

				while(this.entityRiderPitchDelta >= 180.0D) {
					this.entityRiderPitchDelta -= 360.0D;
				}

				while(this.entityRiderPitchDelta < -180.0D) {
					this.entityRiderPitchDelta += 360.0D;
				}

				double d1 = this.entityRiderYawDelta * 0.5D;
				double d3 = this.entityRiderPitchDelta * 0.5D;
				float f5 = 10.0F;
				if(d1 > (double)f5) {
					d1 = (double)f5;
				}

				if(d1 < (double)(-f5)) {
					d1 = (double)(-f5);
				}

				if(d3 > (double)f5) {
					d3 = (double)f5;
				}

				if(d3 < (double)(-f5)) {
					d3 = (double)(-f5);
				}

				this.entityRiderYawDelta -= d1;
				this.entityRiderPitchDelta -= d3;
				this.rotationYaw = (float)((double)this.rotationYaw + d1);
				this.rotationPitch = (float)((double)this.rotationPitch + d3);
			}
		}
	}

	public void updateRiderPosition() {
		this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
	}

	public double getYOffset() {
		return (double)this.yOffset;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D;
	}

	public void mountEntity(Entity entity1) {
		this.entityRiderPitchDelta = 0.0D;
		this.entityRiderYawDelta = 0.0D;
		if(entity1 == null) {
			if(this.ridingEntity != null) {
				this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.boundingBox.minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
				this.ridingEntity.riddenByEntity = null;
			}

			this.ridingEntity = null;
		} else if(this.ridingEntity == entity1) {
			this.ridingEntity.riddenByEntity = null;
			this.ridingEntity = null;
			this.setLocationAndAngles(entity1.posX, entity1.boundingBox.minY + (double)entity1.height, entity1.posZ, this.rotationYaw, this.rotationPitch);
		} else {
			if(this.ridingEntity != null) {
				this.ridingEntity.riddenByEntity = null;
			}

			if(entity1.riddenByEntity != null) {
				entity1.riddenByEntity.ridingEntity = null;
			}

			this.ridingEntity = entity1;
			entity1.riddenByEntity = this;
		}
	}

	public void setPositionAndRotation2(double d1, double d3, double d5, float f7, float f8, int i9) {
		this.setPosition(d1, d3, d5);
		this.setRotation(f7, f8);
		List list10 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.func_28195_e(8.0D / 256D, 0.0D, 8.0D / 256D));
		if(list10.size() > 0) {
			double d11 = 0.0D;

			for(int i13 = 0; i13 < list10.size(); ++i13) {
				AxisAlignedBB axisAlignedBB14 = (AxisAlignedBB)list10.get(i13);
				if(axisAlignedBB14.maxY > d11) {
					d11 = axisAlignedBB14.maxY;
				}
			}

			d3 += d11 - this.boundingBox.minY;
			this.setPosition(d1, d3, d5);
		}

	}

	public float getCollisionBorderSize() {
		return 0.1F;
	}

	public Vec3D getLookVec() {
		return null;
	}

	public void setInPortal() {
	}

	public void setVelocity(double d1, double d3, double d5) {
		this.motionX = d1;
		this.motionY = d3;
		this.motionZ = d5;
	}

	public void handleHealthUpdate(byte b1) {
	}

	public void performHurtAnimation() {
	}

	public void updateCloak() {
	}

	public void outfitWithItem(int i1, int i2, int i3) {
	}

	public boolean isBurning() {
		return this.fire > 0 || this.getEntityFlag(0);
	}

	public boolean isRiding() {
		return this.ridingEntity != null || this.getEntityFlag(2);
	}

	public boolean isSneaking() {
		return this.getEntityFlag(1);
	}

	protected boolean getEntityFlag(int i1) {
		return (this.dataWatcher.getWatchableObjectByte(0) & 1 << i1) != 0;
	}

	protected void setEntityFlag(int i1, boolean z2) {
		byte b3 = this.dataWatcher.getWatchableObjectByte(0);
		if(z2) {
			this.dataWatcher.updateObject(0, (byte)(b3 | 1 << i1));
		} else {
			this.dataWatcher.updateObject(0, (byte)(b3 & ~(1 << i1)));
		}

	}

	public void onStruckByLightning(EntityLightningBolt entityLightningBolt1) {
		this.dealFireDamage(5);
		++this.fire;
		if(this.fire == 0) {
			this.fire = 300;
		}

	}

	public void onKillEntity(EntityLiving entityLiving1) {
	}

	protected boolean pushOutOfBlocks(double d1, double d3, double d5) {
		int i7 = MathHelper.floor_double(d1);
		int i8 = MathHelper.floor_double(d3);
		int i9 = MathHelper.floor_double(d5);
		double d10 = d1 - (double)i7;
		double d12 = d3 - (double)i8;
		double d14 = d5 - (double)i9;

		if(this.worldObj.isBlockNormalCube(i7, i8, i9) && !purity.no_knockback) {
			boolean z16 = !this.worldObj.isBlockNormalCube(i7 - 1, i8, i9);
			boolean z17 = !this.worldObj.isBlockNormalCube(i7 + 1, i8, i9);
			boolean z18 = !this.worldObj.isBlockNormalCube(i7, i8 - 1, i9);
			boolean z19 = !this.worldObj.isBlockNormalCube(i7, i8 + 1, i9);
			boolean z20 = !this.worldObj.isBlockNormalCube(i7, i8, i9 - 1);
			boolean z21 = !this.worldObj.isBlockNormalCube(i7, i8, i9 + 1);
			byte b22 = -1;
			double d23 = 9999.0D;
			if(z16 && d10 < d23) {
				d23 = d10;
				b22 = 0;
			}

			if(z17 && 1.0D - d10 < d23) {
				d23 = 1.0D - d10;
				b22 = 1;
			}

			if(z18 && d12 < d23) {
				d23 = d12;
				b22 = 2;
			}

			if(z19 && 1.0D - d12 < d23) {
				d23 = 1.0D - d12;
				b22 = 3;
			}

			if(z20 && d14 < d23) {
				d23 = d14;
				b22 = 4;
			}

			if(z21 && 1.0D - d14 < d23) {
				d23 = 1.0D - d14;
				b22 = 5;
			}

			float f25 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(b22 == 0) {
				this.motionX = (double)(-f25);
			}

			if(b22 == 1) {
				this.motionX = (double)f25;
			}

			if(b22 == 2) {
				this.motionY = (double)(-f25);
			}

			if(b22 == 3) {
				this.motionY = (double)f25;
			}

			if(b22 == 4) {
				this.motionZ = (double)(-f25);
			}

			if(b22 == 5) {
				this.motionZ = (double)f25;
			}
		}

		return false;
	}
}
