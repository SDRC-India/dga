package org.sdrc.dgacg.collect.android.widgets;

import android.support.annotation.NonNull;

import net.bytebuddy.utility.RandomString;

import org.javarosa.core.model.data.StringData;
import org.sdrc.dgacg.collect.android.widgets.base.GeneralStringWidgetTest;
import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */
public class StringWidgetTest extends GeneralStringWidgetTest<StringWidget, StringData> {
    public StringWidgetTest() {
        super();
    }

    @NonNull
    @Override
    public StringWidget createWidget() {
        return new StringWidget(RuntimeEnvironment.application, formEntryPrompt, false);
    }

    @NonNull
    @Override
    public StringData getNextAnswer() {
        return new StringData(RandomString.make());
    }
}
