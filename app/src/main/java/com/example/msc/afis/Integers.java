// Part of SourceAFIS for Java: https://sourceafis.machinezoo.com/java
package com.example.msc.afis;

class Integers {
	static int sq(int value) {
		return value * value;
	}
	static int roundUpDiv(int dividend, int divisor) {
		return (dividend + divisor - 1) / divisor;
	}
}
