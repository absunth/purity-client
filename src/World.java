package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class World implements IBlockAccess {
	public boolean scheduledUpdatesAreImmediate;
	private List lightingToUpdate;
	public List loadedEntityList;
	private List unloadedEntityList;
	private TreeSet scheduledTickTreeSet;
	private Set scheduledTickSet;
	public List loadedTileEntityList;
	private List field_30900_E;
	public List playerEntities;
	public List weatherEffects;
	private long field_1019_F;
	public int skylightSubtracted;
	protected int field_9437_g;
	protected final int field_9436_h;
	protected float prevRainingStrength;
	protected float rainingStrength;
	protected float prevThunderingStrength;
	protected float thunderingStrength;
	protected int field_27168_F;
	public int field_27172_i;
	public boolean editingBlocks;
	private long lockTimestamp;
	protected int autosavePeriod;
	public int difficultySetting;
	public Random rand;
	public boolean isNewWorld;
	public final WorldProvider worldProvider;
	protected List worldAccesses;
	protected IChunkProvider chunkProvider;
	protected final ISaveHandler saveHandler;
	protected WorldInfo worldInfo;
	public boolean findingSpawnPoint;
	private boolean allPlayersSleeping;
	public MapStorage field_28108_z;
	private ArrayList collidingBoundingBoxes;
	private boolean field_31055_L;
	private int lightingUpdatesCounter;
	private boolean spawnHostileMobs;
	private boolean spawnPeacefulMobs;
	static int lightingUpdatesScheduled = 0;
	private Set positionsToUpdate;
	private int soundCounter;
	private List field_1012_M;
	public boolean multiplayerWorld;

	public WorldChunkManager getWorldChunkManager() {
		return this.worldProvider.worldChunkMgr;
	}

	public World(ISaveHandler iSaveHandler1, String string2, WorldProvider worldProvider3, long j4) {
		this.scheduledUpdatesAreImmediate = false;
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.field_30900_E = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.field_1019_F = 16777215L;
		this.skylightSubtracted = 0;
		this.field_9437_g = (new Random()).nextInt();
		this.field_9436_h = 1013904223;
		this.field_27168_F = 0;
		this.field_27172_i = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.lightingUpdatesCounter = 0;
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.field_1012_M = new ArrayList();
		this.multiplayerWorld = false;
		this.saveHandler = iSaveHandler1;
		this.worldInfo = new WorldInfo(j4, string2);
		this.worldProvider = worldProvider3;
		this.field_28108_z = new MapStorage(iSaveHandler1);
		worldProvider3.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		this.calculateInitialSkylight();
		this.func_27163_E();
	}

	public World(World world1, WorldProvider worldProvider2) {
		this.scheduledUpdatesAreImmediate = false;
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.field_30900_E = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.field_1019_F = 16777215L;
		this.skylightSubtracted = 0;
		this.field_9437_g = (new Random()).nextInt();
		this.field_9436_h = 1013904223;
		this.field_27168_F = 0;
		this.field_27172_i = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.lightingUpdatesCounter = 0;
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.field_1012_M = new ArrayList();
		this.multiplayerWorld = false;
		this.lockTimestamp = world1.lockTimestamp;
		this.saveHandler = world1.saveHandler;
		this.worldInfo = new WorldInfo(world1.worldInfo);
		this.field_28108_z = new MapStorage(this.saveHandler);
		this.worldProvider = worldProvider2;
		worldProvider2.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		this.calculateInitialSkylight();
		this.func_27163_E();
	}

	public World(ISaveHandler iSaveHandler1, String string2, long j3) {
		this(iSaveHandler1, string2, j3, (WorldProvider)null);
	}

	public World(ISaveHandler iSaveHandler1, String string2, long j3, WorldProvider worldProvider5) {
		this.scheduledUpdatesAreImmediate = false;
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.field_30900_E = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.field_1019_F = 16777215L;
		this.skylightSubtracted = 0;
		this.field_9437_g = (new Random()).nextInt();
		this.field_9436_h = 1013904223;
		this.field_27168_F = 0;
		this.field_27172_i = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.lightingUpdatesCounter = 0;
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.field_1012_M = new ArrayList();
		this.multiplayerWorld = false;
		this.saveHandler = iSaveHandler1;
		this.field_28108_z = new MapStorage(iSaveHandler1);
		this.worldInfo = iSaveHandler1.loadWorldInfo();
		this.isNewWorld = this.worldInfo == null;
		if(worldProvider5 != null) {
			this.worldProvider = worldProvider5;
		} else if(this.worldInfo != null && this.worldInfo.getDimension() == -1) {
			this.worldProvider = WorldProvider.getProviderForDimension(-1);
		} else {
			this.worldProvider = WorldProvider.getProviderForDimension(0);
		}

		boolean z6 = false;
		if(this.worldInfo == null) {
			this.worldInfo = new WorldInfo(j3, string2);
			z6 = true;
		} else {
			this.worldInfo.setWorldName(string2);
		}

		this.worldProvider.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		if(z6) {
			this.getInitialSpawnLocation();
		}

		this.calculateInitialSkylight();
		this.func_27163_E();
	}

	protected IChunkProvider getChunkProvider() {
		IChunkLoader iChunkLoader1 = this.saveHandler.getChunkLoader(this.worldProvider);
		return new ChunkProvider(this, iChunkLoader1, this.worldProvider.getChunkProvider());
	}

	protected void getInitialSpawnLocation() {
		this.findingSpawnPoint = true;
		int i1 = 0;
		byte b2 = 64;

		int i3;
		for(i3 = 0; !this.worldProvider.canCoordinateBeSpawn(i1, i3); i3 += this.rand.nextInt(64) - this.rand.nextInt(64)) {
			i1 += this.rand.nextInt(64) - this.rand.nextInt(64);
		}

		this.worldInfo.setSpawn(i1, b2, i3);
		this.findingSpawnPoint = false;
	}

	public void setSpawnLocation() {
		if(this.worldInfo.getSpawnY() <= 0) {
			this.worldInfo.setSpawnY(64);
		}

		int i1 = this.worldInfo.getSpawnX();

		int i2;
		for(i2 = this.worldInfo.getSpawnZ(); this.getFirstUncoveredBlock(i1, i2) == 0; i2 += this.rand.nextInt(8) - this.rand.nextInt(8)) {
			i1 += this.rand.nextInt(8) - this.rand.nextInt(8);
		}

		this.worldInfo.setSpawnX(i1);
		this.worldInfo.setSpawnZ(i2);
	}

	public int getFirstUncoveredBlock(int i1, int i2) {
		int i3;
		for(i3 = 63; !this.isAirBlock(i1, i3 + 1, i2); ++i3) {
		}

		return this.getBlockId(i1, i3, i2);
	}

	public void emptyMethod1() {
	}

	public void spawnPlayerWithLoadedChunks(EntityPlayer entityPlayer1) {
		try {
			NBTTagCompound nBTTagCompound2 = this.worldInfo.getPlayerNBTTagCompound();
			if(nBTTagCompound2 != null) {
				entityPlayer1.readFromNBT(nBTTagCompound2);
				this.worldInfo.setPlayerNBTTagCompound((NBTTagCompound)null);
			}

			if(this.chunkProvider instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate3 = (ChunkProviderLoadOrGenerate)this.chunkProvider;
				int i4 = MathHelper.floor_float((float)((int)entityPlayer1.posX)) >> 4;
				int i5 = MathHelper.floor_float((float)((int)entityPlayer1.posZ)) >> 4;
				chunkProviderLoadOrGenerate3.setCurrentChunkOver(i4, i5);
			}

			this.entityJoinedWorld(entityPlayer1);
		} catch (Exception exception6) {
			exception6.printStackTrace();
		}

	}

	public void saveWorld(boolean z1, IProgressUpdate iProgressUpdate2) {
		if(this.chunkProvider.canSave()) {
			if(iProgressUpdate2 != null) {
				iProgressUpdate2.func_594_b("Saving level");
			}

			this.saveLevel();
			if(iProgressUpdate2 != null) {
				iProgressUpdate2.displayLoadingString("Saving chunks");
			}

			this.chunkProvider.saveChunks(z1, iProgressUpdate2);
		}
	}

	private void saveLevel() {
		this.checkSessionLock();
		this.saveHandler.saveWorldInfoAndPlayer(this.worldInfo, this.playerEntities);
		this.field_28108_z.saveAllData();
	}

	public boolean func_650_a(int i1) {
		if(!this.chunkProvider.canSave()) {
			return true;
		} else {
			if(i1 == 0) {
				this.saveLevel();
			}

			return this.chunkProvider.saveChunks(false, (IProgressUpdate)null);
		}
	}

	public int getBlockId(int i1, int i2, int i3) {
		return i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000 ? (i2 < 0 ? 0 : (i2 >= 128 ? 0 : this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).getBlockID(i1 & 15, i2, i3 & 15))) : 0;
	}

	public boolean isAirBlock(int i1, int i2, int i3) {
		return this.getBlockId(i1, i2, i3) == 0;
	}

	public boolean blockExists(int i1, int i2, int i3) {
		return i2 >= 0 && i2 < 128 ? this.chunkExists(i1 >> 4, i3 >> 4) : false;
	}

	public boolean doChunksNearChunkExist(int i1, int i2, int i3, int i4) {
		return this.checkChunksExist(i1 - i4, i2 - i4, i3 - i4, i1 + i4, i2 + i4, i3 + i4);
	}

	public boolean checkChunksExist(int i1, int i2, int i3, int i4, int i5, int i6) {
		if(i5 >= 0 && i2 < 128) {
			i1 >>= 4;
			i2 >>= 4;
			i3 >>= 4;
			i4 >>= 4;
			i5 >>= 4;
			i6 >>= 4;

			for(int i7 = i1; i7 <= i4; ++i7) {
				for(int i8 = i3; i8 <= i6; ++i8) {
					if(!this.chunkExists(i7, i8)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean chunkExists(int i1, int i2) {
		return this.chunkProvider.chunkExists(i1, i2);
	}

	public Chunk getChunkFromBlockCoords(int i1, int i2) {
		return this.getChunkFromChunkCoords(i1 >> 4, i2 >> 4);
	}

	public Chunk getChunkFromChunkCoords(int i1, int i2) {
		return this.chunkProvider.provideChunk(i1, i2);
	}

	public boolean setBlockAndMetadata(int i1, int i2, int i3, int i4, int i5) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return false;
			} else if(i2 >= 128) {
				return false;
			} else {
				Chunk chunk6 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				return chunk6.setBlockIDWithMetadata(i1 & 15, i2, i3 & 15, i4, i5);
			}
		} else {
			return false;
		}
	}

	public boolean setBlock(int i1, int i2, int i3, int i4) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return false;
			} else if(i2 >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				return chunk5.setBlockID(i1 & 15, i2, i3 & 15, i4);
			}
		} else {
			return false;
		}
	}

	public Material getBlockMaterial(int i1, int i2, int i3) {
		int i4 = this.getBlockId(i1, i2, i3);
		return i4 == 0 ? Material.air : Block.blocksList[i4].blockMaterial;
	}

	public int getBlockMetadata(int i1, int i2, int i3) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return 0;
			} else if(i2 >= 128) {
				return 0;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				return chunk4.getBlockMetadata(i1, i2, i3);
			}
		} else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int i1, int i2, int i3, int i4) {
		if(this.setBlockMetadata(i1, i2, i3, i4)) {
			int i5 = this.getBlockId(i1, i2, i3);
			if(Block.field_28032_t[i5 & 255]) {
				this.notifyBlockChange(i1, i2, i3, i5);
			} else {
				this.notifyBlocksOfNeighborChange(i1, i2, i3, i5);
			}
		}

	}

	public boolean setBlockMetadata(int i1, int i2, int i3, int i4) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return false;
			} else if(i2 >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				chunk5.setBlockMetadata(i1, i2, i3, i4);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int i1, int i2, int i3, int i4) {
		if(this.setBlock(i1, i2, i3, i4)) {
			this.notifyBlockChange(i1, i2, i3, i4);
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int i1, int i2, int i3, int i4, int i5) {
		if(this.setBlockAndMetadata(i1, i2, i3, i4, i5)) {
			this.notifyBlockChange(i1, i2, i3, i4);
			return true;
		} else {
			return false;
		}
	}

	public void markBlockNeedsUpdate(int i1, int i2, int i3) {
		for(int i4 = 0; i4 < this.worldAccesses.size(); ++i4) {
			((IWorldAccess)this.worldAccesses.get(i4)).markBlockAndNeighborsNeedsUpdate(i1, i2, i3);
		}

	}

	protected void notifyBlockChange(int i1, int i2, int i3, int i4) {
		this.markBlockNeedsUpdate(i1, i2, i3);
		this.notifyBlocksOfNeighborChange(i1, i2, i3, i4);
	}

	public void markBlocksDirtyVertical(int i1, int i2, int i3, int i4) {
		if(i3 > i4) {
			int i5 = i4;
			i4 = i3;
			i3 = i5;
		}

		this.markBlocksDirty(i1, i3, i2, i1, i4, i2);
	}

	public void markBlockAsNeedsUpdate(int i1, int i2, int i3) {
		for(int i4 = 0; i4 < this.worldAccesses.size(); ++i4) {
			((IWorldAccess)this.worldAccesses.get(i4)).markBlockRangeNeedsUpdate(i1, i2, i3, i1, i2, i3);
		}

	}

	public void markBlocksDirty(int i1, int i2, int i3, int i4, int i5, int i6) {
		for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
			((IWorldAccess)this.worldAccesses.get(i7)).markBlockRangeNeedsUpdate(i1, i2, i3, i4, i5, i6);
		}

	}

	public void notifyBlocksOfNeighborChange(int i1, int i2, int i3, int i4) {
		this.notifyBlockOfNeighborChange(i1 - 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1 + 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 - 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 + 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 - 1, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 + 1, i4);
	}

	private void notifyBlockOfNeighborChange(int i1, int i2, int i3, int i4) {
		if(!this.editingBlocks && !this.multiplayerWorld) {
			Block block5 = Block.blocksList[this.getBlockId(i1, i2, i3)];
			if(block5 != null) {
				block5.onNeighborBlockChange(this, i1, i2, i3, i4);
			}

		}
	}

	public boolean canBlockSeeTheSky(int i1, int i2, int i3) {
		return this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).canBlockSeeTheSky(i1 & 15, i2, i3 & 15);
	}

	public int getFullBlockLightValue(int i1, int i2, int i3) {
		if(i2 < 0) {
			return 0;
		} else {
			if(i2 >= 128) {
				i2 = 127;
			}

			return this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).getBlockLightValue(i1 & 15, i2, i3 & 15, 0);
		}
	}

	public int getBlockLightValue(int i1, int i2, int i3) {
		return this.getBlockLightValue_do(i1, i2, i3, true);
	}

	public int getBlockLightValue_do(int i1, int i2, int i3, boolean z4) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(z4) {
				int i5 = this.getBlockId(i1, i2, i3);
				if(i5 == Block.stairSingle.blockID || i5 == Block.tilledField.blockID || i5 == Block.stairCompactCobblestone.blockID || i5 == Block.stairCompactPlanks.blockID) {
					int i6 = this.getBlockLightValue_do(i1, i2 + 1, i3, false);
					int i7 = this.getBlockLightValue_do(i1 + 1, i2, i3, false);
					int i8 = this.getBlockLightValue_do(i1 - 1, i2, i3, false);
					int i9 = this.getBlockLightValue_do(i1, i2, i3 + 1, false);
					int i10 = this.getBlockLightValue_do(i1, i2, i3 - 1, false);
					if(i7 > i6) {
						i6 = i7;
					}

					if(i8 > i6) {
						i6 = i8;
					}

					if(i9 > i6) {
						i6 = i9;
					}

					if(i10 > i6) {
						i6 = i10;
					}

					return i6;
				}
			}

			if(i2 < 0) {
				return 0;
			} else {
				if(i2 >= 128) {
					i2 = 127;
				}

				Chunk chunk11 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				return chunk11.getBlockLightValue(i1, i2, i3, this.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public boolean canExistingBlockSeeTheSky(int i1, int i2, int i3) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return false;
			} else if(i2 >= 128) {
				return true;
			} else if(!this.chunkExists(i1 >> 4, i3 >> 4)) {
				return false;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				return chunk4.canBlockSeeTheSky(i1, i2, i3);
			}
		} else {
			return false;
		}
	}

	public int getHeightValue(int i1, int i2) {
		if(i1 >= -32000000 && i2 >= -32000000 && i1 < 32000000 && i2 <= 32000000) {
			if(!this.chunkExists(i1 >> 4, i2 >> 4)) {
				return 0;
			} else {
				Chunk chunk3 = this.getChunkFromChunkCoords(i1 >> 4, i2 >> 4);
				return chunk3.getHeightValue(i1 & 15, i2 & 15);
			}
		} else {
			return 0;
		}
	}

	public void neighborLightPropagationChanged(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		if(!this.worldProvider.hasNoSky || enumSkyBlock1 != EnumSkyBlock.Sky) {
			if(this.blockExists(i2, i3, i4)) {
				if(enumSkyBlock1 == EnumSkyBlock.Sky) {
					if(this.canExistingBlockSeeTheSky(i2, i3, i4)) {
						i5 = 15;
					}
				} else if(enumSkyBlock1 == EnumSkyBlock.Block) {
					int i6 = this.getBlockId(i2, i3, i4);
					if(Block.lightValue[i6] > i5) {
						i5 = Block.lightValue[i6];
					}
				}

				if(this.getSavedLightValue(enumSkyBlock1, i2, i3, i4) != i5) {
					this.scheduleLightingUpdate(enumSkyBlock1, i2, i3, i4, i2, i3, i4);
				}

			}
		}
	}

	public int getSavedLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4) {
		if(i3 < 0) {
			i3 = 0;
		}

		if(i3 >= 128) {
			i3 = 127;
		}

		if(i3 >= 0 && i3 < 128 && i2 >= -32000000 && i4 >= -32000000 && i2 < 32000000 && i4 <= 32000000) {
			int i5 = i2 >> 4;
			int i6 = i4 >> 4;
			if(!this.chunkExists(i5, i6)) {
				return 0;
			} else {
				Chunk chunk7 = this.getChunkFromChunkCoords(i5, i6);
				return chunk7.getSavedLightValue(enumSkyBlock1, i2 & 15, i3, i4 & 15);
			}
		} else {
			return enumSkyBlock1.field_1722_c;
		}
	}

	public void setLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		if(i2 >= -32000000 && i4 >= -32000000 && i2 < 32000000 && i4 <= 32000000) {
			if(i3 >= 0) {
				if(i3 < 128) {
					if(this.chunkExists(i2 >> 4, i4 >> 4)) {
						Chunk chunk6 = this.getChunkFromChunkCoords(i2 >> 4, i4 >> 4);
						chunk6.setLightValue(enumSkyBlock1, i2 & 15, i3, i4 & 15, i5);

						for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
							((IWorldAccess)this.worldAccesses.get(i7)).markBlockAndNeighborsNeedsUpdate(i2, i3, i4);
						}

					}
				}
			}
		}
	}

	public float getBrightness(int i1, int i2, int i3, int i4) {
		int i5 = this.getBlockLightValue(i1, i2, i3);
		if(i5 < i4) {
			i5 = i4;
		}

		return this.worldProvider.lightBrightnessTable[i5];
	}

	public float getLightBrightness(int i1, int i2, int i3) {
		return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(i1, i2, i3)];
	}

	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3D vec3D1, Vec3D vec3D2) {
		return this.func_28105_a(vec3D1, vec3D2, false, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3D vec3D1, Vec3D vec3D2, boolean z3) {
		return this.func_28105_a(vec3D1, vec3D2, z3, false);
	}

	public MovingObjectPosition func_28105_a(Vec3D vec3D1, Vec3D vec3D2, boolean z3, boolean z4) {
		if(!Double.isNaN(vec3D1.xCoord) && !Double.isNaN(vec3D1.yCoord) && !Double.isNaN(vec3D1.zCoord)) {
			if(!Double.isNaN(vec3D2.xCoord) && !Double.isNaN(vec3D2.yCoord) && !Double.isNaN(vec3D2.zCoord)) {
				int i5 = MathHelper.floor_double(vec3D2.xCoord);
				int i6 = MathHelper.floor_double(vec3D2.yCoord);
				int i7 = MathHelper.floor_double(vec3D2.zCoord);
				int i8 = MathHelper.floor_double(vec3D1.xCoord);
				int i9 = MathHelper.floor_double(vec3D1.yCoord);
				int i10 = MathHelper.floor_double(vec3D1.zCoord);
				int i11 = this.getBlockId(i8, i9, i10);
				int i12 = this.getBlockMetadata(i8, i9, i10);
				Block block13 = Block.blocksList[i11];
				if((!z4 || block13 == null || block13.getCollisionBoundingBoxFromPool(this, i8, i9, i10) != null) && i11 > 0 && block13.canCollideCheck(i12, z3)) {
					MovingObjectPosition movingObjectPosition14 = block13.collisionRayTrace(this, i8, i9, i10, vec3D1, vec3D2);
					if(movingObjectPosition14 != null) {
						return movingObjectPosition14;
					}
				}

				i11 = 200;

				while(i11-- >= 0) {
					if(Double.isNaN(vec3D1.xCoord) || Double.isNaN(vec3D1.yCoord) || Double.isNaN(vec3D1.zCoord)) {
						return null;
					}

					if(i8 == i5 && i9 == i6 && i10 == i7) {
						return null;
					}

					boolean z39 = true;
					boolean z40 = true;
					boolean z41 = true;
					double d15 = 999.0D;
					double d17 = 999.0D;
					double d19 = 999.0D;
					if(i5 > i8) {
						d15 = (double)i8 + 1.0D;
					} else if(i5 < i8) {
						d15 = (double)i8 + 0.0D;
					} else {
						z39 = false;
					}

					if(i6 > i9) {
						d17 = (double)i9 + 1.0D;
					} else if(i6 < i9) {
						d17 = (double)i9 + 0.0D;
					} else {
						z40 = false;
					}

					if(i7 > i10) {
						d19 = (double)i10 + 1.0D;
					} else if(i7 < i10) {
						d19 = (double)i10 + 0.0D;
					} else {
						z41 = false;
					}

					double d21 = 999.0D;
					double d23 = 999.0D;
					double d25 = 999.0D;
					double d27 = vec3D2.xCoord - vec3D1.xCoord;
					double d29 = vec3D2.yCoord - vec3D1.yCoord;
					double d31 = vec3D2.zCoord - vec3D1.zCoord;
					if(z39) {
						d21 = (d15 - vec3D1.xCoord) / d27;
					}

					if(z40) {
						d23 = (d17 - vec3D1.yCoord) / d29;
					}

					if(z41) {
						d25 = (d19 - vec3D1.zCoord) / d31;
					}

					boolean z33 = false;
					byte b42;
					if(d21 < d23 && d21 < d25) {
						if(i5 > i8) {
							b42 = 4;
						} else {
							b42 = 5;
						}

						vec3D1.xCoord = d15;
						vec3D1.yCoord += d29 * d21;
						vec3D1.zCoord += d31 * d21;
					} else if(d23 < d25) {
						if(i6 > i9) {
							b42 = 0;
						} else {
							b42 = 1;
						}

						vec3D1.xCoord += d27 * d23;
						vec3D1.yCoord = d17;
						vec3D1.zCoord += d31 * d23;
					} else {
						if(i7 > i10) {
							b42 = 2;
						} else {
							b42 = 3;
						}

						vec3D1.xCoord += d27 * d25;
						vec3D1.yCoord += d29 * d25;
						vec3D1.zCoord = d19;
					}

					Vec3D vec3D34 = Vec3D.createVector(vec3D1.xCoord, vec3D1.yCoord, vec3D1.zCoord);
					i8 = (int)(vec3D34.xCoord = (double)MathHelper.floor_double(vec3D1.xCoord));
					if(b42 == 5) {
						--i8;
						++vec3D34.xCoord;
					}

					i9 = (int)(vec3D34.yCoord = (double)MathHelper.floor_double(vec3D1.yCoord));
					if(b42 == 1) {
						--i9;
						++vec3D34.yCoord;
					}

					i10 = (int)(vec3D34.zCoord = (double)MathHelper.floor_double(vec3D1.zCoord));
					if(b42 == 3) {
						--i10;
						++vec3D34.zCoord;
					}

					int i35 = this.getBlockId(i8, i9, i10);
					int i36 = this.getBlockMetadata(i8, i9, i10);
					Block block37 = Block.blocksList[i35];
					if((!z4 || block37 == null || block37.getCollisionBoundingBoxFromPool(this, i8, i9, i10) != null) && i35 > 0 && block37.canCollideCheck(i36, z3)) {
						MovingObjectPosition movingObjectPosition38 = block37.collisionRayTrace(this, i8, i9, i10, vec3D1, vec3D2);
						if(movingObjectPosition38 != null) {
							return movingObjectPosition38;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSoundAtEntity(Entity entity1, String string2, float f3, float f4) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playSound(string2, entity1.posX, entity1.posY - (double)entity1.yOffset, entity1.posZ, f3, f4);
		}

	}

	public void playSoundEffect(double d1, double d3, double d5, String string7, float f8, float f9) {
		for(int i10 = 0; i10 < this.worldAccesses.size(); ++i10) {
			((IWorldAccess)this.worldAccesses.get(i10)).playSound(string7, d1, d3, d5, f8, f9);
		}

	}

	public void playRecord(String string1, int i2, int i3, int i4) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).playRecord(string1, i2, i3, i4);
		}

	}

	public void spawnParticle(String string1, double d2, double d4, double d6, double d8, double d10, double d12) {
		for(int i14 = 0; i14 < this.worldAccesses.size(); ++i14) {
			((IWorldAccess)this.worldAccesses.get(i14)).spawnParticle(string1, d2, d4, d6, d8, d10, d12);
		}

	}

	public boolean addWeatherEffect(Entity entity1) {
		this.weatherEffects.add(entity1);
		return true;
	}

	public boolean entityJoinedWorld(Entity entity1) {
		int i2 = MathHelper.floor_double(entity1.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity1.posZ / 16.0D);
		boolean z4 = false;
		if(entity1 instanceof EntityPlayer) {
			z4 = true;
		}

		if(!z4 && !this.chunkExists(i2, i3)) {
			return false;
		} else {
			if(entity1 instanceof EntityPlayer) {
				EntityPlayer entityPlayer5 = (EntityPlayer)entity1;
				this.playerEntities.add(entityPlayer5);
				this.updateAllPlayersSleepingFlag();
			}

			this.getChunkFromChunkCoords(i2, i3).addEntity(entity1);
			this.loadedEntityList.add(entity1);
			this.obtainEntitySkin(entity1);
			return true;
		}
	}

	protected void obtainEntitySkin(Entity entity1) {
		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).obtainEntitySkin(entity1);
		}

	}

	protected void releaseEntitySkin(Entity entity1) {
		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).releaseEntitySkin(entity1);
		}

	}

	public void setEntityDead(Entity entity1) {
		if(entity1.riddenByEntity != null) {
			entity1.riddenByEntity.mountEntity((Entity)null);
		}

		if(entity1.ridingEntity != null) {
			entity1.mountEntity((Entity)null);
		}

		entity1.setEntityDead();
		if(entity1 instanceof EntityPlayer) {
			this.playerEntities.remove((EntityPlayer)entity1);
			this.updateAllPlayersSleepingFlag();
		}

	}

	public void addWorldAccess(IWorldAccess iWorldAccess1) {
		this.worldAccesses.add(iWorldAccess1);
	}

	public void removeWorldAccess(IWorldAccess iWorldAccess1) {
		this.worldAccesses.remove(iWorldAccess1);
	}

	public List getCollidingBoundingBoxes(Entity entity1, AxisAlignedBB axisAlignedBB2) {
		this.collidingBoundingBoxes.clear();
		int i3 = MathHelper.floor_double(axisAlignedBB2.minX);
		int i4 = MathHelper.floor_double(axisAlignedBB2.maxX + 1.0D);
		int i5 = MathHelper.floor_double(axisAlignedBB2.minY);
		int i6 = MathHelper.floor_double(axisAlignedBB2.maxY + 1.0D);
		int i7 = MathHelper.floor_double(axisAlignedBB2.minZ);
		int i8 = MathHelper.floor_double(axisAlignedBB2.maxZ + 1.0D);

		for(int i9 = i3; i9 < i4; ++i9) {
			for(int i10 = i7; i10 < i8; ++i10) {
				if(this.blockExists(i9, 64, i10)) {
					for(int i11 = i5 - 1; i11 < i6; ++i11) {
						Block block12 = Block.blocksList[this.getBlockId(i9, i11, i10)];
						if(block12 != null) {
							if (entity1 instanceof EntityPlayer && block12 instanceof BlockFluid) {
								BlockFluid fluidBlock = (BlockFluid) block12;
								fluidBlock.getCollidingBoundingBoxesPlayer(this, i9, i11, i10, axisAlignedBB2, this.collidingBoundingBoxes);
							} else {
								block12.getCollidingBoundingBoxes(this, i9, i11, i10, axisAlignedBB2, this.collidingBoundingBoxes);
							}
						}
					}
				}
			}
		}

		double d14 = 0.25D;
		List list15 = this.getEntitiesWithinAABBExcludingEntity(entity1, axisAlignedBB2.expand(d14, d14, d14));

		for(int i16 = 0; i16 < list15.size(); ++i16) {
			AxisAlignedBB axisAlignedBB13 = ((Entity)list15.get(i16)).getBoundingBox();
			if(axisAlignedBB13 != null && axisAlignedBB13.intersectsWith(axisAlignedBB2)) {
				this.collidingBoundingBoxes.add(axisAlignedBB13);
			}

			axisAlignedBB13 = entity1.getCollisionBox((Entity)list15.get(i16));
			if(axisAlignedBB13 != null && axisAlignedBB13.intersectsWith(axisAlignedBB2)) {
				this.collidingBoundingBoxes.add(axisAlignedBB13);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public int calculateSkylightSubtracted(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = 1.0F - (MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		f3 = 1.0F - f3;
		f3 = (float)((double)f3 * (1.0D - (double)(this.func_27162_g(f1) * 5.0F) / 16.0D));
		f3 = (float)((double)f3 * (1.0D - (double)(this.func_27166_f(f1) * 5.0F) / 16.0D));
		f3 = 1.0F - f3;
		return (int)(f3 * 11.0F);
	}

	public Vec3D func_4079_a(Entity entity1, float f2) {
		float f3 = this.getCelestialAngle(f2);
		float f4 = MathHelper.cos(f3 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f4 < 0.0F) {
			f4 = 0.0F;
		}

		if(f4 > 1.0F) {
			f4 = 1.0F;
		}

		int i5 = MathHelper.floor_double(entity1.posX);
		int i6 = MathHelper.floor_double(entity1.posZ);
		float f7 = (float)this.getWorldChunkManager().getTemperature(i5, i6);
		int i8 = this.getWorldChunkManager().getBiomeGenAt(i5, i6).getSkyColorByTemp(f7);
		float f9 = (float)(i8 >> 16 & 255) / 255.0F;
		float f10 = (float)(i8 >> 8 & 255) / 255.0F;
		float f11 = (float)(i8 & 255) / 255.0F;
		f9 *= f4;
		f10 *= f4;
		f11 *= f4;
		float f12 = this.func_27162_g(f2);
		float f13;
		float f14;
		if(f12 > 0.0F) {
			f13 = (f9 * 0.3F + f10 * 0.59F + f11 * 0.11F) * 0.6F;
			f14 = 1.0F - f12 * 0.75F;
			f9 = f9 * f14 + f13 * (1.0F - f14);
			f10 = f10 * f14 + f13 * (1.0F - f14);
			f11 = f11 * f14 + f13 * (1.0F - f14);
		}

		f13 = this.func_27166_f(f2);
		if(f13 > 0.0F) {
			f14 = (f9 * 0.3F + f10 * 0.59F + f11 * 0.11F) * 0.2F;
			float f15 = 1.0F - f13 * 0.75F;
			f9 = f9 * f15 + f14 * (1.0F - f15);
			f10 = f10 * f15 + f14 * (1.0F - f15);
			f11 = f11 * f15 + f14 * (1.0F - f15);
		}

		if(this.field_27172_i > 0) {
			f14 = (float)this.field_27172_i - f2;
			if(f14 > 1.0F) {
				f14 = 1.0F;
			}

			f14 *= 0.45F;
			f9 = f9 * (1.0F - f14) + 0.8F * f14;
			f10 = f10 * (1.0F - f14) + 0.8F * f14;
			f11 = f11 * (1.0F - f14) + 1.0F * f14;
		}

		return Vec3D.createVector((double)f9, (double)f10, (double)f11);
	}

	public float getCelestialAngle(float f1) {
		return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), f1);
	}

	public Vec3D func_628_d(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		float f4 = (float)(this.field_1019_F >> 16 & 255L) / 255.0F;
		float f5 = (float)(this.field_1019_F >> 8 & 255L) / 255.0F;
		float f6 = (float)(this.field_1019_F & 255L) / 255.0F;
		float f7 = this.func_27162_g(f1);
		float f8;
		float f9;
		if(f7 > 0.0F) {
			f8 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.6F;
			f9 = 1.0F - f7 * 0.95F;
			f4 = f4 * f9 + f8 * (1.0F - f9);
			f5 = f5 * f9 + f8 * (1.0F - f9);
			f6 = f6 * f9 + f8 * (1.0F - f9);
		}

		f4 *= f3 * 0.9F + 0.1F;
		f5 *= f3 * 0.9F + 0.1F;
		f6 *= f3 * 0.85F + 0.15F;
		f8 = this.func_27166_f(f1);
		if(f8 > 0.0F) {
			f9 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.2F;
			float f10 = 1.0F - f8 * 0.95F;
			f4 = f4 * f10 + f9 * (1.0F - f10);
			f5 = f5 * f10 + f9 * (1.0F - f10);
			f6 = f6 * f10 + f9 * (1.0F - f10);
		}

		return Vec3D.createVector((double)f4, (double)f5, (double)f6);
	}

	public Vec3D getFogColor(float f1) {
		float f2 = this.getCelestialAngle(f1);
		return this.worldProvider.func_4096_a(f2, f1);
	}

	public int findTopSolidBlock(int i1, int i2) {
		Chunk chunk3 = this.getChunkFromBlockCoords(i1, i2);
		int i4 = 127;
		i1 &= 15;

		for(i2 &= 15; i4 > 0; --i4) {
			int i5 = chunk3.getBlockID(i1, i4, i2);
			Material material6 = i5 == 0 ? Material.air : Block.blocksList[i5].blockMaterial;
			if(material6.getIsSolid() || material6.getIsLiquid()) {
				return i4 + 1;
			}
		}

		return -1;
	}

	public float getStarBrightness(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = 1.0F - (MathHelper.cos(f2 * (float)Math.PI * 2.0F) * 2.0F + 0.75F);
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		return f3 * f3 * 0.5F;
	}

	public void scheduleBlockUpdate(int i1, int i2, int i3, int i4, int i5) {
		NextTickListEntry nextTickListEntry6 = new NextTickListEntry(i1, i2, i3, i4);
		byte b7 = 8;
		if(this.scheduledUpdatesAreImmediate) {
			if(this.checkChunksExist(nextTickListEntry6.xCoord - b7, nextTickListEntry6.yCoord - b7, nextTickListEntry6.zCoord - b7, nextTickListEntry6.xCoord + b7, nextTickListEntry6.yCoord + b7, nextTickListEntry6.zCoord + b7)) {
				int i8 = this.getBlockId(nextTickListEntry6.xCoord, nextTickListEntry6.yCoord, nextTickListEntry6.zCoord);
				if(i8 == nextTickListEntry6.blockID && i8 > 0) {
					Block.blocksList[i8].updateTick(this, nextTickListEntry6.xCoord, nextTickListEntry6.yCoord, nextTickListEntry6.zCoord, this.rand);
				}
			}

		} else {
			if(this.checkChunksExist(i1 - b7, i2 - b7, i3 - b7, i1 + b7, i2 + b7, i3 + b7)) {
				if(i4 > 0) {
					nextTickListEntry6.setScheduledTime((long)i5 + this.worldInfo.getWorldTime());
				}

				if(!this.scheduledTickSet.contains(nextTickListEntry6)) {
					this.scheduledTickSet.add(nextTickListEntry6);
					this.scheduledTickTreeSet.add(nextTickListEntry6);
				}
			}

		}
	}

	public void updateEntities() {
		int i1;
		Entity entity2;
		for(i1 = 0; i1 < this.weatherEffects.size(); ++i1) {
			entity2 = (Entity)this.weatherEffects.get(i1);
			entity2.onUpdate();
			if(entity2.isDead) {
				this.weatherEffects.remove(i1--);
			}
		}

		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int i3;
		int i4;
		for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
			entity2 = (Entity)this.unloadedEntityList.get(i1);
			i3 = entity2.chunkCoordX;
			i4 = entity2.chunkCoordZ;
			if(entity2.addedToChunk && this.chunkExists(i3, i4)) {
				this.getChunkFromChunkCoords(i3, i4).removeEntity(entity2);
			}
		}

		for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(i1));
		}

		this.unloadedEntityList.clear();

		for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			entity2 = (Entity)this.loadedEntityList.get(i1);
			if(entity2.ridingEntity != null) {
				if(!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
					continue;
				}

				entity2.ridingEntity.riddenByEntity = null;
				entity2.ridingEntity = null;
			}

			if(!entity2.isDead) {
				this.updateEntity(entity2);
			}

			if(entity2.isDead) {
				i3 = entity2.chunkCoordX;
				i4 = entity2.chunkCoordZ;
				if(entity2.addedToChunk && this.chunkExists(i3, i4)) {
					this.getChunkFromChunkCoords(i3, i4).removeEntity(entity2);
				}

				this.loadedEntityList.remove(i1--);
				this.releaseEntitySkin(entity2);
			}
		}

		this.field_31055_L = true;
		Iterator iterator10 = this.loadedTileEntityList.iterator();

		while(iterator10.hasNext()) {
			TileEntity tileEntity5 = (TileEntity)iterator10.next();
			if(!tileEntity5.func_31006_g()) {
				tileEntity5.updateEntity();
			}

			if(tileEntity5.func_31006_g()) {
				iterator10.remove();
				Chunk chunk7 = this.getChunkFromChunkCoords(tileEntity5.xCoord >> 4, tileEntity5.zCoord >> 4);
				if(chunk7 != null) {
					chunk7.removeChunkBlockTileEntity(tileEntity5.xCoord & 15, tileEntity5.yCoord, tileEntity5.zCoord & 15);
				}
			}
		}

		this.field_31055_L = false;
		if(!this.field_30900_E.isEmpty()) {
			Iterator iterator6 = this.field_30900_E.iterator();

			while(iterator6.hasNext()) {
				TileEntity tileEntity8 = (TileEntity)iterator6.next();
				if(!tileEntity8.func_31006_g()) {
					if(!this.loadedTileEntityList.contains(tileEntity8)) {
						this.loadedTileEntityList.add(tileEntity8);
					}

					Chunk chunk9 = this.getChunkFromChunkCoords(tileEntity8.xCoord >> 4, tileEntity8.zCoord >> 4);
					if(chunk9 != null) {
						chunk9.setChunkBlockTileEntity(tileEntity8.xCoord & 15, tileEntity8.yCoord, tileEntity8.zCoord & 15, tileEntity8);
					}

					this.markBlockNeedsUpdate(tileEntity8.xCoord, tileEntity8.yCoord, tileEntity8.zCoord);
				}
			}

			this.field_30900_E.clear();
		}

	}

	public void func_31054_a(Collection collection1) {
		if(this.field_31055_L) {
			this.field_30900_E.addAll(collection1);
		} else {
			this.loadedTileEntityList.addAll(collection1);
		}

	}

	public void updateEntity(Entity entity1) {
		this.updateEntityWithOptionalForce(entity1, true);
	}

	public void updateEntityWithOptionalForce(Entity entity1, boolean z2) {
		int i3 = MathHelper.floor_double(entity1.posX);
		int i4 = MathHelper.floor_double(entity1.posZ);
		byte b5 = 32;
		if(!z2 || this.checkChunksExist(i3 - b5, 0, i4 - b5, i3 + b5, 128, i4 + b5)) {
			entity1.lastTickPosX = entity1.posX;
			entity1.lastTickPosY = entity1.posY;
			entity1.lastTickPosZ = entity1.posZ;
			entity1.prevRotationYaw = entity1.rotationYaw;
			entity1.prevRotationPitch = entity1.rotationPitch;
			if(z2 && entity1.addedToChunk) {
				if(entity1.ridingEntity != null) {
					entity1.updateRidden();
				} else {
					entity1.onUpdate();
				}
			}

			if(Double.isNaN(entity1.posX) || Double.isInfinite(entity1.posX)) {
				entity1.posX = entity1.lastTickPosX;
			}

			if(Double.isNaN(entity1.posY) || Double.isInfinite(entity1.posY)) {
				entity1.posY = entity1.lastTickPosY;
			}

			if(Double.isNaN(entity1.posZ) || Double.isInfinite(entity1.posZ)) {
				entity1.posZ = entity1.lastTickPosZ;
			}

			if(Double.isNaN((double)entity1.rotationPitch) || Double.isInfinite((double)entity1.rotationPitch)) {
				entity1.rotationPitch = entity1.prevRotationPitch;
			}

			if(Double.isNaN((double)entity1.rotationYaw) || Double.isInfinite((double)entity1.rotationYaw)) {
				entity1.rotationYaw = entity1.prevRotationYaw;
			}

			int i6 = MathHelper.floor_double(entity1.posX / 16.0D);
			int i7 = MathHelper.floor_double(entity1.posY / 16.0D);
			int i8 = MathHelper.floor_double(entity1.posZ / 16.0D);
			if(!entity1.addedToChunk || entity1.chunkCoordX != i6 || entity1.chunkCoordY != i7 || entity1.chunkCoordZ != i8) {
				if(entity1.addedToChunk && this.chunkExists(entity1.chunkCoordX, entity1.chunkCoordZ)) {
					this.getChunkFromChunkCoords(entity1.chunkCoordX, entity1.chunkCoordZ).removeEntityAtIndex(entity1, entity1.chunkCoordY);
				}

				if(this.chunkExists(i6, i8)) {
					entity1.addedToChunk = true;
					this.getChunkFromChunkCoords(i6, i8).addEntity(entity1);
				} else {
					entity1.addedToChunk = false;
				}
			}

			if(z2 && entity1.addedToChunk && entity1.riddenByEntity != null) {
				if(!entity1.riddenByEntity.isDead && entity1.riddenByEntity.ridingEntity == entity1) {
					this.updateEntity(entity1.riddenByEntity);
				} else {
					entity1.riddenByEntity.ridingEntity = null;
					entity1.riddenByEntity = null;
				}
			}

		}
	}

	public boolean checkIfAABBIsClear(AxisAlignedBB axisAlignedBB1) {
		List list2 = this.getEntitiesWithinAABBExcludingEntity((Entity)null, axisAlignedBB1);

		for(int i3 = 0; i3 < list2.size(); ++i3) {
			Entity entity4 = (Entity)list2.get(i3);
			if(!entity4.isDead && entity4.preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public boolean getIsAnyLiquid(AxisAlignedBB axisAlignedBB1) {
		int i2 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i3 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i4 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i5 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i6 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i7 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);
		if(axisAlignedBB1.minX < 0.0D) {
			--i2;
		}

		if(axisAlignedBB1.minY < 0.0D) {
			--i4;
		}

		if(axisAlignedBB1.minZ < 0.0D) {
			--i6;
		}

		for(int i8 = i2; i8 < i3; ++i8) {
			for(int i9 = i4; i9 < i5; ++i9) {
				for(int i10 = i6; i10 < i7; ++i10) {
					Block block11 = Block.blocksList[this.getBlockId(i8, i9, i10)];
					if(block11 != null && block11.blockMaterial.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB axisAlignedBB1) {
		int i2 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i3 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i4 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i5 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i6 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i7 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);
		if(this.checkChunksExist(i2, i4, i6, i3, i5, i7)) {
			for(int i8 = i2; i8 < i3; ++i8) {
				for(int i9 = i4; i9 < i5; ++i9) {
					for(int i10 = i6; i10 < i7; ++i10) {
						int i11 = this.getBlockId(i8, i9, i10);
						if(i11 == Block.fire.blockID || i11 == Block.lavaMoving.blockID || i11 == Block.lavaStill.blockID) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public boolean handleMaterialAcceleration(AxisAlignedBB axisAlignedBB1, Material material2, Entity entity3) {
		int i4 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i5 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i6 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i7 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i8 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i9 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);
		if(!this.checkChunksExist(i4, i6, i8, i5, i7, i9)) {
			return false;
		} else {
			boolean z10 = false;
			Vec3D vec3D11 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

			for(int i12 = i4; i12 < i5; ++i12) {
				for(int i13 = i6; i13 < i7; ++i13) {
					for(int i14 = i8; i14 < i9; ++i14) {
						Block block15 = Block.blocksList[this.getBlockId(i12, i13, i14)];
						if(block15 != null && block15.blockMaterial == material2) {
							double d16 = (double)((float)(i13 + 1) - BlockFluid.getPercentAir(this.getBlockMetadata(i12, i13, i14)));
							if((double)i7 >= d16) {
								z10 = true;
								block15.velocityToAddToEntity(this, i12, i13, i14, entity3, vec3D11);
							}
						}
					}
				}
			}

			if(vec3D11.lengthVector() > 0.0D) {
				vec3D11 = vec3D11.normalize();
				double d18 = 0.014D;
				entity3.motionX += vec3D11.xCoord * d18;
				entity3.motionY += vec3D11.yCoord * d18;
				entity3.motionZ += vec3D11.zCoord * d18;
			}

			return z10;
		}
	}

	public boolean isMaterialInBB(AxisAlignedBB axisAlignedBB1, Material material2) {
		int i3 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i4 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i5 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i6 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i7 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i8 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);

		for(int i9 = i3; i9 < i4; ++i9) {
			for(int i10 = i5; i10 < i6; ++i10) {
				for(int i11 = i7; i11 < i8; ++i11) {
					Block block12 = Block.blocksList[this.getBlockId(i9, i10, i11)];
					if(block12 != null && block12.blockMaterial == material2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAABBInMaterial(AxisAlignedBB axisAlignedBB1, Material material2) {
		int i3 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i4 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i5 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i6 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i7 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i8 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);

		for(int i9 = i3; i9 < i4; ++i9) {
			for(int i10 = i5; i10 < i6; ++i10) {
				for(int i11 = i7; i11 < i8; ++i11) {
					Block block12 = Block.blocksList[this.getBlockId(i9, i10, i11)];
					if(block12 != null && block12.blockMaterial == material2) {
						int i13 = this.getBlockMetadata(i9, i10, i11);
						double d14 = (double)(i10 + 1);
						if(i13 < 8) {
							d14 = (double)(i10 + 1) - (double)i13 / 8.0D;
						}

						if(d14 >= axisAlignedBB1.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public Explosion createExplosion(Entity entity1, double d2, double d4, double d6, float f8) {
		return this.newExplosion(entity1, d2, d4, d6, f8, false);
	}

	public Explosion newExplosion(Entity entity1, double d2, double d4, double d6, float f8, boolean z9) {
		Explosion explosion10 = new Explosion(this, entity1, d2, d4, d6, f8);
		explosion10.isFlaming = z9;
		explosion10.doExplosionA();
		explosion10.doExplosionB(true);
		return explosion10;
	}

	public float func_675_a(Vec3D vec3D1, AxisAlignedBB axisAlignedBB2) {
		double d3 = 1.0D / ((axisAlignedBB2.maxX - axisAlignedBB2.minX) * 2.0D + 1.0D);
		double d5 = 1.0D / ((axisAlignedBB2.maxY - axisAlignedBB2.minY) * 2.0D + 1.0D);
		double d7 = 1.0D / ((axisAlignedBB2.maxZ - axisAlignedBB2.minZ) * 2.0D + 1.0D);
		int i9 = 0;
		int i10 = 0;

		for(float f11 = 0.0F; f11 <= 1.0F; f11 = (float)((double)f11 + d3)) {
			for(float f12 = 0.0F; f12 <= 1.0F; f12 = (float)((double)f12 + d5)) {
				for(float f13 = 0.0F; f13 <= 1.0F; f13 = (float)((double)f13 + d7)) {
					double d14 = axisAlignedBB2.minX + (axisAlignedBB2.maxX - axisAlignedBB2.minX) * (double)f11;
					double d16 = axisAlignedBB2.minY + (axisAlignedBB2.maxY - axisAlignedBB2.minY) * (double)f12;
					double d18 = axisAlignedBB2.minZ + (axisAlignedBB2.maxZ - axisAlignedBB2.minZ) * (double)f13;
					if(this.rayTraceBlocks(Vec3D.createVector(d14, d16, d18), vec3D1) == null) {
						++i9;
					}

					++i10;
				}
			}
		}

		return (float)i9 / (float)i10;
	}

	public void onBlockHit(EntityPlayer entityPlayer1, int i2, int i3, int i4, int i5) {
		if(i5 == 0) {
			--i3;
		}

		if(i5 == 1) {
			++i3;
		}

		if(i5 == 2) {
			--i4;
		}

		if(i5 == 3) {
			++i4;
		}

		if(i5 == 4) {
			--i2;
		}

		if(i5 == 5) {
			++i2;
		}

		if(this.getBlockId(i2, i3, i4) == Block.fire.blockID) {
			this.func_28107_a(entityPlayer1, 1004, i2, i3, i4, 0);
			this.setBlockWithNotify(i2, i3, i4, 0);
		}

	}

	public Entity func_4085_a(Class class1) {
		return null;
	}

	public String func_687_d() {
		return "All: " + this.loadedEntityList.size();
	}

	public String func_21119_g() {
		return this.chunkProvider.makeString();
	}

	public TileEntity getBlockTileEntity(int i1, int i2, int i3) {
		Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
		return chunk4 != null ? chunk4.getChunkBlockTileEntity(i1 & 15, i2, i3 & 15) : null;
	}

	public void setBlockTileEntity(int i1, int i2, int i3, TileEntity tileEntity4) {
		if(!tileEntity4.func_31006_g()) {
			if(this.field_31055_L) {
				tileEntity4.xCoord = i1;
				tileEntity4.yCoord = i2;
				tileEntity4.zCoord = i3;
				this.field_30900_E.add(tileEntity4);
			} else {
				this.loadedTileEntityList.add(tileEntity4);
				Chunk chunk5 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				if(chunk5 != null) {
					chunk5.setChunkBlockTileEntity(i1 & 15, i2, i3 & 15, tileEntity4);
				}
			}
		}

	}

	public void removeBlockTileEntity(int i1, int i2, int i3) {
		TileEntity tileEntity4 = this.getBlockTileEntity(i1, i2, i3);
		if(tileEntity4 != null && this.field_31055_L) {
			tileEntity4.func_31005_i();
		} else {
			if(tileEntity4 != null) {
				this.loadedTileEntityList.remove(tileEntity4);
			}

			Chunk chunk5 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
			if(chunk5 != null) {
				chunk5.removeChunkBlockTileEntity(i1 & 15, i2, i3 & 15);
			}
		}

	}

	public boolean isBlockOpaqueCube(int i1, int i2, int i3) {
		Block block4 = Block.blocksList[this.getBlockId(i1, i2, i3)];
		return block4 == null ? false : block4.isOpaqueCube();
	}

	public boolean isBlockNormalCube(int i1, int i2, int i3) {
		Block block4 = Block.blocksList[this.getBlockId(i1, i2, i3)];
		return block4 == null ? false : block4.blockMaterial.getIsTranslucent() && block4.renderAsNormalBlock();
	}

	public void saveWorldIndirectly(IProgressUpdate iProgressUpdate1) {
		this.saveWorld(true, iProgressUpdate1);
	}

	public boolean updatingLighting() {
		if(this.lightingUpdatesCounter >= 50) {
			return false;
		} else {
			++this.lightingUpdatesCounter;

			try {
				int i1 = 500;

				boolean z2;
				while(this.lightingToUpdate.size() > 0) {
					--i1;
					if(i1 <= 0) {
						z2 = true;
						return z2;
					}

					((MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1)).func_4127_a(this);
				}

				z2 = false;
				return z2;
			} finally {
				--this.lightingUpdatesCounter;
			}
		}
	}

	public void scheduleLightingUpdate(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5, int i6, int i7) {
		this.scheduleLightingUpdate_do(enumSkyBlock1, i2, i3, i4, i5, i6, i7, true);
	}

	public void scheduleLightingUpdate_do(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5, int i6, int i7, boolean z8) {
		if(!this.worldProvider.hasNoSky || enumSkyBlock1 != EnumSkyBlock.Sky) {
			++lightingUpdatesScheduled;

			try {
				if(lightingUpdatesScheduled == 50) {
					return;
				}

				int i9 = (i5 + i2) / 2;
				int i10 = (i7 + i4) / 2;
				if(!this.blockExists(i9, 64, i10)) {
					return;
				}

				if(!this.getChunkFromBlockCoords(i9, i10).func_21167_h()) {
					int i11 = this.lightingToUpdate.size();
					int i12;
					if(z8) {
						i12 = 5;
						if(i12 > i11) {
							i12 = i11;
						}

						for(int i13 = 0; i13 < i12; ++i13) {
							MetadataChunkBlock metadataChunkBlock14 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - i13 - 1);
							if(metadataChunkBlock14.field_1299_a == enumSkyBlock1 && metadataChunkBlock14.func_866_a(i2, i3, i4, i5, i6, i7)) {
								return;
							}
						}
					}

					this.lightingToUpdate.add(new MetadataChunkBlock(enumSkyBlock1, i2, i3, i4, i5, i6, i7));
					i12 = 1000000;
					if(this.lightingToUpdate.size() > 1000000) {
						System.out.println("More than " + i12 + " updates, aborting lighting updates");
						this.lightingToUpdate.clear();
					}

					return;
				}
			} finally {
				--lightingUpdatesScheduled;
			}

		}
	}

	public void calculateInitialSkylight() {
		int i1 = this.calculateSkylightSubtracted(1.0F);
		if(i1 != this.skylightSubtracted) {
			this.skylightSubtracted = i1;
		}

	}

	public void setAllowedMobSpawns(boolean z1, boolean z2) {
		this.spawnHostileMobs = z1;
		this.spawnPeacefulMobs = z2;
	}

	public void tick() {
		this.updateWeather();
		long j2;
		if(this.isAllPlayersFullyAsleep()) {
			boolean z1 = false;
			if(this.spawnHostileMobs && this.difficultySetting >= 1) {
				z1 = SpawnerAnimals.performSleepSpawning(this, this.playerEntities);
			}

			if(!z1) {
				j2 = this.worldInfo.getWorldTime() + 24000L;
				this.worldInfo.setWorldTime(j2 - j2 % 24000L);
				this.wakeUpAllPlayers();
			}
		}

		SpawnerAnimals.performSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs);
		this.chunkProvider.unload100OldestChunks();
		int i4 = this.calculateSkylightSubtracted(1.0F);
		if(i4 != this.skylightSubtracted) {
			this.skylightSubtracted = i4;

			for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
				((IWorldAccess)this.worldAccesses.get(i5)).updateAllRenderers();
			}
		}

		j2 = this.worldInfo.getWorldTime() + 1L;
		if(j2 % (long)this.autosavePeriod == 0L) {
			this.saveWorld(false, (IProgressUpdate)null);
		}

		this.worldInfo.setWorldTime(j2);
		this.TickUpdates(false);
		this.updateBlocksAndPlayCaveSounds();
	}

	private void func_27163_E() {
		if(this.worldInfo.getRaining()) {
			this.rainingStrength = 1.0F;
			if(this.worldInfo.getThundering()) {
				this.thunderingStrength = 1.0F;
			}
		}

	}

	protected void updateWeather() {
		if(!this.worldProvider.hasNoSky) {
			if(this.field_27168_F > 0) {
				--this.field_27168_F;
			}

			int i1 = this.worldInfo.getThunderTime();
			if(i1 <= 0) {
				if(this.worldInfo.getThundering()) {
					this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
				} else {
					this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
				}
			} else {
				--i1;
				this.worldInfo.setThunderTime(i1);
				if(i1 <= 0) {
					this.worldInfo.setThundering(!this.worldInfo.getThundering());
				}
			}

			int i2 = this.worldInfo.getRainTime();
			if(i2 <= 0) {
				if(this.worldInfo.getRaining()) {
					this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
				} else {
					this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
				}
			} else {
				--i2;
				this.worldInfo.setRainTime(i2);
				if(i2 <= 0) {
					this.worldInfo.setRaining(!this.worldInfo.getRaining());
				}
			}

			this.prevRainingStrength = this.rainingStrength;
			if(this.worldInfo.getRaining()) {
				this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
			} else {
				this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
			}

			if(this.rainingStrength < 0.0F) {
				this.rainingStrength = 0.0F;
			}

			if(this.rainingStrength > 1.0F) {
				this.rainingStrength = 1.0F;
			}

			this.prevThunderingStrength = this.thunderingStrength;
			if(this.worldInfo.getThundering()) {
				this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
			} else {
				this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
			}

			if(this.thunderingStrength < 0.0F) {
				this.thunderingStrength = 0.0F;
			}

			if(this.thunderingStrength > 1.0F) {
				this.thunderingStrength = 1.0F;
			}

		}
	}

	private void stopPrecipitation() {
		this.worldInfo.setRainTime(0);
		this.worldInfo.setRaining(false);
		this.worldInfo.setThunderTime(0);
		this.worldInfo.setThundering(false);
	}

	protected void updateBlocksAndPlayCaveSounds() {
		this.positionsToUpdate.clear();

		int i3;
		int i4;
		int i6;
		int i7;
		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			EntityPlayer entityPlayer2 = (EntityPlayer)this.playerEntities.get(i1);
			i3 = MathHelper.floor_double(entityPlayer2.posX / 16.0D);
			i4 = MathHelper.floor_double(entityPlayer2.posZ / 16.0D);
			byte b5 = 9;

			for(i6 = -b5; i6 <= b5; ++i6) {
				for(i7 = -b5; i7 <= b5; ++i7) {
					this.positionsToUpdate.add(new ChunkCoordIntPair(i6 + i3, i7 + i4));
				}
			}
		}

		if(this.soundCounter > 0) {
			--this.soundCounter;
		}

		Iterator iterator12 = this.positionsToUpdate.iterator();

		while(iterator12.hasNext()) {
			ChunkCoordIntPair chunkCoordIntPair13 = (ChunkCoordIntPair)iterator12.next();
			i3 = chunkCoordIntPair13.chunkXPos * 16;
			i4 = chunkCoordIntPair13.chunkZPos * 16;
			Chunk chunk14 = this.getChunkFromChunkCoords(chunkCoordIntPair13.chunkXPos, chunkCoordIntPair13.chunkZPos);
			int i8;
			int i9;
			int i10;
			if(this.soundCounter == 0) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				i6 = this.field_9437_g >> 2;
				i7 = i6 & 15;
				i8 = i6 >> 8 & 15;
				i9 = i6 >> 16 & 127;
				i10 = chunk14.getBlockID(i7, i9, i8);
				i7 += i3;
				i8 += i4;
				if(i10 == 0 && this.getFullBlockLightValue(i7, i9, i8) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, i7, i9, i8) <= 0) {
					EntityPlayer entityPlayer11 = this.getClosestPlayer((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D, 8.0D);
					if(entityPlayer11 != null && entityPlayer11.getDistanceSq((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D) > 4.0D) {
						this.playSoundEffect((double)i7 + 0.5D, (double)i9 + 0.5D, (double)i8 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
						this.soundCounter = this.rand.nextInt(12000) + 6000;
					}
				}
			}

			if(this.rand.nextInt(100000) == 0 && this.func_27161_C() && this.func_27160_B()) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				i6 = this.field_9437_g >> 2;
				i7 = i3 + (i6 & 15);
				i8 = i4 + (i6 >> 8 & 15);
				i9 = this.findTopSolidBlock(i7, i8);
				if(this.canBlockBeRainedOn(i7, i9, i8)) {
					this.addWeatherEffect(new EntityLightningBolt(this, (double)i7, (double)i9, (double)i8));
					this.field_27168_F = 2;
				}
			}

			int i15;
			if(this.rand.nextInt(16) == 0) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				i6 = this.field_9437_g >> 2;
				i7 = i6 & 15;
				i8 = i6 >> 8 & 15;
				i9 = this.findTopSolidBlock(i7 + i3, i8 + i4);
				if(this.getWorldChunkManager().getBiomeGenAt(i7 + i3, i8 + i4).getEnableSnow() && i9 >= 0 && i9 < 128 && chunk14.getSavedLightValue(EnumSkyBlock.Block, i7, i9, i8) < 10) {
					i10 = chunk14.getBlockID(i7, i9 - 1, i8);
					i15 = chunk14.getBlockID(i7, i9, i8);
					if(this.func_27161_C() && i15 == 0 && Block.snow.canPlaceBlockAt(this, i7 + i3, i9, i8 + i4) && i10 != 0 && i10 != Block.ice.blockID && Block.blocksList[i10].blockMaterial.getIsSolid()) {
						this.setBlockWithNotify(i7 + i3, i9, i8 + i4, Block.snow.blockID);
					}

					if(i10 == Block.waterStill.blockID && chunk14.getBlockMetadata(i7, i9 - 1, i8) == 0) {
						this.setBlockWithNotify(i7 + i3, i9 - 1, i8 + i4, Block.ice.blockID);
					}
				}
			}

			for(i6 = 0; i6 < 80; ++i6) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				i7 = this.field_9437_g >> 2;
				i8 = i7 & 15;
				i9 = i7 >> 8 & 15;
				i10 = i7 >> 16 & 127;
				i15 = chunk14.blocks[i8 << 11 | i9 << 7 | i10] & 255;
				if(Block.tickOnLoad[i15]) {
					Block.blocksList[i15].updateTick(this, i8 + i3, i10, i9 + i4, this.rand);
				}
			}
		}

	}

	public boolean TickUpdates(boolean z1) {
		int i2 = this.scheduledTickTreeSet.size();
		if(i2 != this.scheduledTickSet.size()) {
			throw new IllegalStateException("TickNextTick list out of synch");
		} else {
			if(i2 > 1000) {
				i2 = 1000;
			}

			for(int i3 = 0; i3 < i2; ++i3) {
				NextTickListEntry nextTickListEntry4 = (NextTickListEntry)this.scheduledTickTreeSet.first();
				if(!z1 && nextTickListEntry4.scheduledTime > this.worldInfo.getWorldTime()) {
					break;
				}

				this.scheduledTickTreeSet.remove(nextTickListEntry4);
				this.scheduledTickSet.remove(nextTickListEntry4);
				byte b5 = 8;
				if(this.checkChunksExist(nextTickListEntry4.xCoord - b5, nextTickListEntry4.yCoord - b5, nextTickListEntry4.zCoord - b5, nextTickListEntry4.xCoord + b5, nextTickListEntry4.yCoord + b5, nextTickListEntry4.zCoord + b5)) {
					int i6 = this.getBlockId(nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord);
					if(i6 == nextTickListEntry4.blockID && i6 > 0) {
						Block.blocksList[i6].updateTick(this, nextTickListEntry4.xCoord, nextTickListEntry4.yCoord, nextTickListEntry4.zCoord, this.rand);
					}
				}
			}

			return this.scheduledTickTreeSet.size() != 0;
		}
	}

	public void randomDisplayUpdates(int i1, int i2, int i3) {
		byte b4 = 16;
		Random random5 = new Random();

		for(int i6 = 0; i6 < 1000; ++i6) {
			int i7 = i1 + this.rand.nextInt(b4) - this.rand.nextInt(b4);
			int i8 = i2 + this.rand.nextInt(b4) - this.rand.nextInt(b4);
			int i9 = i3 + this.rand.nextInt(b4) - this.rand.nextInt(b4);
			int i10 = this.getBlockId(i7, i8, i9);
			if(i10 > 0) {
				Block.blocksList[i10].randomDisplayTick(this, i7, i8, i9, random5);
			}
		}

	}

	public List getEntitiesWithinAABBExcludingEntity(Entity entity1, AxisAlignedBB axisAlignedBB2) {
		this.field_1012_M.clear();
		int i3 = MathHelper.floor_double((axisAlignedBB2.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((axisAlignedBB2.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((axisAlignedBB2.maxZ + 2.0D) / 16.0D);

		for(int i7 = i3; i7 <= i4; ++i7) {
			for(int i8 = i5; i8 <= i6; ++i8) {
				if(this.chunkExists(i7, i8)) {
					this.getChunkFromChunkCoords(i7, i8).getEntitiesWithinAABBForEntity(entity1, axisAlignedBB2, this.field_1012_M);
				}
			}
		}

		return this.field_1012_M;
	}

	public List getEntitiesWithinAABB(Class class1, AxisAlignedBB axisAlignedBB2) {
		int i3 = MathHelper.floor_double((axisAlignedBB2.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((axisAlignedBB2.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((axisAlignedBB2.maxZ + 2.0D) / 16.0D);
		ArrayList arrayList7 = new ArrayList();

		for(int i8 = i3; i8 <= i4; ++i8) {
			for(int i9 = i5; i9 <= i6; ++i9) {
				if(this.chunkExists(i8, i9)) {
					this.getChunkFromChunkCoords(i8, i9).getEntitiesOfTypeWithinAAAB(class1, axisAlignedBB2, arrayList7);
				}
			}
		}

		return arrayList7;
	}

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void func_698_b(int i1, int i2, int i3, TileEntity tileEntity4) {
		if(this.blockExists(i1, i2, i3)) {
			this.getChunkFromBlockCoords(i1, i3).setChunkModified();
		}

		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			((IWorldAccess)this.worldAccesses.get(i5)).doNothingWithTileEntity(i1, i2, i3, tileEntity4);
		}

	}

	public int countEntities(Class class1) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.loadedEntityList.size(); ++i3) {
			Entity entity4 = (Entity)this.loadedEntityList.get(i3);
			if(class1.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public void func_636_a(List list1) {
		this.loadedEntityList.addAll(list1);

		for(int i2 = 0; i2 < list1.size(); ++i2) {
			this.obtainEntitySkin((Entity)list1.get(i2));
		}

	}

	public void func_632_b(List list1) {
		this.unloadedEntityList.addAll(list1);
	}

	public void func_656_j() {
		while(this.chunkProvider.unload100OldestChunks()) {
		}

	}

	public boolean canBlockBePlacedAt(int i1, int i2, int i3, int i4, boolean z5, int i6) {
		int i7 = this.getBlockId(i2, i3, i4);
		Block block8 = Block.blocksList[i7];
		Block block9 = Block.blocksList[i1];
		AxisAlignedBB axisAlignedBB10 = block9.getCollisionBoundingBoxFromPool(this, i2, i3, i4);
		if(z5) {
			axisAlignedBB10 = null;
		}

		if(axisAlignedBB10 != null && !this.checkIfAABBIsClear(axisAlignedBB10)) {
			return false;
		} else {
			if(block8 == Block.waterMoving || block8 == Block.waterStill || block8 == Block.lavaMoving || block8 == Block.lavaStill || block8 == Block.fire || block8 == Block.snow) {
				block8 = null;
			}

			return i1 > 0 && block8 == null && block9.canPlaceBlockOnSide(this, i2, i3, i4, i6);
		}
	}

	public PathEntity getPathToEntity(Entity entity1, Entity entity2, float f3) {
		int i4 = MathHelper.floor_double(entity1.posX);
		int i5 = MathHelper.floor_double(entity1.posY);
		int i6 = MathHelper.floor_double(entity1.posZ);
		int i7 = (int)(f3 + 16.0F);
		int i8 = i4 - i7;
		int i9 = i5 - i7;
		int i10 = i6 - i7;
		int i11 = i4 + i7;
		int i12 = i5 + i7;
		int i13 = i6 + i7;
		ChunkCache chunkCache14 = new ChunkCache(this, i8, i9, i10, i11, i12, i13);
		return (new Pathfinder(chunkCache14)).createEntityPathTo(entity1, entity2, f3);
	}

	public PathEntity getEntityPathToXYZ(Entity entity1, int i2, int i3, int i4, float f5) {
		int i6 = MathHelper.floor_double(entity1.posX);
		int i7 = MathHelper.floor_double(entity1.posY);
		int i8 = MathHelper.floor_double(entity1.posZ);
		int i9 = (int)(f5 + 8.0F);
		int i10 = i6 - i9;
		int i11 = i7 - i9;
		int i12 = i8 - i9;
		int i13 = i6 + i9;
		int i14 = i7 + i9;
		int i15 = i8 + i9;
		ChunkCache chunkCache16 = new ChunkCache(this, i10, i11, i12, i13, i14, i15);
		return (new Pathfinder(chunkCache16)).createEntityPathTo(entity1, i2, i3, i4, f5);
	}

	public boolean isBlockProvidingPowerTo(int i1, int i2, int i3, int i4) {
		int i5 = this.getBlockId(i1, i2, i3);
		return i5 == 0 ? false : Block.blocksList[i5].isIndirectlyPoweringTo(this, i1, i2, i3, i4);
	}

	public boolean isBlockGettingPowered(int i1, int i2, int i3) {
		return this.isBlockProvidingPowerTo(i1, i2 - 1, i3, 0) ? true : (this.isBlockProvidingPowerTo(i1, i2 + 1, i3, 1) ? true : (this.isBlockProvidingPowerTo(i1, i2, i3 - 1, 2) ? true : (this.isBlockProvidingPowerTo(i1, i2, i3 + 1, 3) ? true : (this.isBlockProvidingPowerTo(i1 - 1, i2, i3, 4) ? true : this.isBlockProvidingPowerTo(i1 + 1, i2, i3, 5)))));
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int i1, int i2, int i3, int i4) {
		if(this.isBlockNormalCube(i1, i2, i3)) {
			return this.isBlockGettingPowered(i1, i2, i3);
		} else {
			int i5 = this.getBlockId(i1, i2, i3);
			return i5 == 0 ? false : Block.blocksList[i5].isPoweringTo(this, i1, i2, i3, i4);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int i1, int i2, int i3) {
		return this.isBlockIndirectlyProvidingPowerTo(i1, i2 - 1, i3, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1, i2 + 1, i3, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1, i2, i3 - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1, i2, i3 + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1 - 1, i2, i3, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(i1 + 1, i2, i3, 5)))));
	}

	public EntityPlayer getClosestPlayerToEntity(Entity entity1, double d2) {
		return this.getClosestPlayer(entity1.posX, entity1.posY, entity1.posZ, d2);
	}

	public EntityPlayer getClosestPlayer(double d1, double d3, double d5, double d7) {
		double d9 = -1.0D;
		EntityPlayer entityPlayer11 = null;

		for(int i12 = 0; i12 < this.playerEntities.size(); ++i12) {
			EntityPlayer entityPlayer13 = (EntityPlayer)this.playerEntities.get(i12);
			double d14 = entityPlayer13.getDistanceSq(d1, d3, d5);
			if((d7 < 0.0D || d14 < d7 * d7) && (d9 == -1.0D || d14 < d9)) {
				d9 = d14;
				entityPlayer11 = entityPlayer13;
			}
		}

		return entityPlayer11;
	}

	public EntityPlayer getPlayerEntityByName(String string1) {
		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			if(string1.equals(((EntityPlayer)this.playerEntities.get(i2)).username)) {
				return (EntityPlayer)this.playerEntities.get(i2);
			}
		}

		return null;
	}

	public void setChunkData(int i1, int i2, int i3, int i4, int i5, int i6, byte[] b7) {
		int i8 = i1 >> 4;
		int i9 = i3 >> 4;
		int i10 = i1 + i4 - 1 >> 4;
		int i11 = i3 + i6 - 1 >> 4;
		int i12 = 0;
		int i13 = i2;
		int i14 = i2 + i5;
		if(i2 < 0) {
			i13 = 0;
		}

		if(i14 > 128) {
			i14 = 128;
		}

		for(int i15 = i8; i15 <= i10; ++i15) {
			int i16 = i1 - i15 * 16;
			int i17 = i1 + i4 - i15 * 16;
			if(i16 < 0) {
				i16 = 0;
			}

			if(i17 > 16) {
				i17 = 16;
			}

			for(int i18 = i9; i18 <= i11; ++i18) {
				int i19 = i3 - i18 * 16;
				int i20 = i3 + i6 - i18 * 16;
				if(i19 < 0) {
					i19 = 0;
				}

				if(i20 > 16) {
					i20 = 16;
				}

				i12 = this.getChunkFromChunkCoords(i15, i18).setChunkData(b7, i16, i13, i19, i17, i14, i20, i12);
				this.markBlocksDirty(i15 * 16 + i16, i13, i18 * 16 + i19, i15 * 16 + i17, i14, i18 * 16 + i20);
			}
		}

	}

	public void sendQuittingDisconnectingPacket() {
	}

	public void checkSessionLock() {
		this.saveHandler.func_22150_b();
	}

	public void setWorldTime(long j1) {
		this.worldInfo.setWorldTime(j1);
	}

	public long getRandomSeed() {
		return this.worldInfo.getRandomSeed();
	}

	public long getWorldTime() {
		return this.worldInfo.getWorldTime();
	}

	public ChunkCoordinates getSpawnPoint() {
		return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
	}

	public void setSpawnPoint(ChunkCoordinates chunkCoordinates1) {
		this.worldInfo.setSpawn(chunkCoordinates1.x, chunkCoordinates1.y, chunkCoordinates1.z);
	}

	public void joinEntityInSurroundings(Entity entity1) {
		int i2 = MathHelper.floor_double(entity1.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity1.posZ / 16.0D);
		byte b4 = 2;

		for(int i5 = i2 - b4; i5 <= i2 + b4; ++i5) {
			for(int i6 = i3 - b4; i6 <= i3 + b4; ++i6) {
				this.getChunkFromChunkCoords(i5, i6);
			}
		}

		if(!this.loadedEntityList.contains(entity1)) {
			this.loadedEntityList.add(entity1);
		}

	}

	public boolean func_6466_a(EntityPlayer entityPlayer1, int i2, int i3, int i4) {
		return true;
	}

	public void func_9425_a(Entity entity1, byte b2) {
	}

	public void updateEntityList() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int i1;
		Entity entity2;
		int i3;
		int i4;
		for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
			entity2 = (Entity)this.unloadedEntityList.get(i1);
			i3 = entity2.chunkCoordX;
			i4 = entity2.chunkCoordZ;
			if(entity2.addedToChunk && this.chunkExists(i3, i4)) {
				this.getChunkFromChunkCoords(i3, i4).removeEntity(entity2);
			}
		}

		for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(i1));
		}

		this.unloadedEntityList.clear();

		for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			entity2 = (Entity)this.loadedEntityList.get(i1);
			if(entity2.ridingEntity != null) {
				if(!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
					continue;
				}

				entity2.ridingEntity.riddenByEntity = null;
				entity2.ridingEntity = null;
			}

			if(entity2.isDead) {
				i3 = entity2.chunkCoordX;
				i4 = entity2.chunkCoordZ;
				if(entity2.addedToChunk && this.chunkExists(i3, i4)) {
					this.getChunkFromChunkCoords(i3, i4).removeEntity(entity2);
				}

				this.loadedEntityList.remove(i1--);
				this.releaseEntitySkin(entity2);
			}
		}

	}

	public IChunkProvider getIChunkProvider() {
		return this.chunkProvider;
	}

	public void playNoteAt(int i1, int i2, int i3, int i4, int i5) {
		int i6 = this.getBlockId(i1, i2, i3);
		if(i6 > 0) {
			Block.blocksList[i6].playBlock(this, i1, i2, i3, i4, i5);
		}

	}

	public WorldInfo getWorldInfo() {
		return this.worldInfo;
	}

	public void updateAllPlayersSleepingFlag() {
		this.allPlayersSleeping = !this.playerEntities.isEmpty();
		Iterator iterator1 = this.playerEntities.iterator();

		while(iterator1.hasNext()) {
			EntityPlayer entityPlayer2 = (EntityPlayer)iterator1.next();
			if(!entityPlayer2.isPlayerSleeping()) {
				this.allPlayersSleeping = false;
				break;
			}
		}

	}

	protected void wakeUpAllPlayers() {
		this.allPlayersSleeping = false;
		Iterator iterator1 = this.playerEntities.iterator();

		while(iterator1.hasNext()) {
			EntityPlayer entityPlayer2 = (EntityPlayer)iterator1.next();
			if(entityPlayer2.isPlayerSleeping()) {
				entityPlayer2.wakeUpPlayer(false, false, true);
			}
		}

		this.stopPrecipitation();
	}

	public boolean isAllPlayersFullyAsleep() {
		if(this.allPlayersSleeping && !this.multiplayerWorld) {
			Iterator iterator1 = this.playerEntities.iterator();

			EntityPlayer entityPlayer2;
			do {
				if(!iterator1.hasNext()) {
					return true;
				}

				entityPlayer2 = (EntityPlayer)iterator1.next();
			} while(entityPlayer2.isPlayerFullyAsleep());

			return false;
		} else {
			return false;
		}
	}

	public float func_27166_f(float f1) {
		return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * f1) * this.func_27162_g(f1);
	}

	public float func_27162_g(float f1) {
		return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * f1;
	}

	public void func_27158_h(float f1) {
		this.prevRainingStrength = f1;
		this.rainingStrength = f1;
	}

	public boolean func_27160_B() {
		return (double)this.func_27166_f(1.0F) > 0.9D;
	}

	public boolean func_27161_C() {
		return (double)this.func_27162_g(1.0F) > 0.2D;
	}

	public boolean canBlockBeRainedOn(int i1, int i2, int i3) {
		if(!this.func_27161_C()) {
			return false;
		} else if(!this.canBlockSeeTheSky(i1, i2, i3)) {
			return false;
		} else if(this.findTopSolidBlock(i1, i3) > i2) {
			return false;
		} else {
			BiomeGenBase biomeGenBase4 = this.getWorldChunkManager().getBiomeGenAt(i1, i3);
			return biomeGenBase4.getEnableSnow() ? false : biomeGenBase4.canSpawnLightningBolt();
		}
	}

	public void setItemData(String string1, MapDataBase mapDataBase2) {
		this.field_28108_z.setData(string1, mapDataBase2);
	}

	public MapDataBase loadItemData(Class class1, String string2) {
		return this.field_28108_z.loadData(class1, string2);
	}

	public int getUniqueDataId(String string1) {
		return this.field_28108_z.getUniqueDataId(string1);
	}

	public void func_28106_e(int i1, int i2, int i3, int i4, int i5) {
		this.func_28107_a((EntityPlayer)null, i1, i2, i3, i4, i5);
	}

	public void func_28107_a(EntityPlayer entityPlayer1, int i2, int i3, int i4, int i5, int i6) {
		for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
			((IWorldAccess)this.worldAccesses.get(i7)).func_28136_a(entityPlayer1, i2, i3, i4, i5, i6);
		}

	}
}
