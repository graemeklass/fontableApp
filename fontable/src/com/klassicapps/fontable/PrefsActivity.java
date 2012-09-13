package com.klassicapps.fontable;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefsActivity extends PreferenceActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);        
       
        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName("fontablePrefs");
 
        //--load the preferences from an XML file---
        addPreferencesFromResource(R.layout.preferences_layout);
        
    }
    
    
    
    
    
    
}
