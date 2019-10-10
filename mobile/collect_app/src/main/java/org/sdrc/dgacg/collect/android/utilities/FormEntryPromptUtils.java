/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sdrc.dgacg.collect.android.utilities;

import android.content.Context;

import org.javarosa.core.model.data.DateData;
import org.javarosa.core.model.data.DateTimeData;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.SelectMultiData;
import org.javarosa.core.model.data.helper.Selection;
import org.javarosa.form.api.FormEntryPrompt;
import org.sdrc.dgacg.collect.android.dao.ItemsetDao;
import org.sdrc.dgacg.collect.android.logic.FormController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;

import static org.javarosa.core.model.Constants.DATATYPE_TEXT;

public class FormEntryPromptUtils {

    private FormEntryPromptUtils() {
    }

    public static String getAnswerText(FormEntryPrompt fep, Context context, FormController formController) {
        IAnswerData data = fep.getAnswerValue();
        final String appearance = fep.getQuestion().getAppearanceAttr();

        if (data instanceof SelectMultiData) {
            StringBuilder b = new StringBuilder();
            String sep = "";
            for (Selection value : (List<Selection>) data.getValue()) {
                b.append(sep);
                sep = ", ";
                b.append(fep.getSelectItemText(value));
            }
            return b.toString();
        }

        if (data instanceof DateTimeData) {
            return DateTimeUtils.getDateTimeLabel((Date) data.getValue(),
                    DateTimeUtils.getDatePickerDetails(appearance), true, context);
        }

        if (data instanceof DateData) {
            return DateTimeUtils.getDateTimeLabel((Date) data.getValue(),
                    DateTimeUtils.getDatePickerDetails(appearance), false, context);
        }

        if (data != null && appearance != null && appearance.contains("thousands-sep")) {
            try {
                final BigDecimal answerAsDecimal = new BigDecimal(fep.getAnswerText());

                DecimalFormat df = new DecimalFormat();
                df.setGroupingSize(3);
                df.setGroupingUsed(true);
                df.setMaximumFractionDigits(Integer.MAX_VALUE);

                // Use . as decimal marker for consistency with DecimalWidget
                DecimalFormatSymbols customFormat = new DecimalFormatSymbols();
                customFormat.setDecimalSeparator('.');

                if (df.getDecimalFormatSymbols().getGroupingSeparator() == '.') {
                    customFormat.setGroupingSeparator(' ');
                }

                df.setDecimalFormatSymbols(customFormat);

                return df.format(answerAsDecimal);
            } catch (NumberFormatException e) {
                return fep.getAnswerText();
            }
        }

        if (data != null && data.getValue() != null && fep.getDataType() == DATATYPE_TEXT
                && fep.getQuestion().getAdditionalAttribute(null, "query") != null) { // ItemsetWidget
            return new ItemsetDao().getItemLabel(fep.getAnswerValue().getDisplayText(), formController.getMediaFolder().getAbsolutePath(), formController.getLanguage());
        }

        return fep.getAnswerText();
    }

    public static String markQuestionIfIsRequired(String questionText, boolean isRequired) {
        if (isRequired) {
            if (questionText == null) {
                questionText = "";
            }
            questionText = "<span style=\"color:#F44336\">*</span> " + questionText;
        }

        return questionText;
    }
}
