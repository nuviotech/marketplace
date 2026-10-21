package marketplace.admin.api.marketplace_Admin.Implementation;

import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.Refund;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.enititys.ReturnDetails;
import marketplace.admin.api.marketplace_Admin.repository.RefundDetailsRepository;
import marketplace.admin.api.marketplace_Admin.repository.UserRepository;
import marketplace.admin.api.marketplace_Admin.service.OrderDetailsService;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RefundDetailsRepository returnDetailsRepository;
	
	
	/**
	 * Refund the order by marketplace admin or seller
	 * refund payment by razorpay
	 * 
	 * */
	public String refundThePlacedOrder(int returnDetailsId, MarketplaceAdmin admin) {
		
		try {
			String RZ_KEY = admin.getRz_Key();
			String RZ_TEST_KEY = admin.getRz_Secret(); 
			System.out.println("rzkey : "+RZ_KEY+" test : "+RZ_TEST_KEY);
			
			ReturnDetails rds=returnDetailsRepository.findById(returnDetailsId).get();
			RazorpayClient razorpay = new RazorpayClient(RZ_KEY, RZ_TEST_KEY);
			System.out.println("Payment id : "+rds.getPaymentId());
			String paymentId = rds.getPaymentId();
			JSONObject refundRequest = new JSONObject();
			refundRequest.put("amount",rds.getAmt());
			refundRequest.put("speed","normal");
			JSONObject notes = new JSONObject();
			//notes.put("notes_key_1","testing...");
			//notes.put("notes_key_2","Tea,.");
			refundRequest.put("notes",notes);
			//update recipt number here
			refundRequest.put("receipt","Receip no "+new Random().nextInt(10000));
			              
			Refund r =razorpay.payments.refund(paymentId,refundRequest);
			rds.setRefundRZstatus(""+r);
			rds.setStatus("refunded");
			returnDetailsRepository.save(rds);
			return "0";
		}catch(Exception e) {
			System.out.println("ERROR(refund order service ) : "+e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
		
	}	
	
	
}
