package me.winter.gdx.animation;

/**
 * Thrown when the specified Entity could not be found
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public class EntityNotFoundException extends RuntimeException
{
	public EntityNotFoundException(String name)
	{
		super("Entity with name " + name + " could not be found.");
	}
}
