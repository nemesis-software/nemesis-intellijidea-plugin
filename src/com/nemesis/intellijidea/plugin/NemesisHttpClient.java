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

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Petar Tahchiev
 * @since 1.5
 */
public class NemesisHttpClient {

    public static Map.Entry<Boolean, String> importCsv(final String csv, final String url, final String username, final String password) {
        try {
            SSLContext context = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(context, new NoopHostnameVerifier());
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", csf).build();
            HttpClientConnectionManager ccm = new PoolingHttpClientConnectionManager(registry);

            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(AuthScope.ANY, credentials);

            HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).setConnectionManager(ccm).build();

            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

            Map<String, String> params = new HashMap<>();
            params.put("csv", csv);

            httpPost.setEntity(new StringEntity(new Gson().toJson(params), ContentType.APPLICATION_JSON));

            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return new AbstractMap.SimpleEntry<>(Boolean.TRUE, "");
            } else {
                return new AbstractMap.SimpleEntry<>(Boolean.FALSE, response.getStatusLine().getReasonPhrase());
            }
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | IOException e) {
            return new AbstractMap.SimpleEntry<>(Boolean.FALSE, e.getMessage());
        }
    }
}
