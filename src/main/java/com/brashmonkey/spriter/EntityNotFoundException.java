package com.brashmonkey.spriter;

/**
 * Thrown when the specified SpriterEntity could not be found
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public class EntityNotFoundException extends RuntimeException
{
	public EntityNotFoundException(String name)
	{
		super("SpriterEntity with name " + name + " could not be found.");
	}
}
