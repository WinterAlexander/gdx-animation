package me.winter.gdx.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import me.winter.gdx.animation.drawable.SpriteDrawable;

/**
 * Represents an object in a Spriter SCML file. A file has the same properties
 * as a bone with an alpha and file extension.
 *
 * @author Alexander Winter
 */
public class Sprite extends AnimatedPart
{
	private SpriteDrawable drawable;
	private float alpha;

	public Sprite()
	{
		this(null, new Vector2(0, 0), new Vector2(1f, 1f), 0f, 1f);
	}

	public Sprite(Sprite other)
	{
		super(other);

		this.drawable = other.drawable;
		this.alpha = other.alpha;
	}

	public Sprite(SpriteDrawable drawable, Vector2 position, Vector2 scale, float angle, float alpha)
	{
		super(position, scale, angle);
		this.alpha = alpha;
		this.drawable = drawable;
	}

	public void draw(Batch batch)
	{
		if(drawable != null)
			drawable.draw(this, batch);
	}

	/**
	 * Sets the values of this object to the values of the given object.
	 *
	 * @param object the object
	 */
	@Override
	public void set(AnimatedPart object)
	{
		super.set(object);

		if(object instanceof Sprite)
		{
			this.alpha = ((Sprite)object).alpha;
			this.drawable = ((Sprite)object).drawable;
		}
	}

	public SpriteDrawable getDrawable()
	{
		return drawable;
	}

	public void setDrawable(SpriteDrawable drawable)
	{
		this.drawable = drawable;
	}

	public float getAlpha()
	{
		return alpha;
	}

	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}
}
