/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davide.billing;

import com.davide.billing.config.BillingConfig;
import com.davide.billing.utils.FooterHelper;
import com.davide.billing.utils.PdfTableUtility;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davide Guastella <davide.guastella90@gmail.com>
 */
public class Billing
{

    private static final Rectangle DOCUMENT_RECT = new Rectangle(35, 55, 560, 780);
    private Document document;
    private List<String[]> billing_data;
    private String logo_fname = "";
    private String customer_email;
    private String customer_name;
    private String billing_id;

    public enum LOGO_RESIZE_METHOD
    {
        Absolute,
        Percent
    }

    private String billing_header_title;

    private LOGO_RESIZE_METHOD logo_resize_method;
    private int logo_default_width;
    private int logo_default_height;
    private float logo_scaling_percent_value;

    /**
     * Create a new pdf billing.
     *
     * @param output_fname the output billing file name
     */
    public Billing(String output_fname)
    {
        billing_data = new ArrayList<>();
        logo_resize_method = LOGO_RESIZE_METHOD.Percent;
        logo_scaling_percent_value = 100;
        billing_header_title = "Billing";

        try
        {
            document = new Document();
            document.setMargins(35f, 35f, 72f, 72f);  //A4

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(output_fname));

            writer.setBoxSize("billing_doc", DOCUMENT_RECT);                    //Set the document content rectangle area (A4)
            writer.setPageEvent(new FooterHelper());                      //Set the event handler for the pdf writer. This will write the footer at the end of every page
        } catch (FileNotFoundException | DocumentException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Populate the billing.
     *
     * @throws DocumentException
     */
    public void GenerateDocument() throws DocumentException
    {
        Logger.getLogger(Billing.class.getName()).log(Level.INFO, "Opening document");
        document.open();

        Logger.getLogger(Billing.class.getName()).log(Level.INFO, "Creating billing");
        document.add(createHeaderParagragh());
        document.add(Chunk.NEWLINE);
        document.add(createUserInformationParagraph());
        document.add(createBillingBodyParagraph());

        Logger.getLogger(Billing.class.getName()).log(Level.INFO, "Closing document");
        document.close();
    }

    /**
     * Add a record to the billing
     *
     * @param date
     * @param article
     * @param quantity
     * @param price
     */
    public void AddBillingEntry(String date, String article, String quantity, String price)
    {
        billing_data.add(new String[]
        {
            date, article, quantity, price
        });
    }

    /**
     * Set the customer name
     *
     * @param id
     */
    public void SetCustomerName(String id)
    {
        customer_name = id;
    }

    /**
     * Set the customer e-mail
     *
     * @param email
     */
    public void SetCustomerEmail(String email)
    {
        customer_email = email;
    }

    /**
     * Set the billing identifier
     *
     * @param billing_id
     */
    public void SetBillingIdentifier(String billing_id)
    {
        this.billing_id = billing_id;
    }

    /**
     * Set the billing header title
     *
     * @param title
     */
    public void SetBillingHeaderTitle(String title)
    {
        this.billing_header_title = title;
    }

    /**
     * Set the file name of the logo that has to put into the billing header
     *
     * @param logo_fname
     */
    public void SetBillingLogoFilename(String logo_fname)
    {
        this.logo_fname = logo_fname;
    }

    /**
     * Set the resize method for the logo
     *
     * @param m
     */
    public void SetBillingLogoResizeMethod(LOGO_RESIZE_METHOD m)
    {
        this.logo_resize_method = m;
    }

    /**
     * Set the size of the logo
     *
     * @param width
     * @param height
     */
    public void SetBillingLogoAbsoluteSize(int width, int height)
    {
        this.logo_default_width = width;
        this.logo_default_height = height;
    }

    /**
     * Set the scaling percent of the logo
     *
     * @param p
     */
    public void SetBillingLogoScalingPercent(float p)
    {
        this.logo_scaling_percent_value = p;
    }

    /**
     *
     * @return
     */
    private Paragraph createHeaderParagragh()
    {
        Paragraph header_paragraph = new Paragraph();
        PdfPTable header_table = new PdfPTable(2);

        //set the table width
        header_table.setWidthPercentage(100);

        //add the logo cell to the header table
        if (!logo_fname.isEmpty())
        {
            header_table.addCell(getBillingLogoCell());
        }

        //Set the header billing text
        PdfPCell text_cell = new PdfPCell(new Phrase(this.billing_header_title, BillingConfig.TITLE_FONT));
        text_cell.setBorder(0);
        text_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        header_table.addCell(text_cell);

        //Add the table to the header paragraph
        header_paragraph.add(header_table);

        return header_paragraph;
    }

    /**
     * Create a table cell containing the logo of the billing
     *
     * @return
     */
    private PdfPCell getBillingLogoCell()
    {
        Image logo_image = null;

        try
        {
            logo_image = Image.getInstance(logo_fname);

            switch (this.logo_resize_method)
            {
                case Absolute:
                    logo_image.scaleToFit(logo_default_width, logo_default_height);
                    break;

                case Percent:
                    logo_image.scalePercent(logo_scaling_percent_value);
                    break;
            }
        } catch (BadElementException | IOException ex)
        {
            Logger.getLogger(Billing.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Create the cell
        PdfPCell im_cell = new PdfPCell(logo_image);

        //Cell does not have border
        im_cell.setBorder(0);

        return im_cell;
    }

    /**
     *
     * @return
     */
    private Paragraph createUserInformationParagraph() throws DocumentException
    {
        //create a new paragraph
        Paragraph user_information_paragraph = new Paragraph();

        //create the main paragraph table
        PdfPTable main_table = new PdfPTable(3);

        main_table.setWidths(new float[]
        {
            100, 50, 200
        });

        //set the table expanded to the page width
        main_table.setWidthPercentage(100);

        //Create the user info table
        PdfPTable user_information_table = new PdfPTable(1);
        PdfPCell user_info_cell = new PdfPCell(getUserInformationPhrase());
        PdfPCell info_cell = new PdfPCell();

        user_information_table.setWidthPercentage(100);
        user_information_table.addCell(user_info_cell);
        info_cell.setBorderWidth(0);
        info_cell.addElement(user_information_table);

        //Create the  billing id table
        PdfPTable billing_id_table = new PdfPTable(1);
        PdfPCell billing_id_cell = new PdfPCell(getBillingIdentifierPhrase());
        PdfPCell billing_cell = new PdfPCell();

        billing_id_table.setWidthPercentage(100);
        billing_id_table.addCell(billing_id_cell);
        billing_cell.setBorderWidth(0);
        billing_cell.addElement(billing_id_table);
        PdfTableUtility.AddEmptyCellToTable(billing_id_table);
        PdfTableUtility.AddEmptyCellToTable(billing_id_table);

        //add the tables to the main table
        main_table.addCell(billing_cell);
        PdfTableUtility.AddEmptyCellToTable(main_table);
        main_table.addCell(info_cell);

        //add the main table to the paragraph
        user_information_paragraph.add(main_table);

        return user_information_paragraph;
    }

    /**
     *
     * @return
     */
    private Phrase getBillingIdentifierPhrase()
    {
        Phrase billing_phrase = new Phrase();

        Chunk billing = new Chunk("Billing:   ", BillingConfig.TABLE_CONTENT_FONT_1);
        Chunk id = new Chunk(this.billing_id, BillingConfig.TABLE_CONTENT_FONT_2);

        billing_phrase.add(billing);
        billing_phrase.add(id);

        return billing_phrase;
    }

    /**
     *
     * @return
     */
    private Phrase getUserInformationPhrase()
    {
        Phrase user_info = new Phrase();

        Chunk customer = new Chunk("Customer   ", BillingConfig.TABLE_CONTENT_FONT_1);
        Chunk customer_id_chunk = new Chunk(this.customer_name, BillingConfig.TABLE_CONTENT_FONT_2);

        Chunk email = new Chunk("E-mail   ", BillingConfig.TABLE_CONTENT_FONT_1);
        Chunk customer_email_chunk = new Chunk(this.customer_email, BillingConfig.TABLE_CONTENT_FONT_2);

        user_info.add(customer);
        user_info.add(customer_id_chunk);
        user_info.add("\n");
        user_info.add(email);
        user_info.add(customer_email_chunk);

        return user_info;
    }

    /**
     *
     */
    private Phrase getTotalBillingPricePhrase()
    {
        Phrase phrase = new Phrase();

        Chunk init = new Chunk("Total price\n", BillingConfig.TABLE_CONTENT_FONT_1);
        Chunk total_price = new Chunk(calculateTotalBillingPrice(), BillingConfig.TABLE_CONTENT_FONT_2);

        phrase.add(init);
        phrase.add(total_price);

        return phrase;
    }

    /**
     * Create the main billing paragraph that contains the table of products
     *
     * @return
     */
    private Paragraph createBillingBodyParagraph()
    {
        Paragraph main_paragraph = new Paragraph("");
        main_paragraph.setSpacingAfter(100);
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSplitLate(false);
        table.setHeaderRows(2);
        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        //add the header row
        PdfTableUtility.AddRowToTable(table, new Phrase("Date", BillingConfig.TABLE_HEADER_FONT),
                new Phrase("Article", BillingConfig.TABLE_HEADER_FONT),
                new Phrase("Quantity", BillingConfig.TABLE_HEADER_FONT),
                new Phrase("Unit price", BillingConfig.TABLE_HEADER_FONT));

        //add the footer row containing the total price information
        PdfTableUtility.AddEmptyCellToTable(table);
        PdfTableUtility.AddEmptyCellToTable(table);
        PdfTableUtility.AddEmptyCellToTable(table);
        PdfTableUtility.AddCellToTable(table, getTotalBillingPricePhrase());

        table.setFooterRows(1);

        //fill the billing tables with user data
        for (String[] data : billing_data)
        {
            PdfTableUtility.AddRowToTable(table, data[0], data[1], data[2], data[3]);
        }

        main_paragraph.add(Chunk.createWhitespace(""));
        main_paragraph.add(table);

        return main_paragraph;
    }

    /**
     * Calculate the sum of the prices of the articles in the billing.
     *
     * @return the string representing the total price of the articles
     */
    private String calculateTotalBillingPrice()
    {
        int total_price = 0;
        String total_price_str;

        for (String[] data : billing_data)
        {
            String priceStr = data[3];
            if (priceStr.endsWith("$"))
            {
                priceStr = priceStr.substring(0, priceStr.length() - 1);
            }

            total_price += Float.parseFloat(priceStr);
        }
        total_price_str = String.format("%d" + BillingConfig.CURRENCY_SYMBOL, total_price);

        return total_price_str;
    }
}
