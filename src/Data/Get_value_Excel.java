package Data;

/*
 * PTT Stock - Excel to Text
 * 
 * version: May 21, 2018 00:54 AM
 * Last revision: June 06, 2018 06:58 PM
 * 
 */

/*
 * JAR
 * poi-3.17.jar
 * poi-ooxml-3.17.jar
 * 
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Get_value_Excel 
{
	private static final String FILE_NAME = "D:\\Phelps\\GitHub\\PTT_Stock\\source\\20180605.csv";	
	
	public Get_value_Excel() throws Exception
	{
//		 FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
//         Workbook workbook = new XSSFWorkbook(excelFile);
//         Sheet datatypeSheet = workbook.getSheetAt(0);
//         Iterator<Row> iterator = datatypeSheet.iterator();
         
		getStudentsListFromExcel();
	}
	
	private static List getStudentsListFromExcel() 
	{
        List studentList = new ArrayList();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FILE_NAME);

            // Using XSSF for xlsx format, for xls use HSSF
            Workbook workbook = new XSSFWorkbook(fis);

            int numberOfSheets = workbook.getNumberOfSheets();

            //looping over each workbook sheet
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator rowIterator = sheet.iterator();

                //iterating over each row
                while (rowIterator.hasNext()) {

                    //Student student = new Student();
                    Row row = (Row) rowIterator.next();
                    Iterator cellIterator = row.cellIterator();

                    //Iterating over each cell (column wise)  in a particular row.
                    while (cellIterator.hasNext()) 
                    {
                    	System.out.println(cellIterator.next());
                    }
                    
                }
            }

            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentList;
    }
	
	public static void main(String[] args) 
	{
		try {
			Get_value_Excel excel = new Get_value_Excel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
