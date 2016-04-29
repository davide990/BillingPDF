# Welcome
Billing is a simple billing generator written in java, using iText PDF library (itextpdf.com).

## Examples of usage

```
//create the billing
Billing b = new Billing("/path/to/billing");

//Set the billing title
b.SetBillingHeaderTitle("My Billing");

//Set the billing logo
b.SetBillingLogoFilename("/path/to/logo/image");
b.SetBillingLogoResizeMethod(LOGO_RESIZE_METHOD.Percent);
b.SetBillingLogoScalingPercent(50);

//Add entries to billing
b.AddBillingEntry("2015-3-25", "Canon PowerShot SX520", "1", "199$");
b.AddBillingEntry("2015-2-1", "Samsung Galaxy S6", "1", "299$");
b.AddBillingEntry("2015-2-1", "Fender Mini Tone Master", "1", "34$");
b.AddBillingEntry("2014-12-20", "Sennheiser HD 598 Over-Ear Headphones", "1", "149$");
b.AddBillingEntry("2015-12-17", "Samsung 850 EVO 250GB", "1", "106$");

//Set the customer data
b.SetCustomerEmail("my.self@the_mail.com");
b.SetCustomerName("John Doe");
b.SetBillingIdentifier("126438");

//Generate the billing
try
{
    b.GenerateDocument();
} catch (DocumentException ex) { }
```
