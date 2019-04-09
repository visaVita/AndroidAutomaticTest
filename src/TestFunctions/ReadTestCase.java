package TestFunctions;

/*
*
*
* */

import GUI.MainFrame;
import TestFunctions.TestExecutor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import regex.RegexMatches;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;


public class ReadTestCase implements Runnable{
    private File          myExcel;
    private XSSFWorkbook  excelWorkBook;
    private XSSFCell      cell;
    private XSSFRow       row;
    private boolean       result;
    private String        AppName;
    private static String AppLoc;
    private RegexMatches  regex;
    private TestExecutor  executor;
    private MainFrame     frame;
    //private Map<String,Integer> Direction;


    public ReadTestCase(MainFrame frame,String AppName, String AppPath, String CasePath){
        myExcel      = new File(CasePath);
        this.AppName = AppName;
        this.AppLoc  = AppPath;
        this.frame   = frame;
        executor     = new TestExecutor();
        regex        = new RegexMatches();


    }

    public void readExcel(File file){
        try {
            InputStream inputStream = new FileInputStream(file.getAbsoluteFile());
            FileOutputStream fileOutputStream = null;

            excelWorkBook = new XSSFWorkbook(inputStream);

            XSSFSheet excelSheet = excelWorkBook.getSheetAt(0);

            int rowLength = excelSheet.getLastRowNum();
            if (rowLength>1) {


                for (int i = 1; i <= rowLength; i++) {
                    Thread.sleep(1000);
                    //获取用例ID
                    Cell CaseIdCell = excelSheet.getRow(i).getCell(0);
                    String CaseID = CaseIdCell.getStringCellValue();
                    //获取步骤ID
                    Cell StepIdCell = excelSheet.getRow(i).getCell(1);
                    double StepId = StepIdCell.getNumericCellValue();
                    //获取定位方式
                    Cell ModeCell = excelSheet.getRow(i).getCell(2);
                    String Mode = ModeCell.getStringCellValue();
                    //获取操作
                    Cell OperCell = excelSheet.getRow(i).getCell(3);
                    String Oper = OperCell.getStringCellValue();

                    frame.setMsg("执行用例"+CaseID);

                    //如果定位方式是Id，则获取Id
                    if (Mode.toLowerCase().equals("id")) {
                        frame.setRawMsg("执行步骤"+StepId+"：");
                        Cell IdCell = excelSheet.getRow(i).getCell(4);
                        String Id = IdCell.getStringCellValue();
                        int res = regex.match(Oper);
                        switch (res) {
                            case 0:/* todo throw an excep */
                                frame.setMsg("未找到对应操作");
                                break;
                            case 1:
                                frame.setMsg("执行初始化，打开App");
                                executor.initialize(regex.AppName);
                                break;
                            case 2:
                                frame.setMsg("点击操作");
                                try {
                                    executor.Operation("click", Id, Mode);
                                } catch (XPathExpressionException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 3:
                                frame.setMsg("滑动操作");
                                switch (regex.SwipeTo) {
                                    case "向上":
                                        executor.SwipeUp();
                                        break;
                                    case "向下":
                                        executor.SwipeDown();
                                        break;
                                    case "向左":
                                        executor.SwipeLeft();
                                        break;
                                    case "向右":
                                        executor.SwipeRight();
                                        break;
                                    default://todo throw an exception
                                }
                                break;

                            case 4:
                                frame.setMsg("缩放操作");
                                switch (regex.ZoomAt) {
                                    case "放大":
                                        executor.ZoomOut();
                                        break;
                                    case "缩小":
                                        executor.ZoomIn();
                                        break;
                                    default://todo throw an exception
                                }
                                break;
                            case 5:
                                frame.setMsg("输入操作");
                                try {
                                    executor.Operation("sendKeys", Id, Mode, regex.Input);
                                } catch (XPathExpressionException e) {

                                    e.printStackTrace();
                                }
                                break;

                            default://todo throw an exception
                        }

                    } else {
                        frame.setRawMsg("执行步骤"+StepId+"：");
                        int res = regex.match(Oper);
                        switch (res) {
                            case 0:/* todo throw an excep */
                                frame.setMsg("未找到对应操作");
                                break;
                            case 1:
                                frame.setMsg("初始化并打开App");
                                executor.initialize(regex.AppName);
                                break;
                            case 2:
                                frame.setMsg("点击操作");
                                try {
                                    executor.Operation("click", regex.ConName, Mode);
                                } catch (XPathExpressionException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 3:
                                frame.setMsg("滑动操作");
                                switch (regex.SwipeTo) {
                                    case "向上":
                                        executor.SwipeUp();
                                        break;
                                    case "向下":
                                        executor.SwipeDown();
                                        break;
                                    case "向左":
                                        executor.SwipeLeft();
                                        break;
                                    case "向右":
                                        executor.SwipeRight();
                                        break;
                                    default://todo throw an exception
                                }
                                break;

                            case 4:
                                frame.setMsg("缩放操作");
                                switch (regex.ZoomAt) {
                                    case "放大":
                                        executor.ZoomOut();
                                        break;
                                    case "缩小":
                                        executor.ZoomIn();
                                        break;
                                    default://todo throw an exception
                                }
                                break;
                            case 5:
                                frame.setMsg("输入操作");
                                try {
                                    executor.Operation("sendKeys", regex.ConName, Mode, regex.Input);
                                } catch (XPathExpressionException e) {

                                    e.printStackTrace();
                                }
                                break;

                            default:
                                frame.setMsg("未找到对应操作");
                                //todo throw an exception
                        }
                    }

                }
                frame.FinishCallback();
            }
            else frame.CaseInvalidateCallback("用例为空");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        readExcel(myExcel);
    }
}
