package io.silvestri.wafflecalculator;

import android.content.SharedPreferences;

public class Units {
	public static final String VOLUME = "volume";
	public static final String WEIGHT = "weight";
	public static final String SMALL_QTY = "small_qty";

	public static final int MILLILITRES = 0;
	public static final int FLUID_OUNCES = 1;
	public static final int GRAMS = 2;
	public static final int OUNCES = 3;
	public static final int TEASPOONS = 4;

	public int Volume;
	public int Weight;
	public int Small_qty;

	void loadValues(SharedPreferences prefs) {
		Volume = prefs.getInt(Units.VOLUME, Units.MILLILITRES);
		Weight = prefs.getInt(Units.WEIGHT, Units.GRAMS);
		Small_qty = prefs.getInt(Units.SMALL_QTY, Units.TEASPOONS);
	}

	void saveValues(SharedPreferences prefs) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Units.VOLUME, Volume);
		editor.putInt(Units.WEIGHT, Weight);
		editor.putInt(Units.SMALL_QTY, Small_qty);
		editor.apply();
	}
}
