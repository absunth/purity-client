package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

public class NetClientHandler extends NetHandler {
	private boolean disconnected = false;
	private NetworkManager netManager;
	public String field_1209_a;
	private Minecraft mc;
	private WorldClient worldClient;
	private boolean field_1210_g = false;
	public MapStorage field_28118_b = new MapStorage((ISaveHandler)null);
	Random rand = new Random();

	public NetClientHandler(Minecraft minecraft1, String string2, int i3) throws UnknownHostException, IOException {
		this.mc = minecraft1;
		Socket socket4 = new Socket(InetAddress.getByName(string2), i3);
		this.netManager = new NetworkManager(socket4, "Client", this);
	}

	public void processReadPackets() {
		if(!this.disconnected) {
			this.netManager.processReadPackets();
		}

		this.netManager.wakeThreads();
	}

	public void handleLogin(Packet1Login packet1Login1) {
		this.mc.playerController = new PlayerControllerMP(this.mc, this);
		this.mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
		this.worldClient = new WorldClient(this, packet1Login1.mapSeed, packet1Login1.dimension);
		this.worldClient.multiplayerWorld = true;
		this.mc.changeWorld1(this.worldClient);
		this.mc.thePlayer.dimension = packet1Login1.dimension;
		this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
		this.mc.thePlayer.entityId = packet1Login1.protocolVersion;
	}

	public void handlePickupSpawn(Packet21PickupSpawn packet21PickupSpawn1) {
		double d2 = (double)packet21PickupSpawn1.xPosition / 32.0D;
		double d4 = (double)packet21PickupSpawn1.yPosition / 32.0D;
		double d6 = (double)packet21PickupSpawn1.zPosition / 32.0D;
		EntityItem entityItem8 = new EntityItem(this.worldClient, d2, d4, d6, new ItemStack(packet21PickupSpawn1.itemID, packet21PickupSpawn1.count, packet21PickupSpawn1.itemDamage));
		entityItem8.motionX = (double)packet21PickupSpawn1.rotation / 128.0D;
		entityItem8.motionY = (double)packet21PickupSpawn1.pitch / 128.0D;
		entityItem8.motionZ = (double)packet21PickupSpawn1.roll / 128.0D;
		entityItem8.serverPosX = packet21PickupSpawn1.xPosition;
		entityItem8.serverPosY = packet21PickupSpawn1.yPosition;
		entityItem8.serverPosZ = packet21PickupSpawn1.zPosition;
		this.worldClient.func_712_a(packet21PickupSpawn1.entityId, entityItem8);
	}

