package protocolsupport.protocol.packet.middleimpl.clientbound.status.v_1_5__1_6;

import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.ServerPing;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.chat.ChatAPI;
import protocolsupport.protocol.packet.ClientBoundPacket;
import protocolsupport.protocol.packet.middle.clientbound.status.MiddleServerInfo;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.utils.ServerPingSerializers;
import protocolsupport.utils.recyclable.RecyclableCollection;
import protocolsupport.utils.recyclable.RecyclableSingletonList;

public class ServerInfo extends MiddleServerInfo<RecyclableCollection<ClientBoundPacketData>> {

	@Override
	public RecyclableCollection<ClientBoundPacketData> toData(ProtocolVersion version) {
		ClientBoundPacketData serializer = ClientBoundPacketData.create(ClientBoundPacket.STATUS_SERVER_INFO_ID, version);
		ServerPing serverPing = ServerPingSerializers.PING_GSON.fromJson(pingJson, ServerPing.class);
		int versionId = serverPing.getServerData().getProtocolVersion();
		String response =
		"§1\u0000" + (versionId == ProtocolVersion.getLatest().getId() ? serializer.getVersion().getId() : versionId) +
		"\u0000" + serverPing.getServerData().a() +
		"\u0000" + ChatAPI.fromJSON(ChatSerializer.a(serverPing.a())).toLegacyText() +
		"\u0000" + serverPing.b().b() +
		"\u0000" + serverPing.b().a();
		serializer.writeString(response);
		return RecyclableSingletonList.create(serializer);
	}

}
