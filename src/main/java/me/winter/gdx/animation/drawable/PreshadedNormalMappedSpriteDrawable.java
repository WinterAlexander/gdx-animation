package me.winter.gdx.animation.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.winter.gdx.animation.Sprite;
import me.winter.gdx.animation.SpriterLightingBatch;

/**
 * Draws the content of a texture along with its normal map
 * <p>
 * Created on 2020-05-05.
 *
 * @author Alexander Winter
 */
public class PreshadedNormalMappedSpriteDrawable extends TextureSpriteDrawable
{
	private final TextureRegion normalMap, preshaded;

	public PreshadedNormalMappedSpriteDrawable(TextureRegion flat,
	                                           TextureRegion normalMap,
	                                           TextureRegion preshaded,
	                                           float pivotX, float pivotY)
	{
		super(flat, pivotX, pivotY);
		this.normalMap = normalMap;
		this.preshaded = preshaded;
	}

	public PreshadedNormalMappedSpriteDrawable(TextureRegion flat,
	                                           TextureRegion normalMap,
	                                           TextureRegion preshaded,
	                                           float pivotX, float pivotY,
	                                           float width, float height)
	{
		super(flat, pivotX, pivotY, width, height);
		this.normalMap = normalMap;
		this.preshaded = preshaded;
	}

	@Override
	public void draw(Sprite sprite, Batch batch)
	{
		if(region == null
				|| region.getTexture() == null
				|| normalMap == null
				|| normalMap.getTexture() == null
				|| preshaded == null
				|| preshaded.getTexture() == null
				|| !(batch instanceof SpriterLightingBatch))
			return;

		float originX = width * getPivotX();
		float originY = height * getPivotY();

		float prevColor = batch.getPackedColor();

		Color tmp = batch.getColor();
		tmp.a *= sprite.getAlpha();
		batch.setColor(tmp);

		((SpriterLightingBatch)batch).drawNormalMapped(region, normalMap, preshaded,
				sprite.getPosition().x - originX,
				sprite.getPosition().y - originY,
				originX,
				originY,
				width,
				height,
				sprite.getScale().x,
				sprite.getScale().y,
				sprite.getAngle());

		batch.setPackedColor(prevColor);
	}

	public TextureRegion getNormalMap()
	{
		return normalMap;
	}

	public TextureRegion getPreshaded()
	{
		return preshaded;
	}
}