	public void handleVehicleSpawn(Packet23VehicleSpawn packet23VehicleSpawn1) {
		double d2 = (double)packet23VehicleSpawn1.xPosition / 32.0D;
		double d4 = (double)packet23VehicleSpawn1.yPosition / 32.0D;
		double d6 = (double)packet23VehicleSpawn1.zPosition / 32.0D;
		Object object8 = null;
		if(packet23VehicleSpawn1.type == 10) {
			object8 = new EntityMinecart(this.worldClient, d2, d4, d6, 0);
		}

		if(packet23VehicleSpawn1.type == 11) {
			object8 = new EntityMinecart(this.worldClient, d2, d4, d6, 1);
		}

		if(packet23VehicleSpawn1.type == 12) {
			object8 = new EntityMinecart(this.worldClient, d2, d4, d6, 2);
		}

		if(packet23VehicleSpawn1.type == 90) {
			object8 = new EntityFish(this.worldClient, d2, d4, d6);
		}

		if(packet23VehicleSpawn1.type == 60) {
			object8 = new EntityArrow(this.worldClient, d2, d4, d6);
		}

		if(packet23VehicleSpawn1.type == 61) {
			object8 = new EntitySnowball(this.worldClient, d2, d4, d6);
		}

		if(packet23VehicleSpawn1.type == 63) {
			object8 = new EntityFireball(this.worldClient, d2, d4, d6, (double)packet23VehicleSpawn1.field_28047_e / 8000.0D, (double)packet23VehicleSpawn1.field_28046_f / 8000.0D, (double)packet23VehicleSpawn1.field_28045_g / 8000.0D);
			packet23VehicleSpawn1.field_28044_i = 0;
		}

		if(packet23VehicleSpawn1.type == 62) {
			object8 = new EntityEgg(this.worldClient, d2, d4, d6);
		}

		if(packet23VehicleSpawn1.type == 1) {
			object8 = new EntityBoat(this.worldClient, d2, d4, d6);
		}

		if(packet23VehicleSpawn1.type == 50) {
			object8 = new EntityTNTPrimed(this.worldClient, d2, d4, d6);
		}

		if(packet23VehicleSpawn1.type == 70) {
			object8 = new EntityFallingSand(this.worldClient, d2, d4, d6, Block.sand.blockID);
		}

		if(packet23VehicleSpawn1.type == 71) {
			object8 = new EntityFallingSand(this.worldClient, d2, d4, d6, Block.gravel.blockID);
		}

		if(object8 != null) {
			((Entity)object8).serverPosX = packet23VehicleSpawn1.xPosition;
			((Entity)object8).serverPosY = packet23VehicleSpawn1.yPosition;
			((Entity)object8).serverPosZ = packet23VehicleSpawn1.zPosition;
			((Entity)object8).rotationYaw = 0.0F;
			((Entity)object8).rotationPitch = 0.0F;
			((Entity)object8).entityId = packet23VehicleSpawn1.entityId;
			this.worldClient.func_712_a(packet23VehicleSpawn1.entityId, (Entity)object8);
			if(packet23VehicleSpawn1.field_28044_i > 0) {
				if(packet23VehicleSpawn1.type == 60) {
					Entity entity9 = this.getEntityByID(packet23VehicleSpawn1.field_28044_i);
					if(entity9 instanceof EntityLiving) {
						((EntityArrow)object8).owner = (EntityLiving)entity9;
					}
				}

				((Entity)object8).setVelocity((double)packet23VehicleSpawn1.field_28047_e / 8000.0D, (double)packet23VehicleSpawn1.field_28046_f / 8000.0D, (double)packet23VehicleSpawn1.field_28045_g / 8000.0D);
			}
		}

	}

	public void handleWeather(Packet71Weather packet71Weather1) {
		double d2 = (double)packet71Weather1.field_27053_b / 32.0D;
		double d4 = (double)packet71Weather1.field_27057_c / 32.0D;
		double d6 = (double)packet71Weather1.field_27056_d / 32.0D;
		EntityLightningBolt entityLightningBolt8 = null;
		if(packet71Weather1.field_27055_e == 1) {
			entityLightningBolt8 = new EntityLightningBolt(this.worldClient, d2, d4, d6);
		}

		if(entityLightningBolt8 != null) {
			entityLightningBolt8.serverPosX = packet71Weather1.field_27053_b;
			entityLightningBolt8.serverPosY = packet71Weather1.field_27057_c;
			entityLightningBolt8.serverPosZ = packet71Weather1.field_27056_d;
			entityLightningBolt8.rotationYaw = 0.0F;
			entityLightningBolt8.rotationPitch = 0.0F;
			entityLightningBolt8.entityId = packet71Weather1.field_27054_a;
			this.worldClient.addWeatherEffect(entityLightningBolt8);
		}

	}

	public void func_21146_a(Packet25EntityPainting packet25EntityPainting1) {
		EntityPainting entityPainting2 = new EntityPainting(this.worldClient, packet25EntityPainting1.xPosition, packet25EntityPainting1.yPosition, packet25EntityPainting1.zPosition, packet25EntityPainting1.direction, packet25EntityPainting1.title);
		this.worldClient.func_712_a(packet25EntityPainting1.entityId, entityPainting2);
	}

	public void func_6498_a(Packet28EntityVelocity packet28EntityVelocity1) {
		Entity entity2 = this.getEntityByID(packet28EntityVelocity1.entityId);
		if(entity2 != null && !purity.no_knockback) {
			entity2.setVelocity((double)packet28EntityVelocity1.motionX / 8000.0D, (double)packet28EntityVelocity1.motionY / 8000.0D, (double)packet28EntityVelocity1.motionZ / 8000.0D);
		}
	}

