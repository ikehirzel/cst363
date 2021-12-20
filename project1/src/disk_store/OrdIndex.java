package disk_store;

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered index.  Duplicate search key values are allowed,
 * but not duplicate index table entries.  In DB terminology, a
 * search key is not a superkey.
 * 
 * A limitation of this class is that only single integer search
 * keys are supported.
 *
 */


public class OrdIndex implements DBIndex {
	
	private class Entry {
		int key;
		ArrayList<BlockCount> blocks;
		
		private Entry(int key, int blockNum) {
			this.key = key;
			
			blocks = new ArrayList<>(1);
			
			blocks.add(new BlockCount(blockNum));
		}
	}
	
	private class BlockCount {
		int blockNo;
		int count;
		
		private BlockCount(int blockNo) {
			this.blockNo = blockNo;
			this.count = 1;
		}
	}
	
	ArrayList<Entry> entries;	
	
	/**
	 * Create an new ordered index.
	 */
	public OrdIndex() {
		entries = new ArrayList<>();
	}
	
	int getInsertIndex(int key) {
		int low = 0;
		int high = entries.size();
		
		while (low < high) {
			int mid = (low + high) / 2;
			int value = entries.get(mid).key;		
			
			if (key < value) {
				high = mid;				
			}
			else if (key > value) {
				low = mid + 1;
			}
			else {
				return mid;
			}
		}
		
		return low;
	}
	
	// 1 2
	
	int getLookupIndex(int key) {
		if (entries.isEmpty())
			return -1;
	
		int low = 0;
		int high = entries.size();
		
		while (low < high) {
			int mid = (low + high) / 2;
			int value = entries.get(mid).key;
			
			if (key < value) {
				high = mid;
			}
			else if (key > value) {
				low = mid + 1;
			}
			else {
				return mid;
			}
		}
		
		return -1;
	}

	@Override
	public List<Integer> lookup(int key) {
		// binary search of entries arraylist
		// return list of block numbers (no duplicates). 
		// if key not found, return empty list
		
		ArrayList<Integer> out = new ArrayList<>();
		
		int index = getLookupIndex(key);
		
		if (index != -1) {
			Entry entry = entries.get(index);
			out.ensureCapacity(entry.blocks.size());
			
			for (BlockCount b : entry.blocks) {
				out.add(b.blockNo);
			}
		}
		
		return out;
	}
	
	@Override
	public void insert(int key, int blockNum) {		
		int index = getInsertIndex(key);
		
		if (index < entries.size()) {
			Entry e = entries.get(index);
			
			if (e.key != key) {
				entries.add(index, new Entry(key, blockNum));				
			}
			else {
				for (BlockCount b : e.blocks) {
					if (b.blockNo == blockNum) {
						b.count += 1;
						return;
					}
				}
				
				e.blocks.add(new BlockCount(blockNum));
			}
		}
		else {
			entries.add(new Entry(key, blockNum));
		}
		
	}

	@Override
	public void delete(int key, int blockNum) {
		// lookup key 
		// if key not found, should not occur.  Ignore it.
		// decrement count for blockNum.
		// if count is now 0, remove the blockNum.
		// if there are no block number for this key, remove the key entry.

		int index = getLookupIndex(key);
		
		System.out.println("Got delete index: " + index);
		
		if (index == -1)
			return;
		
		Entry entry = entries.get(index);
		
		for (BlockCount b : entry.blocks) {
			if (b.blockNo == blockNum) {
				b.count -= 1;
				
				if (b.count == 0)
					entry.blocks.remove(b);
				
				break;
			}
		}
		
		if (entry.blocks.isEmpty())
			entries.remove(entry);
	}
	
	/**
	 * Return the number of entries in the index
	 * @return
	 */
	public int size() {		
		int size = 0;
		
		for (Entry e : entries) {
			size += e.blocks.size();
		}
		
		return size;
	}
	
	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}