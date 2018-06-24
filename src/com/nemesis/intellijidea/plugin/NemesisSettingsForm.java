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
package com.nemesis.intellijidea.plugin;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author Petar Tahchiev
 * @since 1.5
 */
public class NemesisSettingsForm {

    private JPanel mainPanel;

    private JTextField username;

    private JPasswordField password;

    private JTextField url;

    public void setData(final NemesisPluginSettings data) {
        username.setText(data.getUsername());
        password.setText(data.getPassword());
        url.setText(data.getUrl());
    }

    public void getData(final NemesisPluginSettings data) {
        data.setUsername(username.getText());
        data.setPassword(new String(password.getPassword()));
        data.setUrl(getUrl());
    }

    @NotNull
    private String getUrl() {
        String url = this.url.getText().trim();
        return url.endsWith("/") ? url.substring(0, url.length() - 1).trim() : url;
    }

    public boolean isModified(final NemesisPluginSettings data) {
        if (!StringUtil.equals(getUrl(), data.getUrl())) {
            return true;
        }
        if (!StringUtil.equals(new String(password.getPassword()), data.getPassword())) {
            return true;
        }
        if (!StringUtil.equals(username.getText(), data.getUsername())) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
