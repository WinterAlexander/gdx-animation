package me.winter.gdx.animation.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import me.winter.gdx.animation.Sprite;

/**
 * Draws the content of a texture region into a Sprite
 *
 * @author Alexander Winter
 */
public class TextureSpriteDrawable implements SpriteDrawable
{
	protected final TextureRegion region;
	protected final float pivotX, pivotY;
	protected final float width, height;

	public TextureSpriteDrawable(TextureRegion region, float pivotX, float pivotY)
	{
		this(region,
				pivotX,
				pivotY,
				region != null ? region.getRegionWidth() : 0f,
				region != null ? region.getRegionHeight() : 0f);
	}

	/**
	 * Constructs a SpriterAsset
	 *
	 * @param region TextureRegion of this asset
	 * @param pivotX x position of the pivot point for this asset
	 * @param pivotY y position of the pivot point for this asset
	 * @param width width of the asset
	 * @param height height of the asset
	 */
	public TextureSpriteDrawable(TextureRegion region, float pivotX, float pivotY, float width, float height)
	{
		this.region = region;
		this.pivotX = pivotX;
		this.pivotY = pivotY;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Sprite sprite, Batch batch)
	{
		if(region == null || region.getTexture() == null)
			return;

		float originX = width * getPivotX();
		float originY = height * getPivotY();

		float prevColor = batch.getPackedColor();

		Color tmp = batch.getColor();
		tmp.a *= sprite.getAlpha();
		batch.setColor(tmp);

		batch.draw(region,
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

	public TextureRegion getRegion()
	{
		return region;
	}

	public float getPivotX()
	{
		return pivotX;
	}

	public float getPivotY()
	{
		return pivotY;
	}
}
