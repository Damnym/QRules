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

@Component("/orders/view-order")
public class OrderPDFView extends AbstractPdfView{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@SuppressWarnings("unused")
	private static final String changeLine = "\r\n" ;
	private static final String orderClientHeader = "Client pre-order " ;
	private static final String orderDetailsHeader = "Pre-order details" ;
	private static final String mandatorySelectedHeader = "Mandatory and/or selected items" ;
	private static final String personalDataHeader = "Personal information" ;
	private static final String HealthConditionHeader = "Health condition" ;
	private static final String opDecision = "Surgery decision" ;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, 
																									HttpServletResponse response) throws Exception {
		
		Order order = (Order) model.get("order");
		PdfPCell cell = null; 
		PdfPTable dataTable = null ;
		Font font = new Font(); 
		font.setFamily(FontFactory.TIMES_ROMAN);
        
		document.setMargins(1f, 1f, 1f, 1f);
		document.setPageSize(PageSize.LETTER);
		document.setPageCount(1);
		
		document.addTitle("Pre-Order_" + order.getClient().getName() + "_" + order.getDate());
		document.add(mainTable(dataTable, cell, order, model));  
		document.add(orderTotal(order));
	}
	
	private PdfPTable personalData(PdfPTable dataTable, PdfPCell cell, Order order) {
		PdfPTable personalInfTable = new PdfPTable(2);
		personalInfTable.setSpacingAfter(10);
		
		//Encabezado 
		personalInfTable.addCell(genericHeaderCell(personalDataHeader, 2));
		
		//Contenido
		dataTable = new PdfPTable(1);   // celdas de la tabla de datos
		cell = cellData("DNI: " + order.getClient().getDni(), false);
		dataTable.addCell(cell);
		cell = cellData("Full name: " + order.getClient().getName() + " " + order.getClient().getSurname(), true);
		dataTable.addCell(cell);
		cell = cellData("Gender: " + order.getClient().getGender(), false);
		dataTable.addCell(cell);
		
		cell = new PdfPCell(dataTable);  // tabla interior
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorLeft(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		personalInfTable.addCell(cell);
		
		dataTable = new PdfPTable(1);   // celdas de la tabla interior
		cell = cellData("Age: " + order.getClient().getAge(), false);
		dataTable.addCell(cell);
		cell = cellData("Mobile phone number: " + order.getClient().getMobile(), false);
		dataTable.addCell(cell);
		cell = cellData("Email: " + order.getClient().getEmail(), false);
		dataTable.addCell(cell);
		cell = new PdfPCell(dataTable);  // tabla interior
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorRight(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		personalInfTable.addCell(cell);   // Tabla a incluir
		personalInfTable.setWidthPercentage(100f);
		
		return personalInfTable;
	}	
	
	private PdfPTable healtAndSurgeryData(PdfPTable dataTable, PdfPCell cell, Order order, Map<String, Object> model) {
		PdfPTable healtAndSurgeryInfTable = new PdfPTable(2);
		healtAndSurgeryInfTable.setSpacingAfter(10);
		
		//Contenido de tabla Condición de Salud
		PdfPTable healtInfTable = new PdfPTable(1);
		healtInfTable.addCell(genericHeaderCell(HealthConditionHeader, 0));
		// Celdas de la tabla de datos Condición de Salud
		dataTable = new PdfPTable(1);   
		cell = cellData("Weight: " + order.getClient().getWeight(), false);
		dataTable.addCell(cell);
		String height = order.getClient().getHeightFeetOrCentimeters().toString() ;
		if (order.getClient().getHeightInches()!=null)
			height += "." + order.getClient().getHeightInches() + "\"";
		cell = cellData("Height: " + height, false);
		dataTable.addCell(cell);
		cell = cellData("Hemoglobin: " + order.getClient().getHbg(), false);
		dataTable.addCell(cell);
		cell = cellData("Conditions: " + order.getClient().getConditionsName(), true);
		dataTable.addCell(cell);
		cell = new PdfPCell(dataTable);  // tabla de datos en una celda
		cell.setPadding(5f);
		healtInfTable.addCell(cell);	
		cell = new PdfPCell(healtInfTable);
		cell.setPaddingRight(5f);
		cell.setBorderColor(new Color(255, 255, 255));
		disableBorders(cell);
		healtAndSurgeryInfTable.addCell(cell);
		
		// Celdas de la tabla interior decisión de operación
		//Contenido de tabla Condición de Salud
		PdfPTable decisionInfTable = new PdfPTable(1);
		decisionInfTable.addCell(genericHeaderCell(opDecision, 0));
		dataTable = new PdfPTable(1);   
		cell = cellData("Doctor: " + model.get("doctor"), true); 
		dataTable.addCell(cell);
		cell = cellData("Principal procedure: " + model.get("p1"), false); 
		dataTable.addCell(cell);
		String combo = (model.get("combo")!=null)?(String)model.get("combo"):"Not Combo";
		cell = cellData("Combo: " + combo, false); 
		dataTable.addCell(cell);
		cell = cellData("Desired surgery date: " + order.getClient().getDate(), false);
		dataTable.addCell(cell);
		cell = new PdfPCell(dataTable);  // tabla interior
		cell.setPadding(5f);
		decisionInfTable.addCell(cell);
		cell = new PdfPCell(decisionInfTable);
		cell.setPaddingLeft(5f);
		disableBorders(cell);
		healtAndSurgeryInfTable.addCell(cell);
		
		healtAndSurgeryInfTable.setWidthPercentage(100f);
		
		return healtAndSurgeryInfTable;
	}	
	
	private PdfPTable orderDetailsData(PdfPTable dataTable, PdfPCell cell, Order order, Map<String, Object> model) {
		PdfPTable orderDetailsTable = new PdfPTable(2);
		orderDetailsTable.setSpacingAfter(10);
		
		//Encabezado 
		orderDetailsTable.addCell(genericHeaderCell(orderDetailsHeader, 2));
		//Contenido
		dataTable = new PdfPTable(1);   // tabla de datos generales detalles de la orden
		cell = cellData("Date: " + order.getDate(), false);
		dataTable.addCell(cell);
		cell = cellData(model.get("p1") + " price: " + order.getProcedureList().get(0).getSubTotal(), true);   
		dataTable.addCell(cell);
		String comboP = (model.get("combo")!=null)?model.get("combo") + " price: " + order.getProcedureList().get(1).getSubTotal():"Not Combo";
		cell = cellData(comboP, false); 
		dataTable.addCell(cell);
		cell = new PdfPCell(dataTable);  // Haciendo celda con la tabla de los datos
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
		cell.setBorderColorLeft(new Color(0, 0, 0));
        cell.setBorderColorBottom(new Color(0, 0, 0));
        orderDetailsTable.addCell(cell);    // adicionando la tabla 
        
        dataTable = new PdfPTable(1);   // tabla de items obligatorios
        cell = genericHeaderCell("Included", 1);
		dataTable.addCell(cell);
		String included = model.get("included").toString();
		included = included.substring(1, included.length()-1);
		cell = cellData(included, false);
		dataTable.addCell(cell);
		
		cell = new PdfPCell(dataTable); 
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColor(new Color(255, 255, 255));
        cell.setBorderColorRight(new Color(0, 0, 0));
        cell.setBorderColorBottom(new Color(0, 0, 0));
        orderDetailsTable.addCell(cell);   // Tabla a incluir	
        
        orderDetailsTable.setWidthPercentage(100f);
        
        return orderDetailsTable;
	}
	
	private PdfPTable mandatoryAndSelectedItems(PdfPTable dataTable, PdfPCell cell, Order order) {
        PdfPTable mandatoryAndSelectedItemsTable = new PdfPTable(1);
        mandatoryAndSelectedItemsTable.setSpacingAfter(10);
        mandatoryAndSelectedItemsTable.addCell(genericHeaderCell(mandatorySelectedHeader, 0));
        dataTable = new PdfPTable(4);   
        dataTable.addCell(blackAndCenterCell("Item"));
        dataTable.addCell(blackAndCenterCell("Price"));
        dataTable.addCell( blackAndCenterCell("Amount"));
        dataTable.addCell(blackAndCenterCell("Subtotal"));
        
        Boolean flag = true ;
        for(OrderItem item: order.getItemList()) {
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
        cell.setPadding(5f);
        
        mandatoryAndSelectedItemsTable.addCell(cell); // adicionando la celda (tabla) a la tabla intermedia
		mandatoryAndSelectedItemsTable.setWidthPercentage(100f);
		
        return mandatoryAndSelectedItemsTable;
	}
	
	private PdfPTable orderObservation(PdfPTable dataTable, PdfPCell cell, Order order) {
		PdfPTable observation = new PdfPTable(1);
		observation.setSpacingAfter(10);
		//Encabezado 
		observation.addCell(genericHeaderCell("Observation", 0));
		//Contenido
		dataTable = new PdfPTable(1);   // celdas de la tabla interior
		cell = cellData(order.getObservation(), false);
		cell.setBorderColor(new Color(255, 193, 7));
		dataTable.addCell(cell);
		cell = new PdfPCell(dataTable);  // Crear celda con la tabla de datos para insertar en la q la contiene
		cell.setPadding(10f);
		cell.setUseVariableBorders(true);
		cell.setBorderColorLeft(new Color(0, 0, 0));
		cell.setBorderColorBottom(new Color(0, 0, 0));
		observation.addCell(cell);

		observation.setWidthPercentage(100f);
		
		return observation;
	}	
	
	private PdfPTable mainTable(PdfPTable dataTable, PdfPCell cell, Order order, Map<String, Object> model) {
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.setSpacingAfter(10);
		//Encabezado 
		headerTable.addCell(genericHeaderCell(orderClientHeader + order.getClient().getName(), 0));
		headerTable.setWidthPercentage(100f);
		
		cell = new PdfPCell(personalData(dataTable, cell, order));
		cell.setPadding(5f);
		cell.disableBorderSide(3);
		headerTable.addCell(cell);
		
		cell = new PdfPCell(healtAndSurgeryData(dataTable, cell, order, model));
		cell.setPadding(5f);
		cell.disableBorderSide(1);
		cell.disableBorderSide(3);
		headerTable.addCell(cell);
		
		cell = new PdfPCell(orderDetailsData(dataTable, cell, order, model));
		cell.setPadding(5f);
		cell.disableBorderSide(1);
		cell.disableBorderSide(3);
		headerTable.addCell(cell);
		
		cell = new PdfPCell(mandatoryAndSelectedItems(dataTable, cell, order));
		cell.setPadding(5f);
		cell.disableBorderSide(1);
		cell.disableBorderSide(3);
		headerTable.addCell(cell);
		
		cell = new PdfPCell(orderObservation(dataTable, cell, order));
		cell.setPadding(5f);
		cell.disableBorderSide(1);
		headerTable.addCell(cell);
		
		return headerTable;
	}
	
	private Paragraph orderTotal(Order order) {
		Font f = new Font();
		f.setFamily(FontFactory.TIMES_ROMAN);
		f.setSize(10f);
		f.setStyle("bold, underline");
		
		Paragraph total = new Paragraph();
		total.setFont(f);
		total.add("Total pre-order: $" + order.getTotalOrder());
		total.setAlignment(Paragraph.ALIGN_RIGHT);

		return total;
	}
	
	private PdfPCell cellData(String data, Boolean isShadow) {
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
	
	private PdfPCell genericHeaderCell(String  data, int colSpan) {
		PdfPCell cell = new PdfPCell(new Phrase(data, headerFont()));
		cell.setBackgroundColor(new Color(15, 54, 120));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setPadding(5f);
		if (colSpan>0)
			cell.setColspan(colSpan); // Merge la celda
		return cell;
	}
	
	private Font headerFont() {
		Font hFont = new Font();
		hFont.setFamily(FontFactory.TIMES_ROMAN);
		hFont.setStyle(1);
		hFont.setSize(10f);
		hFont.setColor(255, 255, 255);
		return hFont;
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
	
}