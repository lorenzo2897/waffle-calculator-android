package io.silvestri.wafflecalculator;

import android.content.Context;
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
	private Context context;

	Units(Context context) {
		this.context = context;
	}

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

	String volumeUnit(boolean plural) {
		if(Volume == Units.MILLILITRES) {
			return "ml";
		} else if(Volume == Units.FLUID_OUNCES) {
			return "fl oz";
		}
		return "";
	}

	String weightUnit(boolean plural) {
		if(Weight == Units.GRAMS) {
			return "g";
		} else if(Weight == Units.OUNCES) {
			return plural ? context.getString(R.string.unit_ounces) : context.getString(R.string.unit_ounce);
		}
		return "";
	}

	String smallQtyUnit(boolean plural) {
		if(Small_qty == Units.TEASPOONS) {
			return plural ? context.getString(R.string.unit_teaspoons) : context.getString(R.string.unit_teaspoon);
		} else if(Small_qty == Units.MILLILITRES) {
			return "ml";
		}
		return "";
	}

	double convert(double value, String category) {
		if(category.equals(VOLUME)) {
			if(Volume == Units.FLUID_OUNCES) {
				return value * 0.0351951;
			} else {
				return value;
			}

		} else if(category.equals(WEIGHT)) {
			if(Weight == Units.OUNCES) {
				return value * 0.035274;
			} else {
				return value;
			}

		} else if(category.equals(SMALL_QTY)) {
			if(Small_qty == Units.MILLILITRES) {
				return value * 6;
			} else {
				return value;
			}

		}
		return 0;
	}
}
