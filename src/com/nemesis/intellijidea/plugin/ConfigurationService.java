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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.util.xmlb.XmlSerializerUtil;

import javax.annotation.Nullable;

/**
 * @author Petar Tahchiev
 * @since 1.5
 */
@State(name = "ConfigurationService", storages = { @Storage(value = "ConfigurationService.xml", scheme = StorageScheme.DIRECTORY_BASED) })
public class ConfigurationService implements PersistentStateComponent<NemesisPluginSettings> {

    private NemesisPluginSettings settings = new NemesisPluginSettings();

    @Override
    public NemesisPluginSettings getState() {
        return settings;
    }

    @Override
    public void loadState(NemesisPluginSettings state) {
        XmlSerializerUtil.copyBean(state, this.settings);
    }

    @Nullable
    public static ConfigurationService getInstance() {
        return ServiceManager.getService(ConfigurationService.class);
    }
}

