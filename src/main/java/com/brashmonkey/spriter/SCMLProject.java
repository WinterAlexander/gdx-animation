package com.brashmonkey.spriter;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

/**
 * Represents a .SCML project file for spriter
 *
 * @author Alexander Winter
 */
public class SCMLProject
{
	private final IntMap<TextureRegionDrawable> assets;
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
	 * @return the entity with the given name
	 *
	 * @throws EntityNotFoundException if the spriter entity could not be found
	 */
	public SpriterEntity getEntity(String name)
	{
		for(SpriterEntity entity : entities)
			if(entity.getName().equals(name))
				return new SpriterEntity(entity);

		throw new EntityNotFoundException(name);
	}

	public void putAsset(int folderID, int fileID, TextureRegionDrawable asset)
	{
		assets.put(getAssetKey(folderID, fileID), asset);
	}

	public TextureRegionDrawable getAsset(int folderID, int fileID)
	{
		return assets.get(getAssetKey(folderID, fileID));
	}

	public Array<SpriterEntity> getSourceEntities()
	{
		return entities;
	}

	public static int getAssetKey(int folder, int file)
	{
		return (folder << 16) + file;
	}
}
