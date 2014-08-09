package com.govan.listfragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends ActionBarActivity implements
		OnQueryTextListener {

	public ImageAdapter adapter;
	private boolean bstop=false;
	public static SimpleListFragment list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// lunch in full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		System.out.println("inside onCreate \n");
		FragmentManager fm = getSupportFragmentManager();
		if (fm.findFragmentById(android.R.id.content) == null) {
			list = new SimpleListFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, list).commit();
		}
	}

	/* List the application */

	public class SimpleListFragment extends ListFragment {

		ArrayList<String> packName = null;
		Hashtable appPos = null;
		Intent appIntent = null;
		List<ApplicationInfo> packages = null;
		final PackageManager pm = getPackageManager();
		Container temp;
		ArrayList<Container> data = null;

		SimpleListFragment()

		{
			// appLabel = new ArrayList<String>(); //ArrayAdapter
			packName = new ArrayList<String>();
			temp = new Container();
			data = new ArrayList<Container>();
			appPos = new Hashtable();

		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

		
			temp = adapter.getItem(position);
			if (appPos.get(temp.Text) != null) {

				appIntent = pm.getLaunchIntentForPackage(packName
						.get((int) appPos.get(temp.Text)));
				if (appIntent != null) {
					startActivity(appIntent);
				} else {
					// updateListData();
					Toast.makeText(getApplicationContext(),
							temp.Text + " is not installed", 2).show();

				}
			} else {
				Toast.makeText(getApplicationContext(),
						temp.Text + " is not installed", 2).show();

			}
		}

		/*
		 * update the org arraylist in imageadapter class when application is
		 * uninstalled
		 */
		public void updateListData() {
			createListData(adapter.org);
			adapter.notifyDataSetChanged();
		}

		/*
		 * Get the installed application from package manager and store it in to
		 * input "dataList"
		 */
		public void createListData(ArrayList<Container> dataList) {
			if (dataList != null) {

				dataList.clear();
				appPos.clear();
				packName.clear();
			}
			Package MyPack = MainActivity.class.getPackage();
			packages = pm
					.getInstalledApplications(PackageManager.GET_META_DATA);
			int i = 0;

			for (ApplicationInfo packageInfo : packages) {

				if ((pm.getLaunchIntentForPackage(packageInfo.packageName) != null)
						&& (!pm.getLaunchIntentForPackage(
								packageInfo.packageName).equals(""))) {

					if (!packageInfo.packageName.equals(MyPack.getName())) {
						packName.add(packageInfo.packageName);
						appPos.put(
								(String) pm.getApplicationLabel(packageInfo), i);

						Container temp = new Container(
								(String) pm.getApplicationLabel(packageInfo),
								pm.getApplicationIcon(packageInfo));

						dataList.add(temp);

						i++;
					}
				}

			}

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			createListData(data);
			adapter = new ImageAdapter(inflater.getContext(),
					R.layout.editlayout, data);
			setListAdapter(adapter);
			adapter.setNotifyOnChange(true);

			return super.onCreateView(inflater, container, savedInstanceState);
		}

		public void onDestroyView() {
			packName = null;
			appPos = null;
			System.gc();
			super.onDestroyView();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		searchView.setOnQueryTextListener(this);

		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_search) {
			// Toast.makeText(this, "Selected Item: " + item.getTitle(),
			// Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public boolean onQueryTextChange(String newText) {

		adapter.getFilter().filter(newText);
		return true;
	}

	public boolean onQueryTextSubmit(String query) {

		adapter.getFilter().filter(query);
		return true;
	}

	protected void onPause() {
		super.onPause();
		// System.out.println("inside onPause \n");
	}

	protected void onStart() {
		
		super.onStart();
		


		// System.out.println("inside onSa \n");
	}
	
	protected void onResume()
	{
		super.onResume();
		
		if(broascastReceiver.Installed==true && bstop==true)
		{
			list.updateListData();
			broascastReceiver.Installed=false;
		}
	
	}

	protected void onStop() {
		super.onStop();
		bstop=true;
		// System.out.println("inside onStop \n");
	}

	protected void onDestroy() {
		adapter = null;
		System.gc();
		super.onDestroy();
	}

}
