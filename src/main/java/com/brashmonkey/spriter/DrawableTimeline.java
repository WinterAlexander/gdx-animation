package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Timeline of a drawable SpriterObject, having a z index
 * <p>
 * Created on 2017-07-15.
 *
 * @author Alexander Winter
 */
public class DrawableTimeline extends Timeline
{
	private int zIndex;

	public DrawableTimeline(int id, String name, Array<TimelineKey> timelineKeys, int zIndex)
	{
		super(id, name, timelineKeys);
		this.zIndex = zIndex;
	}

	public DrawableTimeline(DrawableTimeline timeline)
	{
		super(timeline);
		this.zIndex = timeline.zIndex;
	}

	@Override
	public DrawableTimeline clone()
	{
		return new DrawableTimeline(this);
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
