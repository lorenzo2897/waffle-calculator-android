package io.silvestri.wafflecalculator;


import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;


public class Recipe {

	private Context context;

	String[] titles = new String[]{
			context.getString(R.string.step0_title),
			context.getString(R.string.step1_title),
			context.getString(R.string.step2_title),
			context.getString(R.string.step3_title),
			context.getString(R.string.step4_title),
			context.getString(R.string.step5_title),
			context.getString(R.string.step6_title),
			context.getString(R.string.step7_title),
			context.getString(R.string.step8_title)
	};
	String[] descriptions = new String[]{
			context.getString(R.string.step0_description),
			context.getString(R.string.step1_description),
			context.getString(R.string.step2_description),
			context.getString(R.string.step3_description),
			context.getString(R.string.step4_description),
			context.getString(R.string.step5_description),
			context.getString(R.string.step6_description),
			context.getString(R.string.step7_description),
			context.getString(R.string.step8_description)
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
			return context.getString(R.string.set_timer_button);
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
							((Button) v).setText(context.getString(R.string.time_remaining_counter, millisUntilFinished / 1000));
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
				.setContentTitle(context.getString(R.string.waffle_notif_title))
				.setContentText(context.getString(R.string.waffle_notif_content))
				.setAutoCancel(true)
				.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}
}
