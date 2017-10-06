package io.silvestri.wafflecalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class MainActivity extends Activity {

	Units units;
	int wafflesToMake = 6;

	IngredientListAdapter ingredientListAdapter;
	ArrayList<Ingredient> ingredients;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// load from preferences
		units = new Units();
		units.loadValues(getSharedPreferences("units", MODE_PRIVATE));

		wafflesToMake = getSharedPreferences("waffles", MODE_PRIVATE).getInt("waffles", 6);


		// set up the ingredients list
		ingredients = new ArrayList<>();
		ingredientListAdapter = new IngredientListAdapter(this, ingredients);
		((ListView) findViewById(R.id.ingredientsList)).setAdapter(ingredientListAdapter);


		// add event handlers

		findViewById(R.id.waffleCountIncrease).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				increaseWaffleCount();
			}
		});

		findViewById(R.id.waffleCountDecrease).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				decreaseWaffleCount();
			}
		});

		findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToRecipe();
			}
		});

		updateWaffleCounts();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ingredients_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.export) {
			shareIngredients();
			return true;
		}
		else if (id == R.id.about) {
			showAbout();
			return true;
		}
		else if (id == R.id.units) {
			showUnitSelection();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	void increaseWaffleCount() {
		if(wafflesToMake >= 10) {
			wafflesToMake += 2;
		} else {
			wafflesToMake++;
		}

		getSharedPreferences("waffles", MODE_PRIVATE).edit().putInt("waffles", wafflesToMake).apply();

		updateWaffleCounts();
	}

	void decreaseWaffleCount() {
		if(wafflesToMake <= 1) return;

		if(wafflesToMake > 10) {
			wafflesToMake -= 2;
		} else {
			wafflesToMake--;
		}

		getSharedPreferences("waffles", MODE_PRIVATE).edit().putInt("waffles", wafflesToMake).apply();

		updateWaffleCounts();
	}

	void updateWaffleCounts() {
		TextView waffleCountLabel = (TextView) findViewById(R.id.waffleCountLabel);
		waffleCountLabel.setText(String.valueOf(wafflesToMake));

		ingredients.clear();

		ingredients.add(new Ingredient(getString(R.string.ingredient_flour), units.convert(40 * wafflesToMake, Units.WEIGHT), units.weightUnit(false), units.weightUnit(true)));

		ingredients.add(new Ingredient(getString(R.string.ingredient_powder), units.convert(4.0 / 6.0 * wafflesToMake, Units.SMALL_QTY), units.smallQtyUnit(false), units.smallQtyUnit(true)));

		ingredients.add(new Ingredient(getString(R.string.ingredient_salt), units.convert(0.5 / 6.0 * wafflesToMake, Units.SMALL_QTY), units.smallQtyUnit(false), units.smallQtyUnit(true)));

		ingredients.add(new Ingredient(getString(R.string.ingredient_sugar), units.convert(10 * wafflesToMake, Units.WEIGHT), units.weightUnit(false), units.weightUnit(true)));

		ingredients.add(new Ingredient(getString(R.string.ingredient_eggs), 2.0 / 6 * wafflesToMake, getString(R.string.unit_egg), getString(R.string.unit_eggs), true));

		ingredients.add(new Ingredient(getString(R.string.ingredient_oil), units.convert(20 * wafflesToMake, Units.VOLUME), units.volumeUnit(false), units.volumeUnit(true)));

		ingredients.add(new Ingredient(getString(R.string.ingredient_milk), units.convert(90 * wafflesToMake, Units.VOLUME), units.volumeUnit(false), units.volumeUnit(true)));

		ingredients.add(new Ingredient(getString(R.string.ingredient_vanilla), units.convert(1.0 / 6 * wafflesToMake, Units.SMALL_QTY), units.smallQtyUnit(false), units.smallQtyUnit(true)));

		ingredientListAdapter.notifyDataSetChanged();
	}

	void goToRecipe() {
		Intent intent = new Intent(this, RecipeActivity.class);
		//intent.putExtra(Intent.EXTRA_INDEX, wafflesToMake);
		startActivity(intent);
	}

	void shareIngredients() {
		String shareBody = "Ingredients for " + String.valueOf(wafflesToMake) + " waffles";

		for(Ingredient i : ingredients) {
			shareBody += "\n- " + i.name + " (" + i.makeQuantityText() + ")";
		}

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ingredients for " + String.valueOf(wafflesToMake) + " waffles");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
	}

	void showAbout() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.about_waffle_title);
		alert.setMessage(R.string.about_text);

		alert.setPositiveButton(R.string.close, null);

		alert.show();
	}

	void showUnitSelection() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.pick_units_title);
		final View view = getLayoutInflater().inflate(R.layout.modal_units, null);
		alert.setView(view);
		alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// change prefs
					if(((RadioButton) view.findViewById(R.id.vol_ml)).isChecked()) units.Volume = Units.MILLILITRES;
					if(((RadioButton) view.findViewById(R.id.vol_floz)).isChecked()) units.Volume = Units.FLUID_OUNCES;
					if(((RadioButton) view.findViewById(R.id.w_g)).isChecked()) units.Weight = Units.GRAMS;
					if(((RadioButton) view.findViewById(R.id.w_oz)).isChecked()) units.Weight = Units.OUNCES;
					if(((RadioButton) view.findViewById(R.id.sq_tea)).isChecked()) units.Small_qty = Units.TEASPOONS;
					if(((RadioButton) view.findViewById(R.id.sq_ml)).isChecked()) units.Small_qty = Units.MILLILITRES;
					// save prefs
					units.saveValues(getSharedPreferences("units", MODE_PRIVATE));
					updateWaffleCounts();
				}
			}
		);
		alert.setNegativeButton(R.string.cancel, null);

		((RadioButton) view.findViewById(R.id.vol_ml)).setChecked(units.Volume == Units.MILLILITRES);
		((RadioButton) view.findViewById(R.id.vol_floz)).setChecked(units.Volume == Units.FLUID_OUNCES);
		((RadioButton) view.findViewById(R.id.w_g)).setChecked(units.Weight == Units.GRAMS);
		((RadioButton) view.findViewById(R.id.w_oz)).setChecked(units.Weight == Units.OUNCES);
		((RadioButton) view.findViewById(R.id.sq_tea)).setChecked(units.Small_qty == Units.TEASPOONS);
		((RadioButton) view.findViewById(R.id.sq_ml)).setChecked(units.Small_qty == Units.MILLILITRES);

		alert.show();
	}
}
