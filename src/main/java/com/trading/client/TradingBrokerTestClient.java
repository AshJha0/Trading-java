package com.trading.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trading.config.TradingBrokerTestConfig;
import com.trading.error.TradingBrokerTestError;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Low-level HTTP client that wraps OkHttp3 for the TradingBrokerTest REST API.
 */
public class TradingBrokerTestClient {

    private final TradingBrokerTestConfig config;
    private final OkHttpClient http;
    final ObjectMapper mapper;

    public TradingBrokerTestClient(TradingBrokerTestConfig config) {
        this.config = config;
        this.http = new OkHttpClient.Builder()
                .connectTimeout(config.timeoutMs(), TimeUnit.MILLISECONDS)
                .readTimeout(config.timeoutMs(), TimeUnit.MILLISECONDS)
                .build();
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    // Package-private for testing
    TradingBrokerTestClient(TradingBrokerTestConfig config, OkHttpClient http) {
        this.config = config;
        this.http = http;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    private String baseUrl() {
        return config.effectiveBaseUrl();
    }

    private Request.Builder authorizedRequest(String path) {
        return new Request.Builder()
                .url(baseUrl() + path)
                .addHeader("Authorization", "Bearer " + config.apiKey())
                .addHeader("Accept", "application/json");
    }

    public String get(String path) throws TradingBrokerTestError {
        Request request = authorizedRequest(path).get().build();
        return execute(request);
    }

    public String post(String path, Map<String, String> form) throws TradingBrokerTestError {
        FormBody.Builder formBuilder = new FormBody.Builder();
        form.forEach(formBuilder::add);
        Request request = authorizedRequest(path)
                .post(formBuilder.build())
                .build();
        return execute(request);
    }

    public String delete(String path) throws TradingBrokerTestError {
        Request request = authorizedRequest(path).delete().build();
        return execute(request);
    }

    public String put(String path, Map<String, String> form) throws TradingBrokerTestError {
        FormBody.Builder formBuilder = new FormBody.Builder();
        form.forEach(formBuilder::add);
        Request request = authorizedRequest(path)
                .put(formBuilder.build())
                .build();
        return execute(request);
    }

    private String execute(Request request) throws TradingBrokerTestError {
        try (Response response = http.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : "";
            int code = response.code();
            if (code == 401 || code == 403) {
                throw new TradingBrokerTestError.AuthError("HTTP " + code);
            }
            if (code == 429) {
                throw new TradingBrokerTestError.RateLimitError();
            }
            if (!response.isSuccessful()) {
                throw new TradingBrokerTestError.ApiError(code, body);
            }
            return body;
        } catch (IOException e) {
            throw new TradingBrokerTestError.NetworkError(e.getMessage(), e);
        }
    }

    public TradingBrokerTestConfig getConfig() {
        return config;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
