package com.winteralexander.gdx.animation.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.winteralexander.gdx.animation.Sprite;

/**
 * Drawable tinted with a specified color
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public class TintedSpriteDrawable implements SpriteDrawable {
	private final SpriteDrawable drawable;
	private Color color;

	public TintedSpriteDrawable(SpriteDrawable drawable, Color color) {
		this.drawable = drawable;

		if(color == null)
			this.color = Color.WHITE;
		else
			this.color = color;
	}

	@Override
	public void draw(Sprite sprite, Batch batch) {
		if(drawable == null)
			return;

		float prevColor = batch.getPackedColor();
		batch.setColor(batch.getColor().mul(color));

		drawable.draw(sprite, batch);

		batch.setPackedColor(prevColor);
	}

	public SpriteDrawable getInnerDrawable() {
		return drawable;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if(color == null)
			this.color = Color.WHITE;
		else
			this.color = color;
	}
}
