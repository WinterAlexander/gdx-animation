package com.winteralexander.gdx.animation.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a curve in a Spriter SCML file. An instance of this class is responsible for tweening
 * given data. The most important method of this class is
 * {@link #interpolate(float, float, float)}.
 *
 * @author Alexander Winter
 */
public class Curve {
	/**
	 * The constraints of a curve which will affect a curve of the types different from
	 * {@link CurveType#LINEAR} and {@link CurveType#INSTANT}.
	 */
	public final Constraints constraints = new Constraints(0, 0, 0, 0);
	private CurveType type;
	private float lastCubicSolution = 0f;

	/**
	 * Creates a new curve with the given type.
	 *
	 * @param type the curve type
	 */
	public Curve(CurveType type) {
		this.type = type;
	}

	/**
	 * Interpolates the given two points with the given weight and saves the result in the target
	 * point.
	 *
	 * @param a the start point
	 * @param b the end point
	 * @param value the weight which lies between 0.0 and 1.0
	 * @param target the target point to save the result in
	 */
	public void interpolateVector(Vector2 a, Vector2 b, float value, Vector2 target) {
		target.set(interpolate(a.x, b.x, value), interpolate(a.y, b.y, value));
	}

	/**
	 * Returns a tweened angle based on the given angles, weight and the spin.
	 *
	 * @param a the start angle
	 * @param b the end angle
	 * @param value the weight which lies between 0.0 and 1.0
	 * @param spin the spin, which is either 0, 1 or -1
	 * @return tweened angle
	 */
	public float interpolateAngle(float a, float b, float value, int spin) {
		if(spin > 0) {
			if(b - a < 0)
				b += 360;
		} else if(spin < 0) {
			if(b - a > 0)
				b -= 360;
		} else
			return a;

		return interpolate(a, b, value);
	}

	public float interpolate(float a, float b, float value) {
		switch(type) {
			case INSTANT:
				return a;
			case LINEAR:
				return Interpolator.linear(a, b, value);
			case QUADRATIC:
				return Interpolator.quadratic(a, Interpolator.linear(a, b, constraints.c1), b, value);
			case CUBIC:
				return Interpolator.cubic(a, Interpolator.linear(a, b, constraints.c1), Interpolator.linear(a, b, constraints.c2), b,
						value);
			case QUARTIC:
				return Interpolator.quartic(a, Interpolator.linear(a, b, constraints.c1), Interpolator.linear(a, b, constraints.c2),
						Interpolator.linear(a, b, constraints.c3), b, value);
			case QUINTIC:
				return Interpolator.quintic(a, Interpolator.linear(a, b, constraints.c1), Interpolator.linear(a, b, constraints.c2),
						Interpolator.linear(a, b, constraints.c3), Interpolator.linear(a, b, constraints.c4), b, value);
			case BEZIER:
				float cubicSolution = Interpolator.solveCubic(3f * (constraints.c1 - constraints.c3) + 1f,
						3f * (constraints.c3 - 2f * constraints.c1), 3f * constraints.c1, -value);
				if(cubicSolution == -1) //TODO (check if actually happen)
					cubicSolution = lastCubicSolution;
				else
					lastCubicSolution = cubicSolution;
				return Interpolator.linear(a, b, Interpolator.bezier(cubicSolution, 0f, constraints.c2, constraints.c4, 1f));
			default:
				return Interpolator.linear(a, b, value);
		}
	}

	/**
	 * Returns the type of this curve.
	 *
	 * @return the curve type
	 */
	public CurveType getType() {
		return this.type;
	}

	/**
	 * Sets the type of this curve.
	 *
	 * @param type the curve type.
	 */
	public void setType(CurveType type) {
		this.type = type;
	}


	/**
	 * Represents a curve type in a Spriter SCML file.
	 *
	 * @author Alexander Winter
	 */
	public enum CurveType {
		INSTANT, LINEAR, QUADRATIC, CUBIC, QUARTIC, QUINTIC, BEZIER
	}

	/**
	 * Represents constraints for a curve. Constraints are important for curves which have a order
	 * higher than 1.
	 *
	 * @author Alexander Winter
	 */
	public static class Constraints {
		public float c1, c2, c3, c4;

		public Constraints(float c1, float c2, float c3, float c4) {
			this.set(c1, c2, c3, c4);
		}

		public void set(float c1, float c2, float c3, float c4) {
			this.c1 = c1;
			this.c2 = c2;
			this.c3 = c3;
			this.c4 = c4;
		}
	}
}
