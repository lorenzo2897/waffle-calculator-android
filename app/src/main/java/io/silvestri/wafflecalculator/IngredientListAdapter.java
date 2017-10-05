package io.silvestri.wafflecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class IngredientListAdapter extends ArrayAdapter<Ingredient> {
	private final Context context;
	public final ArrayList<Ingredient> ingredients;

	public IngredientListAdapter(Context context, ArrayList<Ingredient> ingredients) {
		super(context, -1, ingredients);
		this.context = context;
		this.ingredients = ingredients;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.ingredient_item, parent, false);
		TextView nameText = (TextView) rowView.findViewById(R.id.ingredientName);
		TextView qtyText = (TextView) rowView.findViewById(R.id.ingredientQuantity);
		nameText.setText(ingredients.get(position).name);
		qtyText.setText(ingredients.get(position).makeQuantityText());

		return rowView;
	}

}
