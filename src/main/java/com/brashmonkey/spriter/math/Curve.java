package com.brashmonkey.spriter.math;

import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.brashmonkey.spriter.math.Interpolator.*;
import static java.lang.Math.pow;

/**
 * Represents a curve in a Spriter SCML file. An instance of this class is responsible for tweening given data. The most
 * important method of this class is {@link #tween(float, float, float)}. Curves can be changed with sub curves {@link
 * Curve#subCurve}.
 *
 * @author Trixt0r
 */
public class Curve
{
	private CurveType type;
	private Curve subCurve;

	private float lastCubicSolution = 0f;

	/**
	 * The constraints of a curve which will affect a curve of the types different from {@link CurveType#LINEAR} and {@link
	 * CurveType#INSTANT}.
	 */
	public final Constraints constraints = new Constraints(0, 0, 0, 0);

	/**
	 * Creates a new linear curve.
	 */
	public Curve()
	{
		this(CurveType.LINEAR);
	}

	/**
	 * Creates a new curve with the given type.
	 *
	 * @param type the curve type
	 */
	public Curve(CurveType type)
	{
		this.type = type;
		this.subCurve = null;
	}

	/**
	 * Interpolates the given two points with the given weight and saves the result in the target point.
	 *
	 * @param a the start point
	 * @param b the end point
	 * @param weight the weight which lies between 0.0 and 1.0
	 * @param target the target point to save the result in
	 */
	public void tweenPoint(Vector2 a, Vector2 b, float weight, Vector2 target)
	{
		target.set(tweenScalar(a.x, b.x, weight), tweenScalar(a.y, b.y, weight));
	}

	private float tweenSub(float a, float b, float t)
	{
		if(subCurve != null)
			return subCurve.tweenScalar(a, b, t);
		else
			return t;
	}

	/**
	 * Returns a tweened angle based on the given angles, weight and the spin.
	 *
	 * @param a the start angle
	 * @param b the end angle
	 * @param t the weight which lies between 0.0 and 1.0
	 * @param spin the spin, which is either 0, 1 or -1
	 * @return tweened angle
	 */
	public float tweenAngle(float a, float b, float t, int spin)
	{
		if(spin > 0)
		{
			if(b - a < 0)
				b += 360;
		}
		else if(spin < 0)
		{
			if(b - a > 0)
				b -= 360;
		}
		else
			return a;

		return tweenScalar(a, b, t);
	}

	public float tweenScalar(float a, float b, float t)
	{
		t = tweenSub(0f, 1f, t);
		switch(type)
		{
			case INSTANT:
				return a;
			case LINEAR:
				return linearAngle(a, b, t);
			case QUADRATIC:
				return quadraticAngle(a, linearAngle(a, b, constraints.c1), b, t);
			case CUBIC:
				return cubicAngle(a, linearAngle(a, b, constraints.c1), linearAngle(a, b, constraints.c2), b, t);
			case QUARTIC:
				return quarticAngle(a, linearAngle(a, b, constraints.c1), linearAngle(a, b, constraints.c2), linearAngle(a, b, constraints.c3), b, t);
			case QUINTIC:
				return quinticAngle(a, linearAngle(a, b, constraints.c1), linearAngle(a, b, constraints.c2), linearAngle(a, b, constraints.c3), linearAngle(a, b, constraints.c4), b, t);
			case BEZIER:
				float cubicSolution = solveCubic(3f * (constraints.c1 - constraints.c3) + 1f, 3f * (constraints.c3 - 2f * constraints.c1), 3f * constraints.c1, -t);
				if(cubicSolution == -1) //TODO WTFFFFFF
					cubicSolution = lastCubicSolution;
				else
					lastCubicSolution = cubicSolution;
				return linearAngle(a, b, bezier(cubicSolution, 0f, constraints.c2, constraints.c4, 1f));
			default:
				return linearAngle(a, b, t);
		}
	}

	/**
	 * Sets the type of this curve.
	 *
	 * @param type the curve type.
	 * @throws IllegalArgumentException if the type is <code>null</code>
	 */
	public void setType(CurveType type)
	{
		this.type = type;
	}

	/**
	 * Returns the type of this curve.
	 *
	 * @return the curve type
	 */
	public CurveType getType()
	{
		return this.type;
	}


	/**
	 * Represents constraints for a curve. Constraints are important for curves which have a order higher than 1.
	 *
	 * @author Trixt0r
	 */
	public static class Constraints
	{
		public float c1, c2, c3, c4;

		public Constraints(float c1, float c2, float c3, float c4)
		{
			this.set(c1, c2, c3, c4);
		}

		public void set(float c1, float c2, float c3, float c4)
		{
			this.c1 = c1;
			this.c2 = c2;
			this.c3 = c3;
			this.c4 = c4;
		}
	}

	/**
	 * Represents a curve type in a Spriter SCML file.
	 *
	 * @author Trixt0r
	 */
	public enum CurveType
	{
		INSTANT, LINEAR, QUADRATIC, CUBIC, QUARTIC, QUINTIC, BEZIER
	}

}
