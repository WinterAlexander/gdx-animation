package com.brashmonkey.spriter;

import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.math.Curve;

/**
 * Represents a mainline key in a Spriter SCML file. A mainline key holds a {@link #time}, a {@link
 * #curve} and lists of bone and object references which build a tree hierarchy.
 *
 * @author Trixt0r
 */
public class MainlineKey
{
	public final int time;
	public final Array<BoneRef> boneRefs;
	public final Array<ObjectRef> objectRefs;
	public final Curve curve;

	public MainlineKey(int time, Curve curve, int boneRefs, int objectRefs)
	{
		this.time = time;
		this.curve = curve;
		this.boneRefs = new Array<>(boneRefs);
		this.objectRefs = new Array<>(objectRefs);
	}

	public MainlineKey(MainlineKey other)
	{
		this.time = other.time;
		this.curve = other.curve;
		this.boneRefs = new Array<>(other.boneRefs.size);
		this.objectRefs = new Array<>(other.objectRefs.size);

		//Map<BoneRef, BoneRef> boneIso = new IdentityHashMap<>();

		for(BoneRef ref : other.boneRefs)
			boneRefs.add(new BoneRef(ref/*, boneIso*/));


		//Map<ObjectRef, ObjectRef> objIso = new IdentityHashMap<>();

		for(ObjectRef ref : other.objectRefs)
			objectRefs.add(new ObjectRef(ref/*, objIso*/));
	}

	/**
	 * Returns a {@link BoneRef} with the given index.
	 *
	 * @param index the index of the bone reference
	 * @return the bone reference or null if no reference exists with the given index
	 */
	public BoneRef getBoneRef(int index)
	{
		if(index < 0 || index >= this.boneRefs.size)
			return null;

		else
			return this.boneRefs.get(index);
	}
}
