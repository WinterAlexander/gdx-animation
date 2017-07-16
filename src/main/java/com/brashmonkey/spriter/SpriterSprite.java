package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents an object in a Spriter SCML file. A file has the same properties as a bone with an alpha and file
 * extension.
 *
 * @author Trixt0r
 */
public class SpriterSprite extends SpriterObject
{
	private SpriterAsset asset;
	private float alpha;

	public SpriterSprite()
	{
		this(null, new Vector2(0, 0), new Vector2(1f, 1f), new Vector2(0f, 1f), 0f, 1f);
	}

	public SpriterSprite(SpriterSprite other)
	{
		super(other);
		this.asset = new SpriterAsset(other.asset);
		this.alpha = other.alpha;
	}

	public SpriterSprite(SpriterAsset asset, Vector2 position, Vector2 scale, Vector2 pivot, float angle, float alpha)
	{
		super(position, scale, pivot, angle);
		this.alpha = alpha;
		this.asset = asset;
	}

	public void draw(Batch batch)
	{
		if(asset == null || asset.getTexture() == null)
			return;

		float width = asset.getRegionWidth();
		float height = asset.getRegionHeight();

		float originX = width * pivot.x;
		float originY = height * pivot.y;

		float prevColor = batch.getPackedColor();

		batch.getColor().a *= alpha;
		batch.setColor(batch.getColor());

		batch.draw(asset, position.x - originX, position.y - originY, originX, originY, width, height, scale.x, scale.y, angle);

		batch.setColor(prevColor);
	}

	/**
	 * Sets the values of this object to the values of the given object.
	 *
	 * @param object the object
	 */
	@Override
	public void set(SpriterObject object)
	{
		super.set(object);

		if(object instanceof SpriterSprite)
		{
			this.alpha = ((SpriterSprite)object).alpha;
			this.asset = ((SpriterSprite)object).asset;
		}
	}


	public SpriterAsset getAsset()
	{
		return asset;
	}

	public void setAsset(SpriterAsset asset)
	{
		this.asset = asset;
	}

	public float getAlpha()
	{
		return alpha;
	}

	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}
}
