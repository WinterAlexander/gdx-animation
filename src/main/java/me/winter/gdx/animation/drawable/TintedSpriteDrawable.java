package me.winter.gdx.animation.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import me.winter.gdx.animation.Sprite;

/**
 * Drawable tinted with a specified color
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public class TintedSpriteDrawable implements SpriteDrawable
{
	private final SpriteDrawable drawable;
	private final Color color = new Color();

	public TintedSpriteDrawable(SpriteDrawable drawable, Color color)
	{
		this.drawable = drawable;
		setColor(color);
	}

	@Override
	public void draw(Sprite sprite, Batch batch)
	{
		if(drawable == null)
			return;

		float prevColor = batch.getPackedColor();
		batch.setColor(color);

		drawable.draw(sprite, batch);

		batch.setColor(prevColor);
	}

	public void setColor(Color color)
	{
		this.color.set(color);
	}
}
