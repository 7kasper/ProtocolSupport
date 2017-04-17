package protocolsupport.protocol.typeremapper.id;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import gnu.trove.map.hash.TIntIntHashMap;
import protocolsupport.protocol.typeremapper.watchedentity.remapper.SpecificRegistry.WatchedRemap;
import protocolsupport.protocol.typeremapper.watchedentity.types.WatchedType;

public class RemappingTable {

	public abstract static class IdRemappingTable extends RemappingTable {

		public abstract void setRemap(int from, int to);

		public abstract int getRemap(int id);

	}

	public static class ArrayBasedIdRemappingTable extends IdRemappingTable {

		protected final int[] table;
		public ArrayBasedIdRemappingTable(int size) {
			table = new int[size];
			for (int i = 0; i < table.length; i++) {
				table[i] = i;
			}
		}

		@Override
		public void setRemap(int from, int to) {
			table[from] = to;
		}

		@Override
		public int getRemap(int id) {
			return table[id];
		}

	}

	public static class HashMapBasedIdRemappingTable extends IdRemappingTable {

		protected final TIntIntHashMap table = new TIntIntHashMap(16, 0.75F, -1, -1);

		@Override
		public void setRemap(int from, int to) {
			table.put(from, to);
		}

		@Override
		public int getRemap(int id) {
			int r = table.get(id);
			return r != -1 ? r : id;
		}

	}

	public static class GenericRemappingTable<T> extends RemappingTable {

		protected final HashMap<T, T> table = new HashMap<>();

		public void setRemap(T from, T to) {
			table.put(from, to);
		}

		public T getRemap(T from) {
			return table.getOrDefault(from, from);
		}

	}
	
	public static class WatchedRemappingTable extends RemappingTable {
		
		protected final EnumMap<WatchedType, ArrayList<WatchedRemap>> table = new EnumMap<>(WatchedType.class);
		{
			for (WatchedType t : WatchedType.values()) {
				table.put(t, new ArrayList<WatchedRemap>());
			}
		}
		
		public List<WatchedRemap> getRemaps(WatchedType type) {
			return table.get(type);
		}
		
		public void addRemap(WatchedType type, WatchedRemap remap) {
			table.get(type).add(remap);
			if(type.getSuperType() != null){
				table.get(type).addAll(getRemaps(type.getSuperType()));
			}
		}
	}

}
