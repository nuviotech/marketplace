package marketplace.admin.api.marketplace_Admin.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import marketplace.admin.api.marketplace_Admin.enititys.Settlement;

@Service
public class ExcelService {
	
	//settlement report excel file generation
	public Workbook generateSettlementExcelFile(List<Settlement> settlements) throws IOException {
		XSSFWorkbook workbook=new XSSFWorkbook();
		POIXMLProperties xmlProps = workbook.getProperties();    
		POIXMLProperties.CoreProperties coreProps =  xmlProps.getCoreProperties();
		coreProps.setCreator("MANAGEECOM");
		try {
			String HEADERS[]= {
				"ID",
				"SELLER_EMAIL",
				"PRODUCT_ID",
				"DATE",
				"QUANTITY",
				"ITEM_AMOUNT",
				"MARKETPLACE_CHARGE",
				"SERVICE_CHARGE",
				"FINAL_AMOUNT",
			};
			
			Sheet sheet = workbook.createSheet("Settlement_report");
			
			Row row = sheet.createRow(0);
			for(int i=0;i<HEADERS.length;i++) {
				Cell cell=row.createCell(i);
				cell.setCellValue(HEADERS[i]);
			}
			
			int rowIndex=1;
			for(Settlement settlement:settlements) {
				Row dataRow = sheet.createRow(rowIndex);
				dataRow.createCell(0).setCellValue(settlement.getSettlementId());
				dataRow.createCell(1).setCellValue(settlement.getSellerId());
				dataRow.createCell(2).setCellValue(settlement.getProductId());
				dataRow.createCell(3).setCellValue(settlement.getCreatedDtTime());
				dataRow.createCell(4).setCellValue(settlement.getItemQuantity());
				dataRow.createCell(5).setCellValue(settlement.getItemPrice());
				dataRow.createCell(6).setCellValue(settlement.getMarketplaceCharge());
				dataRow.createCell(7).setCellValue(settlement.getServiceCharge());
				dataRow.createCell(8).setCellValue(settlement.getFinalAmt());
				rowIndex++;
			}
		 //workbook.write(out);
		// return new ByteArrayInputStream(out.toByteArray());
			return workbook;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
