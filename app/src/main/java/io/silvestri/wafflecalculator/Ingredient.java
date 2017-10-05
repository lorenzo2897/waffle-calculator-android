package io.silvestri.wafflecalculator;

public class Ingredient {
	final String name;
	double quantity;
	final String unit;
	final String unit_plural;
	boolean must_round = false;

	public Ingredient(String name, double quantity, String unit, String unit_plural) {
		this.name = name;
		this.quantity = quantity;
		this.unit = unit;
		this.unit_plural = unit_plural;
	}

	public Ingredient(String name, double quantity, String unit, String unit_plural, boolean must_round) {
		this(name, quantity, unit, unit_plural);
		this.must_round = must_round;
	}

	public String makeQuantityText() {
		if(quantity - Math.floor(quantity) < 0.05 || must_round) {
			if(Math.round(quantity) <= 1) {
				return "1 " + unit;
			}
			return String.valueOf(Math.max(Math.round(quantity), 1)) + " " + unit_plural;
		} else {
			return String.valueOf(Math.round(quantity * 10) / 10.0) + " " + unit_plural;
		}
	}
}
