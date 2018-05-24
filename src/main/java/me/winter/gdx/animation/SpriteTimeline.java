package me.winter.gdx.animation;

import com.badlogic.gdx.utils.Array;

/**
 * Timeline of a Sprite, having a z-index.
 * <p>
 * Created on 2017-07-15.
 *
 * @author Alexander Winter
 */
public class SpriteTimeline extends Timeline
{
	private final int zIndex;

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
}
