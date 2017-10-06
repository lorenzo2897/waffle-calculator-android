package io.silvestri.wafflecalculator;


import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import static android.R.attr.description;

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

	String getStepAction(int index) {
		if(index == 7) {
			return "Set a 5-minute timer";
		}
		return null;
	}

	View.OnClickListener getStepActionClickListener(int index) {
		if(index == 7) {
			return new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					// prepare timer
					final CountDownTimer timer = new CountDownTimer(5 * 60 * 1000, 1000) {
						@Override
						public void onTick(long millisUntilFinished) {
							((Button) v).setText("Time remaining: " + millisUntilFinished / 1000 + " seconds");
						}

						@Override
						public void onFinish() {
							// reset button
							((Button) v).setText(R.string.times_up);
							v.setOnClickListener(getStepActionClickListener(7));

							// ring alarm
							displayNotification(v.getContext());
							Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							Ringtone r = RingtoneManager.getRingtone(v.getContext(), notification);
							r.play();
						}
					};

					// change click listener to stop timer
					v.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							timer.cancel();
							((Button) v).setText(getStepAction(7));
							v.setOnClickListener(getStepActionClickListener(7));
						}
					});

					// start timer
					timer.start();
				}
			};
		}
		return null;
	}

	void displayNotification(Context context) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_waffle)
				.setContentTitle("Your waffle is ready!")
				.setContentText("The 5 minutes are up")
				.setAutoCancel(true)
				.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}
}
