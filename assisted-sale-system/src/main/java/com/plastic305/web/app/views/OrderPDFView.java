package com.plastic305.web.app.views;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.OrderItem;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.SuperOrder;

@Component("/orders/view-order")
public class OrderPDFView extends AbstractPdfView
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private static final String changeLine = "\r\n" ;
	private static final String orderClientHeader = "Client pre-order " ;
	private static final String orderDetailsHeader = "Pre-order details" ;
	private static final String mandatorySelectedHeader = "Mandatory and/or selected items" ;
	private static final String personalDataHeader = "Personal information" ;
	private static final String HealthConditionHeader = "Health condition" ;
	private static final String subOrderHeader = "Suborder " ;
	private static final String sOrderHeader = "Order Summary" ;
	
	private Font headerFont(int r, int g, int b) 
	{
		Font hFont = new Font();
		hFont.setFamily(FontFactory.TIMES_ROMAN);
		hFont.setStyle(1);
		hFont.setSize(10f);
		hFont.setColor(r, g, b);
		return hFont;
	}
	private Font headerFont() 
	{
		Font hFont = new Font();
		hFont.setFamily(FontFactory.TIMES_ROMAN);
		hFont.setStyle(1);
		hFont.setSize(10f);
		hFont.setColor(255, 255, 255);
		return hFont;
	}
	private PdfPCell mainHeaderCell(String  data, int colSpan) 
	{
		PdfPCell cell = new PdfPCell(new Phrase(data, headerFont(16, 31, 58)));  
		cell.setBackgroundColor(new Color(255, 255, 255));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setPadding(5f);
		if (colSpan>0)
			cell.setColspan(colSpan); // Merge la celda
		return cell;
	}
	private PdfPCell genericHeaderCell(String  data, int colSpan) 
	{
		PdfPCell cell = new PdfPCell(new Phrase(data, headerFont()));
		cell.setBackgroundColor(new Color(16, 31, 58));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setPadding(5f);
		if (colSpan>0)
			cell.setColspan(colSpan); // Merge la celda
		return cell;
	}
	private PdfPCell cellData(String data, Boolean isShadow) 
	{
		Font cellFont = new Font();
		cellFont.setFamily(FontFactory.TIMES_ROMAN);
		cellFont.setSize(10f);
		if (isShadow)
			cellFont.setStyle(1);
		
	    PdfPCell cell = new PdfPCell(new Phrase(data, cellFont));
	    cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(223, 223, 223));
		cell.setPadding(5f);
		return cell;
	}
	private void disableBorders(PdfPCell cell) {
		cell.disableBorderSide(1);
		cell.disableBorderSide(2);
		cell.disableBorderSide(3);
		cell.disableBorderSide(4);
	}
	private PdfPCell blackAndCenterCell(String phrase) {
		PdfPCell cell = new PdfPCell(new Phrase(phrase, headerFont()));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setBackgroundColor(new Color (52,58,64));
		cell.setPadding(5f);
		return cell;
	}
	private PdfPCell boldAndCenterCell(String phrase) 
	{
		Font f = new Font(); 
		f.setFamily(FontFactory.TIMES_ROMAN);
		PdfPCell cell = new PdfPCell(new Phrase(phrase, f)); 
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setBackgroundColor(new Color (255,255,255));
		cell.setPadding(5f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		return cell;
	}
	private PdfPCell cellData(String data, Boolean isBold, Boolean isShadow) {
		Font cellFont = new Font();
		cellFont.setFamily(FontFactory.TIMES_ROMAN);
		cellFont.setSize(10f);
		if (isBold) cellFont.setStyle(1);
		
	    PdfPCell cell = new PdfPCell(new Phrase(data, cellFont));
	    cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(223, 223, 223));
		if (isShadow) cell.setBackgroundColor(new Color(242, 242, 242)); 
		cell.setPadding(5f);
		return cell;
	}
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, 
									HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		SuperOrder sOrder = (SuperOrder) model.get("superOrder");
//		Order order = (Order) model.get("order");
		PdfPCell cell = null; 
		PdfPTable dataTable = null ;
		Font font = new Font(); 
		font.setFamily(FontFactory.TIMES_ROMAN);
        
