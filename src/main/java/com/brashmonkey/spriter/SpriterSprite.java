package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents an object in a Spriter SCML file. A file has the same properties
 * as a bone with an alpha and file extension.
 *
 * @author Trixt0r
 */
public class SpriterSprite extends SpriterObject
{
	private SpriterDrawable drawable;
	private float alpha;

	public SpriterSprite()
	{
		this(null, new Vector2(0, 0), new Vector2(1f, 1f), 0f, 1f);
	}

	public SpriterSprite(SpriterSprite other)
	{
		super(other);

		this.drawable = other.drawable;
		this.alpha = other.alpha;
	}

	public SpriterSprite(SpriterDrawable drawable, Vector2 position, Vector2 scale, float angle, float alpha)
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
	public void set(SpriterObject object)
	{
		super.set(object);

		if(object instanceof SpriterSprite)
		{
			this.alpha = ((SpriterSprite)object).alpha;
			this.drawable = ((SpriterSprite)object).drawable;
		}
	}

	public SpriterDrawable getDrawable()
	{
		return drawable;
	}

	public void setDrawable(SpriterDrawable drawable)
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
