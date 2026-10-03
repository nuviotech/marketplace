package com.marketplace.api.serviceClasses;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

/**
 * RazorpayService handles all Razorpay payment operations.
 * 
 * Responsibilities:
 * - Create Razorpay orders server-side
 * - Verify payment signatures (server-side verification)
 * - Verify webhook signatures
 * - Manage test/live key selection
 * 
 * Security:
 * - Never logs secrets
 * - Never exposes secrets to frontend
 * - Always verifies signatures before marking payment successful
 */
@Service
public class RazorpayService {
	Logger logger = LoggerFactory.getLogger(RazorpayService.class);

	@Value("${razorpay.mode:test}")
	private String razorpayMode;

	@Value("${razorpay.RZTestKey:}")
	private String testKeyId;

	@Value("${razorpay.RZTestSecret:}")
	private String testKeySecret;

	@Value("${razorpay.RZLiveKey:}")
	private String liveKeyId;

	@Value("${razorpay.RZLiveSecret:}")
	private String liveKeySecret;

	@Value("${razorpay.webhook.secret:}")
	private String webhookSecret;

	/**
	 * Get active Razorpay Key ID (public) based on configured mode.
	 * This Key ID is sent to frontend for Razorpay Checkout.
	 * 
	 * @return Active Key ID (test or live)
	 */
	public String getActiveKeyId() {
		if ("live".equalsIgnoreCase(razorpayMode)) {
			return liveKeyId;
		}
		return testKeyId;
	}

	/**
	 * Get active Razorpay Secret (private) based on configured mode.
	 * This Secret is NEVER sent to frontend. Used only for verification.
	 * 
	 * @return Active Secret (test or live)
	 */
	private String getActiveSecret() {
		if ("live".equalsIgnoreCase(razorpayMode)) {
			return liveKeySecret;
		}
		return testKeySecret;
	}

	/**
	 * Create a Razorpay Order on the backend.
	 * The amount is the authoritative server-side calculated amount.
	 * 
	 * @param amount Amount in paise (e.g., 50000 for ₹500)
	 * @param orderId Unique order ID from marketplace
	 * @param userEmail Customer email
	 * @return Razorpay Order object with order ID
	 * @throws Exception if order creation fails
	 */
	public Order createOrder(long amount, String orderId, String userEmail) throws Exception {
		try {
			String keyId = getActiveKeyId();
			String secret = getActiveSecret();

			if (keyId == null || keyId.isEmpty() || secret == null || secret.isEmpty()) {
				logger.error("Razorpay credentials not configured for mode: " + razorpayMode);
				throw new Exception("Razorpay credentials not configured");
			}

			RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount); // Amount in paise
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", orderId);
			orderRequest.put("notes", new JSONObject().put("order_id", orderId).put("customer_email", userEmail));

			Order order = razorpayClient.orders.create(orderRequest);

			logger.info("Razorpay order created: " + order.get("id") + " for marketplace order: " + orderId);
			return order;

		} catch (Exception e) {
			logger.error("Error creating Razorpay order for orderId: " + orderId + " - " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Verify Razorpay payment signature server-side.
	 * This is the critical security function - frontend cannot be trusted.
	 * 
	 * Signature formula: HMAC-SHA256(order_id + "|" + payment_id, secret)
	 * 
	 * @param orderId Razorpay Order ID
	 * @param paymentId Razorpay Payment ID
	 * @param signature Razorpay Signature (from frontend)
	 * @return true if signature is valid, false otherwise
	 */
	public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
		try {
			String secret = getActiveSecret();

			if (secret == null || secret.isEmpty()) {
				logger.error("Razorpay secret not configured for signature verification");
				return false;
			}

			String data = orderId + "|" + paymentId;
			String hash = hmacSHA256(data, secret);

			boolean isValid = hash.equals(signature);

			if (isValid) {
				logger.info("Payment signature verified for order: " + orderId);
			} else {
				logger.warn("Payment signature verification FAILED for order: " + orderId);
			}

			return isValid;

		} catch (Exception e) {
			logger.error("Error verifying payment signature: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Verify Razorpay webhook signature.
	 * Ensures webhook comes from Razorpay and hasn't been tampered with.
	 * 
	 * Signature formula: HMAC-SHA256(webhook_body, webhook_secret)
	 * 
	 * @param webhookBody Raw webhook body as string
	 * @param webhookSignature Signature header from Razorpay
	 * @return true if webhook signature is valid, false otherwise
	 */
	public boolean verifyWebhookSignature(String webhookBody, String webhookSignature) {
		try {
			if (webhookSecret == null || webhookSecret.isEmpty()) {
				logger.error("Webhook secret not configured");
				return false;
			}

			String hash = hmacSHA256(webhookBody, webhookSecret);
			boolean isValid = hash.equals(webhookSignature);

			if (isValid) {
				logger.info("Webhook signature verified");
			} else {
				logger.warn("Webhook signature verification FAILED");
			}

			return isValid;

		} catch (Exception e) {
			logger.error("Error verifying webhook signature: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Generate HMAC-SHA256 hash.
	 * 
	 * @param data Data to hash
	 * @param secret Secret key
	 * @return Hex encoded HMAC-SHA256 hash
	 * @throws Exception if hashing fails
	 */
	private String hmacSHA256(String data, String secret) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		mac.init(secretKeySpec);
		byte[] hash = mac.doFinal(data.getBytes());

		// Convert to hex
		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	/**
	 * Get currently active Razorpay mode (test or live).
	 * Useful for logging and debugging.
	 * 
	 * @return "test" or "live"
	 */
	public String getActiveMode() {
		return razorpayMode;
	}

	/**
	 * Check if Razorpay is properly configured.
	 * 
	 * @return true if both key and secret are configured
	 */
	public boolean isConfigured() {
		String keyId = getActiveKeyId();
		String secret = getActiveSecret();
		return keyId != null && !keyId.isEmpty() && secret != null && !secret.isEmpty();
	}
}
