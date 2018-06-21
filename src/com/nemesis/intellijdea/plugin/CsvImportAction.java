/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nemesis.intellijdea.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;

import java.util.Map;

/**
 * @author Petar Tahchiev
 * @since 2.0.1
 */
public class CsvImportAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();

        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        String csvSnippet = null;

        if (start != end) {
            csvSnippet = document.getText(new TextRange(start, end));
        } else {
            csvSnippet = document.getText();
        }

        ConfigurationService cs = ConfigurationService.getInstance();

        if (cs != null && cs.getState() != null) {
            String url = cs.getState().getUrl();
            String username = cs.getState().getUsername();
            String password = cs.getState().getPassword();

            if (url == null || username == null || password == null) {
                Messages.showErrorDialog(project, "Please configure the plugin first!", "Plugin Not Configured!");
                return;
            }

            Map.Entry<Boolean, String> result = NemesisHttpClient.importCsv(csvSnippet, url, username, password);
            if (result.getKey()) {
                Notifications.Bus.notify(new Notification(Notifications.SYSTEM_MESSAGES_GROUP_ID, "Success!", "Csv was imported successfully!",
                                                          NotificationType.INFORMATION));
            } else {
                Notifications.Bus.notify(
                                new Notification(Notifications.SYSTEM_MESSAGES_GROUP_ID, "Error!", "Could not import the csv!", NotificationType.ERROR));
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible(project != null && editor != null && ((EditorImpl) editor).getVirtualFile().getCanonicalPath().endsWith(".csv"));
    }

}
