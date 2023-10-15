package com.winteralexander.gdx.animation.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.winteralexander.gdx.animation.Sprite;

/**
 * Draws multiple sprite drawables, one on top of to the other
 * <p>
 * Created on 2018-07-21.
 *
 * @author Alexander Winter
 */
public class MultiSpriteDrawable implements SpriteDrawable {
	private final SpriteDrawable[] drawables;

	public MultiSpriteDrawable(SpriteDrawable... drawables) {
		this.drawables = drawables;
	}

	@Override
	public void draw(Sprite sprite, Batch batch) {
		for(SpriteDrawable drawable : drawables)
			drawable.draw(sprite, batch);
	}
}
