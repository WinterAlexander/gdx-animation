package com.brashmonkey.spriter;

import com.badlogic.gdx.math.Vector2;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-03-17.
 */
public class SpriterBone extends SpriterObject
{
	public SpriterBone(Vector2 position, Vector2 scale, Vector2 pivot, float angle)
	{
		super(position, scale, pivot, angle);
	}

	public SpriterBone(SpriterBone other)
	{
		super(other);
	}
}