//		document.setPageCount(1);
		
		document.addTitle("Pre-Order_" + sOrder.getClient().getName() + "_" + sOrder.getDate());
		
		document.add(mainTable(dataTable, cell, sOrder, model));  

		document.setMargins(1f, 1f, 1f, 1f);
		document.setPageSize(PageSize.LETTER);
//		document.add(orderTotal(order));
	}
	
	private PdfPTable mainTable(PdfPTable dataTable, PdfPCell cell, SuperOrder sOrder, Map<String, Object> model) 
	{
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.setSpacingAfter(10);
		//Encabezado 
		headerTable.addCell(mainHeaderCell(orderClientHeader + sOrder.getClient().getName(), 0));
		headerTable.setWidthPercentage(100f);
		
		cell = new PdfPCell(personalDataNHealthCond(dataTable, cell, sOrder));
		cell.setPadding(5f);
		cell.disableBorderSide(3);
		headerTable.addCell(cell); // ok
		
		cell = new PdfPCell(orderSummaryData(dataTable, cell, sOrder, model));
		cell.setPadding(5f);
//		cell.disableBorderSide(1);
		cell.disableBorderSide(3);
		headerTable.addCell(cell); // ok
		
		int orderNumber = 1 ;
		for (Order order: sOrder.getOrderList())
		{
			cell = new PdfPCell(orderDetailsDataSx(dataTable, cell, order, model, orderNumber++));
			cell.setPadding(5f);
			cell.disableBorderSide(3);
			headerTable.addCell(cell);

			cell = new PdfPCell(orderDetailsDataItem(dataTable, cell, order, model));
			cell.setPadding(5f);
			cell.disableBorderSide(3);
			headerTable.addCell(cell);
		}
//		
		
//		cell = new PdfPCell(mandatoryAndSelectedItems(dataTable, cell, sOrder));
//		cell.setPadding(5f);
//		cell.disableBorderSide(1);
//		cell.disableBorderSide(3);
//		headerTable.addCell(cell);
//		
//		cell = new PdfPCell(orderObservation(dataTable, cell, sOrder));
//		cell.setPadding(5f);
//		cell.disableBorderSide(1);
//		headerTable.addCell(cell);
		
		return headerTable;
	}
	
	private PdfPTable personalDataNHealthCond(PdfPTable dataTable, PdfPCell cell, SuperOrder order) 
	{
		PdfPTable personalInfTable = new PdfPTable(2);
		personalInfTable.setSpacingAfter(10);
		
		//Encabezado 
		personalInfTable.addCell(genericHeaderCell(personalDataHeader + " & " + HealthConditionHeader, 2));
		
		//Contenido
		dataTable = new PdfPTable(1);   // celdas de la tabla de datos
		
		cell = cellData("DNI: " + order.getClient().getDni(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Full name: " + order.getClient().getName() + " " + order.getClient().getSurname(), true);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Gender: " + order.getClient().getGender(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Age: " + order.getClient().getAge(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Mobile phone number: " + order.getClient().getMobile(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Email: " + order.getClient().getEmail(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = new PdfPCell(dataTable);  // incluir en tabla interior
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorLeft(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		personalInfTable.addCell(cell);
		
		dataTable = new PdfPTable(1);   // celdas de la tabla interior
		
		cell = cellData("Weight: " + order.getClient().getWeight(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		String height = order.getClient().getHeightFeetOrCentimeters().toString() ;
		if (order.getClient().getHeightInches()!=null)
			height += "." + order.getClient().getHeightInches() + "\"";
		cell = cellData("Height: " + height, false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Hemoglobin: " + order.getClient().getHbg(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Conditions: " + order.getClient().getConditionsName(), true);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = new PdfPCell(dataTable);  //incluir en tabla interior
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorRight(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		personalInfTable.addCell(cell);   // Tabla a incluir
		personalInfTable.setWidthPercentage(100f);
		
		return personalInfTable;
	}	

	private PdfPTable orderSummaryData(PdfPTable dataTable, PdfPCell cell, SuperOrder order, Map<String, Object> model)
	{
		PdfPTable orderSummaryTable = new PdfPTable(1);
		orderSummaryTable.setSpacingAfter(10);
		
		//Encabezado 
		orderSummaryTable.addCell(genericHeaderCell(sOrderHeader, 1));
		
		//Contenido
		dataTable = new PdfPTable(1);   // celdas de la tabla de datos
		
		cell = cellData("Date: " + order.getDate(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("SubOrder amount: " + order.getOrderList().size(), true);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = cellData("Overall observation: " + order.getObservation(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		String included = model.get("included").toString();
		included = included.substring(1, included.length()-1);
		included = "Included: " + included ;
		cell = cellData(included, false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		
		cell = new PdfPCell(dataTable);  // incluir en tabla interior
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorLeft(new Color(0, 0, 0));
		cell.setBorderColorRight(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		orderSummaryTable.addCell(cell);
		
		return orderSummaryTable;
	}
	
	
	private PdfPTable orderDetailsDataSx(PdfPTable dataTable, PdfPCell cell, Order order, Map<String, Object> model, int orderNumber)
	{
		PdfPTable orderTable=new PdfPTable(1);

		//Encabezado 
		orderTable.addCell(genericHeaderCell(subOrderHeader + orderNumber, 1));
		
		//Contenido
		dataTable = new PdfPTable(1);   // celdas de la tabla de datos de las cirugías
		
		cell = cellData("Suborder: #" + orderNumber++, false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		cell = cellData("Estimated surgery date: " + order.getDateSurgery(), false);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		cell = cellData("Surgeon: " + order.getDoctorName(), true);
		cell.setBorderColor(new Color(255, 255, 255));
		dataTable.addCell(cell);
		String str;
		for (OrderProcedure procedure: order.getProcedureList())
		{
			str = procedure.getProcedure().getName() ;
			str += !procedure.getIncludedInPrice()? " $" + procedure.getSubTotal(): "Free or included in price";
			// Aquí falta   lo de las ofertas
			cell = cellData(str, false);
			cell.setBorderColor(new Color(255, 255, 255));
			dataTable.addCell(cell);
		}
		
		cell = new PdfPCell(dataTable);  // incluir en tabla interior
		cell.setPaddingLeft(10f);
		cell.setPaddingRight(10f);
		cell.setPaddingTop(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorLeft(new Color(0, 0, 0));
		cell.setBorderColorRight(new Color(0, 0, 0));
		
		orderTable.addCell(cell);
//		orderTable.setWidthPercentage(100f);	
		
		return orderTable;
	}
	
	private PdfPTable orderDetailsDataItem(PdfPTable dataTable, PdfPCell cell, Order order, Map<String, Object> model)
	{
		PdfPTable orderTable=new PdfPTable(1);
		
//		Ahora los items
		dataTable = new PdfPTable(4);
		dataTable.setSpacingAfter(10);
		
		cell = boldAndCenterCell("Items");
		cell.setColspan(4) ;
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		
		dataTable.addCell(cell);
		dataTable.addCell(blackAndCenterCell("Item"));
		dataTable.addCell(blackAndCenterCell("Price"));
		dataTable.addCell( blackAndCenterCell("Amount"));
		dataTable.addCell(blackAndCenterCell("Subtotal"));
		
		Boolean flag = true ;
		for(OrderItem item: order.getItemList()) 
		{
			cell = cellData(item.getProduct().getName(), false, flag);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			dataTable.addCell(cell);
			
			cell = cellData(""+item.getProduct().getPrice(), false, flag);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			dataTable.addCell(cell);
			
			cell = cellData(""+item.getAmount(), false, flag);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			dataTable.addCell(cell);
			
			cell = cellData(""+item.getSubTotal(), true, flag);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			dataTable.addCell(cell);
			
			flag =!flag;
		}
		
		cell = new PdfPCell(dataTable);  // Hacer celda con la tabla de datos
		cell.setPaddingLeft(10f);
		cell.setPaddingRight(10f);
		cell.setPaddingBottom(10f);
		
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorLeft(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		cell.setBorderColorRight(new Color(0, 0, 0));
		
		orderTable.addCell(cell);
		orderTable.setWidthPercentage(100f);	
		
		return orderTable;
	}
	
	
	
}