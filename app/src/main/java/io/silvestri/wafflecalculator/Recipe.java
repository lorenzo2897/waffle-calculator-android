package io.silvestri.wafflecalculator;


import android.content.Context;

public class Recipe {

	private Context context;

	String[] titles = new String[]{
			"Sift the dry ingredients together",
			"Separate the egg whites",
			"Beat the egg whites",
			"Mix egg yolks, milk, oil, vanilla",
			"Add to dry ingredients and mix",
			"Fold in the egg whites",
			"Oil the waffle maker",
			"Cook for 5 to 10 minutes",
			"Enjoy!"
	};
	String[] descriptions = new String[]{
			"Get a large bowl and sift the flour, sugar, baking power, and salt.",
			"Get another bowl, and separate the egg whites into it.",
			"Beat the egg whites until stiff peaks form.",
			"In a third bowl, mix together the egg yolks, milk, oil, and vanilla. Stir slightly.",
			"Add the egg/milk/oil mix to the bowl with the dry ingredients, and mix well.",
			"Fold the egg whites into the mixture",
			"With a kitchen towel, spread some oil over the waffle maker to avoid sticking.",
			"Pour the batter evenly onto the waffle iron, close it, and let it cook for 5 to 10 minutes.",
			"You can store batter in the fridge for up to three days, or freeze it for later."
	};
	int[] images = new int[]{
			R.drawable.step0_bowl,
			R.drawable.step1_eggs,
			R.drawable.step2_beat,
			R.drawable.step3_liquid,
			R.drawable.step4_mix,
			R.drawable.step5_fold,
			R.drawable.step6_oil,
			R.drawable.step7_cook,
			R.drawable.waffle
	};

	Recipe(Context context) {
		this.context = context;
	}

	int numberOfSteps() {
		return titles.length;
	}

	String getStepTitle(int index) {
		return titles[index];
	}

	String getStepDescription(int index) {
		return descriptions[index];
	}

	int getStepImage(int index) {
		return images[index];
	}
}
