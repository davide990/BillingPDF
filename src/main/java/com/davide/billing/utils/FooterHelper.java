/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davide.billing.utils;

import com.davide.billing.config.BillingConfig;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This helper is used to place a default footer on every page of the billing
 *
 * @author Davide Guastella <davide.guastella90@gmail.com>
 */
public class FooterHelper extends PdfPageEventHelper
{

    @Override
    public void onEndPage(PdfWriter writer, Document document)
    {
        Rectangle rect = writer.getBoxSize("billing_doc");
        Phrase footer_phrase = new Phrase("My Company (c), 2015", BillingConfig.SUBTITLE_FONT);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER,
                footer_phrase,
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 15, 0);

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER,
                new Phrase(String.format("Page %d", writer.getPageNumber()), BillingConfig.SUBTITLE_FONT),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 28, 0);

        super.onEndPage(writer, document);
    }

}
