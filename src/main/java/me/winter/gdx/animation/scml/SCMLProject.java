package me.winter.gdx.animation.scml;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.winter.gdx.animation.EntityNotFoundException;
import me.winter.gdx.animation.Entity;
import me.winter.gdx.animation.drawable.TextureSpriteDrawable;

/**
 * Represents a .SCML project file for Spriter.
 *
 * @author Alexander Winter
 */
public class SCMLProject
{
	private final IntMap<TextureSpriteDrawable> assets;
	private final Array<Entity> entities;

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
	public Entity getEntity(String name)
	{
		for(Entity entity : entities)
			if(entity.getName().equals(name))
				return new Entity(entity);

		throw new EntityNotFoundException(name);
	}

	public void putAsset(int folderID, int fileID, TextureSpriteDrawable asset)
	{
		assets.put(getAssetKey(folderID, fileID), asset);
	}

	public TextureSpriteDrawable getAsset(int folderID, int fileID)
	{
		return assets.get(getAssetKey(folderID, fileID));
	}

	public Array<Entity> getSourceEntities()
	{
		return entities;
	}

	public static int getAssetKey(int folder, int file)
	{
		return (folder << 16) + file;
	}
}
