/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.writer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dbunit.dataset.excel.XlsDataSetWriter;

/**
 *
 * DBUnitのExcel出力クラスの拡張版<br>
 * オリジナルだと、Dateがlong値で出力されるため分かりづらいから 日付フォーマットへ変換するために作成した。<br>
 * 加えて、2007以降のExcelへ対応する。
 *
 */
public class XlsxDataSetWriter extends XlsDataSetWriter {

    /** 日付パターン：時間があるもの */
    private static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /** 日付パターン：時間がないもの */
    private static final String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setDateCell(Cell cell, Date value, Workbook workbook) {
        SimpleDateFormat sdf = null;
        if (value.getTime() % 60000 == 0) {
            sdf = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
        } else {
            sdf = new SimpleDateFormat(FULL_DATE_PATTERN);
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
