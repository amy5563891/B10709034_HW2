package com.example.android.b10709034_hw2;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override

    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_visualizer);
        //↓設定summary
        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
        int count = prefScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if ((p instanceof ListPreference)) {

                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {//SharePreference變動時,Summary一起更換
        Preference preference = findPreference(key);
        if (null != preference) {
            if (preference instanceof ListPreference) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    private void setPreferenceSummary(Preference p, String value) {
        if (p instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) p;
            int prefIndex = listPreference.findIndexOfValue(value);         //find index

            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


}