package com.brashmonkey.spriter.math;


import static com.badlogic.gdx.math.MathUtils.cos;
import static java.lang.Math.pow;

/**
 * Utility class for various interpolation techniques Spriter is using.
 *
 * @author Trixt0r
 */
public class Interpolator
{
	public static float linear(float a, float b, float t)
	{
		return a + (b - a) * t;
	}

	public static float linearAngle(float a, float b, float t)
	{
		return a + (((((b - a) % 360) + 540) % 360) - 180) * t;
	}

	public static float quadratic(float a, float b, float c, float t)
	{
		return linear(linear(a, b, t), linear(b, c, t), t);
	}

	public static float quadraticAngle(float a, float b, float c, float t)
	{
		return linearAngle(linearAngle(a, b, t), linearAngle(b, c, t), t);
	}

	public static float cubic(float a, float b, float c, float d, float t)
	{
		return linear(quadratic(a, b, c, t), quadratic(b, c, d, t), t);
	}

	public static float cubicAngle(float a, float b, float c, float d, float t)
	{
		return linearAngle(quadraticAngle(a, b, c, t), quadraticAngle(b, c, d, t), t);
	}

	public static float quartic(float a, float b, float c, float d, float e, float t)
	{
		return linear(cubic(a, b, c, d, t), cubic(b, c, d, e, t), t);
	}

	public static float quarticAngle(float a, float b, float c, float d, float e, float t)
	{
		return linearAngle(cubicAngle(a, b, c, d, t), cubicAngle(b, c, d, e, t), t);
	}

	public static float quintic(float a, float b, float c, float d, float e, float f, float t)
	{
		return linear(quartic(a, b, c, d, e, t), quartic(b, c, d, e, f, t), t);
	}

	public static float quinticAngle(float a, float b, float c, float d, float e, float f, float t)
	{
		return linearAngle(quarticAngle(a, b, c, d, e, t), quarticAngle(b, c, d, e, f, t), t);
	}

	public static float bezier(float t, float x1, float x2, float x3, float x4)
	{
		float temp = t * t;
		float temp1 = t * t;
		float temp2 = t * t;

		return (-temp * t + 3 * temp - 3 * t + 1) * x1 + (3 * t * temp1 - 6 * temp1 + 3 * t) * x2 + (-3 * temp2 * t + 3 * temp2) * x3 + t * t * t * x4;
	}


	/**
	 * Solves the equation a*x^3 + b*x^2 + c*x +d = 0.
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return the solution of the cubic function if it belongs [0, 1], -1 otherwise.
	 */
	public static float solveCubic(float a, float b, float c, float d)
	{
		if(a == 0)
			return solveQuadratic(b, c, d);
		if(d == 0)
			return 0f;

		b /= a;
		c /= a;
		d /= a;
		float squaredB = b * b;
		float q = (3f * c - squaredB) / 9f;
		float r = (-27f * d + b * (9f * c - 2f * squaredB)) / 54f;
		float disc = q * q * q + r * r;
		float term1 = b / 3f;

		if(disc > 0)
		{
			float sqrtDisc = (float)Math.sqrt(disc);
			float s = r + sqrtDisc;
			float f1 = -s;
			s = (s < 0) ? -(float)pow(f1, 1f / 3f) : (float)pow(s, 1f / 3f);
			float t = r - sqrtDisc;
			float f = -t;
			t = (t < 0) ? -(float)pow(f, 1f / 3f) : (float)pow(t, 1f / 3f);

			float result = -term1 + s + t;
			if(result >= 0 && result <= 1)
				return result;
		}
		else if(disc == 0)
		{
			float f = -r;
			float r13 = (r < 0) ? -(float)pow(f, 1f / 3f) : (float)pow(r, 1f / 3f);

			float result = -term1 + 2f * r13;
			if(result >= 0 && result <= 1)
				return result;

			result = -(r13 + term1);
			if(result >= 0 && result <= 1)
				return result;
		}
		else
		{
			q = -q;
			float dum1 = q * q * q;
			dum1 = (float)Math.acos(r / (float)Math.sqrt(dum1));
			float r13 = 2f * (float)Math.sqrt(q);

			float result = -term1 + r13 * cos(dum1 / 3f);
			if(result >= 0 && result <= 1)
				return result;

			result = -term1 + r13 * cos((dum1 + 2f * (float)Math.PI) / 3f);
			if(result >= 0 && result <= 1)
				return result;

			result = -term1 + r13 * cos((dum1 + 4f * (float)Math.PI) / 3f);
			if(result >= 0 && result <= 1)
				return result;
		}

		return -1;
	}

	/**
	 * Solves the equation a*x^2 + b*x + c = 0
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return the solution for the quadratic function if it belongs [0, 1], -1 otherwise.
	 */
	public static float solveQuadratic(float a, float b, float c)
	{
		float squaredB = b * b;
		float twoA = 2 * a;
		float fourAC = 4 * a * c;
		float sqrt = (float)Math.sqrt(squaredB - fourAC);
		float result = (-b + sqrt) / twoA;
		if(result >= 0 && result <= 1)
			return result;

		result = (-b - sqrt) / twoA;
		if(result >= 0 && result <= 1)
			return result;

		return -1;
	}
}
