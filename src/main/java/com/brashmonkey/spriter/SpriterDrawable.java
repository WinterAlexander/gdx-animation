package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Undocumented :(
 * <p>
 * Created on 2017-07-16.
 *
 * @author Alexander Winter
 */
public class SpriterDrawable implements Comparable<SpriterDrawable>
{
	private SpriterSprite sprite;
	private int zIndex;

	public SpriterDrawable(SpriterSprite sprite, int zIndex)
	{
		this.sprite = sprite;
		this.zIndex = zIndex;
	}

	public void draw(Batch batch)
	{
		if(sprite.getAsset() == null || sprite.getAsset().getTexture() == null)
			return;

		float width = sprite.getAsset().getRegionWidth();
		float height = sprite.getAsset().getRegionHeight();

		float originX = width * sprite.getAsset().getPivotX();
		float originY = height * sprite.getAsset().getPivotY();

		float prevColor = batch.getPackedColor();

		Color tmp = batch.getColor();
		tmp.a *= sprite.getAlpha();
		batch.setColor(tmp);

		batch.draw(sprite.getAsset(),
				sprite.getPosition().x - originX,
				sprite.getPosition().y - originY,
				originX,
				originY,
				width,
				height,
				sprite.getScale().x,
				sprite.getScale().y,
				sprite.getAngle());

		batch.setColor(prevColor);
	}


	@Override
	public int compareTo(SpriterDrawable other)
	{
		return Integer.compare(zIndex, other.getZIndex());
	}

	public SpriterSprite getSprite()
	{
		return sprite;
	}

	public void setSprite(SpriterSprite sprite)
	{
		this.sprite = sprite;
	}

	public int getZIndex()
	{
		return zIndex;
	}

	public void setZIndex(int zIndex)
	{
		this.zIndex = zIndex;
	}
}
