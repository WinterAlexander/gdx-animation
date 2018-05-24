package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Draws an animation into a SpriterSprite
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public class AnimationDrawable implements SpriterDrawable
{
	private Animation animation;
	private float scale;

	public AnimationDrawable(Animation animation, float scale)
	{
		this.animation = animation;
		this.scale = scale;
	}

	@Override
	public void draw(SpriterSprite sprite, Batch batch)
	{
		SpriterObject obj = animation.getRoot();

		obj.getPosition().set(sprite.getPosition());
		obj.getScale().set(sprite.getScale().x * scale, sprite.getScale().y * scale);
		obj.setAngle(sprite.getAngle());

		animation.setAlpha(sprite.getAlpha());
		animation.update(0f);
		animation.draw(batch);
	}
}
