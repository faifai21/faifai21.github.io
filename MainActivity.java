package com.faisalalqadi.resume.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.ami.fundapter.interfaces.StaticImageLoader;
import com.faisalalqadi.resume.R;
import com.faisalalqadi.resume.base.BaseActivity;
import com.faisalalqadi.resume.fragment.EducationFragment;
import com.faisalalqadi.resume.fragment.LandingFragment;
import com.faisalalqadi.resume.fragment.MiscellaneousFragment;
import com.faisalalqadi.resume.fragment.SkillsFragment;
import com.faisalalqadi.resume.fragment.WorkHistoryFragment;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify.IconValue;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class MainActivity extends BaseActivity 
{
	public static final String FIRST_LAUNCH = "FIRST_LAUNCH";
	private static final Fragment lFrag = LandingFragment.newInstance();
	private static final String ARG_CURR_NAV_DRAWER = "nav_drawer_index";
	private static final String ARG_AB_TITLE = "action_bar_title";
	@InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	@InjectView(R.id.left_drawer) ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private String mDrawerTitle;
	private static final String[] mDrawerListItems = {"Home", "Proficiency", "Work History", "Education", "Misc."};
	FunDapter<String> adapter;
	BindDictionary<String> dict;
	private int mDisplayedNavItem = 0;
	private Bundle savedInstace;
	private boolean shouldToggleAB = false;
	private boolean resetAB = true;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
//		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_solid_shadow));
		initSectionFragments();
		
		this.savedInstace = savedInstanceState;
		mDrawerTitle = getResources().getString(R.string.app_name);
		SharedPreferences myPrefs = getSharedPreferences("myPrefs", 0);  
		boolean launchIntro = myPrefs.getBoolean(FIRST_LAUNCH, true);
		// First launch; start up the introduction
		if(launchIntro){
			Intent in = new Intent(this, IntroActivity.class);
			startActivity(in);
		}
		// Fundapter usage; makes creating an adapter a breeze
		dict = new BindDictionary<String>();
		dict.addStaticImageField(R.id.row_icon, new StaticImageLoader<String>(){

			@Override
			public void loadImage(String item, ImageView imageView, int position) {
				IconValue val = IconValue.fa_home;
				switch(position){
					case 1:
						val = IconValue.fa_code;
						break;
					case 2:
						val = IconValue.fa_briefcase;
						break;
					case 3:
						val = IconValue.fa_graduation_cap;
						break;
					case 4:
						val = IconValue.fa_rocket;
						break;
				}
				imageView.setImageDrawable(new IconDrawable(MainActivity.this, val)
								.colorRes(android.R.color.white)
								.sizeDp(18));
			}
			
		});

		dict.addStringField(R.id.row_title, new StringExtractor<String>(){

			@Override
			public String getStringValue(String item, int position) {
				return mDrawerListItems[position];
			}
		});
		ArrayList<String> listArray = new ArrayList<String>(Arrays.asList(mDrawerListItems));
		adapter = new FunDapter<String>(MainActivity.this, listArray, R.layout.menu_row, dict);
        // Set the adapter for the list view
		mDrawerList.setAdapter(adapter);
   
        mDrawerToggle = new ActionBarDrawerToggle(
        		MainActivity.this,
        		mDrawerLayout,
        		R.drawable.ic_navigation_drawer,
        		R.string.drawer_open,
        		R.string.drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Work History Fragment has a map view. If that map view is open, be sure to hide the action bar.
                if(mDisplayedNavItem == 2){
                	WorkHistoryFragment whf = getSavedFragment(WorkHistoryFragment.class);
                	if(whf != null && whf.isSlidingLayerOpen())
                		getActionBar().hide();
                }
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Make sure to set Action Bar visible when opening the drawer
                shouldToggleAB = !getActionBar().isShowing();
                if(shouldToggleAB)
                	getActionBar().show();
                mTitle = getActionBar().getTitle();
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        	
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
        if(savedInstanceState != null)
    		mDisplayedNavItem = savedInstanceState.getInt(ARG_CURR_NAV_DRAWER);
        else
        	mDisplayedNavItem = 0;
        // Remove WorkHistory fragment as it needs to be recreated with every oncreate
        Fragment savedWorkFragment = getSavedFragment(WorkHistoryFragment.class);
        if(savedWorkFragment != null)
        	getSupportFragmentManager().beginTransaction().remove(savedWorkFragment).commit();
        selectItem(mDisplayedNavItem);
        
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// Hide all action bar items when drawer is open and redisplay them when drawer is closed
		MenuItem info = menu.findItem(R.id.action_info);
		if(info != null)
			info.setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	// Check if the required fragments exist, if not create them and add them to the fragment manager
	private void initSectionFragments(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// Fragment tag should ideally be within each fragment. That'd make life a bit better.
		//TODO: move fragment tags to within each fragment
		ft.add(R.id.content_frame, lFrag, "lading-page");
		if(getSavedFragment(SkillsFragment.class) == null)
			addAndHideFrag(ft, SkillsFragment.newInstance(), "skills");
//		if(getSavedFragment(WorkHistoryFragment.class) == null)
//			addAndHideFrag(ft, new WorkHistoryFragment(), "work-history");
		if(getSavedFragment(EducationFragment.class) == null)
			addAndHideFrag(ft, EducationFragment.newInstance(), "education");
		if(getSavedFragment(MiscellaneousFragment.class) == null)
			addAndHideFrag(ft, MiscellaneousFragment.newInstance(), "misc");
		ft.commit();
		
	}
	// add Fragment to the transaction and then hides it. Used in intialization
	public void addAndHideFrag(FragmentTransaction ft, Fragment frag, String tag){
		ft.add(R.id.content_frame, frag, tag);
		ft.hide(frag);
	}
	
	/** Swaps fragments in the main content view */
	@OnItemClick(R.id.left_drawer)
	public void selectItem(int position) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment;
		FragmentTransaction ft = fragmentManager.beginTransaction();
		switch(position){
			case 1:
				fragment = getSavedFragment(SkillsFragment.class);
				break;
			case 2:
				// Create WorkHistory fragment if it doesn't exist. The reason this fragment is created here as opposed to in the
				// initSectionFragments() function is because WorkHistoryFragment cannot be statically invoked. This is due to it
				// being a subclass from MapFragment (I think. Adding a private constructor gave errors)
				fragment = getSavedFragment(WorkHistoryFragment.class);
				if(fragment == null){
					fragment = new WorkHistoryFragment();
					ft.add(R.id.content_frame, fragment, "work-history");
				}
				else{
					// If fragment already exists, check if we need to hide Action Bar
					if(((WorkHistoryFragment)fragment).isSlidingLayerOpen())
						getActionBar().hide();
				}
				break;
			case 3:
				fragment = getSavedFragment(EducationFragment.class);
				break;
			case 4:
				// Misc fragment requires networking, but only make the network call when the user clicks on the fragment
				fragment = getSavedFragment(MiscellaneousFragment.class);
				((MiscellaneousFragment)fragment).makeNetworkRequest(MiscellaneousFragment.CACHED_DURATION);
				break;
			default:
				fragment = lFrag;				
				break;
				
			}
		
		ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		ft.show(fragment);
		hideAllOtherFragments(ft, fragment);
		resetAB = mDisplayedNavItem != position;
		mDisplayedNavItem = position;
	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    if(position != 0)
	    	setTitle(mDrawerListItems[position]);
	    else
	    	setTitle(mDrawerTitle);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}
	// Hide all fragments other than ft
	private void hideAllOtherFragments(FragmentTransaction ft, Fragment f){
		List<Fragment> frags = getSupportFragmentManager().getFragments();
		if(frags == null)
			return;
		for(int i = 0; i < frags.size(); i++){
			Fragment otherFrag = frags.get(i);
			if(otherFrag != null && !f.equals(otherFrag))
				ft.hide(otherFrag);
		}
		ft.commit();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	    Crouton.clearCroutonsForActivity(this);
	}
	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ARG_CURR_NAV_DRAWER, mDisplayedNavItem);
		outState.putCharSequence(ARG_AB_TITLE, getActionBar().getTitle());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mDisplayedNavItem = savedInstanceState.getInt(ARG_CURR_NAV_DRAWER);
		selectItem(mDisplayedNavItem);
		getActionBar().setTitle(savedInstanceState.getCharSequence(ARG_AB_TITLE));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
        }
