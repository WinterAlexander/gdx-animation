package me.winter.gdx.animation;

import com.badlogic.gdx.utils.IdentityMap;

/**
 * Represents a bone reference in a Spriter SCML file. A bone reference holds a {@link #timeline} and a
 * {@link #key}. A bone reference may have a parent reference.
 *
 * @author Alexander Winter
 */
public class ObjectRef
{
	public final int key, timeline;
	public final ObjectRef parent;

	public ObjectRef(int timeline, int key, ObjectRef parent)
	{
		this.timeline = timeline;
		this.key = key;
		this.parent = parent;
	}

	public ObjectRef(ObjectRef other, IdentityMap<ObjectRef, ObjectRef> graphIsomorphism)
	{
		this.key = other.key;
		this.timeline = other.timeline;

		if(other.parent != null)
			this.parent = other.parent.clone(graphIsomorphism);
		else
			this.parent = null;
	}

	public ObjectRef clone(IdentityMap<ObjectRef, ObjectRef> graphIsomorphism)
	{
		if(graphIsomorphism.containsKey(this))
			return graphIsomorphism.get(this);

		ObjectRef ref = new ObjectRef(this, graphIsomorphism);
		graphIsomorphism.put(this, ref);
		return ref;
	}
}
