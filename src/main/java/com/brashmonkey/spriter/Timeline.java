package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Represents a time line in a Spriter SCML file. A time line holds an {@link #id}, a {@link #name} and at least one
 * {@link TimelineKey}.
 *
 * @author Trixt0r
 */
public class Timeline
{
	private String name;

	private final Array<TimelineKey> keys;

	public Timeline(String name, Array<TimelineKey> timelineKeys)
	{
		this.name = name;
		this.keys = timelineKeys;
	}

	public Timeline(Timeline timeline)
	{
		this.name = timeline.name;
		this.keys = new Array<>(timeline.getKeys().size);

		for(TimelineKey key : timeline.getKeys())
			keys.add(new TimelineKey(key));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Array<TimelineKey> getKeys()
	{
		return keys;
	}
}
