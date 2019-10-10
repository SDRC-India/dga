package org.sdrc.dgacg.collect.android.preferences;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.sdrc.dgacg.collect.android.R;

import static org.sdrc.dgacg.collect.android.preferences.PreferencesActivity.INTENT_KEY_ADMIN_MODE;

public class BasePreferenceFragment extends PreferenceFragment {

    protected Toolbar toolbar;
    private LinearLayout root;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initToolbar(getPreferenceScreen(), view);

        // removes disabled preferences if in general settings
        if (getActivity() instanceof PreferencesActivity) {
            Bundle args = getArguments();
            if (args != null) {
                final boolean adminMode = getArguments().getBoolean(INTENT_KEY_ADMIN_MODE, false);
                if (!adminMode) {
                    removeAllDisabledPrefs();
                }
            } else {
                removeAllDisabledPrefs();
            }
        }

        super.onViewCreated(view, savedInstanceState);
    }

    // inflates toolbar in the preference fragments
    public void initToolbar(PreferenceScreen preferenceScreen, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (getActivity() instanceof PreferencesActivity) {
                root = (LinearLayout) ((ViewGroup) view.findViewById(android.R.id.list).getRootView()).getChildAt(0);
                toolbar = root.findViewById(R.id.toolbar);

            } else {
                root = (LinearLayout) view.findViewById(android.R.id.list).getParent().getParent();
                toolbar = (Toolbar) LayoutInflater.from(getActivity()).inflate(R.layout.toolbar_without_progressbar, root, false);

                inflateToolbar(preferenceScreen.getTitle());
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            root = (LinearLayout) view.findViewById(android.R.id.list).getParent();
            toolbar = (Toolbar) LayoutInflater.from(getActivity()).inflate(R.layout.toolbar_without_progressbar, root, false);

            inflateToolbar(preferenceScreen.getTitle());
        }
    }

    private void inflateToolbar(CharSequence title) {
        toolbar.setTitle(title);
        root.addView(toolbar, 0);

        View shadow = LayoutInflater.from(getActivity()).inflate(R.layout.toolbar_action_bar_shadow, root, false);
        root.addView(shadow, 1);
    }

    private void removeAllDisabledPrefs() {
        DisabledPreferencesRemover preferencesRemover = new DisabledPreferencesRemover((PreferencesActivity) getActivity(), this);
        preferencesRemover.remove(AdminKeys.adminToGeneral);
        preferencesRemover.removeEmptyCategories();
    }
}