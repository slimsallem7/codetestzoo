package com.zoostudio.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.zoostudio.ngon.Config;

public class NgonRestClient {

    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;
    private int responseCode;
    private String message;
    private String response;
    private String jsonBody;
    private String mTokenKey;
    private String mTokenSecret;
    private boolean isAuth;
    private String mDumpData;

    private final static int TIMEOUT = 30 * 1000; // Milliseconds

    public NgonRestClient(String token_key, String token_secret) {
        this(token_key, token_secret, true);
    }

    public NgonRestClient(String token_key, String token_secret, boolean isAuth) {

        mTokenKey = token_key;
        mTokenSecret = token_secret;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
        this.isAuth = isAuth;

        Log.e("Token", token_key);
        Log.e("Token secret", token_secret);
    }

    public void addParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void addParam(String name, double value) {
        params.add(new BasicNameValuePair(name, value + ""));
    }

    public void addParam(String name, int value) {
        params.add(new BasicNameValuePair(name, value + ""));
    }

    public void addParam(String name, float value) {
        params.add(new BasicNameValuePair(name, value + ""));
    }

    public void get(String url) {
        url = buildApiUrl(url);
        try {
            HttpGet request = new HttpGet(url + addGetParams());
            request = (HttpGet) addHeaderParams(request);
            executeRequest(request, url);
            params.removeAll(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void post(String url) {
        url = buildApiUrl(url);
        HttpPost request = new HttpPost(url);
        try {
            request = (HttpPost) addHeaderParams(request);
            request = (HttpPost) addBodyParams(request);
            executeRequest(request, url);
            params.removeAll(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postMultiPart(String url, String paramName, ContentBody file) {
        url = buildApiUrl(url);

        HttpPost request = new HttpPost(url);
        try {
            request = (HttpPost) addHeaderParams(request);
            MultipartEntity reqEntity = new MultipartEntity();

            reqEntity.addPart(paramName, file);
            if (params != null) {
                for (NameValuePair param : params) {
                    reqEntity.addPart(param.getName(), new StringBody(param.getValue()));
                }
            }

            request.setEntity(reqEntity);
            executeRequest(request, url);
            params.removeAll(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String url) {
        url = buildApiUrl(url);
        HttpPut request = new HttpPut(url);
        try {
            request = (HttpPut) addHeaderParams(request);
            request = (HttpPut) addBodyParams(request);
            executeRequest(request, url);
            params.removeAll(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String url) {
        url = buildApiUrl(url);
        HttpDelete request = new HttpDelete(url);
        try {
            request = (HttpDelete) addHeaderParams(request);
            params.removeAll(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executeRequest(request, url);
    }

    public String getErrorMessage() {
        return message;
    }

    public String getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setJSONString(String data) {
        jsonBody = data;
    }

    private void executeRequest(HttpUriRequest request, String url) {

        CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(Config.CONSUMER_KEY, Config.CONSUMER_SECRET);
        HttpParameters httpParam = new HttpParameters();
        httpParam.put("oauth_callback", "abc");
        consumer.setAdditionalParameters(httpParam);

        if (isAuth) {
            consumer.setTokenWithSecret(mTokenKey, mTokenSecret);
        }

        DefaultHttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);

        HttpResponse httpResponse;

        try {
            consumer.sign(request);
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
//                Log.e("Response", "data: " + response);
                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            Log.e("HTTP", "Connection is refuse");
            Log.e("Dump data", "data: " + mDumpData);
            if (Config.DEBUG_MODE && mDumpData != null) {
                response = getDumpData();
                responseCode = 200;
            }
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }
    }

    private HttpUriRequest addHeaderParams(HttpUriRequest request) throws Exception {
        for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
        }

        return request;
    }

    private HttpUriRequest addBodyParams(HttpUriRequest request) throws Exception {
        if (jsonBody != null) {
            request.addHeader("Content-Type", "application/json");
            if (request instanceof HttpPost) ((HttpPost) request).setEntity(new StringEntity(jsonBody, "UTF-8"));
            else if (request instanceof HttpPut) ((HttpPut) request).setEntity(new StringEntity(jsonBody, "UTF-8"));

        } else if (!params.isEmpty()) {
            if (request instanceof HttpPost) ((HttpPost) request).setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            else if (request instanceof HttpPut) ((HttpPut) request).setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        }
        return request;
    }

    private String addGetParams() throws Exception {
        StringBuffer combinedParams = new StringBuffer();
        if (!params.isEmpty()) {
            combinedParams.append("?");
            for (NameValuePair p : params) {
                
                Log.e("get param", "param: "+params);
                
                combinedParams.append((combinedParams.length() > 1 ? "&" : "") + p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8"));
            }
        }
        return combinedParams.toString();
    }

    private static String buildApiUrl(String url) {
        url = Config.ROOT_URL + url;
        Log.i("Url request", "url :" + url);
        return url;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getDumpData() {
        return mDumpData;
    }

    public void setDumpData(String mDumpData) {
        this.mDumpData = mDumpData;
    }
}
