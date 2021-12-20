package table;

import java.util.ArrayList;
import java.util.List;

/**
 * A block of bytes, similar to a disk block
 * @author Glenn
 *
 */

public class Block {
	
	static final int BLOCK_SIZE = 4096; // total bytes per block
	
	private Schema schema;
	private byte[] bytes;
	private Bitmap bitmap;
	private int tupleSize;	   // number of bytes per record
	private int bitmapSize;    // number of bytes in bitmap
	private int maxRecs;       // max records per block
	
	/**
	 * Create a new block.
	 * @param schema
	 */
	public Block(Schema schema) {
		this.schema = schema;
		
		// how many records can fit into a block?
		bytes = new byte[BLOCK_SIZE];
		tupleSize = schema.getLen();
		maxRecs = (int)Math.floor((BLOCK_SIZE - 1)/(tupleSize + 1.0/8.0)); 
		
		// create bitmap
		bitmapSize = (int)Math.ceil(maxRecs/8.0);
		bitmap = new Bitmap(bytes, bitmapSize);
		
		// make sure size parameters are okay
		if (tupleSize * maxRecs + bitmapSize > BLOCK_SIZE) {
			throw new RuntimeException("internal block error: bad block size");
		}
	}
	
	/**
	 * Insert a tuple into the block.
	 * @param tuple
	 */
	public void insert(Tuple tuple) {	
		
		// The following is an implementation hint.  Read it only if you want.
		//
		// rough outline of what needs to be done:
		// - using bitmap, see if there is room for another tuple; if not, throw IllegalStateException
		// - if tuple has a primary key, make sure a record with the same values
		//   for the primary key fields is not already in the block
		// - put record into position associated with first 0 in bitmap
		//   - figure out the location in bytes[] where the tuple should go
		//   - serialize the tuple into that location
		//   - make sure to set the proper bit in the bitmap afterward
		
		// YOUR CODE HERE
		
		if (bitmap.sum() == maxRecs)
			throw new IllegalStateException();
		
		List<String> primary_keys = schema.getPrimaryKey();
		Tuple stored = schema.defaultTuple();
		
		int index = -1;
		
		for (int i = 0; i < bitmapSize; ++i) {
			if (bitmap.getBit(i)) {
				if (primary_keys != null) {				
					stored.deserialize(bytes, bitmapSize + i * tupleSize);
				
					if (equalOnFields(tuple, stored, primary_keys))
						throw new IllegalArgumentException();
				}
			}
			else if (index == -1) {
				index = i;
			}
		}
			
		tuple.serialize(bytes, bitmapSize + index * tupleSize);	
		bitmap.setBit(index, true);
	}
	
	/**
	 * Delete all tuples in the block have the given field value.
	 * @param fieldName
	 * @param field
	 */
	public void delete(String fieldName, Field field) {
		
		// The following is an implementation hint.  Read it only if you want.
		//
		// rough outline of what needs to be done:
		// - see if the name of the given field appears in the schema for this block;
		//   is not, throw an IllegalArgumentException
		// - iterate over the bit map to find positions where a bit is set
		//   - for each set bit, determine the position of the record in the block
		//     (the toString() method below may help to understand this)
		//   - deserialize the tuple at that position
		//   - see if the field value of the deserialized tuple and the value of
		//     the 'field' parameter are equal
		//   - if they are equal, the tuple should be deleted, so clear the bit map
		//     at this position
		
		// YOUR CODE HERE
		
		if (schema.getFieldIndex(fieldName) < 0) {
			throw new IllegalArgumentException("Error: no such field in schema");
		}
		
		Tuple tuple = schema.defaultTuple();
		
		for (int i = 0; i < bitmap.size(); ++i) {
			if (bitmap.getBit(i)) {
				tuple.deserialize(bytes, bitmapSize + i * tupleSize);
				if (tuple.get(fieldName).equals(field)) {
					bitmap.setBit(i, false);
				}
			}
		}
	}
	
	// return true if tuples t1 and t2 both have values for the given field
	// names, and the values are equal
	private static boolean equalOnFields(Tuple t1, Tuple t2, List<String> fields) {
		for (String fname: fields) {
			Field fieldVal1 = t1.get(fname);
			Field fieldVal2 = t2.get(fname);
			if (fieldVal1 == null || fieldVal2 == null || !fieldVal1.equals(fieldVal2)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Return the number of records in the block.
	 * @return
	 */
	public int size() {
		return bitmap.sum();
	}
	
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[\n");
		
		Tuple tup = schema.defaultTuple();
		for (int i = 0; i < bitmap.size(); i++) {
			if (bitmap.getBit(i)) {
				int recPos = bitmapSize + i*tupleSize;
				tup.deserialize(bytes, recPos);
				sbuf.append(tup+"\n");
			}
		}
		
		sbuf.append("]\n");
		return sbuf.toString();
	}

}
