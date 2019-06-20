package com.mc.world;

public interface WorldListener {

	void allBlocksUpdated();
	void onBlockUpdate(int x, int y, int z, byte oldID, byte newID);
	
}