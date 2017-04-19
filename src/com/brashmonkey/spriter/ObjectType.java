package com.brashmonkey.spriter;

/**
 * Represents the object types Spriter supports.
 *
 * @author Trixt0r
 */
public enum ObjectType
{
	SPRITE, BONE;

	/**
	 * Returns the object type for the given name
	 *
	 * @param name the name of the type
	 * @return the object type, SPRITE is the default value
	 */
	public static ObjectType getType(String name)
	{
		if(name.equalsIgnoreCase("bone"))
			return BONE;
		else if(name.equalsIgnoreCase("object") || name.equalsIgnoreCase("sprite"))
			return SPRITE;
		return null;
	}
}
