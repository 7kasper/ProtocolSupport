package protocolsupport.protocol.typeremapper.watchedentity.remapper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.value.ValueRemapper;
import protocolsupport.protocol.typeremapper.watchedentity.types.WatchedType;

public class SpecificTable {
	
	protected final EnumMap<WatchedType, ArrayList<WatchedMapping>> table = new EnumMap<>(WatchedType.class);
	{
		for (WatchedType t : WatchedType.values()) {
			table.put(t, new ArrayList<WatchedMapping>());
		}
	}
	
	public List<WatchedMapping> getMappings(WatchedType type) {
		return table.get(type);
	}
	
	public void addMappings(WatchedType type, List<WatchedMapping> mappings) {
		table.get(type).addAll(mappings);
		if(type.getSuperType() != null){
			table.get(type).addAll(getMappings(type.getSuperType()));
		}
	}
	
	public static class WatchedMapping {
		public final int idFrom;
		public final List<WatchedEntry> entries = new ArrayList<>();
		
		
		public WatchedMapping(int idFrom) {
			this.idFrom = idFrom;
		}
		
		public WatchedMapping addRemap(int to, ValueRemapper<?> valueremap, ProtocolVersion... versions) {
			entries.add(new WatchedEntry(to, valueremap, versions));
			return this;
		}
		
		public List<WatchedEntry> getEntries(){
			return this.entries;
		}
		
		public static class WatchedEntry {
			public final int idTo;
			public final ValueRemapper<?> vremap;
			public final ProtocolVersion[] versions;
			public WatchedEntry(int to, ValueRemapper<?> vremap, ProtocolVersion[] versions) {
				this.idTo = to;
				this.vremap = vremap;
				this.versions = versions;
			}
		}
	}
}
