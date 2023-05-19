package com.example.BackendPerfectHashing;
import java.util.*;


/*
 * In this method of hashing, two hashing levels are used.
 * The first level involves using a hash table of size m = n (n is the size of the input)
 * If a collision happens in the first level table then a secondary hash table is used to keys at this index.
 * This is similar to chaining except that another hash table is used instead of a linked list. 
 * If there are nj collisions in the jth index then the size of the second-level table must be mj = nj^2 
 * A hash function is chosen randomly for this second-level table and hashing is attempted. This is known as building the table
 * If a collision occurs in the second-level table then it is rebuilt and hashing is reattempted. Repeat until no collisions occur.
 * 
 * 
 * This method should end up using O(n) space and O(1) search\insert\delete time if done correctly.
 * A count of the number of rebuilds should be maintained for performance measuring
 * 
 */


public class NSolution extends PerfectHashing{

	private int size;
	private Lvl2Table[] table;
	UniverseHashing hashFunc;

	// Constructor
	public NSolution(int size) 
	{
		
		size = 1 << log2(size);
		this.size = size;
		table = new Lvl2Table[size];
		for (int i=0; i<size; i++)
		{
			table[i] = new Lvl2Table();
		}
		hashFunc = new UniverseHashing();
		hashFunc.newHashMatrix(this.size);
	}

	@Override
    public boolean insert(String key)
	{
		String binaryStr = hashFunc.hash_string(key);
        int index = 0;
		// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
		
		return table[index].insert(key);
    }
	
    public boolean delete(String key)
	{
		String binaryStr = hashFunc.hash_string(key);
        int index = 0;
		// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
		
        return table[index].delete(key);
    }

    public boolean search(String key){
        String binaryStr = hashFunc.hash_string(key);
        int index = 0;
		// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
		
        return table[index].search(key);
    }

	private int log2(int x)
	{
		int log = 0;
		for (;size > 0; log++)
		{
			size = size >> 1;
		}
		return log;
	}

	class Lvl2Table extends PerfectHashing 
	{
		private int prevRebuildCount;
		private int entryCount = 0;
		String[] table = new String[0];
		UniverseHashing hashFunc = new UniverseHashing();


		public boolean insert(String key)
		{
			String binaryStr = hashFunc.hash_string(key);
			int index = 0;
			// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
			//If key already exists
			if(search(key))
			{
				return false;
			}
			entryCount++;
			for (int i=0; i<table.length; i++)
			{
				if (table[i] == null)
				{
					table[i] = key;
					break;
				}
			}
			rebuild();
			
			return true;
		}

		public boolean search(String key)
		{
			String binaryStr = hashFunc.hash_string(key);
			int index = 0;
			// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
			return table[index].equals(key);
		}

		public boolean delete(String key)
		{
			if (!search(key)) return false;
			String binaryStr = hashFunc.hash_string(key);
			int index = 0;
			// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
			table[index] = null;
			entryCount -= 1;
			return true;
		}

		private void rebuild()
		{
			String[] allEntries = new String[entryCount];
			int j = 0;
			for (int i=0; i<table.length; i++)
			{
				if (table[i] != null)
				{
					allEntries[j] = table[i];
					j++;
				}
			}
			
			int size = 1 << log2(entryCount*entryCount);
			table = new String[size];
			prevRebuildCount = -1;
			findingNoCollisionsFunc:
			while(true)
			{
				prevRebuildCount++;
				hashFunc.newHashMatrix(table.length);
				Arrays.fill(table, null);
				for (j=0; j<entryCount; j++)
				{
					String binaryStr = hashFunc.hash_string(allEntries[j]);
					int index = 0;
					// index = hashFunc.hash(hashFunc.getHashMatrix(), binaryStr);
					if (table[index] != null) continue findingNoCollisionsFunc;
				}	
				break;
			}
		}

		public int get_prev_rebuilds(){return prevRebuildCount;}
	}
}
