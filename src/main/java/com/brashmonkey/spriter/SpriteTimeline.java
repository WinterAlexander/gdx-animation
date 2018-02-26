package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Undocumented :(
 * <p>
 * Created on 2017-07-15.
 *
 * @author Alexander Winter
 */
public class SpriteTimeline extends Timeline
{
	private int zIndex;

	public SpriteTimeline(int id, String name, Array<TimelineKey> timelineKeys, int zIndex)
	{
		super(id, name, timelineKeys);
		this.zIndex = zIndex;
	}

	public SpriteTimeline(SpriteTimeline timeline)
	{
		super(timeline);
		this.zIndex = timeline.zIndex;
	}

	@Override
	public SpriteTimeline clone()
	{
		return new SpriteTimeline(this);
	}

	public int getZIndex()
	{
		return zIndex;
	}

	public void setZIndex(int zIndex)
	{
		this.zIndex = zIndex;
	}
}
