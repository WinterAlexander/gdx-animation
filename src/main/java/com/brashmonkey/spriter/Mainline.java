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
	 * Returns the last previous MainlineKey before specified time
	 *
	 * @param time the time a key has to be before
	 * @param wrapAround true if should wrap around the timeline, otherwise false
	 *
	 * @return last previous key before specified time, when not found first one is returned
	 */
	public MainlineKey getKeyBeforeTime(int time, boolean wrapAround)
	{
		MainlineKey found = wrapAround ? keys.get(keys.size - 1) : keys.get(0);

		for(MainlineKey key : keys)
		{
			if(key.time > time)
				break;
			found = key;
		}

		return found;
	}

	public MainlineKey next(MainlineKey previous, boolean wrapAround)
	{
		int index = keys.indexOf(previous, true);

		if(index + 1 == keys.size)
			return wrapAround ? keys.get(0) : keys.get(keys.size - 1);

		return keys.get(index + 1);
	}

	public Array<MainlineKey> getKeys()
	{
		return keys;
	}
}
