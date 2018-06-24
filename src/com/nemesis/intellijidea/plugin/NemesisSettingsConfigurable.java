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

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nullable;
import javax.swing.JComponent;

/**
 * @author Petar Tahchiev
 * @since 1.5
 */
public class NemesisSettingsConfigurable implements Configurable {

    protected final NemesisSettingsForm settingsForm = new NemesisSettingsForm();

    @Nls
    @Override
    public String getDisplayName() {
        return "Nemesis IDEA Plugin";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Nemesis IDEA Plugin configuration.";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        final ConfigurationService configurationService = ConfigurationService.getInstance();

        this.settingsForm.setData(configurationService.getState());

        return this.settingsForm.getMainPanel();
    }

    @Override
    public boolean isModified() {
        return this.settingsForm.isModified(ConfigurationService.getInstance().getState());
    }

    @Override
    public void apply() throws ConfigurationException {
        final ConfigurationService configurationService = ConfigurationService.getInstance();

        this.settingsForm.getData(configurationService.getState());
    }

    @Override
    public void reset() {
        this.settingsForm.setData(ConfigurationService.getInstance().getState());
    }

    @Override
    public void disposeUIResources() {
    }
}
