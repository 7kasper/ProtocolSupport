package protocolsupport.protocol.typeremapper.id;

import java.util.ArrayList;
import java.util.EnumMap;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.typeremapper.id.RemappingTable.GenericRemappingTable;
import protocolsupport.protocol.typeremapper.id.RemappingTable.IdRemappingTable;
import protocolsupport.protocol.typeremapper.id.RemappingTable.WatchedRemappingTable;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.SpecificRegistry.WatchedRemap;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.SpecificTable.WatchedMapping;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.SpecificTable.WatchedMapping.WatchedEntry;
import protocolsupport.protocol.typeremapper.watchedentity.types.WatchedType;

public abstract class RemappingRegistry<T extends RemappingTable> {

	private final EnumMap<ProtocolVersion, T> remappings = new EnumMap<>(ProtocolVersion.class);

	public RemappingRegistry() {
		for (ProtocolVersion version : ProtocolVersion.values()) {
			if (version.isSupported()) {
				remappings.put(version, createTable());
			}
		}
	}

	public T getTable(ProtocolVersion version) {
		return remappings.get(version);
	}

	protected abstract T createTable();

	public static abstract class IdRemappingRegistry<T extends IdRemappingTable> extends RemappingRegistry<T> {

		public void registerRemapEntry(int from, int to, ProtocolVersion... versions) {
			for (ProtocolVersion version : versions) {
				getTable(version).setRemap(from, to);
			}
		}

	}

	public static abstract class GenericRemappingRegistry<T, R extends GenericRemappingTable<T>> extends RemappingRegistry<R> {

		public void registerRemapEntry(T from, T to, ProtocolVersion... versions) {
			for (ProtocolVersion version : versions) {
				getTable(version).setRemap(from, to);
			}
		}

	}
	
	public static abstract class WatchedRemappingRegistry <T extends WatchedRemappingTable> extends RemappingRegistry<T> {
		public void registerRemaps(WatchedType type, ArrayList<WatchedMapping> mappings) {
			for (WatchedMapping mapping : mappings) {
				for (WatchedEntry entry : mapping.entries){
					for (ProtocolVersion version : entry.versions) {
						getTable(version).addRemap(type, new WatchedRemap(mapping.idFrom, entry.idTo, entry.vremap));
					}
				}
			}
		}
	}

}
