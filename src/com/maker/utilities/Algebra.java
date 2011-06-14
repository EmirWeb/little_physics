package com.maker.utilities;

public class Algebra {

	public static float[] multiplyVectorByConstant(float[] vector, float constant) {
		float[] result = new float[vector.length];
		for (int i = 0; i < vector.length; i++)
			result[i] = vector[i] * constant;
		return result;
	}
	
	public static float[] divideVectorByConstant(float[] vector, float constant) {
		float[] result = new float[vector.length];
		for (int i = 0; i < vector.length; i++)
			result[i] = vector[i] / constant;
		return result;
	}

	public static float[] dotProduct(float[] v1, float[] v2) {
		if (v1.length != v2.length)
			return null;

		float[] result = new float[v1.length];
		for (int i = 0; i < v1.length; i++)
			result[i] = v1[i] * v2[i];

		return result;
	}

	public static float[] subtract(float[] v1, float[] v2) {
		if (v1.length != v2.length)
			return null;

		float[] result = new float[v1.length];
		for (int i = 0; i < v1.length; i++)
			result[i] = v1[i] - v2[i];

		return result;
	}
	
	public static float[] add(float[] v1, float[] v2) {
		if (v1.length != v2.length)
			return null;

		float[] result = new float[v1.length];
		for (int i = 0; i < v1.length; i++)
			result[i] = v1[i] + v2[i];

		return result;
	}
	
	public static float[] getReflection(float[] vector, float[] normal){
		float[] a = Algebra.dotProduct(vector, normal);
		float[] b = Algebra.dotProduct(a, normal);
		float[] c = Algebra.multiplyVectorByConstant(b,2);
		float[] result = Algebra.subtract(vector, c);
		
		return result;
	}

	public static float getMagnitude(float[] vector) {
		float magnitude = 0;
		
		for (int i = 0; i < vector.length; i++ )
			magnitude+= Math.pow(vector[i],2);
		
		return (float) Math.sqrt(magnitude);
	}

	public static float[] normalize(float[] vector) {
		return divideVectorByConstant(vector, getMagnitude(vector));
	}

}
