package com.brashmonkey.spriter;

import com.brashmonkey.spriter.math.Curve;

/**
 * Represents a time line key in a Spriter SCML file. A key holds a {@link #time}, a {@link #spin}, an
 * {@link #getObject()} and a {@link #curve}.
 *
 * @author Trixt0r
 */
public class TimelineKey
{
	private int time;
	private int spin;
	private Curve curve;
	private SpriterObject object;

	public TimelineKey(int time, int spin, Curve curve)
	{
		this.time = time;
		this.spin = spin;
		this.curve = curve;
	}

	public TimelineKey(TimelineKey key)
	{
		this.time = key.time;
		this.spin = key.spin;
		this.curve = key.curve;
		this.object = key.object.clone();
	}

	public int getSpin()
	{
		return spin;
	}

	public void setSpin(int spin)
	{
		this.spin = spin;
	}

	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public Curve getCurve()
	{
		return curve;
	}

	public void setCurve(Curve curve)
	{
		this.curve = curve;
	}

	public void setObject(SpriterObject object)
	{
		this.object = object;
	}

	public SpriterObject getObject()
	{
		return this.object;
	}

}
