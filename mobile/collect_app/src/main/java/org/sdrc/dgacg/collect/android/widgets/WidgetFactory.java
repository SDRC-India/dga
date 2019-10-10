/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.sdrc.dgacg.collect.android.widgets;

import android.content.Context;

import org.javarosa.core.model.Constants;
import org.javarosa.form.api.FormEntryPrompt;

import java.util.Locale;

import timber.log.Timber;

/**
 * Convenience class that handles creation of widgets.
 *
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class WidgetFactory {

    private WidgetFactory() {

    }

    /**
     * Returns the appropriate QuestionWidget for the given FormEntryPrompt.
     *
     * @param fep              prompt element to be rendered
     * @param context          Android context
     * @param readOnlyOverride a flag to be ORed with JR readonly attribute.
     */
    public static QuestionWidget createWidgetFromPrompt(FormEntryPrompt fep, Context context,
                                                        boolean readOnlyOverride) {

        // get appearance hint and clean it up so it is lower case and never null...
        String appearance = fep.getAppearanceHint();
        if (appearance == null) {
            appearance = "";
        }
        // for now, all appearance tags are in english...
        appearance = appearance.toLowerCase(Locale.ENGLISH);

        final QuestionWidget questionWidget;
        switch (fep.getControlType()) {
            case Constants.CONTROL_INPUT:
                switch (fep.getDataType()) {
                    case Constants.DATATYPE_DATE_TIME:
                        questionWidget = new DateTimeWidget(context, fep);
                        break;
                    case Constants.DATATYPE_DATE:
                        if (appearance.contains("ethiopian")) {
                            questionWidget = new EthiopianDateWidget(context, fep);
                        } else if (appearance.contains("coptic")) {
                            questionWidget = new CopticDateWidget(context, fep);
                        } else if (appearance.contains("islamic")) {
                            questionWidget = new IslamicDateWidget(context, fep);
                        } else {
                            questionWidget = new DateWidget(context, fep);
                        }
                        break;
                    case Constants.DATATYPE_TIME:
                        questionWidget = new TimeWidget(context, fep);
                        break;
                    case Constants.DATATYPE_DECIMAL:
                        if (appearance.startsWith("ex:")) {
                            questionWidget = new ExDecimalWidget(context, fep);
                        } else if (appearance.equals("bearing")) {
                            questionWidget = new BearingWidget(context, fep);
                        } else {
                            boolean useThousandSeparator = false;
                            if (appearance.contains("thousands-sep")) {
                                useThousandSeparator = true;
                            }
                            questionWidget = new DecimalWidget(context, fep, readOnlyOverride,
                                    useThousandSeparator);
                        }
                        break;
                    case Constants.DATATYPE_INTEGER:
                        if (appearance.startsWith("ex:")) {
                            questionWidget = new ExIntegerWidget(context, fep);
                        } else {
                            boolean useThousandSeparator = false;
                            if (appearance.contains("thousands-sep")) {
                                useThousandSeparator = true;
                            }
                            questionWidget = new IntegerWidget(context, fep, readOnlyOverride,
                                    useThousandSeparator);
                        }
                        break;
                    case Constants.DATATYPE_GEOPOINT:
                        questionWidget = new GeoPointWidget(context, fep);
                        break;
                    case Constants.DATATYPE_GEOSHAPE:
                        questionWidget = new GeoShapeWidget(context, fep);
                        break;
                    case Constants.DATATYPE_GEOTRACE:
                        questionWidget = new GeoTraceWidget(context, fep);
                        break;
                    case Constants.DATATYPE_BARCODE:
                        questionWidget = new BarcodeWidget(context, fep);
                        break;
                    case Constants.DATATYPE_TEXT:
                        String query = fep.getQuestion().getAdditionalAttribute(null, "query");
                        if (query != null) {
                            questionWidget = new ItemsetWidget(context, fep, appearance.startsWith("quick"));
                        } else if (appearance.startsWith("printer")) {
                            questionWidget = new ExPrinterWidget(context, fep);
                        } else if (appearance.startsWith("ex:")) {
                            questionWidget = new ExStringWidget(context, fep);
                        } else if (appearance.contains("numbers")) {
                            boolean useThousandsSeparator = false;
                            if (appearance.contains("thousands-sep")) {
                                useThousandsSeparator = true;
                            }
                            questionWidget = new StringNumberWidget(context, fep, readOnlyOverride,
                                    useThousandsSeparator);
                        } else if (appearance.equals("url")) {
                            questionWidget = new UrlWidget(context, fep);
                        } else {
                            questionWidget = new StringWidget(context, fep, readOnlyOverride);
                        }
                        break;
                    case Constants.DATATYPE_BOOLEAN:
                        questionWidget = new BooleanWidget(context, fep);
                        break;
                    default:
                        questionWidget = new StringWidget(context, fep, readOnlyOverride);
                        break;
                }
                break;
            case Constants.CONTROL_FILE_CAPTURE:
                questionWidget = new ArbitraryFileWidget(context, fep);
                break;
            case Constants.CONTROL_IMAGE_CHOOSE:
                if (appearance.equals("web")) {
                    questionWidget = new ImageWebViewWidget(context, fep);
                } else if (appearance.equals("signature")) {
                    questionWidget = new SignatureWidget(context, fep);
                } else if (appearance.contains("annotate")) {
                    questionWidget = new AnnotateWidget(context, fep);
                } else if (appearance.equals("draw")) {
                    questionWidget = new DrawWidget(context, fep);
                } else if (appearance.startsWith("align:")) {
                    questionWidget = new AlignedImageWidget(context, fep);
                } else {
                    questionWidget = new ImageWidget(context, fep);
                }
                break;
            case Constants.CONTROL_OSM_CAPTURE:
                questionWidget = new OSMWidget(context, fep);
                break;
            case Constants.CONTROL_AUDIO_CAPTURE:
                questionWidget = new AudioWidget(context, fep);
                break;
            case Constants.CONTROL_VIDEO_CAPTURE:
                questionWidget = new VideoWidget(context, fep);
                break;
            case Constants.CONTROL_SELECT_ONE:
                // SurveyCTO-revised support for dynamic select content (from .csv files)
                // consider traditional ODK appearance to be first word in appearance string
                if (appearance.startsWith("compact") || appearance.startsWith("quickcompact")) {
                    int numColumns = -1;
                    try {
                        String firstWord = appearance.split("\\s+")[0];
                        int idx = firstWord.indexOf('-');
                        if (idx != -1) {
                            numColumns =
                                    Integer.parseInt(firstWord.substring(idx + 1));
                        }
                    } catch (Exception e) {
                        // Do nothing, leave numColumns as -1
                        Timber.e("Exception parsing numColumns");
                    }

                    if (appearance.startsWith("quick")) {
                        questionWidget = new GridWidget(context, fep, numColumns, true);
                    } else {
                        questionWidget = new GridWidget(context, fep, numColumns, false);
                    }
                } else if (appearance.startsWith("minimal")) {
                    questionWidget = new SpinnerWidget(context, fep);
                } else if (appearance.startsWith("quick")) {
                    questionWidget = new SelectOneWidget(context, fep, true);
                } else if (appearance.equals("list-nolabel")) {
                    questionWidget = new ListWidget(context, fep, false);
                } else if (appearance.equals("list")) {
                    questionWidget = new ListWidget(context, fep, true);
                } else if (appearance.equals("label")) {
                    questionWidget = new LabelWidget(context, fep);
                } else if (appearance.contains("search") || appearance.contains("autocomplete")) {
                    questionWidget = new SelectOneSearchWidget(context, fep);
                } else if (appearance.startsWith("image-map")) {
                    questionWidget = new SelectOneImageMapWidget(context, fep);
                } else {
                    questionWidget = new SelectOneWidget(context, fep, false);
                }
                break;
            case Constants.CONTROL_SELECT_MULTI:
                // SurveyCTO-revised support for dynamic select content (from .csv files)
                // consider traditional ODK appearance to be first word in appearance string
                if (appearance.startsWith("compact")) {
                    int numColumns = -1;
                    try {
                        String firstWord = appearance.split("\\s+")[0];
                        int idx = firstWord.indexOf('-');
                        if (idx != -1) {
                            numColumns =
                                    Integer.parseInt(firstWord.substring(idx + 1));
                        }
                    } catch (Exception e) {
                        // Do nothing, leave numColumns as -1
                        Timber.e("Exception parsing numColumns");
                    }

                    questionWidget = new GridMultiWidget(context, fep, numColumns);
                } else if (appearance.startsWith("minimal")) {
                    questionWidget = new SpinnerMultiWidget(context, fep);
                } else if (appearance.startsWith("list-nolabel")) {
                    questionWidget = new ListMultiWidget(context, fep, false);
                } else if (appearance.startsWith("list")) {
                    questionWidget = new ListMultiWidget(context, fep, true);
                } else if (appearance.startsWith("label")) {
                    questionWidget = new LabelWidget(context, fep);
                } else if (appearance.contains("autocomplete")) {
                    questionWidget = new SelectMultipleAutocompleteWidget(context, fep);
                } else if (appearance.startsWith("image-map")) {
                    questionWidget = new SelectMultiImageMapWidget(context, fep);
                } else {
                    questionWidget = new SelectMultiWidget(context, fep);
                }
                break;
            case Constants.CONTROL_TRIGGER:
                questionWidget = new TriggerWidget(context, fep);
                break;
            case Constants.CONTROL_RANGE:
                switch (fep.getDataType()) {
                    case Constants.DATATYPE_INTEGER:
                        questionWidget = new RangeIntegerWidget(context, fep);
                        break;
                    case Constants.DATATYPE_DECIMAL:
                        questionWidget = new RangeDecimalWidget(context, fep);
                        break;
                    default:
                        questionWidget = new StringWidget(context, fep, readOnlyOverride);
                        break;
                }
                break;
            default:
                questionWidget = new StringWidget(context, fep, readOnlyOverride);
                break;
        }
        return questionWidget;
    }

}