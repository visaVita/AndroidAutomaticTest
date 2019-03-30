package IO;

/*
*
*
* */

import TestFunctions.TestExecutor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import regex.RegexMatches;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.util.List;
import java.util.Map;


public class ReadTestCase {
    private File         myExcel;
    private XSSFWorkbook excelWorkBook;
    private XSSFCell     cell;
    private XSSFRow      row;
    private boolean      result;
    private String       AppName = "Maxjia";
    private final static String AppLoc = "G:\\Android_Automatic_Testing\\test app\\";
    private RegexMatches regex;
    private TestExecutor executor;
    //private Map<String,Integer> Direction;


    ReadTestCase(){
        myExcel = new File("G:\\Android_Automatic_Testing\\TestCase\\myExcel.xls");
        executor = new TestExecutor();
        regex = new RegexMatches();

    }

    public void readExcel(File file){
        try {
            InputStream inputStream = new FileInputStream(file.getAbsoluteFile());
            FileOutputStream fileOutputStream = null;

            excelWorkBook = new XSSFWorkbook(inputStream);

            XSSFSheet excelSheet = excelWorkBook.getSheetAt(0);

            int rowLength = excelSheet.getLastRowNum();
            for (int i = 1; i <= rowLength; i++){
                //获取用例ID
                Cell CaseIdCell = excelSheet.getRow(i).getCell(0);
                String CaseID   = CaseIdCell.getStringCellValue();
                //获取定位方式
                Cell ModeCell   = excelSheet.getRow(i).getCell(3);
                String Mode     = ModeCell.getStringCellValue();
                //获取操作
                Cell OperCell   = excelSheet.getRow(i).getCell(4);
                String Oper     = OperCell.getStringCellValue();
                //如果定位方式是Id，则获取Id
                if (Mode.toLowerCase().equals("id")){
                    Cell IdCell = excelSheet.getRow(i).getCell(5);
                    String Id   = IdCell.getStringCellValue();
                    int res = regex.match(Oper);
                    switch (res){
                        case 0:/* todo throw an excep */break;
                        case 1: executor.initialize(regex.AppName); break;
                        case 2:
                            try {
                                executor.Operation("click",Id,Mode);
                            }catch (XPathExpressionException e){
                                e.printStackTrace();
                            }
                            break;

                        case 3:
                            switch (regex.SwipeTo){
                                case "向上":executor.SwipeUp();   break;
                                case "向下":executor.SwipeDown(); break;
                                case "向左":executor.SwipeLeft(); break;
                                case "向右":executor.SwipeRight();break;
                                default://todo throw an exception
                            }
                            break;

                        case 4:
                            switch (regex.ZoomAt){
                                case "放大":executor.ZoomOut(); break;
                                case "缩小":executor.ZoomIn();  break;
                                default://todo throw an exception
                            }
                            break;
                        case 5:
                            try{
                                executor.Operation("sendKeys",Id ,Mode,regex.Input);
                            }catch (XPathExpressionException e){

                                e.printStackTrace();
                            }
                            break;

                        default://todo throw an exception
                    }

                }
                else {
                    int res = regex.match(Oper);
                    switch (res){
                        case 0:/* todo throw an excep */break;
                        case 1: executor.initialize(regex.AppName); break;
                        case 2:
                            try {
                                executor.Operation("click",regex.ConName,Mode);
                            }catch (XPathExpressionException e){
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            switch (regex.SwipeTo){
                                case "向上":executor.SwipeUp();   break;
                                case "向下":executor.SwipeDown(); break;
                                case "向左":executor.SwipeLeft(); break;
                                case "向右":executor.SwipeRight();break;
                                default://todo throw an exception
                            }
                            break;

                        case 4:
                            switch (regex.ZoomAt){
                                case "放大":executor.ZoomOut(); break;
                                case "缩小":executor.ZoomIn();  break;
                                default://todo throw an exception
                            }
                            break;
                        case 5:
                            try{
                                executor.Operation("sendKeys",regex.ConName ,Mode,regex.Input);
                            }catch (XPathExpressionException e){

                                e.printStackTrace();
                            }
                            break;

                        default://todo throw an exception
                    }
                }

            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
