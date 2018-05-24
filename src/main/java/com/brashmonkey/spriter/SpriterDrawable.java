package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Something in spriter that can draw itself given a SpriterSprite.
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public interface SpriterDrawable
{
	void draw(SpriterSprite sprite, Batch batch);
}
