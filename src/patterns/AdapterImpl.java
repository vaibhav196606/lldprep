package patterns;

interface InvoiceDetails {
    public double getTotalAmountInUSD();
    public double getTotalWeightInLBS();
}

class InvoiceDetailsImpl implements InvoiceDetails {
    public double getTotalAmountInUSD() {
        return 150.00;
    }
    public double getTotalWeightInLBS() {
        return 200.00;
    }
}

class InvoiceForIndia{
    public int getTotalAmountInINR() {
        return 10000;
    }
    public int getTotalWeightInKG() {
        return 10;
    }

}

class InvoiceAdaptor implements InvoiceDetails {

    private InvoiceForIndia invoiceForIndia;
    public InvoiceAdaptor(InvoiceForIndia invoiceForIndia) {
        this.invoiceForIndia = invoiceForIndia;
    }
    public double getTotalAmountInUSD() {
        return invoiceForIndia.getTotalAmountInINR()/87.0;
    }
    public double getTotalWeightInLBS() {
        return invoiceForIndia.getTotalWeightInKG()*2.2;
    }

}

public class AdapterImpl {
    public static void main(String[] args) {
        InvoiceAdaptor invoiceAdaptor = new InvoiceAdaptor(new InvoiceForIndia());
        System.out.println(invoiceAdaptor.getTotalAmountInUSD());
    }
}