//		if(item.getItemId() == R.id.action_settings){
//			View hidden = getLayoutInflater().inflate(R.layout.dialog_misc_legend, null);
//			ActivitySplitter as = new ActivitySplitter(MainActivity.this, hidden, getActionBar().getHeight());
//			as.prepareAnimation(-1);
//			as.animate(1000);
//		}
		return super.onOptionsItemSelected(item);
	}
	// Some logic for handling back presses
	@Override
	public void onBackPressed() {
		// Close sliding layer in we're in the Education fragment and sliding layer is open
		if(mDisplayedNavItem == 3){
			EducationFragment frag =  getSavedFragment(EducationFragment.class);
			if(frag != null && frag.isSlidingLayerOpen()){
				frag.closeSlidingLayer();
				return;
			}
		}
		// Close job details view if we're in WorkHistory and it's open
		else if(mDisplayedNavItem == 2){
			WorkHistoryFragment frag = getSavedFragment(WorkHistoryFragment.class);
			if(frag != null && frag.isDetailsViewOpen()){
				frag.minimizeDetailsView();
				return;
			}
		}
		// Otherwise, we return to landing page fragment
		if(mDisplayedNavItem != 0){
			if(!getActionBar().isShowing())
				getActionBar().show();
			selectItem(0);
			return;
		}
		// Exit app
		super.onBackPressed();
	}
	
	// Get specific fragment, if it exists in the fragment manager
	<T> T getSavedFragment(Class<T> fragmentClass){
		final List<Fragment> frags = getSupportFragmentManager().getFragments();
		if(frags == null)
			return null;
		for(int i = 0; i < frags.size(); i++){
			Fragment frag = frags.get(i);
			if(frag != null && fragmentClass == frags.get(i).getClass()){
				return fragmentClass.cast(frag);
			}
		}
		
		return null;
	}
	
}
