package me.winter.gdx.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import me.winter.gdx.animation.drawable.SpriteDrawable;
import me.winter.gdx.animation.drawable.TintedSpriteDrawable;

/**
 * Represents a set of animation under a common name. Usually associated to a
 * single game object.
 *
 * @author Alexander Winter
 */
public class Entity
{
	private final String name;
	private final Array<Animation> animations;

	public Entity(String name)
	{
		this(name, new Array<>());
	}

	public Entity(String name, Array<Animation> animations)
	{
		this.name = name;
		this.animations = animations;
	}

	public Entity(Entity entity)
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
	public void setSpriteDrawable(String name, SpriteDrawable drawable)
	{
		for(Animation animation : animations)
			for(Timeline timeline : animation.getTimelines())
				if(timeline.getName().equals(name))
					for(TimelineKey key : timeline.getKeys())
						if(key.getObject() instanceof Sprite)
							((Sprite)key.getObject()).setDrawable(drawable);
	}

	public void tintSprite(String name, Color color)
	{
		for(Animation animation : animations)
			for(Timeline timeline : animation.getTimelines())
				if(timeline.getName().equals(name))
					for(TimelineKey key : timeline.getKeys())
						if(key.getObject() instanceof Sprite)
						{
							Sprite sprite = (Sprite)key.getObject();

							if(!(sprite.getDrawable() instanceof TintedSpriteDrawable))
								sprite.setDrawable(new TintedSpriteDrawable(((Sprite)key.getObject()).getDrawable(), color));
							else
								((TintedSpriteDrawable)sprite.getDrawable()).setColor(color);
						}
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

	public int getAnimationId(String name)
	{
		return animations.indexOf(getAnimation(name), true);
	}

	public int getAnimationId(Animation animation)
	{
		return animations.indexOf(animation, true);
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