	public void func_21148_a(Packet40EntityMetadata packet40EntityMetadata1) {
		Entity entity2 = this.getEntityByID(packet40EntityMetadata1.entityId);
		if(entity2 != null && packet40EntityMetadata1.func_21047_b() != null) {
			entity2.getDataWatcher().updateWatchedObjectsFromList(packet40EntityMetadata1.func_21047_b());
		}

	}

	public void handleNamedEntitySpawn(Packet20NamedEntitySpawn packet20NamedEntitySpawn1) {
		double d2 = (double)packet20NamedEntitySpawn1.xPosition / 32.0D;
		double d4 = (double)packet20NamedEntitySpawn1.yPosition / 32.0D;
		double d6 = (double)packet20NamedEntitySpawn1.zPosition / 32.0D;
		float f8 = (float)(packet20NamedEntitySpawn1.rotation * 360) / 256.0F;
		float f9 = (float)(packet20NamedEntitySpawn1.pitch * 360) / 256.0F;
		EntityOtherPlayerMP entityOtherPlayerMP10 = new EntityOtherPlayerMP(this.mc.theWorld, packet20NamedEntitySpawn1.name);
		entityOtherPlayerMP10.prevPosX = entityOtherPlayerMP10.lastTickPosX = (double)(entityOtherPlayerMP10.serverPosX = packet20NamedEntitySpawn1.xPosition);
		entityOtherPlayerMP10.prevPosY = entityOtherPlayerMP10.lastTickPosY = (double)(entityOtherPlayerMP10.serverPosY = packet20NamedEntitySpawn1.yPosition);
		entityOtherPlayerMP10.prevPosZ = entityOtherPlayerMP10.lastTickPosZ = (double)(entityOtherPlayerMP10.serverPosZ = packet20NamedEntitySpawn1.zPosition);
		int i11 = packet20NamedEntitySpawn1.currentItem;
		if(i11 == 0) {
			entityOtherPlayerMP10.inventory.mainInventory[entityOtherPlayerMP10.inventory.currentItem] = null;
		} else {
			entityOtherPlayerMP10.inventory.mainInventory[entityOtherPlayerMP10.inventory.currentItem] = new ItemStack(i11, 1, 0);
		}

		entityOtherPlayerMP10.setPositionAndRotation(d2, d4, d6, f8, f9);
		this.worldClient.func_712_a(packet20NamedEntitySpawn1.entityId, entityOtherPlayerMP10);
	}

	public void handleEntityTeleport(Packet34EntityTeleport packet34EntityTeleport1) {
		Entity entity2 = this.getEntityByID(packet34EntityTeleport1.entityId);
		if(entity2 != null) {
			entity2.serverPosX = packet34EntityTeleport1.xPosition;
			entity2.serverPosY = packet34EntityTeleport1.yPosition;
			entity2.serverPosZ = packet34EntityTeleport1.zPosition;
			double d3 = (double)entity2.serverPosX / 32.0D;
			double d5 = (double)entity2.serverPosY / 32.0D + 0.015625D;
			double d7 = (double)entity2.serverPosZ / 32.0D;
			float f9 = (float)(packet34EntityTeleport1.yaw * 360) / 256.0F;
			float f10 = (float)(packet34EntityTeleport1.pitch * 360) / 256.0F;
			entity2.setPositionAndRotation2(d3, d5, d7, f9, f10, 3);
		}
	}

	public void handleEntity(Packet30Entity packet30Entity1) {
		Entity entity2 = this.getEntityByID(packet30Entity1.entityId);
		if(entity2 != null) {
			entity2.serverPosX += packet30Entity1.xPosition;
			entity2.serverPosY += packet30Entity1.yPosition;
			entity2.serverPosZ += packet30Entity1.zPosition;
			double d3 = (double)entity2.serverPosX / 32.0D;
			double d5 = (double)entity2.serverPosY / 32.0D;
			double d7 = (double)entity2.serverPosZ / 32.0D;
			float f9 = packet30Entity1.rotating ? (float)(packet30Entity1.yaw * 360) / 256.0F : entity2.rotationYaw;
			float f10 = packet30Entity1.rotating ? (float)(packet30Entity1.pitch * 360) / 256.0F : entity2.rotationPitch;
			entity2.setPositionAndRotation2(d3, d5, d7, f9, f10, 3);
		}
	}

