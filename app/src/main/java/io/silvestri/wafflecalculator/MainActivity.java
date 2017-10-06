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
		updateWaffleCounts();
	}

	void decreaseWaffleCount() {
		if(wafflesToMake <= 1) return;

		if(wafflesToMake > 10) {
			wafflesToMake -= 2;
		} else {
			wafflesToMake--;
		}

		updateWaffleCounts();
	}

	void updateWaffleCounts() {
		TextView waffleCountLabel = (TextView) findViewById(R.id.waffleCountLabel);
		waffleCountLabel.setText(String.valueOf(wafflesToMake));

		ingredients.clear();

		if(units.Weight == Units.OUNCES) {
			ingredients.add(new Ingredient("Flour", 1.41 * wafflesToMake, "ounce", "ounces"));
		} else {
			ingredients.add(new Ingredient("Flour", 40 * wafflesToMake, "g", "g"));
		}

		if(units.Small_qty == Units.MILLILITRES) {
			ingredients.add(new Ingredient("Baking powder", 4 * wafflesToMake, "ml", "ml"));
		} else {
			ingredients.add(new Ingredient("Baking powder", 4.0 / 6.0 * wafflesToMake, "teaspoon", "teaspoons"));
		}

		if(units.Small_qty == Units.MILLILITRES) {
			ingredients.add(new Ingredient("Salt", 0.5 * wafflesToMake, "ml", "ml"));
		} else {
			ingredients.add(new Ingredient("Salt", 0.5 / 6.0 * wafflesToMake, "teaspoon", "teaspoons"));
		}

		if(units.Weight == Units.OUNCES) {
			ingredients.add(new Ingredient("Sugar", 0.353 * wafflesToMake, "ounce", "ounces"));
		} else {
			ingredients.add(new Ingredient("Sugar", 10 * wafflesToMake, "g", "g"));
		}

		ingredients.add(new Ingredient("Eggs", 2.0 / 6 * wafflesToMake, "egg", "eggs", true));

		if(units.Volume == Units.FLUID_OUNCES) {
			ingredients.add(new Ingredient("Vegetable oil", 0.7 * wafflesToMake, "fl oz", "fl oz"));
		} else {
			ingredients.add(new Ingredient("Vegetable oil", 20 * wafflesToMake, "ml", "ml"));
		}

		if(units.Volume == Units.FLUID_OUNCES) {
			ingredients.add(new Ingredient("Milk", 3.04 * wafflesToMake, "fl oz", "fl oz"));
		} else {
			ingredients.add(new Ingredient("Milk", 90 * wafflesToMake, "ml", "ml"));
		}

		if(units.Small_qty == Units.MILLILITRES) {
			ingredients.add(new Ingredient("Vanilla", wafflesToMake, "ml", "ml"));
		} else {
			ingredients.add(new Ingredient("Vanilla", 1.0 / 6 * wafflesToMake, "teaspoon", "teaspoons"));
		}


		ingredientListAdapter.notifyDataSetChanged();
	}

	void goToRecipe() {
		Intent intent = new Intent(this, RecipeActivity.class);
		intent.putExtra(Intent.EXTRA_INDEX, wafflesToMake);
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
		alert.setMessage("Waffle calculator is an app for all your waffle-making needs." +
				"\nCalculate the ingredients and follow an easy step-by-step recipe for some delicious waffles." +
				"\n\n© 2017 - Lorenzo Silvestri" +
				"\nSupport: waffles@silvestri.io");

		alert.setPositiveButton(R.string.close, null);

		alert.show();
	}

	void showUnitSelection() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Pick units to use for ingredients");
		final View view = getLayoutInflater().inflate(R.layout.modal_units, null);
		alert.setView(view);
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
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
		alert.setNegativeButton("Cancel", null);

		((RadioButton) view.findViewById(R.id.vol_ml)).setChecked(units.Volume == Units.MILLILITRES);
		((RadioButton) view.findViewById(R.id.vol_floz)).setChecked(units.Volume == Units.FLUID_OUNCES);
		((RadioButton) view.findViewById(R.id.w_g)).setChecked(units.Weight == Units.GRAMS);
		((RadioButton) view.findViewById(R.id.w_oz)).setChecked(units.Weight == Units.OUNCES);
		((RadioButton) view.findViewById(R.id.sq_tea)).setChecked(units.Small_qty == Units.TEASPOONS);
		((RadioButton) view.findViewById(R.id.sq_ml)).setChecked(units.Small_qty == Units.MILLILITRES);

		alert.show();
	}
}
