package com.marketplace.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.Invoice;
import com.marketplace.api.entitys.OrderDetails;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{
	public List<Invoice> findByInvoiceOrderId(OrderDetails oderDetails);
}
