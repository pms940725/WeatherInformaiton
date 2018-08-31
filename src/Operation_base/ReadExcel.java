package Operation_base;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	public static String readCell( XSSFCell cell) {
		String value="";
		 switch (cell.getCellTypeEnum()){
         case FORMULA:
             value=cell.getCellFormula();
             break;
         case NUMERIC:
             value=(int)cell.getNumericCellValue()+"";
           
             break;
         case STRING:
         	value=cell.getStringCellValue();
         	   			                    
             break;
         case BLANK:
             value=cell.getBooleanCellValue()+"";
           
             break;
         case ERROR:
             value=cell.getErrorCellValue()+"";
             break;
		default:
			break;
         }
		 
		 return value;
	}
	
	
	public static int checkCity( XSSFRow row, int city_number, String city1, String city2, String city3) {
		
	
		
		if(city_number == 1) {
			XSSFCell city1_cell_r = row.getCell(0);
			if(city1_cell_r == null)	
				return 0;
			
			String city1_cell = readCell(city1_cell_r);
			if(!city1_cell.equals(city1))	
				return 0;
			//return0�� �����Ѵ�.
			  
    	}
    	else if(city_number == 2) {
    		XSSFCell city2_cell_r = row.getCell(1);
    		
    		if(city2_cell_r == null)	
				return 0;
    		
    		XSSFCell city1_cell_r = row.getCell(0);
    		
    		String city1_cell = readCell(city1_cell_r);
    		String city2_cell = readCell(city2_cell_r);
    		if(!city2_cell.equals(city2) || !city1_cell.equals(city1))
    			return 0;
 
    	}
    	else if(city_number == 3) {
    		XSSFCell city3_cell_r = row.getCell(2);
    		if(city3_cell_r == null)	
				return 0;
    		
    		XSSFCell city1_cell_r = row.getCell(0);
    		XSSFCell city2_cell_r = row.getCell(1);
    		
    		String city1_cell = readCell(city1_cell_r);
    		String city2_cell = readCell(city2_cell_r);
    		String city3_cell = readCell(city3_cell_r);
    		if(!city3_cell.equals(city3)||!city2_cell.equals(city2) || !city1_cell.equals(city1))
    			return 0;
    	}
		
		return 1;
	}
	
	
	public static String[] findnxny(String city1, String city2, String city3, String filePath) {
		
		String[] nxny = new String[2];
		
		try {
			int city_number = 0;
			
			
			if(city2 == null || city2.equals("")) {
				city_number = 1;
			}
			else if(city3 == null || city3.equals("")){
				city_number = 2;
			}
			else
				city_number=3;
			
			//System.out.println("city1 = "+city1);
			//System.out.println("city2 = "+city2);
			//System.out.println("city3 = "+city3);
			//System.out.println("filePath = "+filePath);
			//System.out.println("city_number = " + city_number);
			
			FileInputStream fis=new FileInputStream(filePath);
			XSSFWorkbook workbook=new XSSFWorkbook(fis);
			
			int rowindex=0;
			
			
			
			XSSFSheet sheet=workbook.getSheetAt(0);
			
			
			int rows=sheet.getPhysicalNumberOfRows();
			
			for(rowindex=1;rowindex<rows;rowindex++){
			
			    XSSFRow row=sheet.getRow(rowindex);
			    if(row !=null){
			    	
			    	if(checkCity(row, city_number, city1, city2, city3) == 0) {
			    		continue;
			    	}
			    	else {
			    		XSSFCell nx_cell_r = row.getCell(3);
			    		XSSFCell ny_cell_r = row.getCell(4);
			    		if(nx_cell_r != null) {
			    			String nx_cell = readCell(nx_cell_r);
			    			nxny[0] = nx_cell;
			    		}
			    		if(ny_cell_r != null) {
			    			String ny_cell = readCell(ny_cell_r);
			    			nxny[1] = ny_cell;
			    		}
			    		
			    	
			    		
			    		
			    		workbook.close();
			    		
			    		return nxny;
			    	}
			   
			    }
			}
			
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		nxny[0] = "-1";
		nxny[1] = "-1";
		return nxny;
	}
}
