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
	private int id;
	private String name;

	private final Array<TimelineKey> keys;

	public Timeline(int id, String name, Array<TimelineKey> timelineKeys)
	{
		this.id = id;
		this.name = name;
		this.keys = timelineKeys;
	}

	public Timeline(Timeline timeline)
	{
		this.id = timeline.id;
		this.name = timeline.name;
		this.keys = new Array<>(timeline.getKeys().size);

		for(TimelineKey key : timeline.getKeys())
			keys.add(new TimelineKey(key));
	}

	@Override
	public Timeline clone()
	{
		return new Timeline(this);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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
