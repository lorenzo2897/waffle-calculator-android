package io.silvestri.wafflecalculator;


import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeActivity extends Activity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_recipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		public PlaceholderFragment() {
		}

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

			Recipe recipe = new Recipe(rootView.getContext());

			TextView text_title = (TextView) rootView.findViewById(R.id.step_title);
			text_title.setText(recipe.getStepTitle(getArguments().getInt(ARG_SECTION_NUMBER)));

			TextView text_description = (TextView) rootView.findViewById(R.id.step_description);
			text_description.setText(recipe.getStepDescription(getArguments().getInt(ARG_SECTION_NUMBER)));

			ImageView step_image = (ImageView) rootView.findViewById(R.id.step_image);
			step_image.setImageResource(recipe.getStepImage(getArguments().getInt(ARG_SECTION_NUMBER)));

			TextView text_next = (TextView) rootView.findViewById(R.id.step_next);
			text_next.setVisibility(getArguments().getInt(ARG_SECTION_NUMBER) == 0 ? View.VISIBLE : View.INVISIBLE);

			Button step_action = (Button) rootView.findViewById(R.id.step_action);
			if(recipe.getStepAction(getArguments().getInt(ARG_SECTION_NUMBER)) != null) {
				step_action.setText(recipe.getStepAction(getArguments().getInt(ARG_SECTION_NUMBER)));
				step_action.setVisibility(View.VISIBLE);
				step_action.setOnClickListener(recipe.getStepActionClickListener(getArguments().getInt(ARG_SECTION_NUMBER)));
			} else {
				step_action.setVisibility(View.INVISIBLE);
			}

			return rootView;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			return PlaceholderFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return new Recipe(getBaseContext()).numberOfSteps();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return new Recipe(getBaseContext()).getStepTitle(position);
		}
	}
}
