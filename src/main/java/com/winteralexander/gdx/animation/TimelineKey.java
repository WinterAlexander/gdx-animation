package com.winteralexander.gdx.animation;

import com.winteralexander.gdx.animation.math.Curve;

/**
 * Represents a time line key in a Spriter SCML file. A key holds a {@link #time}, a {@link #spin},
 * an {@link #getObject()} and a {@link #curve}.
 *
 * @author Alexander Winter
 */
public class TimelineKey {
	private final int time;
	private final int spin;
	private final Curve curve;
	private AnimatedPart object;

	public TimelineKey(int time, int spin, Curve curve) {
		this.time = time;
		this.spin = spin;
		this.curve = curve;
	}

	public TimelineKey(TimelineKey key) {
		this.time = key.time;
		this.spin = key.spin;
		this.curve = key.curve;
		this.object = key.object.clone();
	}

	public int getSpin() {
		return spin;
	}

	public int getTime() {
		return time;
	}

	public Curve getCurve() {
		return curve;
	}

	public AnimatedPart getObject() {
		return this.object;
	}

	public void setObject(AnimatedPart object) {
		this.object = object;
	}
}
