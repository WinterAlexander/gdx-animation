package me.winter.gdx.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Specification of a customized {@link Batch} that offers support for normal
 * mapped draw calls
 * <p>
 * Created on 2020-05-05.
 *
 * @author Alexander Winter
 */
public interface SpriterLightingBatch extends Batch
{
	/**
	 * Draws the specified texture region with its specified normal map along
	 * with the draw parameters. The normal map should be rotated and scaled
	 * as well as the region.
	 *
	 * @param region region to draw
	 * @param normal normal map
	 * @param x x position
	 * @param y y position
	 * @param originX origin x of the rotation and scaling
	 * @param originY origin y of the rotation and scaling
	 * @param width width to draw
	 * @param height height to draw
	 * @param scaleX scale in x
	 * @param scaleY scale in y
	 * @param rotation rotation of the texture
	 */
	void drawNormalMapped(TextureRegion region,
	          TextureRegion normal,
	          float x, float y,
	          float originX, float originY,
	          float width, float height,
	          float scaleX, float scaleY,
	          float rotation);
}
