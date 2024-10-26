package com.frightfox.poc.service;

//src/main/java/com/example/pdfgenerator/service/PdfService.java

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.frightfox.poc.model.PdfRequest;
import com.frightfox.poc.model.PdfRequest.Item;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfService {

 private static final String DIRECTORY = "pdfs/";


 public FileSystemResource loadPdf(String fileName) {
     File file = new File(DIRECTORY + fileName);
     return new FileSystemResource(file);
 }
 
 public String generateInvoicePdf(PdfRequest invoiceRequest) throws DocumentException, IOException {
	 String pdfFileName = DIRECTORY + "bill_" + System.currentTimeMillis() + ".pdf";
     Document document = new Document();
     PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
     document.open();
     
    
     Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
     Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
     
     // Seller and Buyer Information
     PdfPTable sellerBuyerTable = new PdfPTable(2);
     sellerBuyerTable.setWidthPercentage(100);
     sellerBuyerTable.setSpacingBefore(20f);
     sellerBuyerTable.setSpacingAfter(20f);

     // Seller Information
     PdfPCell sellerCell = new PdfPCell();
     sellerCell.setBorder(PdfPCell.NO_BORDER);
     sellerCell.addElement(new Paragraph("Seller:", boldFont));
     sellerCell.addElement(new Paragraph(invoiceRequest.getSeller(), normalFont));
     sellerCell.addElement(new Paragraph(invoiceRequest.getSellerAddress(), normalFont));
     sellerCell.addElement(new Paragraph("GSTIN: " + invoiceRequest.getSellerGstin(), normalFont));
     
     // Buyer Information
     PdfPCell buyerCell = new PdfPCell();
     buyerCell.setBorder(PdfPCell.NO_BORDER);
     buyerCell.addElement(new Paragraph("Buyer:", boldFont));
     buyerCell.addElement(new Paragraph(invoiceRequest.getBuyer(), normalFont));
     buyerCell.addElement(new Paragraph(invoiceRequest.getBuyerAddress(), normalFont));
     buyerCell.addElement(new Paragraph("GSTIN: " + invoiceRequest.getBuyerGstin(), normalFont));

     // Adding seller and buyer cells to the table
     sellerBuyerTable.addCell(sellerCell);
     sellerBuyerTable.addCell(buyerCell);

     document.add(sellerBuyerTable);

   
     document.add(Chunk.NEWLINE);

     PdfPTable itemTable = new PdfPTable(4);
     itemTable.setWidthPercentage(100);
     itemTable.setSpacingBefore(10f);
     itemTable.setSpacingAfter(10f);
     
     float[] columnWidths = {3f, 1f, 1f, 1f};
     itemTable.setWidths(columnWidths);
     
     itemTable.addCell(new PdfPCell(new Phrase("Item", boldFont)));
     itemTable.addCell(new PdfPCell(new Phrase("Quantity", boldFont)));
     itemTable.addCell(new PdfPCell(new Phrase("Rate", boldFont)));
     itemTable.addCell(new PdfPCell(new Phrase("Amount", boldFont)));
     
    
     for (Item item : invoiceRequest.getItems()) {
         itemTable.addCell(new PdfPCell(new Phrase(item.getName(), normalFont)));
         itemTable.addCell(new PdfPCell(new Phrase(item.getQuantity(), normalFont)));
         itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getRate()), normalFont)));
         itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getAmount()), normalFont)));
     }
     
     // Adding the item table to the document
     document.add(itemTable);

     // Close the document
     document.close();
      
     return pdfFileName;
 }
}
