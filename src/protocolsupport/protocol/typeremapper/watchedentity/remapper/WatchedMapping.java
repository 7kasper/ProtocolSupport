package protocolsupport.protocol.typeremapper.watchedentity.remapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.value.ValueRemapper;
import protocolsupport.protocol.typeremapper.watchedentity.types.WatchedType;

public class WatchedMapping {
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

public static class WatchedMappingTable {
	
	protected final EnumMap<WatchedType, EnumMap<ProtocolVersion, List<WatchedMapping>>> table = new EnumMap<>(WatchedType.class);
	{
		for (WatchedType t : WatchedType.values()){
			table.put(t, new ArrayList<WatchedMapping>());
		}
	}
	
	public void addMappings(WatchedType type, WatchedMapping... entries) {
		addMappings(type, Arrays.asList(entries));
	}
	
	public void addMappings(WatchedType type, List<WatchedMapping> entries) {
		table.get(type).addAll(entries);
		if(type.getSuperType() != null){
			table.get(type).addAll(table.get(type.getSuperType()));
		}
	}
	
	public List<WatchedMapping> getMappings(WatchedType type) {
		return table.get(type);
	}
	
	public List<WatchedMapping> getRemaps(WatchedType type, ProtocolVersion version) {
		List<WatchedMapping> remaps = new ArrayList<>();
		for(WatchedMapping map : getMappings(type)) {
			for(WatchedEntry entry : map.getEntries()) {
				if(entry.versions)
			}
		}
	}
	
}