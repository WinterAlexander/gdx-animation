package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.IdentityMap;

import java.util.Map;

/**
 * Represents a bone reference in a Spriter SCML file. A bone reference holds a {@link #timeline} and a
 * {@link #key}. A bone reference may have a parent reference.
 *
 * @author Trixt0r
 */
public class BoneRef
{
	public final int key, timeline;
	public final BoneRef parent;

	public BoneRef(int timeline, int key, BoneRef parent)
	{
		this.timeline = timeline;
		this.key = key;
		this.parent = parent;
	}

	public BoneRef(BoneRef other, IdentityMap<BoneRef, BoneRef> graphIsomorphism)
	{
		this.key = other.key;
		this.timeline = other.timeline;

		if(other.parent != null)
			this.parent = other.parent.clone(graphIsomorphism);
		else
			this.parent = null;
	}

	public BoneRef clone(IdentityMap<BoneRef, BoneRef> graphIsomorphism)
	{
		if(graphIsomorphism.containsKey(this))
			return graphIsomorphism.get(this);

		BoneRef ref = new BoneRef(this, graphIsomorphism);
		graphIsomorphism.put(this, ref);
		return ref;
	}
}
