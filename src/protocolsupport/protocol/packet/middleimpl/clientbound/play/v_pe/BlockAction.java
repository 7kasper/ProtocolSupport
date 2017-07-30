package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleBlockAction;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.utils.recyclable.RecyclableCollection;
import protocolsupport.utils.recyclable.RecyclableEmptyList;
import protocolsupport.utils.recyclable.RecyclableSingletonList;

public class BlockAction extends MiddleBlockAction {

	@Override
	public RecyclableCollection<ClientBoundPacketData> toData(ProtocolVersion version) {
		System.out.println(position.toString() + ": " + info1 + " " + info2);
		
		switch(type) {
			//Chest / TrappedChest
			case 146:
			case 54: {
				if(info2 == 0) {
					return RecyclableSingletonList.create(create(version, position, info1, 0));
				} else {
					return RecyclableSingletonList.create(create(version, position, info1, 2));
				}
			} default: {
				return RecyclableEmptyList.get();
			}
		}
		
		//return RecyclableSingletonList.create(create(version, position, info1, info2));
	}
	
	public static ClientBoundPacketData create(ProtocolVersion version, protocolsupport.protocol.utils.types.Position position, int info1, int info2) {
		ClientBoundPacketData serializer = ClientBoundPacketData.create(PEPacketIDs.BLOCK_EVENT, version);
		PositionSerializer.writePEPosition(serializer, position);
		VarNumberSerializer.writeVarInt(serializer, info1);
		VarNumberSerializer.writeVarInt(serializer, info2);
		return serializer;
	}

}
