package com.base444.android.headsuptaichung;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

public class Preferences<T> {
	protected SharedPreferences sharedPreferences;
	protected Editor mEditor;

	Preferences(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}

	@SuppressWarnings("unchecked")
	public T clear() throws Exception{
		ensureBeginEdit();
		mEditor.clear();
		return (T) this;
	}

	@SuppressLint("NewApi")
	public void apply() throws Exception{
		ensureBeginEdit();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			mEditor.apply();
		} else {
			// fallback to old api
			mEditor.commit();
		}
	}

	public boolean commit() {
		return mEditor.commit();
	}

	@SuppressLint("CommitPrefEdits")
	@SuppressWarnings("unchecked")
	public T beginEdit() {
		mEditor = sharedPreferences.edit();
		return (T) this;
	}

	public void ensureBeginEdit() throws Exception {
		if (mEditor == null) {
			throw new Exception("beginEdit() must be called before any editing");
		}
	}
}