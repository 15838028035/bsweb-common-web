package com.lj.app.core.common.util;

import java.awt.Color;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
	/*************************************************************************** 
	  * @param fileName EXCEL文件名称 
	  * @param listTitle EXCEL文件第一行列标题集合 
	  * @param listContent EXCEL文件正文数据集合 
	  * @return 
	  */  
	 public  final static String exportExcel(String fileName,String[] Title, List<LinkedHashMap> listContent) {  
	  String result="系统提示：Excel文件导出成功！";    
	  // 以下开始输出到EXCEL  
	  try {      
	   //定义输出流，以便打开保存对话框______________________begin  
	   HttpServletResponse response=ServletActionContext.getResponse();  
	   OutputStream os = response.getOutputStream();// 取得输出流        
	   response.reset();// 清空输出流        
	   response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("UTF-8"),"ISO8859-1"));  
	// 设定输出文件头        
	   response.setContentType("application/msexcel");// 定义输出类型      
	   
	   ExcelUtil.writeExcel(os, fileName, Title, listContent);
	  
	  } catch (Exception e) {  
	   result="系统提示：Excel文件导出失败，原因："+ e.toString();  
	   System.out.println(result);   
	   e.printStackTrace();  
	  }  
	  return result;  
	 } 
	 
 	/**
	 * 读取excel内容
	 */
	public static void readExcelAndPrintContent(String filePath) throws Exception {
		try {

			File template = new File(filePath);
			HashMap<String, String> loginAcctMap = new HashMap<String, String>();
			int startRow = 2;
			Workbook wb = Workbook.getWorkbook(template);
			Sheet sheet = wb.getSheet(0);
			int columns = sheet.getColumns(); // 列数
			int rows = sheet.getRows(); // 行数
			
			for(int i=1; i<rows;i++){
				for(int j=0;j<columns;j++){
					Cell cell = sheet.getCell(j,i);
					System.out.println(cell.getContents()+"======");
				}
				
				System.out.println("-----------------------------");
			}
			wb.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
		
	/*************************************************************************** 
	  * @param fileName EXCEL文件名称 
	  * @param listTitle EXCEL文件第一行列标题集合 
	  * @param listContent EXCEL文件正文数据集合 
	  * @return 
	  */  
	 public  final static String writeExcel(OutputStream os,String fileName,String[] Title, List<LinkedHashMap> listContent) {  
	  // 以下开始输出到EXCEL  
	  String result="系统提示：Excel文件写入成功！";   
	  try {      
	  
	   /** **********创建工作簿************ */  
	   WritableWorkbook workbook = Workbook.createWorkbook(os);  
	  
	   /** **********创建工作表************ */  
	  
	   WritableSheet sheet = workbook.createSheet(fileName, 0);  
	  
	   /** **********设置纵横打印（默认为纵打）、打印纸***************** */  
	   jxl.SheetSettings sheetset = sheet.getSettings();  
	   sheetset.setProtected(false);  
	  
	  
	   /** ************设置单元格字体************** */  
	   WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  
	   WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);  
	  
	   /** ************以下设置三种单元格样式，灵活备用************ */  
	   // 用于标题居中  
	   WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);  
	   wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
	   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
	   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
	   wcf_center.setWrap(false); // 文字是否换行  
	     
	   // 用于正文居左  
	   WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);  
	   wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条  
	   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
	   wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐  
	   wcf_left.setWrap(false); // 文字是否换行     
	   
	  
	   CellView cellView = new CellView();  
       cellView.setAutosize(true); //设置自动大小
       
	   /** ***************以下是EXCEL开头大标题，暂时省略********************* */  
	   //sheet.mergeCells(0, 0, colWidth, 0);  
	   //sheet.addCell(new Label(0, 0, "XX报表", wcf_center));  
	   /** ***************以下是EXCEL第一行列标题********************* */  
	   for (int i = 0; i < Title.length; i++) {  
	    sheet.addCell(new Label(i, 0,Title[i],wcf_center));  
	   }     
	   
	   Label lab = null;  
	   
	   //单元格式
	   jxl.write.WritableFont wfcNav =new jxl.write.WritableFont(WritableFont.ARIAL,12, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
       WritableCellFormat wcfN=new WritableCellFormat(wfcNav);
       
       Color color = Color.decode("#0099cc"); // 自定义的颜色
	  workbook.setColourRGB(Colour.ORANGE, color.getRed(),color.getGreen(), color.getBlue());
		
     //取消设置颜色  wcfN.setBackground(Colour.ORANGE);
      wcfN.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
      wcfN.setAlignment(Alignment.CENTRE); //设置水平对齐
      wcfN.setWrap(true); //设置自动换行
      
      
      /**
       * 内容
       */
      jxl.write.WritableFont wfcontent =new jxl.write.WritableFont(WritableFont.ARIAL,12, WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
      WritableCellFormat wcfcontent = new WritableCellFormat(wfcontent);
      //取消设置边框  wcfcontent.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
      wcfcontent.setAlignment(Alignment.CENTRE);
      
	   /** ***************以下是EXCEL正文数据********************* */  
	   int i=1;  
	   if(listContent!=null){
		   for(Object obj:listContent){  
		       int j=0;  
		       
		       Map map = (HashMap) obj;
		       Iterator it = map.keySet().iterator();
		       while(it.hasNext()){
		    	   Object objKey = it.next();
		    	   Object value =map.get(objKey);
		    	   if(value ==null){
		    		   value = "";
		    	   }
			       
		    	   sheet.setColumnView(i, cellView);//根据内容自动设置列宽  
		    	   //sheet.addCell(new Label(j, i,value.toString(),wcf_left)); 
		        	  lab = new Label(j,i,value.toString(),wcfcontent);   
		              sheet.addCell(lab);  
		               
		    	   j++;
		       }
		       
		       i++;  
		   }  
	   }
	   /** **********将以上缓存中的内容写到EXCEL文件中******** */  
	   workbook.write();  
	   /** *********关闭文件************* */  
	   workbook.close();     
	  
	  } catch (Exception e) {  
	   result="系统提示：Excel文件导出失败，原因："+ e.toString();  
	   System.out.println(result);   
	   e.printStackTrace();  
	  }  
	  return result;  
	 }  
}
