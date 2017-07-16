package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;

/**
 * Represents a mainline in a Spriter SCML file. A mainline holds only keys and occurs only once in an animation. The
 * mainline is responsible for telling which draw order the sprites have and how the objects are related to each other,
 * i.e. which bone is the root and which objects are the children.
 *
 * @author Trixt0r
 */
public class Mainline
{
	private final Array<MainlineKey> keys;

	public Mainline(int keys)
	{
		this.keys = new Array<>(keys);
	}

	public Mainline(Mainline other)
	{
		this.keys = new Array<>(other.keys.size);

		for(MainlineKey key : other.keys)
			keys.add(new MainlineKey(key));
	}

	/**
	 * Returns a {@link MainlineKey} before the given time.
	 *
	 * @param time the time a key has to be before
	 * @return a key which has a time value before the given one, wraps around
	 */
	public MainlineKey getKeyBeforeTime(int time)
	{
		MainlineKey found = keys.get(keys.size - 1);

		for(MainlineKey key : keys)
		{
			if(key.time > time)
				break;
			found = key;
		}

		return found;
	}

	public Array<MainlineKey> getKeys()
	{
		return keys;
	}
}
