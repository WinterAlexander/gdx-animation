package com.brashmonkey.spriter;

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
	private int length;
	private boolean looping;

	private Mainline mainline;
	private final Array<Timeline> timelines;

	private MainlineKey currentKey;
	private SpriterSprite[] tweenedSprites; //sprites made on runtime by tweening original sprites from animation

	/**
	 * Milliseconds
	 */
	private float time = 0;
	private float speed = 1f, alpha = 1f;

	private SpriterObject root = new SpriterObject();

	public Animation(String name, int length, boolean looping, Mainline mainline, Array<Timeline> timelines)
	{
		this.name = name;

		this.length = length;
		this.looping = looping;

		this.mainline = mainline;
		this.timelines = timelines;

		prepare();
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
		tweenedSprites = new SpriterSprite[timelines.size];

		for(int i = 0; i < timelines.size; i++)
			tweenedSprites[i] = new SpriterSprite();

		if(mainline.getKeys().size > 0)
			currentKey = mainline.getKeys().get(0);
	}

	public void draw(Batch batch)
	{
		//if(isDone())
		//	return;

		float prevColor = batch.getPackedColor();
		batch.getColor().a = alpha;
		batch.setColor(batch.getColor()); //update

		for(SpriterSprite sprite : tweenedSprites)
			sprite.draw(batch);

		batch.setColor(prevColor);
	}

	/**
	 * Updates this player. This means the current time gets increased by {@link #speed} and is applied to the current
	 * animation.
	 */
	public void update()
	{
		this.update(1000f / 60f); //assume 60 fps by default
	}

	/**
	 * Updates this player. This means the current time gets increased by {@link #speed} and is applied to the current
	 * animation.
	 *
	 * @param delta time in milliseconds
	 */
	public void update(float delta)
	{
		if(tweenedSprites == null)
			throw new IllegalStateException("Animation not prepared");

		time += speed * delta;

		if(time >= length)
		{
			if(!looping)
				time = length;
			else
				time -= length;
		}

		int intTime = (int)time;

		currentKey = mainline.getKeyBeforeTime(intTime);

		for(ObjectRef ref : currentKey.objectRefs)
			update(ref, intTime);
	}

	protected void update(ObjectRef ref, int time)
	{
		//Get the timelines, the refs pointing to
		Timeline timeline = timelines.get(ref.timeline);

		TimelineKey key = timeline.getKeys().get(ref.key); //get the last previous key
		TimelineKey nextKey;

		if(ref.key + 1 == timeline.getKeys().size)
		{
			if(!looping)
			{
				//no need to tween, stay freezed at first sprite
				SpriterSprite tweenTarget = tweenedSprites[ref.timeline];

				tweenTarget.set(key.getObject());

				SpriterObject parent = ref.parent != null ? tweenedSprites[ref.parent.timeline] : root;
				tweenTarget.unmap(parent);
				return;
			}

			nextKey = timeline.getKeys().get(0);
		}
		else
		{
			nextKey = timeline.getKeys().get(ref.key + 1);
		}

		float timeDiff = nextKey.getTime() - key.getTime();

		//assert timeDiff != 0;

		//Normalize the time
		float norTime = (float)(time - key.getTime()) / timeDiff;

		if(currentKey.time > key.getTime())
		{
			float tMid = (float)(currentKey.time - key.getTime()) / timeDiff;

			norTime = (float)(time - currentKey.time) / (float)(nextKey.getTime() - currentKey.time);

			if(isNaN(norTime) || isInfinite(norTime))
				norTime = 1f;

			norTime = currentKey.curve.tween(tMid, 1f, norTime);
		}
		else
			norTime = currentKey.curve.tween(0f, 1f, norTime);

		//Tween object
		SpriterObject obj1 = key.getObject();
		SpriterObject obj2 = nextKey.getObject();
		SpriterObject tweened = tweenedSprites[ref.timeline];

		Curve curve = key.getCurve();

		tweened.angle = curve.tweenAngle(obj1.angle, obj2.angle, norTime, key.getSpin());
		curve.tweenPoint(obj1.position, obj2.position, norTime, tweened.position);
		curve.tweenPoint(obj1.scale, obj2.scale, norTime, tweened.scale);
		curve.tweenPoint(obj1.pivot, obj2.pivot, norTime, tweened.pivot);

		if(timeline instanceof SpriteTimeline)
		{
			((SpriterSprite)tweened).setAlpha(curve.tweenScalar(((SpriterSprite)obj1).getAlpha(), ((SpriterSprite)obj2).getAlpha(), norTime));
			((SpriterSprite)tweened).setAsset(((SpriterSprite)obj1).getAsset());
		}

		tweened.unmap(ref.parent != null ? tweenedSprites[ref.parent.timeline] : root);
	}

	public void reset()
	{
		time = 0;
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

	/**
	 * Time is in milliseconds
	 *
	 * @return current time of this animation
	 */
	public float getTime()
	{
		return time;
	}

	public void setTime(float time)
	{
		while(time < 0)
			time += length;

		while(time > length)
		{
			if(looping)
				time -= length;
			else
				time = length;
		}

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