	public void handleDestroyEntity(Packet29DestroyEntity packet29DestroyEntity1) {
		this.worldClient.removeEntityFromWorld(packet29DestroyEntity1.entityId);
	}

	public void handleFlying(Packet10Flying packet10Flying1) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		double d3 = entityPlayerSP2.posX;
		double d5 = entityPlayerSP2.posY;
		double d7 = entityPlayerSP2.posZ;
		float f9 = entityPlayerSP2.rotationYaw;
		float f10 = entityPlayerSP2.rotationPitch;
		if(packet10Flying1.moving) {
			d3 = packet10Flying1.xPosition;
			d5 = packet10Flying1.yPosition;
			d7 = packet10Flying1.zPosition;
		}

		if(packet10Flying1.rotating) {
			f9 = packet10Flying1.yaw;
			f10 = packet10Flying1.pitch;
		}

		entityPlayerSP2.ySize = 0.0F;
		entityPlayerSP2.motionX = entityPlayerSP2.motionY = entityPlayerSP2.motionZ = 0.0D;
		entityPlayerSP2.setPositionAndRotation(d3, d5, d7, f9, f10);
		packet10Flying1.xPosition = entityPlayerSP2.posX;
		packet10Flying1.yPosition = entityPlayerSP2.boundingBox.minY;
		packet10Flying1.zPosition = entityPlayerSP2.posZ;
		packet10Flying1.stance = entityPlayerSP2.posY;
		this.netManager.addToSendQueue(packet10Flying1);
		if(!this.field_1210_g) {
			this.mc.thePlayer.prevPosX = this.mc.thePlayer.posX;
			this.mc.thePlayer.prevPosY = this.mc.thePlayer.posY;
			this.mc.thePlayer.prevPosZ = this.mc.thePlayer.posZ;
			this.field_1210_g = true;
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void handlePreChunk(Packet50PreChunk packet50PreChunk1) {
		this.worldClient.doPreChunk(packet50PreChunk1.xPosition, packet50PreChunk1.yPosition, packet50PreChunk1.mode);
	}

	public void handleMultiBlockChange(Packet52MultiBlockChange packet52MultiBlockChange1) {
		Chunk chunk2 = this.worldClient.getChunkFromChunkCoords(packet52MultiBlockChange1.xPosition, packet52MultiBlockChange1.zPosition);
		int i3 = packet52MultiBlockChange1.xPosition * 16;
		int i4 = packet52MultiBlockChange1.zPosition * 16;

		for(int i5 = 0; i5 < packet52MultiBlockChange1.size; ++i5) {
			short s6 = packet52MultiBlockChange1.coordinateArray[i5];
			int i7 = packet52MultiBlockChange1.typeArray[i5] & 255;
			byte b8 = packet52MultiBlockChange1.metadataArray[i5];
			int i9 = s6 >> 12 & 15;
			int i10 = s6 >> 8 & 15;
			int i11 = s6 & 255;
			chunk2.setBlockIDWithMetadata(i9, i11, i10, i7, b8);
			this.worldClient.func_711_c(i9 + i3, i11, i10 + i4, i9 + i3, i11, i10 + i4);
			this.worldClient.markBlocksDirty(i9 + i3, i11, i10 + i4, i9 + i3, i11, i10 + i4);
		}

	}

	public void handleMapChunk(Packet51MapChunk packet51MapChunk1) {
		this.worldClient.func_711_c(packet51MapChunk1.xPosition, packet51MapChunk1.yPosition, packet51MapChunk1.zPosition, packet51MapChunk1.xPosition + packet51MapChunk1.xSize - 1, packet51MapChunk1.yPosition + packet51MapChunk1.ySize - 1, packet51MapChunk1.zPosition + packet51MapChunk1.zSize - 1);
		this.worldClient.setChunkData(packet51MapChunk1.xPosition, packet51MapChunk1.yPosition, packet51MapChunk1.zPosition, packet51MapChunk1.xSize, packet51MapChunk1.ySize, packet51MapChunk1.zSize, packet51MapChunk1.chunk);
	}

	public void handleBlockChange(Packet53BlockChange packet53BlockChange1) {
		this.worldClient.func_714_c(packet53BlockChange1.xPosition, packet53BlockChange1.yPosition, packet53BlockChange1.zPosition, packet53BlockChange1.type, packet53BlockChange1.metadata);
	}

	public void handleKickDisconnect(Packet255KickDisconnect packet255KickDisconnect1) {
		this.netManager.networkShutdown("disconnect.kicked", new Object[0]);
		this.disconnected = true;
		this.mc.changeWorld1((World)null);
		this.mc.displayGuiScreen(new GuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", new Object[]{packet255KickDisconnect1.reason}));
	}

	public void handleErrorMessage(String string1, Object[] object2) {
		if(!this.disconnected) {
			this.disconnected = true;
			this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiConnectFailed("disconnect.lost", string1, object2));
		}
	}

	public void func_28117_a(Packet packet1) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(packet1);
			this.netManager.func_28142_c();
		}
	}

	public void addToSendQueue(Packet packet1) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(packet1);
		}
	}

	public void handleCollect(Packet22Collect packet22Collect1) {
		Entity entity2 = this.getEntityByID(packet22Collect1.collectedEntityId);
		Object object3 = (EntityLiving)this.getEntityByID(packet22Collect1.collectorEntityId);
		if(object3 == null) {
			object3 = this.mc.thePlayer;
		}

		if(entity2 != null) {
			this.worldClient.playSoundAtEntity(entity2, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity2, (Entity)object3, -0.5F));
			this.worldClient.removeEntityFromWorld(packet22Collect1.collectedEntityId);
		}

	}

	public void handleChat(Packet3Chat packet3Chat1) {
		this.mc.ingameGUI.addChatMessage(packet3Chat1.message);
	}

	public void handleArmAnimation(Packet18Animation packet18Animation1) {
		Entity entity2 = this.getEntityByID(packet18Animation1.entityId);
		if(entity2 != null) {
			EntityPlayer entityPlayer3;
			if(packet18Animation1.animate == 1) {
				entityPlayer3 = (EntityPlayer)entity2;
				entityPlayer3.swingItem();
			} else if(packet18Animation1.animate == 2) {
				entity2.performHurtAnimation();
			} else if(packet18Animation1.animate == 3) {
				entityPlayer3 = (EntityPlayer)entity2;
				entityPlayer3.wakeUpPlayer(false, false, false);
			} else if(packet18Animation1.animate == 4) {
				entityPlayer3 = (EntityPlayer)entity2;
				entityPlayer3.func_6420_o();
			}

		}
	}

	public void func_22186_a(Packet17Sleep packet17Sleep1) {
		Entity entity2 = this.getEntityByID(packet17Sleep1.field_22045_a);
		if(entity2 != null) {
			if(packet17Sleep1.field_22046_e == 0) {
				EntityPlayer entityPlayer3 = (EntityPlayer)entity2;
				entityPlayer3.sleepInBedAt(packet17Sleep1.field_22044_b, packet17Sleep1.field_22048_c, packet17Sleep1.field_22047_d);
			}

		}
	}

	public void handleHandshake(Packet2Handshake packet2Handshake1) {
		if(packet2Handshake1.username.equals("-")) {
			this.addToSendQueue(new Packet1Login(this.mc.session.username, 14));
		} else {
			try {
				URL uRL2 = new URL("http://www.minecraft.net/game/joinserver.jsp?user=" + this.mc.session.username + "&sessionId=" + this.mc.session.sessionId + "&serverId=" + packet2Handshake1.username);
				BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(uRL2.openStream()));
				String string4 = bufferedReader3.readLine();
				bufferedReader3.close();
				if(string4.equalsIgnoreCase("ok")) {
					this.addToSendQueue(new Packet1Login(this.mc.session.username, 14));
				} else {
					this.netManager.networkShutdown("disconnect.loginFailedInfo", new Object[]{string4});
				}
			} catch (Exception exception5) {
				exception5.printStackTrace();
				this.netManager.networkShutdown("disconnect.genericReason", new Object[]{"Internal client error: " + exception5.toString()});
			}
		}

	}

	public void disconnect() {
		this.disconnected = true;
		this.netManager.wakeThreads();
		this.netManager.networkShutdown("disconnect.closed", new Object[0]);
	}

	public void handleMobSpawn(Packet24MobSpawn packet24MobSpawn1) {
		double d2 = (double)packet24MobSpawn1.xPosition / 32.0D;
		double d4 = (double)packet24MobSpawn1.yPosition / 32.0D;
		double d6 = (double)packet24MobSpawn1.zPosition / 32.0D;
		float f8 = (float)(packet24MobSpawn1.yaw * 360) / 256.0F;
		float f9 = (float)(packet24MobSpawn1.pitch * 360) / 256.0F;
		EntityLiving entityLiving10 = (EntityLiving)EntityList.createEntity(packet24MobSpawn1.type, this.mc.theWorld);
		entityLiving10.serverPosX = packet24MobSpawn1.xPosition;
		entityLiving10.serverPosY = packet24MobSpawn1.yPosition;
		entityLiving10.serverPosZ = packet24MobSpawn1.zPosition;
		entityLiving10.entityId = packet24MobSpawn1.entityId;
		entityLiving10.setPositionAndRotation(d2, d4, d6, f8, f9);
		entityLiving10.isMultiplayerEntity = true;
		this.worldClient.func_712_a(packet24MobSpawn1.entityId, entityLiving10);
		List list11 = packet24MobSpawn1.getMetadata();
		if(list11 != null) {
			entityLiving10.getDataWatcher().updateWatchedObjectsFromList(list11);
		}

	}

	public void handleUpdateTime(Packet4UpdateTime packet4UpdateTime1) {
		this.mc.theWorld.setWorldTime(packet4UpdateTime1.time);
	}

	public void handleSpawnPosition(Packet6SpawnPosition packet6SpawnPosition1) {
		this.mc.thePlayer.setPlayerSpawnCoordinate(new ChunkCoordinates(packet6SpawnPosition1.xPosition, packet6SpawnPosition1.yPosition, packet6SpawnPosition1.zPosition));
		this.mc.theWorld.getWorldInfo().setSpawn(packet6SpawnPosition1.xPosition, packet6SpawnPosition1.yPosition, packet6SpawnPosition1.zPosition);
	}

	public void func_6497_a(Packet39AttachEntity packet39AttachEntity1) {
		Object object2 = this.getEntityByID(packet39AttachEntity1.entityId);
		Entity entity3 = this.getEntityByID(packet39AttachEntity1.vehicleEntityId);
		if(packet39AttachEntity1.entityId == this.mc.thePlayer.entityId) {
			object2 = this.mc.thePlayer;
		}

		if(object2 != null) {
			((Entity)object2).mountEntity(entity3);
		}
	}

	public void func_9447_a(Packet38EntityStatus packet38EntityStatus1) {
		Entity entity2 = this.getEntityByID(packet38EntityStatus1.entityId);
		if(entity2 != null) {
			entity2.handleHealthUpdate(packet38EntityStatus1.entityStatus);
		}

	}

	private Entity getEntityByID(int i1) {
		return (Entity)(i1 == this.mc.thePlayer.entityId ? this.mc.thePlayer : this.worldClient.func_709_b(i1));
	}

	public void handleHealth(Packet8UpdateHealth packet8UpdateHealth1) {
		this.mc.thePlayer.setHealth(packet8UpdateHealth1.healthMP);
	}

	public void func_9448_a(Packet9Respawn packet9Respawn1) {
		if(packet9Respawn1.field_28048_a != this.mc.thePlayer.dimension) {
			this.field_1210_g = false;
			this.worldClient = new WorldClient(this, this.worldClient.getWorldInfo().getRandomSeed(), packet9Respawn1.field_28048_a);
			this.worldClient.multiplayerWorld = true;
			this.mc.changeWorld1(this.worldClient);
			this.mc.thePlayer.dimension = packet9Respawn1.field_28048_a;
			this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
		}

		this.mc.respawn(true, packet9Respawn1.field_28048_a);
	}

	public void func_12245_a(Packet60Explosion packet60Explosion1) {
		Explosion explosion2 = new Explosion(this.mc.theWorld, (Entity)null, packet60Explosion1.explosionX, packet60Explosion1.explosionY, packet60Explosion1.explosionZ, packet60Explosion1.explosionSize);
		explosion2.destroyedBlockPositions = packet60Explosion1.destroyedBlockPositions;
		explosion2.doExplosionB(true);
	}

	public void func_20087_a(Packet100OpenWindow packet100OpenWindow1) {
		if(packet100OpenWindow1.inventoryType == 0) {
			InventoryBasic inventoryBasic2 = new InventoryBasic(packet100OpenWindow1.windowTitle, packet100OpenWindow1.slotsCount);
			this.mc.thePlayer.displayGUIChest(inventoryBasic2);
			this.mc.thePlayer.craftingInventory.windowId = packet100OpenWindow1.windowId;
		} else if(packet100OpenWindow1.inventoryType == 2) {
			TileEntityFurnace tileEntityFurnace3 = new TileEntityFurnace();
			this.mc.thePlayer.displayGUIFurnace(tileEntityFurnace3);
			this.mc.thePlayer.craftingInventory.windowId = packet100OpenWindow1.windowId;
		} else if(packet100OpenWindow1.inventoryType == 3) {
			TileEntityDispenser tileEntityDispenser4 = new TileEntityDispenser();
			this.mc.thePlayer.displayGUIDispenser(tileEntityDispenser4);
			this.mc.thePlayer.craftingInventory.windowId = packet100OpenWindow1.windowId;
		} else if(packet100OpenWindow1.inventoryType == 1) {
			EntityPlayerSP entityPlayerSP5 = this.mc.thePlayer;
			this.mc.thePlayer.displayWorkbenchGUI(MathHelper.floor_double(entityPlayerSP5.posX), MathHelper.floor_double(entityPlayerSP5.posY), MathHelper.floor_double(entityPlayerSP5.posZ));
			this.mc.thePlayer.craftingInventory.windowId = packet100OpenWindow1.windowId;
		}

	}

	public void func_20088_a(Packet103SetSlot packet103SetSlot1) {
		if(packet103SetSlot1.windowId == -1) {
			this.mc.thePlayer.inventory.setItemStack(packet103SetSlot1.myItemStack);
		} else if(packet103SetSlot1.windowId == 0 && packet103SetSlot1.itemSlot >= 36 && packet103SetSlot1.itemSlot < 45) {
			ItemStack itemStack2 = this.mc.thePlayer.inventorySlots.getSlot(packet103SetSlot1.itemSlot).getStack();
			if(packet103SetSlot1.myItemStack != null && (itemStack2 == null || itemStack2.stackSize < packet103SetSlot1.myItemStack.stackSize)) {
				packet103SetSlot1.myItemStack.animationsToGo = 5;
			}

			this.mc.thePlayer.inventorySlots.putStackInSlot(packet103SetSlot1.itemSlot, packet103SetSlot1.myItemStack);
		} else if(packet103SetSlot1.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			this.mc.thePlayer.craftingInventory.putStackInSlot(packet103SetSlot1.itemSlot, packet103SetSlot1.myItemStack);
		}

	}

	public void func_20089_a(Packet106Transaction packet106Transaction1) {
		Container container2 = null;
		if(packet106Transaction1.windowId == 0) {
			container2 = this.mc.thePlayer.inventorySlots;
		} else if(packet106Transaction1.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			container2 = this.mc.thePlayer.craftingInventory;
		}

		if(container2 != null) {
			if(packet106Transaction1.field_20030_c) {
				container2.func_20113_a(packet106Transaction1.field_20028_b);
			} else {
				container2.func_20110_b(packet106Transaction1.field_20028_b);
				this.addToSendQueue(new Packet106Transaction(packet106Transaction1.windowId, packet106Transaction1.field_20028_b, true));
			}
		}

	}

	public void func_20094_a(Packet104WindowItems packet104WindowItems1) {
		if(packet104WindowItems1.windowId == 0) {
			this.mc.thePlayer.inventorySlots.putStacksInSlots(packet104WindowItems1.itemStack);
		} else if(packet104WindowItems1.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			this.mc.thePlayer.craftingInventory.putStacksInSlots(packet104WindowItems1.itemStack);
		}

	}

	public void handleSignUpdate(Packet130UpdateSign packet130UpdateSign1) {
		if(this.mc.theWorld.blockExists(packet130UpdateSign1.xPosition, packet130UpdateSign1.yPosition, packet130UpdateSign1.zPosition)) {
			TileEntity tileEntity2 = this.mc.theWorld.getBlockTileEntity(packet130UpdateSign1.xPosition, packet130UpdateSign1.yPosition, packet130UpdateSign1.zPosition);
			if(tileEntity2 instanceof TileEntitySign) {
				TileEntitySign tileEntitySign3 = (TileEntitySign)tileEntity2;

				for(int i4 = 0; i4 < 4; ++i4) {
					tileEntitySign3.signText[i4] = packet130UpdateSign1.signLines[i4];
				}

				tileEntitySign3.onInventoryChanged();
			}
		}

	}

	public void func_20090_a(Packet105UpdateProgressbar packet105UpdateProgressbar1) {
		this.registerPacket(packet105UpdateProgressbar1);
		if(this.mc.thePlayer.craftingInventory != null && this.mc.thePlayer.craftingInventory.windowId == packet105UpdateProgressbar1.windowId) {
			this.mc.thePlayer.craftingInventory.func_20112_a(packet105UpdateProgressbar1.progressBar, packet105UpdateProgressbar1.progressBarValue);
		}

	}

	public void handlePlayerInventory(Packet5PlayerInventory packet5PlayerInventory1) {
		Entity entity2 = this.getEntityByID(packet5PlayerInventory1.entityID);
		if(entity2 != null) {
			entity2.outfitWithItem(packet5PlayerInventory1.slot, packet5PlayerInventory1.itemID, packet5PlayerInventory1.itemDamage);
		}

	}

	public void func_20092_a(Packet101CloseWindow packet101CloseWindow1) {
		this.mc.thePlayer.closeScreen();
	}

	public void handleNotePlay(Packet54PlayNoteBlock packet54PlayNoteBlock1) {
		this.mc.theWorld.playNoteAt(packet54PlayNoteBlock1.xLocation, packet54PlayNoteBlock1.yLocation, packet54PlayNoteBlock1.zLocation, packet54PlayNoteBlock1.instrumentType, packet54PlayNoteBlock1.pitch);
	}

	public void func_25118_a(Packet70Bed packet70Bed1) {
		int i2 = packet70Bed1.field_25019_b;
		if(i2 >= 0 && i2 < Packet70Bed.field_25020_a.length && Packet70Bed.field_25020_a[i2] != null) {
			this.mc.thePlayer.addChatMessage(Packet70Bed.field_25020_a[i2]);
		}

		if(i2 == 1) {
			this.worldClient.getWorldInfo().setRaining(true);
			this.worldClient.func_27158_h(1.0F);
		} else if(i2 == 2) {
			this.worldClient.getWorldInfo().setRaining(false);
			this.worldClient.func_27158_h(0.0F);
		}

	}

	public void func_28116_a(Packet131MapData packet131MapData1) {
		if(packet131MapData1.field_28055_a == Item.mapItem.shiftedIndex) {
			ItemMap.func_28013_a(packet131MapData1.field_28054_b, this.mc.theWorld).func_28171_a(packet131MapData1.field_28056_c);
		} else {
			System.out.println("Unknown itemid: " + packet131MapData1.field_28054_b);
		}

	}

	public void func_28115_a(Packet61DoorChange packet61DoorChange1) {
		this.mc.theWorld.func_28106_e(packet61DoorChange1.field_28050_a, packet61DoorChange1.field_28053_c, packet61DoorChange1.field_28052_d, packet61DoorChange1.field_28051_e, packet61DoorChange1.field_28049_b);
	}

	public void func_27245_a(Packet200Statistic packet200Statistic1) {
		((EntityClientPlayerMP)this.mc.thePlayer).func_27027_b(StatList.func_27361_a(packet200Statistic1.field_27052_a), packet200Statistic1.field_27051_b);
	}

	public boolean isServerHandler() {
		return false;
	}
}
