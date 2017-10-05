package io.silvestri.wafflecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends Activity {

	int wafflesToMake = 6;

	IngredientListAdapter ingredientListAdapter;
	ArrayList<Ingredient> ingredients;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ingredients = new ArrayList<>();
		ingredientListAdapter = new IngredientListAdapter(this, ingredients);
		((ListView) findViewById(R.id.ingredientsList)).setAdapter(ingredientListAdapter);

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

		updateWaffleCounts();
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
		ingredients.add(new Ingredient("Flour", 40 * wafflesToMake, "g", "g"));
		ingredients.add(new Ingredient("Baking powder", 4.0 / 6.0 * wafflesToMake, "teaspoon", "teaspoons"));
		ingredients.add(new Ingredient("Salt", 0.5 / 6.0 * wafflesToMake, "teaspoon", "teaspoons"));
		ingredients.add(new Ingredient("Sugar", 10 * wafflesToMake, "g", "g"));
		ingredients.add(new Ingredient("Eggs", 2.0 / 6 * wafflesToMake, "egg", "eggs", true));
		ingredients.add(new Ingredient("Vegetable oil", 20 * wafflesToMake, "ml", "ml"));
		ingredients.add(new Ingredient("Milk", 90 * wafflesToMake, "ml", "ml"));
		ingredients.add(new Ingredient("Vanilla", 1.0 / 6 * wafflesToMake, "teaspoon", "teaspoons"));

		ingredientListAdapter.notifyDataSetChanged();
	}
}
