package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.IdentityMap;

import java.util.Map;

/**
 * Represents an object reference in a Spriter SCML file. An object reference extends a {@link BoneRef} with a {@link
 * #zIndex}, which indicates when the object has to be drawn.
 *
 * @author Trixt0r
 */
public class ObjectRef extends BoneRef implements Comparable<ObjectRef>
{
	private int zIndex;

	public ObjectRef(int timeline, int key, BoneRef parent, int zIndex)
	{
		super(timeline, key, parent);
		this.zIndex = zIndex;
	}

	public ObjectRef(ObjectRef other, IdentityMap<BoneRef, BoneRef> graphIsomorphism)
	{
		super(other, graphIsomorphism);
		this.zIndex = other.zIndex;
	}

	public int compareTo(ObjectRef o)
	{
		return Integer.compare(zIndex, o.zIndex);
	}

	public ObjectRef clone(IdentityMap<BoneRef, BoneRef> graphIsomorphism)
	{
		if(graphIsomorphism.containsKey(this))
			return (ObjectRef)graphIsomorphism.get(this);

		ObjectRef ref = new ObjectRef(this, graphIsomorphism);
		graphIsomorphism.put(this, ref);
		return ref;
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
