/*
 * Copyright (C) 2017 University of Washington
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

package org.sdrc.dgacg.collect.android.utilities;

import android.support.annotation.NonNull;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.exception.BadUrlException;

import java.util.regex.Pattern;

public class UrlUtils {

    private UrlUtils() {

    }

    @NonNull
    public static String getSpreadsheetID(String urlString) throws BadUrlException {
        // now parse the url string if we have one
        final String googleHeader = "docs.google.com/spreadsheets/d/";
        if (urlString == null || urlString.length() < googleHeader.length()) {
            throw new BadUrlException(
                    Collect.getInstance().getString(R.string.invalid_sheet_id, urlString));
        } else {
            int start = urlString.indexOf(googleHeader) + googleHeader.length();
            int end = urlString.indexOf('/', start);
            if (end == -1) {
                // if there wasn't a "/", just try to get the end
                end = urlString.length();
            }
            if (start == -1) {
                throw new BadUrlException(
                        Collect.getInstance().getString(R.string.invalid_sheet_id, urlString));
            }
            return urlString.substring(start, end);
        }
    }
    public static boolean isValidUrl(String url) {

        final Pattern urlPattern = Pattern.compile("^https?:\\/\\/.+$", Pattern.CASE_INSENSITIVE);

        return urlPattern.matcher(url).matches();
    }

}
