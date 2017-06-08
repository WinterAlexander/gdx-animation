package com.brashmonkey.spriter;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

/**
 * Represents a project file for spriter
 *
 * @author Trixt0r
 */
public class SCMLProject
{
	private final IntMap<SpriterAsset> assets;
	private final Array<SpriterEntity> entities;

	public SCMLProject()
	{
		this.assets = new IntMap<>();
		this.entities = new Array<>();
	}

	/**
	 * Returns an copy of the requested SpriterEntity
	 *
	 * @param name the name of the entity
	 * @return the entity with the given name or null if no entity with the given name exists
	 */
	public SpriterEntity getEntity(String name)
	{
		for(SpriterEntity entity : entities)
			if(entity.getName().equals(name))
				return new SpriterEntity(entity);

		return null;
	}

	public void putAsset(int folderID, int fileID, SpriterAsset asset)
	{
		assets.put(key(folderID, fileID), asset);
	}

	public SpriterAsset getAsset(int folderID, int fileID)
	{
		return assets.get(key(folderID, fileID));
	}

	private int key(int folder, int file)
	{
		return (folder << 16) + file;
	}

	public Array<SpriterEntity> getSourceEntities()
	{
		return entities;
	}
}
