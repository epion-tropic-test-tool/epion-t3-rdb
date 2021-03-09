/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dbunit.dataset.excel.XlsDataSetWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DBUnitのExcel出力クラスの拡張版<br>
 * オリジナルだと、Dateがlong値で出力されるため分かりづらいから 日付フォーマットへ変換するために作成した。<br>
 * 加えて、2007以降のExcelへ対応する。
 */
public class XlsxDataSetWriter extends XlsDataSetWriter {

    /**
     * 日付パターン
     */
    private static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setDateCell(Cell cell, Date value, Workbook workbook) {
        SimpleDateFormat sdf = new SimpleDateFormat(FULL_DATE_PATTERN);
        cell.setCellType(CellType.STRING);
        XSSFRichTextString rich = new XSSFRichTextString(sdf.format(value));
        cell.setCellValue(rich);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

}
