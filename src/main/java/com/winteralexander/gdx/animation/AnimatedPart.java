package com.winteralexander.gdx.animation;

import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.signum;

/**
 * Represents a bone in a Spriter SCML file. A bone holds a {@link #position}, {@link #scale} and an
 * {@link #angle}. Bones are the only objects which can be used as a parent for other tweenable
 * objects.
 *
 * @author Alexander Winter
 */
public class AnimatedPart {
	private final Vector2 position, scale;
	private float angle;

	/**
	 * Constructor for root
	 */
	public AnimatedPart() {
		this(new Vector2(), new Vector2(1, 1), 0);
	}

	public AnimatedPart(AnimatedPart other) {
		this.position = new Vector2(other.position);
		this.scale = new Vector2(other.scale);
		this.angle = other.angle;
	}

	public AnimatedPart(Vector2 position, Vector2 scale, float angle) {
		this.position = position;
		this.scale = scale;
		this.angle = angle;
	}

	/**
	 * Sets the values of this object to the values of the given object
	 *
	 * @param object the object
	 */
	public void set(AnimatedPart object) {
		this.angle = object.angle;
		this.position.set(object.position.x, object.position.y);
		this.scale.set(object.scale.x, object.scale.y);
	}

	/**
	 * Maps this bone from it's parent's coordinate system to a global one.
	 *
	 * @param parent the parent bone of this bone
	 */
	public void unmap(AnimatedPart parent) {
		this.angle *= signum(parent.scale.x) * signum(parent.scale.y);
		this.angle += parent.angle;
		this.scale.scl(parent.scale);
		this.position.scl(parent.scale);
		this.position.rotate(parent.angle);
		this.position.add(parent.position);
	}

	@Override
	public AnimatedPart clone() {
		try {
			return getClass().getConstructor(getClass()).newInstance(this);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean isFlippedX() {
		return scale.x < 0;
	}

	public void setFlippedX(boolean flip) {
		if(flip ^ isFlippedX())
			scale.x *= -1;
	}

	public boolean isFlippedY() {
		return scale.y < 0;
	}

	public void setFlippedY(boolean flip) {
		if(flip ^ isFlippedY())
			scale.y *= -1;
	}

	public void setFlipped(boolean x, boolean y) {
		setFlippedX(x);
		setFlippedY(y);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale.x = scale;
		this.scale.y = scale;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
