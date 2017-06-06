package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.math.Curve;

import static java.lang.Float.isInfinite;
import static java.lang.Float.isNaN;

/**
 * Represents an animation of a Spriter SCML file. An animation holds {@link Timeline}s and a {@link Mainline} to
 * animate objects. Furthermore it holds a {@link #length}, a {@link #name} and whether it is {@link
 * #looping} or not.
 *
 * @author Trixt0r
 */
public class Animation
{
	private String name;

	private Mainline mainline;
	private final Array<Timeline> timelines;
	private int length;

	private boolean looping;
	private MainlineKey currentKey;
	private TimelineKey[] tweenedKeys, unmappedTweenedKeys;
	private boolean prepared;

	private float time = 0;
	private float speed = 1f, alpha = 1f;

	private SpriterObject root = new SpriterObject();

	public Animation(Mainline mainline, String name, int length, boolean looping, int timelines)
	{
		this.name = name;

		this.length = length;
		this.looping = looping;

		this.mainline = mainline;
		this.timelines = new Array<>(timelines);
		this.prepared = false;
	}

	public Animation(Animation animation)
	{
		this.name = animation.getName();

		this.length = animation.length;
		this.looping = animation.looping;

		this.mainline = new Mainline(animation.mainline);
		this.timelines = new Array<>(animation.getTimelines().size);

		for(Timeline timeline : animation.getTimelines())
			timelines.add(new Timeline(timeline));

		prepare();
	}

	public void prepare()
	{
		tweenedKeys = new TimelineKey[timelines.size];
		unmappedTweenedKeys = new TimelineKey[timelines.size];

		for(int i = 0; i < timelines.size; i++)
		{
			tweenedKeys[i] = new TimelineKey();
			unmappedTweenedKeys[i] = new TimelineKey();
			tweenedKeys[i].setObject(new SpriterSprite());
			unmappedTweenedKeys[i].setObject(new SpriterSprite());
		}

		if(mainline.getKeys().size > 0)
			currentKey = mainline.getKeys().get(0);
		prepared = true;
	}

	public void draw(Batch batch)
	{
		float prevColor = batch.getPackedColor();
		Color tmp = batch.getColor();
		tmp.a = alpha;
		batch.setColor(tmp);

		for(ObjectRef objectRef : currentKey.objectRefs)
			((SpriterSprite)unmappedTweenedKeys[objectRef.timeline].getObject()).draw(batch);

		batch.setColor(prevColor);
	}

	/**
	 * Updates this player. This means the current time gets increased by {@link #speed} and is applied to the current
	 * animation.
	 */
	public void update()
	{
		this.update(1 / 60f); //assume 60 fps by default
	}

	/**
	 * Updates this player. This means the current time gets increased by {@link #speed} and is applied to the current
	 * animation.
	 */
	public void update(float delta)
	{
		if(!prepared)
			throw new IllegalStateException("Animation not prepared");

		time = time + speed * delta;

		if(time >= length)
		{
			if(!looping)
			{
				time = length;
				return;
			}

			time = time - length;
		}

		if(time < 0)
			time += length;

		int intTime = (int)time;

		currentKey = mainline.getKeyBeforeTime(intTime);

		for(TimelineKey timelineKey : unmappedTweenedKeys)
			timelineKey.setActive(false);

		for(BoneRef ref : currentKey.boneRefs)
			update(ref, intTime);

		for(ObjectRef ref : currentKey.objectRefs)
			update(ref, intTime);
	}

	protected void update(BoneRef ref, int time)
	{
		//Get the timelines, the refs pointing to
		Timeline timeline = timelines.get(ref.timeline);
		TimelineKey key = timeline.getKeys().get(ref.key);
		TimelineKey nextKey = timeline.getKeys().get((ref.key + 1) % timeline.getKeys().size);

		int nextTime = nextKey.getTime();

		if(nextTime < key.getTime())
		{
			if(!looping)
				nextKey = key;
			else
				nextTime = length;
		}

		//Normalize the time
		float norTime = (float)(time - key.getTime()) / (float)(nextTime - key.getTime());

		if(isNaN(norTime) || isInfinite(norTime))
			norTime = 1f;

		if(currentKey.time > key.getTime())
		{
			float tMid = (float)(currentKey.time - key.getTime()) / (float)(nextTime - key.getTime());

			if(isNaN(tMid) || isInfinite(tMid))
				tMid = 0f;

			norTime = (float)(time - currentKey.time) / (float)(nextTime - currentKey.time);

			if(isNaN(norTime) || isInfinite(norTime))
				norTime = 1f;

			norTime = currentKey.curve.tween(tMid, 1f, norTime);
		}
		else
			norTime = currentKey.curve.tween(0f, 1f, norTime);

		//Tween object
		SpriterObject bone1 = key.getObject();
		SpriterObject bone2 = nextKey.getObject();
		SpriterObject tweenTarget = this.tweenedKeys[ref.timeline].getObject();

		if(ref instanceof ObjectRef)
			tweenObject((SpriterSprite)bone1, (SpriterSprite)bone2, (SpriterSprite)tweenTarget, norTime, key.getCurve(), key.getSpin());
		else
			tweenBone(bone1, bone2, tweenTarget, norTime, key.getCurve(), key.getSpin());

		unmappedTweenedKeys[ref.timeline].setActive(true);
		unmapTimelineObject(ref.timeline, ref.parent != null ? unmappedTweenedKeys[ref.parent.timeline].getObject() : root);
	}

	private void unmapTimelineObject(int timeline, SpriterObject root)
	{
		SpriterObject tweenTarget = this.tweenedKeys[timeline].getObject();
		SpriterObject mapTarget = this.unmappedTweenedKeys[timeline].getObject();

		mapTarget.set(tweenTarget);

		mapTarget.unmap(root);
	}

	private void tweenBone(SpriterObject bone1, SpriterObject bone2, SpriterObject target, float t, Curve curve, int spin)
	{
		target.angle = curve.tweenAngle(bone1.angle, bone2.angle, t, spin);
		curve.tweenPoint(bone1.position, bone2.position, t, target.position);
		curve.tweenPoint(bone1.scale, bone2.scale, t, target.scale);
		curve.tweenPoint(bone1.pivot, bone2.pivot, t, target.pivot);
	}

	private void tweenObject(SpriterSprite object1, SpriterSprite object2, SpriterSprite target, float t, Curve curve, int spin)
	{
		this.tweenBone(object1, object2, target, t, curve, spin);

		target.setAlpha(curve.tweenAngle(object1.getAlpha(), object2.getAlpha(), t));
		target.setAsset(object1.getAsset());
	}

	public void reset()
	{
		this.time = 0;
		prepare();
		update(0);
	}

	public SpriterObject getRoot()
	{
		return root;
	}

	public Mainline getMainline()
	{
		return mainline;
	}

	public Array<Timeline> getTimelines()
	{
		return timelines;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public float getTime()
	{
		return time;
	}

	public void setTime(float time)
	{
		this.time = time;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public float getAlpha()
	{
		return alpha;
	}

	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public boolean isLooping()
	{
		return looping;
	}

	public void setLooping(boolean looping)
	{
		this.looping = looping;
	}

	public boolean isDone()
	{
		return time == length;
	}
}
