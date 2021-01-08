package me.winter.gdx.animation.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import me.winter.gdx.animation.AnimatedPart;
import me.winter.gdx.animation.Animation;
import me.winter.gdx.animation.Sprite;

/**
 * Draws an animation into a Sprite
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public class AnimationSpriteDrawable implements SpriteDrawable
{
	private final Animation animation;
	private final float scale;

	public AnimationSpriteDrawable(Animation animation, float scale)
	{
		this.animation = animation;
		this.scale = scale;
	}

	@Override
	public void draw(Sprite sprite, Batch batch)
	{
		AnimatedPart obj = animation.getRoot();

		obj.getPosition().set(sprite.getPosition());
		obj.getScale().set(sprite.getScale().x * scale, sprite.getScale().y * scale);
		obj.setAngle(sprite.getAngle());

		animation.setAlpha(sprite.getAlpha());
		animation.update(0f);
		animation.draw(batch);
	}
}
