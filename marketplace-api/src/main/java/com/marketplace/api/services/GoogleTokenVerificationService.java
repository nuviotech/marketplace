package com.marketplace.api.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;

@Service
public class GoogleTokenVerificationService {

	private static final String GOOGLE_CERTS_URL = "https://www.googleapis.com/oauth2/v1/certs";
	private static final String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";
	
	private Map<String, String> certCache = new HashMap<>();
	private long certCacheTime = 0;
	private static final long CERT_CACHE_DURATION = 3600000; // 1 hour
	
	@Value("${google.client.id}")
	private String googleClientId;
	
	Logger logger = LoggerFactory.getLogger(GoogleTokenVerificationService.class);

	/**
	 * Verify Google ID token and extract claims
	 * 
	 * @param token Google ID token from frontend
	 * @return JSONObject with verified claims (sub, email, given_name, family_name, etc.)
	 */
	public JSONObject verifyGoogleToken(String token) {
		try {
			// Use Google's tokeninfo endpoint to verify and get claims
			// This is simpler and doesn't require managing certificates
			URL url = new URL(GOOGLE_TOKEN_INFO_URL + "?id_token=" + token);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				logger.warn("Google token verification failed with code: " + responseCode);
				return new JSONObject().put("error", "Invalid token").put("status", 1);
			}

			// Read response
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();

			JSONObject verifiedClaims = new JSONObject(response.toString());

			// Verify the audience (client ID)
			String aud = verifiedClaims.optString("aud", "");
			if (!aud.equals(googleClientId)) {
				logger.warn("Google token audience mismatch. Expected: " + googleClientId + ", Got: " + aud);
				return new JSONObject().put("error", "Audience mismatch").put("status", 1);
			}

			// Verify email is present
			String email = verifiedClaims.optString("email", "");
			if (email == null || email.isEmpty()) {
				logger.warn("Google token missing email claim");
				return new JSONObject().put("error", "Email not provided").put("status", 1);
			}

			// Extract sub (stable Google ID)
			String sub = verifiedClaims.optString("sub", "");
			if (sub == null || sub.isEmpty()) {
				logger.warn("Google token missing sub claim");
				return new JSONObject().put("error", "User ID not provided").put("status", 1);
			}

			logger.info("Google token verified successfully for email: " + email);

			// Return verified claims
			return new JSONObject()
					.put("sub", sub)
					.put("email", email)
					.put("given_name", verifiedClaims.optString("given_name", ""))
					.put("family_name", verifiedClaims.optString("family_name", ""))
					.put("picture", verifiedClaims.optString("picture", ""))
					.put("status", 0);

		} catch (Exception e) {
			logger.error("Error verifying Google token: " + e.getMessage());
			e.printStackTrace();
			return new JSONObject().put("error", "Token verification failed").put("status", 1);
		}
	}
}
