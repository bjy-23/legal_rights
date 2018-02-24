package com.wonders.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class FragmentUtil {

	public static void replaceStack(FragmentManager fm, Fragment f, int id) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(id, f);
		ft.addToBackStack("");
		ft.commitAllowingStateLoss();
	}

	public static void addStack(FragmentManager fm, Fragment f, int id) {
		addStack(fm, f, id, "");
	}

	public static void addStack(FragmentManager fm, Fragment f, int id, String tag) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(id, f);
		ft.addToBackStack(tag);
		ft.commit();
	}

	public static void addNoStack(FragmentManager fm, Fragment f, int id) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(id, f);
		ft.commit();
	}


	public static void clearAllFragments(FragmentManager fm){
		int backStackCount = fm.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {
			fm.popBackStackImmediate();
		}
	}
}