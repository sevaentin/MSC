// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.example.msc.afis.fingerprintio.ansi378;

public enum Ansi378v2009ScanType {
	LIVE_PLAIN(0),
	LIVE_ROLLED(1),
	NONLIVE_PLAIN(2),
	NONLIVE_ROLLED(3),
	LIVE_SWIPE(8),
	LIVE_PALM(10),
	NONLIVE_PALM(11),
	LIVE_OPTICAL_CONTACT_PLAIN(20),
	LIVE_OPTICAL_CONTACT_ROLLED(21),
	LIVE_NONOPTICAL_CONTACT_PLAIN(22),
	LIVE_NONOPTICAL_CONTACT_ROLLED(23),
	LIVE_OPTICAL_CONTACTLESS_PLAIN(24),
	LIVE_OPTICAL_CONTACTLESS_ROLLED(25),
	LIVE_NONOPTICAL_CONTACTLESS_PLAIN(26),
	LIVE_NONOPTICAL_CONTACTLESS_ROLLED(27),
	OTHER(28),
	UNKNOWN(29);
	final int code;
	Ansi378v2009ScanType(int code) {
		this.code = code;
	}
}
