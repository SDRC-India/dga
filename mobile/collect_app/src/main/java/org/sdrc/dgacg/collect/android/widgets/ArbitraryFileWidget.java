/*
 * Copyright 2018 Nafundi
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

package org.sdrc.dgacg.collect.android.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryPrompt;
import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.activities.FormEntryActivity;
import org.sdrc.dgacg.collect.android.utilities.ApplicationConstants;
import org.sdrc.dgacg.collect.android.utilities.FileUtil;
import org.sdrc.dgacg.collect.android.utilities.MediaManager;
import org.sdrc.dgacg.collect.android.utilities.MediaUtil;
import org.sdrc.dgacg.collect.android.widgets.interfaces.FileWidget;

import java.io.File;

import timber.log.Timber;

public class ArbitraryFileWidget extends QuestionWidget implements FileWidget {

    @NonNull
    private FileUtil fileUtil;

    private String binaryName;

    private Button chooseFileButton;
    private TextView chosenFileNameTextView;
    private LinearLayout answerLayout;

    public ArbitraryFileWidget(Context context, FormEntryPrompt prompt) {
        this(context, prompt, new FileUtil());
    }

    ArbitraryFileWidget(Context context, FormEntryPrompt prompt, @NonNull FileUtil fileUtil) {
        super(context, prompt);

        this.fileUtil = fileUtil;
        binaryName = prompt.getAnswerText();

        setUpLayout();
    }

    @Override
    public void deleteFile() {
        MediaManager
                .INSTANCE
                .markOriginalFileOrDelete(getFormEntryPrompt().getIndex().toString(),
                        getInstanceFolder() + File.separator + binaryName);
        binaryName = null;
    }

    @Override
    public IAnswerData getAnswer() {
        return binaryName != null ? new StringData(binaryName) : null;
    }

    @Override
    public void clearAnswer() {
        answerLayout.setVisibility(GONE);
        deleteFile();
    }

    @Override
    public void onButtonClick(int buttonId) {
        waitForData();
        performFileSearch();
    }

    @Override
    public void setBinaryData(Object binaryuri) {
        if (binaryuri == null || !(binaryuri instanceof Uri)) {
            Timber.w("ArbitraryFileWidget's setBinaryData must receive a Uri object.");
            return;
        }

        Uri uri = (Uri) binaryuri;

        // get the file path and create a copy in the instance folder
        String sourcePath = getSourcePathFromUri(uri);
        String destinationPath = getDestinationPathFromSourcePath(sourcePath);

        File source = fileUtil.getFileAtPath(sourcePath);
        File newFile = fileUtil.getFileAtPath(destinationPath);

        fileUtil.copyFile(source, newFile);

        if (newFile.exists()) {
            // when replacing an answer remove the current one.
            if (binaryName != null && !binaryName.equals(newFile.getName())) {
                deleteFile();
            }

            binaryName = newFile.getName();
            chosenFileNameTextView.setText(binaryName);
            answerLayout.setVisibility(VISIBLE);
            Timber.i("Setting current answer to %s", newFile.getName());
        } else {
            Timber.e("Inserting Arbitrary file FAILED");
        }
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        chooseFileButton.setOnLongClickListener(l);
        answerLayout.setOnLongClickListener(l);
    }

    private void setUpLayout() {
        LinearLayout widgetLayout = new LinearLayout(getContext());
        widgetLayout.setOrientation(LinearLayout.VERTICAL);

        chooseFileButton = getSimpleButton(getContext().getString(R.string.choose_file));

        answerLayout = new LinearLayout(getContext());
        answerLayout.setOrientation(LinearLayout.HORIZONTAL);
        answerLayout.setGravity(Gravity.CENTER);

        ImageView attachmentImg = new ImageView(getContext());
        attachmentImg.setImageResource(R.drawable.ic_attachment);
        chosenFileNameTextView = getAnswerTextView(binaryName);
        chosenFileNameTextView.setGravity(Gravity.CENTER);

        answerLayout.addView(attachmentImg);
        answerLayout.addView(chosenFileNameTextView);
        answerLayout.setVisibility(binaryName == null ? GONE : VISIBLE);
        answerLayout.setOnClickListener(view -> openFile());

        widgetLayout.addView(chooseFileButton);
        widgetLayout.addView(answerLayout);

        addAnswerView(widgetLayout);
    }

    private void performFileSearch() {
        Intent intent = new Intent(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // all file types
        ((FormEntryActivity) getContext()).startActivityForResult(intent, ApplicationConstants.RequestCodes.ARBITRARY_FILE_CHOOSER);
    }

    private String getSourcePathFromUri(@NonNull Uri uri) {
        return new MediaUtil().getPathFromUri(getContext(), uri, MediaStore.Files.FileColumns.DATA);
    }

    private String getDestinationPathFromSourcePath(@NonNull String sourcePath) {
        String extension = sourcePath.substring(sourcePath.lastIndexOf('.'));
        return getInstanceFolder() + File.separator + new FileUtil().getRandomFilename() + extension;
    }

    public String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return extension != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) : null;
    }

    private void openFile() {
        Uri uri = Uri.fromFile(new File(getInstanceFolder() + File.separator + binaryName));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, getMimeType(getSourcePathFromUri(uri)));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(intent);
    }
}
