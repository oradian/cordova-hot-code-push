package com.nordnetab.chcp.main.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;

/**
 * Created by Nikolay Demyankov on 03.06.16.
 * <p/>
 * Helper class to work with URLConnection
 */
public class URLConnectionHelper {

    // connection timeout in milliseconds
    private static final int CONNECTION_TIMEOUT = 30000;

    // data read timeout in milliseconds
    private static final int READ_TIMEOUT = 30000;
    
    public static void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            java.security.cert.X509Certificate[] myTrustedAnchors = new java.security.cert.X509Certificate[0];
                            return myTrustedAnchors;
                        }

						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
								throws CertificateException {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
								throws CertificateException {
							// TODO Auto-generated method stub
							
						}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				
				@Override
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return true;
				}
			});
        } catch (Exception e) {
        }
    }

    /**
     * Create URLConnection instance.
     *
     * @param url            to what url
     * @param requestHeaders additional request headers
     * @return connection instance
     * @throws IOException when url is invalid or failed to establish connection
     */
    public static URLConnection createConnectionToURL(final String url, final Map<String, String> requestHeaders) throws IOException {
    	trustAllCertificates();
        final URL connectionURL = URLUtility.stringToUrl(url);
        if (connectionURL == null) {
            throw new IOException("Invalid url format: " + url);
        }

        final URLConnection urlConnection = connectionURL.openConnection();
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(READ_TIMEOUT);

        if (requestHeaders != null) {
            for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return urlConnection;
    }

}
