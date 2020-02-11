package com.expense.app.expense.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.expense.app.expense.dto.ExpenseReportDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ExpenseReportCreator {
	
	private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 18);
	
	private static final Font subheaderFont = new Font(Font.FontFamily.HELVETICA, 16);
	
	private static final Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);
	
	private static final Font contentFontItalic = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
	
	public byte[] generatePdfReport(ExpenseReportDto report) throws IOException, DocumentException {
		if (report.getExpenseCount().intValue() == 0) {
			return null;
		}
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			
			addMetadata(document);			
			document.open();			
			addReportHeader(document, report);
			addGeneralInfo(document, report);
			addExpensesByCategory(document, report);
			addExpenseDetails(document, report);			
			document.close();
			
			return out.toByteArray();
		}
	}
	
	private void addMetadata(Document document) {
		document.addCreationDate();
		document.addCreator("Expense Manager");
		document.addTitle("Expense Report");		
		document.setMargins(50f, 50f, 50f, 50f);
	}
	
	private void addReportHeader(Document document, ExpenseReportDto report) throws DocumentException {
		Paragraph reportHeader = new Paragraph("Expense report", headerFont);
		document.add(reportHeader);
		Paragraph reportDates = new Paragraph("From: " + report.getStartDate() + "  To: " + report.getEndDate(), contentFontItalic);
		document.add(reportDates);
	}
	
	private void addGeneralInfo(Document document, ExpenseReportDto report) throws DocumentException {
		Paragraph generalInfoHeader = new Paragraph("General information", subheaderFont);
		generalInfoHeader.setSpacingBefore(10f);
		generalInfoHeader.setSpacingAfter(5f);
		document.add(generalInfoHeader);
		
		PdfPTable generalInfo = new PdfPTable(new float[] {75f, 25f});
		generalInfo.setWidthPercentage(100f);
		
		String[] infoDescriptions = new String[] {
				"Number of expenses included in report",
				"Minimal expense",
				"Maximal expense",
				"Average expense",
				"Total sum of expenses"
		};
		
		String[] infoValues = new String[] {
				report.getExpenseCount().toString(),
				report.getMinExpense().toPlainString(),
				report.getMaxExpense().toPlainString(),
				report.getAvgExpense().toPlainString(),
				report.getSumExpense().toPlainString()
		};
		
		for (int i = 0; i < infoDescriptions.length; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(infoDescriptions[i], contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5f);
			cell.setPaddingLeft(0f);
			cell.setBorder(0);
			generalInfo.addCell(cell);
			
			cell = new PdfPCell(new Phrase(infoValues[i], contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPadding(5f);
			cell.setPaddingLeft(0f);
			cell.setBorder(0);
			generalInfo.addCell(cell);
		}
		
		document.add(generalInfo);
	}
	
	private void addExpensesByCategory(Document document, ExpenseReportDto report) throws DocumentException {
		Paragraph expensesByCategoryHeader = new Paragraph("Expenses by category", subheaderFont);
		expensesByCategoryHeader.setSpacingBefore(5f);
		expensesByCategoryHeader.setSpacingAfter(5f);
		document.add(expensesByCategoryHeader);
		
		PdfPTable expensesByCategory = new PdfPTable(new float[] {75f, 25f});
		expensesByCategory.setWidthPercentage(100f);
		
		report.getExpenseByCategory().entrySet().stream()
				.sorted((a, b) -> a.getKey().getName().compareTo(b.getKey().getName()))
				.forEach(entry -> {
			PdfPCell cell = new PdfPCell(new Phrase(entry.getKey().getName(), contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5f);
			cell.setPaddingLeft(0f);
			cell.setBorder(0);
			expensesByCategory.addCell(cell);
			
			cell = new PdfPCell(new Phrase(entry.getValue().toPlainString(), contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPadding(5f);
			cell.setPaddingLeft(0f);
			cell.setBorder(0);
			expensesByCategory.addCell(cell);
		});
		
		document.add(expensesByCategory);
	}
	
	private void addExpenseDetails(Document document, ExpenseReportDto report) throws DocumentException {
		Paragraph expenseDetailsHeader = new Paragraph("List of expenses included in report", subheaderFont);
		expenseDetailsHeader.setSpacingBefore(5f);
		expenseDetailsHeader.setSpacingAfter(10f);
		document.add(expenseDetailsHeader);
		
		PdfPTable expenseDetails = new PdfPTable(new float[] {33f, 34f, 33f});
		expenseDetails.setWidthPercentage(100f);		
		expenseDetails.setHeaderRows(1);
		
		String[] headers = new String[] { "Date", "Category", "Value" };
		for (int i = 0; i < headers.length; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(headers[i], contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5f);
			expenseDetails.addCell(cell);
		}		
		
		report.getExpenseList().forEach(expense -> {
			PdfPCell cell = new PdfPCell(new Phrase(expense.getDate().toString(), contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5f);
			expenseDetails.addCell(cell);
			
			cell = new PdfPCell(new Phrase(expense.getCategory().getName(), contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5f);
			expenseDetails.addCell(cell);
			
			cell = new PdfPCell(new Phrase(expense.getValue().toPlainString(), contentFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5f);
			expenseDetails.addCell(cell);
		});
		
		document.add(expenseDetails);
	}
}
