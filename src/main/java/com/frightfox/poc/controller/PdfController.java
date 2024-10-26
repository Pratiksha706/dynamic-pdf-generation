package com.frightfox.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frightfox.poc.model.PdfRequest;
import com.frightfox.poc.service.PdfService;
import com.itextpdf.text.DocumentException;

import java.io.IOException;


@RestController
@RequestMapping("api/pdf")
public class PdfController {

	@Autowired
	private PdfService pdfService;

	@PostMapping("/generate")
	public ResponseEntity<String> generatePdf(@RequestBody PdfRequest pdfRequest) {
		String pdfFilePath = "";
				try {
					pdfFilePath = pdfService.generateInvoicePdf(pdfRequest);
				} catch (DocumentException e) {
					ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate PDF: " + e.getMessage());
				} catch (IOException e) {
					ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate PDF: " + e.getMessage());
				}
		
		
		return ResponseEntity.ok(pdfFilePath);
	}

	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadPdf(@PathVariable String fileName) {
		Resource resource =  pdfService.loadPdf(fileName);
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}