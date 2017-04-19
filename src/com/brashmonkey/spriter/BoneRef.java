package com.brashmonkey.spriter;

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

	public BoneRef(BoneRef other)
	{
		this.key = other.key;
		this.timeline = other.timeline;

		if(other.parent != null)
			this.parent = new BoneRef(other.parent); //todo fix this
		else
			this.parent = null;
	}
}
