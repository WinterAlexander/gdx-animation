package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2017-07-15.
 */
public class SpriteTimeline extends Timeline
{
	private int zIndex;

	public SpriteTimeline(String name, Array<TimelineKey> timelineKeys, int zIndex)
	{
		super(name, timelineKeys);
		this.zIndex = zIndex;
	}

	public SpriteTimeline(SpriteTimeline timeline)
	{
		super(timeline);
		this.zIndex = timeline.zIndex;
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
