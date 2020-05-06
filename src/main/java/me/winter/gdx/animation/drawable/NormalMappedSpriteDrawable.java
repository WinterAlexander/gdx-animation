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
public class NormalMappedSpriteDrawable extends TextureSpriteDrawable
{
	private final TextureRegion normalMap;

	public NormalMappedSpriteDrawable(TextureRegion region,
	                                  TextureRegion normalMap,
	                                  float pivotX, float pivotY)
	{
		super(region, pivotX, pivotY);
		this.normalMap = normalMap;
	}

	public NormalMappedSpriteDrawable(TextureRegion region,
	                                  TextureRegion normalMap,
	                                  float pivotX, float pivotY,
	                                  float width, float height)
	{
		super(region, pivotX, pivotY, width, height);
		this.normalMap = normalMap;
	}

	@Override
	public void draw(Sprite sprite, Batch batch)
	{
		if(region == null
				|| region.getTexture() == null)
			return;

		if(normalMap == null
				|| normalMap.getTexture() == null
				|| !(batch instanceof SpriterLightingBatch))
		{
			super.draw(sprite, batch);
			return;
		}

		float originX = width * getPivotX();
		float originY = height * getPivotY();

		float prevColor = batch.getPackedColor();

		Color tmp = batch.getColor();
		tmp.a *= sprite.getAlpha();
		batch.setColor(tmp);

		((SpriterLightingBatch)batch).drawNormalMapped(region, normalMap,
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
}
