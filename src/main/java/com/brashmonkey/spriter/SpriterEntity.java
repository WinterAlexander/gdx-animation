package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Represents an entity of a Spriter SCML file. An entity holds {@link Animation}s, a {@link #name}.
 *
 * @author Trixt0r
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

	public void setAsset(String name, SpriterAsset newAsset)
	{
		for(Animation animation : animations)
			for(Timeline timeline : animation.getTimelines())
				for(TimelineKey key : timeline.getKeys())
					if(key.getObject() instanceof SpriterSprite)
						if(((SpriterSprite)key.getObject()).getAsset().getName().equals(name))
							((SpriterSprite)key.getObject()).setAsset(newAsset);
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
