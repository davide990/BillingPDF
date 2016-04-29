package com.davide.billing.utils;

import com.davide.billing.config.BillingConfig;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 * @author Davide Guastella <davide.guastella90@gmail.com>
 */
public class PdfTableUtility
{

    /**
     * Add a row to a pdf table
     *
     * @param table the table in which the elements have to be added
     * @param cells the set of cell of the row that has to be added
     */
    public static void AddRowToTable(PdfPTable table, String... cells)
    {
        if (table.getNumberOfColumns() != cells.length)
        {
            System.out.println("The number of string items doesn't match the table columns number");
            return;
        }

        for (String cell : cells)
        {
            PdfPCell the_cell = new PdfPCell(new Phrase(cell));
            the_cell.setMinimumHeight(BillingConfig.MIN_CELL_HEIGHT);
            table.addCell(the_cell);
        }
    }

    public static void AddRowToTable(PdfPTable table, Phrase... cells)
    {
        for (Phrase cell : cells)
        {
            table.addCell(cell);
        }
    }

    public static void AddCellToTable(PdfPTable table, Phrase p)
    {
        table.addCell(p);
    }

    public static void AddEmptyCellToTable(PdfPTable table)
    {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorderWidth(0);
        cell.setMinimumHeight(BillingConfig.MIN_CELL_HEIGHT);
        table.addCell(cell);
    }

    /**
     * Add an empty row to a table.
     *
     * @param table the table in which the empty row has to be added
     */
    public static void AddEmptyRowToTable(PdfPTable table)
    {
        for (int i = 0; i < table.getNumberOfColumns(); i++)
        {
            PdfPCell cell = new PdfPCell(new Phrase(""));
            cell.setMinimumHeight(BillingConfig.MIN_CELL_HEIGHT);
            table.addCell(cell);
        }
    }
}
