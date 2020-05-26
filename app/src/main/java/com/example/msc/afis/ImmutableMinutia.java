// Part of SourceAFIS for Java: https://sourceafis.machinezoo.com/java
package com.example.msc.afis;

class ImmutableMinutia {
	final IntPoint position;
	final double direction;
	final MinutiaType type;
	ImmutableMinutia(MutableMinutia mutable) {
		position = mutable.position;
		direction = mutable.direction;
		type = mutable.type;
	}
	MutableMinutia mutable() {
		return new MutableMinutia(position, direction, type);
	}
}
