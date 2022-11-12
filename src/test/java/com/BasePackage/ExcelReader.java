package com.BasePackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	public static void main(String[] args) throws IOException {
		String testCaseName = "login";
		String SheetName = "testdata";
		String DataField = "userName";
		int tcRow = 0;
		int tcColoum = 0;
		int fColoum = 0;
		int fRow = 0;
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//com//resources//workbook.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int sheets = workbook.getNumberOfSheets();
		for (int i = 0; i < sheets; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase(SheetName)) 
			{
				XSSFSheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rows = sheet.iterator();
				Row firstRow = rows.next();
				Iterator<Cell> cells = firstRow.cellIterator();
				int coloumCount = 0;
				while (cells.hasNext()) {
					Cell value = cells.next();
					if (value.getStringCellValue().equalsIgnoreCase("TestCases")) {
						tcColoum = coloumCount;
					}
					coloumCount++;
				}
				int rowCount = 0;

				while (rows.hasNext()) {
					Row row = rows.next();
					if (row.getCell(tcColoum).getStringCellValue().equalsIgnoreCase(testCaseName)) {
						tcRow = rowCount;
						System.out.println("Final Row of test Case " + tcRow);
						System.out.println(row.getCell(tcColoum).getStringCellValue());
						// We got final row number -> finalRow
					}
					rowCount++;
				}
				Iterator<Row> rows2 = sheet.iterator();
				Row firstRow2 = rows.next();
				Iterator<Cell> cells2 = firstRow2.cellIterator();
				int coloumCount2 = 0;
				while (cells2.hasNext()) {
					Cell value2 = cells2.next();
					if (value2.getStringCellValue().equalsIgnoreCase(DataField)) {
						fColoum = coloumCount2;
						System.out.println("Test Cases Coloum Index Number " + fColoum);
						// We got final coloum number -> coloum
					}
					coloumCount2++;
				}
				int rowCount2 = 0;

				while (rows2.hasNext()) {
					Row row2 = rows2.next();
					System.out.println(row2.getCell(tcRow));
					
//					if (row2.getCell(fColoum).getStringCellValue().equalsIgnoreCase(DataField)) {
//						fRow = rowCount2;
//						System.out.println("Final Row of test Case " + fRow);
//						// We got final row number -> finalRow
//					}
					
					rowCount2++;
				}
			}

		}
	}
}