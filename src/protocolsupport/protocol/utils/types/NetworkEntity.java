package protocolsupport.protocol.utils.types;

import java.util.UUID;

import org.bukkit.util.Vector;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.utils.Utils;

public class NetworkEntity {

	public static NetworkEntity createMob(UUID uuid, int id, int typeId) {
		return new NetworkEntity(uuid, id, NetworkEntityType.getMobByTypeId(typeId));
	}

	public static NetworkEntity createObject(UUID uuid, int id, int typeId, int objectData) {
		return new NetworkEntity(uuid, id, NetworkEntityType.getObjectByTypeAndData(typeId, objectData));
	}

	public static NetworkEntity createPlayer(UUID uuid, int id) {
		return new NetworkEntity(uuid, id, NetworkEntityType.PLAYER);
	}

	public static NetworkEntity createPlayer(int id) {
		return createPlayer(null, id);
	}

	private final UUID uuid;
	private final int id;
	private final NetworkEntityType type;

	public NetworkEntity(UUID uuid, int id, NetworkEntityType type) {
		this.uuid = uuid;
		this.id = id;
		this.type = type;
	}

	public UUID getUUID() {
		return uuid;
	}

	public int getId() {
		return id;
	}

	public NetworkEntityType getType() {
		return type;
	}

	private DataCache data = new DataCache();
	
	public boolean isOfType(NetworkEntityType checkWith) {
		return type.isOfType(checkWith);
	}

	public DataCache getDataCache() {
		return data;
	}
	
	public void updateDataCache(DataCache updateWith) {
		data = updateWith;
	}
	
	public void updateMetadata(TIntObjectMap<DataWatcherObject<?>> updateWith) {
		data.updateMeta(updateWith);
	}

	private Vector position;
	private Byte headYaw;
	private Byte yaw;
	private Byte pitch;
	private Boolean onGround = true;
	
	
	public void updatePosition(Vector updateWith) {
		position = updateWith;
	}
	
	public void updateRelPosition(int relX, int relY, int relZ) {
		//X and Y are known. Relative is send (a * 32 - x * 32) * 128 = y                   y / 128 = a32 - x32         (y/128) + x32 = a 32               ((y / 128) + 32x) / 32 = a
		position = new Vector(((relX / 128) + (position.getX() * 32)) / 32, ((relY / 128) + (position.getY() * 32)) / 32, ((relZ / 128) + (position.getZ() * 32)) / 32);
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void updateRotation(byte updateYaw, byte updatePitch) {
		yaw = updateYaw;
		pitch = updatePitch;
	}
	
	public void updateHeadYaw(byte updateWith) {
		headYaw = updateWith;
	}
	
	public byte getHeadYaw() {
		if(headYaw == null) return yaw;
		return headYaw;
	}
	
	public byte getYaw() {
		return yaw;
	}
	
	public byte getPitch() {
		return pitch;
	}
	
	public void updateOnGround(boolean updateWith) {
		onGround = updateWith;
	}
	
	public boolean getOnGround() {
		return onGround;
	}
	
	@Override
	public String toString() {
		return Utils.toStringAllFields(this);
	}

	public class DataCache {
		public boolean firstMeta = true;
		public TIntObjectMap<DataWatcherObject<?>> metadata = new TIntObjectHashMap<>();
		public int attachedId = -1; //Leashed? Data is send in pocket meta, but might be useful to store for other things.
		public boolean inLove = false; //Show just one metaupdate?
		public Rider rider = new Rider(false);
		
		public Object getMetaValue(int index) {
			if(metadata.containsKey(index)) {
				return metadata.get(index).getValue();	
			} else {
				return null;
			}
		}
		
		public boolean getMetaBool(int index) {
			if(metadata.containsKey(index)) {
				try {
					return (boolean) getMetaValue(index);
				} catch (ClassCastException e) {}
			}
			return false;
		}
		
		public boolean getMetaBool(int index, int bitpos) {
			if(metadata.containsKey(index)) {
				return (((byte) getMetaValue(index)) & (1 << (bitpos - 1))) != 0;
			}
			return false;
		}
		
		public void updateMeta(TIntObjectMap<DataWatcherObject<?>> updateWith) {
			for(int index : updateWith.keys()) {
				metadata.put(index, updateWith.get(index));
			}
		}
		
		public class Rider {
			public boolean riding = false;
			public Vector position = new Vector(0, 0.6, 0);
			public boolean rotationLocked = false;
			public Float rotationMin;
			public Float rotationMax;
			
			public Rider(boolean riding) {
				this.riding = riding;
			}
			
			public Rider(Vector position, boolean rotationLocked, float rotationMax, float rotationMin) {
				this(true, position, rotationLocked, rotationMax, rotationMin);
			}
			
			public Rider(Vector position, boolean rotationLocked) {
				this(true, position, rotationLocked, null, null);
			}
			
			public Rider(boolean riding, Vector position, boolean rotationLocked, Float rotationMax, Float rotationMin) {
				this.riding = riding;
				this.position = position;
				this.rotationLocked = rotationLocked;
				this.rotationMax = rotationMax;
				this.rotationMin = rotationMin;
			}
		};
		
		@Override
		public String toString() {
			return Utils.toStringAllFields(this);
		}
	}

}
