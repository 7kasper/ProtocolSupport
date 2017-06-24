package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe;

import org.bukkit.util.Vector;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleEntityLook;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.utils.types.NetworkEntity;
import protocolsupport.utils.recyclable.RecyclableCollection;
import protocolsupport.utils.recyclable.RecyclableEmptyList;
import protocolsupport.utils.recyclable.RecyclableSingletonList;

public class EntityLook extends MiddleEntityLook {

	@Override
	public RecyclableCollection<ClientBoundPacketData> toData(ProtocolVersion version) {
		NetworkEntity entity = cache.getWatchedEntity(entityId);
		if(entity == null) {
			return RecyclableEmptyList.get();
		} else {
			Vector pos = entity.getPosition();
			return RecyclableSingletonList.create(EntityTeleport.create(entity, pos.getX(), pos.getY(), pos.getZ(), (byte) pitch, entity.getHeadYaw(), (byte) yaw, onGround, false, version));
		}
	}

}
