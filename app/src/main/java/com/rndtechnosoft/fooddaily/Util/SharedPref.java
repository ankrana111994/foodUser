package com.rndtechnosoft.fooddaily.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.rndtechnosoft.fooddaily.BuildConfig;

public class SharedPref {

    static SharedPreferences shPreferences;
	public static String strPrefName = BuildConfig.APPLICATION_ID;
	public static String USER_ID = "userid";
	public static String USER_MOBILE = "mobile";
	public static String USER_NAME = "username";
	public static String USER_IMAGE = "user_image";
	public static String USER_GENDER = "user_gender";
	public static String USER_EMAIL = "user_email";
	public static String MIN_ORDER_AMT = "min_amt";
	public static String MIN_MSG = "min_msg";
	public static String WALLET = "wallet";
	public static String CURRENCY = "currency";
	public static String PAYPAL = "paypal";
	public static String DASHED = "dashed";
	public static String APP_STATUS = "app_status";
	public static String FIRST_LAUNCH = "first_launch";

    public static void clearAllPreferences(Activity activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);

		Editor editor = shPreferences.edit();
		editor.clear();
		editor.apply(); // important! Don't forget!
	}

	public static String getPreference(String key, String Default,
									   Activity activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(key, Default);
	}

    public static boolean setPreference(String key, String value,
                                        Activity activity) {
		if (value != null) {
			shPreferences = activity.getSharedPreferences(strPrefName,
					Context.MODE_PRIVATE);
			Editor editor = shPreferences.edit();
			editor.putString(key, value);
			editor.apply();
			return true;
		}
		return false;
	}
	public static String getAppLaunchStatus(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(FIRST_LAUNCH, "true");
	}

    public static String getUserName(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_NAME, "");
	}

	public static String getUserEmail(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_EMAIL, "");
	}

	public static String getUserId(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_ID, "");
	}


	public static String getMobileNumber(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_MOBILE, "");
	}

	public static String getMinAmt(Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(MIN_ORDER_AMT, "");
	}

	public static String getWallet(Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(WALLET, "");
	}

	public static String getMinMsg(Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(MIN_MSG, "");
	}

	public static String getCurrency(Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(CURRENCY, "");
	}

	public static String getPaypal(Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(PAYPAL, "");
	}

	public static String getDASHED(Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(DASHED, "");
	}
}
