package com.marketplace.api.serviceClasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lowagie.text.Anchor;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.marketplace.api.entitys.Invoice;
import com.marketplace.api.entitys.OrderDetails;
import com.marketplace.api.entitys.OrderProducts;

@Service
public class PdfService {
	Logger logger = LoggerFactory.getLogger(PdfService.class);
	public ByteArrayInputStream createPdf() {
		logger.info("pdf created...");
		try {
		String title = "Tax Invoice";
		String para="this para";
		
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		Document document = new Document();
		
		PdfWriter.getInstance(document, out);
		document.open();
		
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);
		Font paraFont = FontFactory.getFont(FontFactory.HELVETICA,20);
		Paragraph titlePara = new Paragraph(title,titleFont);
		titlePara.setAlignment(Element.ALIGN_CENTER);
		document.add(titlePara);
		
		Paragraph para2 = new Paragraph(para,paraFont);
		para2.add("rest");
		titlePara.setAlignment(Element.ALIGN_LEFT);
		document.add(para2);
		document.close();
		return new ByteArrayInputStream(out.toByteArray());
		

		}catch(Exception e) {
			System.out.println("ERROR PDF : "+e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public ByteArrayInputStream createTaxInvoicePdf(Invoice invoice) throws Exception {
		 OrderDetails orderDetails=invoice.getInvoiceOrderId();
		 System.out.println("My first table");
	        // step 1: creation of a document-object
	        Document document = new Document(PageSize.A4);
	        try {
	            // step 2:
	            // we create a writer that listens to the document
	            // and directs a PDF-stream to a file
	        	ByteArrayOutputStream out =new ByteArrayOutputStream(); 
	            PdfWriter.getInstance(document,
	                    out);
	            // step 3: we open the document
	            document.open();
	            // step 4: we create a table and add it to the document
	    		Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
	    		Font orderIdFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,10);

	    		Font normalFont = FontFactory.getFont(FontFactory.HELVETICA,9);
	    		Font normalFont2 = FontFactory.getFont(FontFactory.HELVETICA,11);
	    		Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD,25);

	    		Paragraph title = new Paragraph("Tax Invoice\n\n",titleFont);
	    		title.setAlignment(Element.ALIGN_CENTER);

	    		document.add(title);
	    		 //tabel
	            PdfPTable table2 = new PdfPTable(12); // col, row
	            
	            Paragraph orderDetailParagraph = new Paragraph();
	            Paragraph orderID=new Paragraph("\n\nOrder ID\n"+orderDetails.getOrderId()+"\n",orderIdFont);
	            
	            Paragraph orderDt=new Paragraph("Order Date : ",boldFont);
	            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
	            String date= simpleDateFormat.format(orderDetails.getOrderDate());
	            
	            Paragraph datepara=new Paragraph(date,normalFont);
	            orderDt.add(datepara);
	            
	            Paragraph orderIn=new Paragraph("Invoice Date :",boldFont);
	            Paragraph indate=new Paragraph(date+"\n\n\n",normalFont);
	            orderIn.add(indate);
	            
	            orderDetailParagraph.add(orderID);
	            orderDetailParagraph.add(orderDt);
	            orderDetailParagraph.add(orderIn);
	          
	            PdfPCell orderDetailsCell = new PdfPCell(orderDetailParagraph);
	            orderDetailsCell.setColspan(4);
	            orderDetailsCell.disableBorderSide(Rectangle.TOP);
	            orderDetailsCell.disableBorderSide(Rectangle.RIGHT);
	            orderDetailsCell.disableBorderSide(Rectangle.LEFT);
	            table2.addCell(orderDetailsCell);
	            
	            
	            //_______
	            Paragraph billDetailParagraph = new Paragraph();
	            Paragraph billtitle=new Paragraph("\n\nBill To",boldFont);
	            Paragraph name=new Paragraph(orderDetails.getFname()+" "+orderDetails.getLname(),boldFont);
	            Paragraph address=new Paragraph(orderDetails.getShippingAddressPrimary(),normalFont2);
	            Paragraph address2=new Paragraph(orderDetails.getCity()+", "+orderDetails.getPostalCode()+" "+ orderDetails.getState() +"\n",normalFont2);
	            Paragraph phone=new Paragraph("Phone : XXXXXXXXXX",normalFont2);
	            billDetailParagraph.add(billtitle);
	            billDetailParagraph.add(name);
	            billDetailParagraph.add(address);
	            billDetailParagraph.add(address2);
	            billDetailParagraph.add(phone);
	            
	            PdfPCell BillToDetailsCell = new PdfPCell(billDetailParagraph);
	            BillToDetailsCell.setColspan(4);
	            BillToDetailsCell.disableBorderSide(Rectangle.TOP);
	            BillToDetailsCell.disableBorderSide(Rectangle.RIGHT);
	            BillToDetailsCell.disableBorderSide(Rectangle.LEFT);
	           
	            
	            table2.addCell(BillToDetailsCell);
	            
	            
	            
	            Paragraph shipDetailParagraph = new Paragraph();
	            Paragraph title1=new Paragraph("\n\nShip To",boldFont);
	            Paragraph name1=new Paragraph( orderDetails.getFname()+" "+orderDetails.getLname(),boldFont);
	            Paragraph address1=new Paragraph(orderDetails.getShippingAddressPrimary(),normalFont2);
	            Paragraph address12=new Paragraph(orderDetails.getCity()+", "+orderDetails.getPostalCode()+" "+ orderDetails.getState() +"\n",normalFont2);
	            Paragraph phone1=new Paragraph("Phone : XXXXXXXXXX",normalFont2);
	            shipDetailParagraph.add(title1);
	            shipDetailParagraph.add(name1);
	            shipDetailParagraph.add(address1);
	            shipDetailParagraph.add(address12);
	            shipDetailParagraph.add(phone1);
	            
	            PdfPCell ShipTODetailsCell = new PdfPCell(shipDetailParagraph);
	            ShipTODetailsCell.setColspan(4);
	            ShipTODetailsCell.disableBorderSide(0);
	            ShipTODetailsCell.disableBorderSide(Rectangle.RIGHT);
	            ShipTODetailsCell.disableBorderSide(Rectangle.LEFT);
	            ShipTODetailsCell.disableBorderSide(3);
	            
	            table2.addCell(ShipTODetailsCell);
	            
	            
	            document.add(table2);
	            
	            
	            
	            //tabel
	            PdfPTable table = new PdfPTable(10); // col, row
	  
	            PdfPCell desc = new PdfPCell(new Paragraph("Description"));
	            desc.setColspan(4);
	            table.addCell(desc);
	            table.addCell("Unit Price");
	            table.addCell("Qty");
	            table.addCell("Tax Rate");
	            table.addCell("Tax Type");
	            table.addCell("Tax Amt");
	            table.addCell("Total Amt");
	            
	            int tot=0;
	            for(OrderProducts product :invoice.getItems()) {
		            PdfPCell cell = new PdfPCell(new Paragraph(product.getProduct_name()));
		           
		            cell.setColspan(4);
		            table.addCell(cell);
		            table.addCell((product.getSole_price()-product.getTaxAmt())+"");
		            table.addCell(product.getQuantity()+"");
		            if(invoice.getTaxType().equals("CGST,SGST")) {
		            	int taxrate=(Integer.parseInt(product.getTaxRate())/2);
		            	float totTax=((product.getTaxAmt()*product.getQuantity())/2);
		            	table.addCell(taxrate+"%\n"+taxrate+"%");
		            	table.addCell("CGST\nSGST");
		            	table.addCell(totTax +"\n"+totTax+"\n\n");
		            	
		            }else {
		            	table.addCell(product.getTaxRate());
		            	table.addCell("IGST");
		            	table.addCell(product.getTaxAmt()*product.getQuantity()+"");
		            }
		            
		            table.addCell(product.getSole_price()+"");
		            tot+=product.getSole_price()*product.getQuantity();
	            }
	            
	            PdfPCell cell2 = new PdfPCell(new Paragraph("Total Invoice Price : "));
	           
	            cell2.setColspan(9);
	            table.addCell(cell2);
	            table.addCell(tot+"");

	            
	            document.add(table);
//	            document.add(new Paragraph("converted to PdfPTable:"));
//	            table.setConvert2pdfptable(true);
//	            document.add(table);
	            document.close();
	    		return new ByteArrayInputStream(out.toByteArray());

	        } catch (Exception de) {
	            System.err.println(de.getMessage());
	            return null;
	        }
	        // step 5: we close the document
    }
}
