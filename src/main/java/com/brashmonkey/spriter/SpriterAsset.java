package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A TextureRegion with a pivot
 *
 * @author Winter
 */
public class SpriterAsset extends TextureRegion
{
	private String name;
	private float pivotX, pivotY;

	/**
	 * Constructs a SpriterAsset
	 *
	 * @param region TextureRegion of this asset
	 * @param name Name of this asset
	 * @param pivotX x position of the pivot point for this asset
	 * @param pivotY y position of the pivot point for this asset
	 */
	public SpriterAsset(TextureRegion region, String name, float pivotX, float pivotY)
	{
		if(region != null && region.getTexture() != null)
			setRegion(region);

		this.name = name;
		this.pivotX = pivotX;
		this.pivotY = pivotY;
	}

	/**
	 * Copies a SpriterAsset
	 *
	 * @param other SpriterAsset
	 */
	public SpriterAsset(SpriterAsset other)
	{
		if(other.getTexture() != null)
			setRegion(other);

		this.name = other.name;
		this.pivotX = other.pivotX;
		this.pivotY = other.pivotY;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public float getPivotX()
	{
		return pivotX;
	}

	public void setPivotX(float pivotX)
	{
		this.pivotX = pivotX;
	}

	public float getPivotY()
	{
		return pivotY;
	}

	public void setPivotY(float pivotY)
	{
		this.pivotY = pivotY;
	}
}
