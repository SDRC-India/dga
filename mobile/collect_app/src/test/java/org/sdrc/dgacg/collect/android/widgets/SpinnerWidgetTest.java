package org.sdrc.dgacg.collect.android.widgets;

import android.support.annotation.NonNull;

import org.sdrc.dgacg.collect.android.widgets.base.GeneralSelectOneWidgetTest;
import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */

public class SpinnerWidgetTest extends GeneralSelectOneWidgetTest<SpinnerWidget> {
    @NonNull
    @Override
    public SpinnerWidget createWidget() {
        return new SpinnerWidget(RuntimeEnvironment.application, formEntryPrompt);
    }
}
