package protocolsupport.protocol.typeremapper.watchedentity.remapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.SpecificTable.WatchedMapping.WatchedEntry;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.value.ValueRemapper;
import protocolsupport.protocol.typeremapper.watchedentity.types.WatchedType;

public class SpecificRegistry extends SpecificTable {

	protected final EnumMap<WatchedType, EnumMap<ProtocolVersion, ArrayList<WatchedRemap>>> remapTable = new EnumMap<>(WatchedType.class);
	{
		for (WatchedType t : WatchedType.values()) {
			remapTable.put(t, new EnumMap<ProtocolVersion, ArrayList<WatchedRemap>>(ProtocolVersion.class));
			for (ProtocolVersion v : ProtocolVersion.values()) {
				remapTable.get(t).put(v, new ArrayList<WatchedRemap>());
			}
			
		}
	}
	
	public void registerMappings(WatchedType type, WatchedMapping... mappings) {
		registerMappings(type, Arrays.asList(mappings));
	}
	
	public void registerMappings(WatchedType type, List<WatchedMapping> mappings) {
		
		for(WatchedMapping m : mappings) {
			for(WatchedEntry e : m.entries) {
				for(ProtocolVersion v : e.versions) {
					remapTable.get(type).get(v).add(new WatchedRemap(m.idFrom, e.idTo, e.vremap));
				}
			}
		}
	}
	
	public List<WatchedRemap> getRemaps(WatchedType type, ProtocolVersion version) {
		return remapTable.get(type).get(version);
	}
	
	public static class WatchedRemap {

		protected final int from;
		protected final int to;
		protected final ValueRemapper<?> vremap;

		public WatchedRemap(int from, int to, ValueRemapper<?> vremap) {
			this.from = from;
			this.to = to;
			this.vremap = vremap;
		}

		public int getIdFrom() {
			return from;
		}

		public int getIdTo() {
			return to;
		}

		@SuppressWarnings("rawtypes")
		public ValueRemapper getValueRemapper() {
			return vremap;
		}

	}
}
