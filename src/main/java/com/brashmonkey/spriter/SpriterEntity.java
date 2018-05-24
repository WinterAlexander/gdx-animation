package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Represents a set of animation under a common name. Usually associated to a
 * single game object.
 *
 * @author Alexander Winter
 */
public class SpriterEntity
{
	private final String name;
	private final Array<Animation> animations;

	public SpriterEntity(String name)
	{
		this(name, new Array<>());
	}

	public SpriterEntity(String name, Array<Animation> animations)
	{
		this.name = name;
		this.animations = animations;
	}

	public SpriterEntity(SpriterEntity entity)
	{
		this.name = entity.name;
		this.animations = new Array<>(entity.animations.size);

		for(Animation animation : entity.animations)
			animations.add(new Animation(animation));
	}

	/**
	 * Set the drawable of the name specified sprite in all animations for all
	 * timelines
	 * @param name name of the sprite
	 * @param drawable drawable to set
	 */
	public void setSpriteDrawable(String name, SpriterDrawable drawable)
	{
		for(Animation animation : animations)
			for(Timeline timeline : animation.getTimelines())
				if(timeline.getName().equals(name))
					for(TimelineKey key : timeline.getKeys())
						if(key.getObject() instanceof SpriterSprite)
							((SpriterSprite)key.getObject()).setDrawable(drawable);
	}

	public void setAlpha(float alpha)
	{
		for(Animation animation : animations)
			animation.setAlpha(alpha);
	}

	/**
	 * Returns an Animation for the specified index
	 *
	 * @param index the index of the animation
	 */
	public Animation getAnimation(int index)
	{
		return animations.get(index);
	}

	/**
	 * Returns an Animation for the specified name
	 *
	 * @param name name of the animation
	 */
	public Animation getAnimation(String name)
	{
		for(Animation animation : animations)
			if(animation.getName().equals(name))
				return animation;

		return null;
	}

	public String getName()
	{
		return name;
	}

	public Array<Animation> getAnimations()
	{
		return animations;
	}
}
