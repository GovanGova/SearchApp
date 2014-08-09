package com.govan.listfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class broascastReceiver extends BroadcastReceiver {

	static Boolean Installed=false;
	@Override
	//
	//this method will be called  when any application is installed or uninstalled
	//which is registered as broadcast receiver in manifest file
	//
	public void onReceive(Context context, Intent arg1) {
		
		//MainActivity.list.updateListData();
		//if(arg1.getAction().toString()=="ACTION_PACKAGE_ADDED")
		//	System.out.println("\nPackage added\n");
		//else if(arg1.getAction().toString()=="ACTION_PACKAGE_FULLY_REMOVED")
			//System.out.println("\nPackage removed\n");
			
			Installed=true;
			//Toast.makeText(context, "Appliction is installed or uinstalled"+Installed, 2).show();
	}

}
