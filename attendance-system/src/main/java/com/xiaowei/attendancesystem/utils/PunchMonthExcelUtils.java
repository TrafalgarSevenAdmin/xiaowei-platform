package com.xiaowei.attendancesystem.utils;

import com.xiaowei.attendancesystem.status.PunchRecordType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 月份打卡记录工具
 */
public class PunchMonthExcelUtils {
    //显示的导出表的标题
    private String title;
    //导出表的列名
    private static final String[] ROWNAME = {"序号", "员工姓名", "1号", "2号", "3号", "4号", "5号", "6号", "7号", "8号", "9号", "10号", "11号"
            , "12号", "13号", "14号", "15号", "16号", "17号", "18号", "19号", "20号", "21号", "22号", "23号", "24号", "25号", "26号"
            , "27号", "28号", "29号", "30号", "31号"};

    private List<Object[]> dataList;

    HttpServletResponse response;

    public PunchMonthExcelUtils(String title, List<Object[]> dataList, HttpServletResponse response) {
        this.title = title;
        this.dataList = dataList;
        this.response = response;
    }

    /*
     * 导出数据
     * */
    public void export() throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();                        // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title);                     // 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (ROWNAME.length * 2 - 3)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);

            // 定义所需列数
            int columnNum = ROWNAME.length;
            HSSFRow rowRowName = sheet.createRow(2);                // 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                if (n == 0 || n == 1) {
                    HSSFCell cellRowName = rowRowName.createCell(n);                //创建列头对应个数的单元格
                    cellRowName.setCellType(CellType.STRING);                //设置列头单元格的数据类型
                    HSSFRichTextString text = new HSSFRichTextString(ROWNAME[n]);
                    cellRowName.setCellValue(text);                                    //设置列头单元格的值
                    cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
                } else {
                    HSSFCell cellRowName = rowRowName.createCell(n * 2 - 2);                //创建列头对应个数的单元格
                    cellRowName.setCellType(CellType.STRING);                //设置列头单元格的数据类型
                    HSSFRichTextString text = new HSSFRichTextString(ROWNAME[n]);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, n * 2 - 2, n * 2 - 1));
                    cellRowName.setCellValue(text);                                    //设置列头单元格的值
                    cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式

                    HSSFCell cellRowName2 = rowRowName.createCell(n * 2 - 1);                //创建列头对应个数的单元格
                    cellRowName2.setCellStyle(columnTopStyle);                        //设置列头单元格样式
                }
            }

            HSSFRow clockRow = sheet.createRow(3);
            // 设置上下班标题行
            for (int n = 2; n < columnNum * 2; n++) {
                HSSFCell cellRowName = clockRow.createCell(n);                //创建列头对应个数的单元格
                cellRowName.setCellType(CellType.STRING);                //设置列头单元格的数据类型
                HSSFRichTextString text;
                if (n % 2 == 0) {
                    text = new HSSFRichTextString("上班");
                } else {
                    text = new HSSFRichTextString("下班");
                }
                cellRowName.setCellValue(text);                                    //设置列头单元格的值
                cellRowName.setCellStyle(getStyle(workbook));                        //设置列头单元格样式
            }

            //将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {

                Object[] obj = dataList.get(i);//遍历每个对象
                HSSFRow row = sheet.createRow(i + 4);//创建所需的行数

                for (int j = 0; j < obj.length; j++) {
                    HSSFCellStyle style = getCellStyle(workbook);                    //单元格样式对象
                    HSSFCell cell = null;   //设置单元格的数据类型
                    if (j == 0) {
                        cell = row.createCell(j, CellType.NUMERIC);
                        cell.setCellValue(i + 1);
                    } else if (j == 1) {
                        cell = row.createCell(j, CellType.STRING);
                        cell.setCellValue(obj[j].toString());
                    } else {
                        cell = row.createCell(j, CellType.STRING);
                        if (obj[j] != null) {
                            cell.setCellValue(setStyleByPunchRecordType(style, (PunchRecordType) obj[j]));//设置单元格的值
                        }
                    }
                    cell.setCellStyle(style);                                    //设置单元格样式
                }
            }
            //让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum * 2; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    //当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }

            if (workbook != null) {
                try {
                    String fileName = new String((title + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls").getBytes("utf-8"), "iso-8859-1"); //解决中文乱码问题
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();                    //单元格样式对象
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对其
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    private String setStyleByPunchRecordType(HSSFCellStyle style, PunchRecordType punchRecordType) {
        switch (punchRecordType) {
            case NORMAL:
                style.setFillForegroundColor(HSSFColor.GREEN.index);
                break;//正常
            case BELATE:
                style.setFillForegroundColor(HSSFColor.YELLOW.index);
                break;//迟到
            case EXCEPTION:
                style.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
                break;//异常
            case CLOCKISNULL:
                style.setFillForegroundColor(HSSFColor.RED.index);
                break;//未打卡
            case NOPUNCH:
                style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
                break;//没有记录
        }
        return punchRecordType.getValue();
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }
}
