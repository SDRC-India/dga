package org.sdrc.dgacg.collect.android.widgets;

import android.support.annotation.NonNull;

import org.javarosa.core.model.data.IntegerData;
import org.sdrc.dgacg.collect.android.widgets.base.RangeWidgetTest;
import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */

public class RangeIntegerWidgetTest extends RangeWidgetTest<RangeIntegerWidget, IntegerData> {

    public RangeIntegerWidgetTest() {
        super();
    }

    @NonNull
    @Override
    public RangeIntegerWidget createWidget() {
        return new RangeIntegerWidget(RuntimeEnvironment.application, formEntryPrompt);
    }

    @NonNull
    @Override
    public IntegerData getNextAnswer() {
        return new IntegerData(random.nextInt());
    }
}
