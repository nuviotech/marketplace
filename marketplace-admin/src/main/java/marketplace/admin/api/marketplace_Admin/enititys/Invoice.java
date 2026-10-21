package marketplace.admin.api.marketplace_Admin.enititys;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Invoice {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	int invoiceId;
	
	private String orderId;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invoiceDetails")
	//@JsonIgnore
	@JsonManagedReference
	List<OrderProducts> items=new ArrayList<>();
	
	@ManyToOne
	@JsonBackReference
	private OrderDetails invoiceOrderId;
	
	String taxType;

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<OrderProducts> getItems() {
		return items;
	}

	public void setItems(List<OrderProducts> items) {
		this.items = items;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public OrderDetails getInvoiceOrderId() {
		return invoiceOrderId;
	}

	public void setInvoiceOrderId(OrderDetails invoiceOrderId) {
		this.invoiceOrderId = invoiceOrderId;
	}

	
	@Override
	public String toString() {
		return "{\"invoiceId\":" + invoiceId + ",\" orderId\":\"" + orderId + "\", \"taxType\":\"" + taxType
				+ "\" ,\"items\":"+items+"}";
	}
	
	
}

