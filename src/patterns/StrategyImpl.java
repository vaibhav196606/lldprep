package patterns;

interface PaymentStrategy {
    public void pay();
}

class PaymentContext{
    PaymentStrategy paymentStrategy;
    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void executePayment() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        paymentStrategy.pay();
    }
}

class CreditCard implements PaymentStrategy {
    @Override
    public void pay() {
        System.out.println("Paying this credit card");
    }
}

class DebitCard implements PaymentStrategy {
    @Override
    public void pay() {
        System.out.println("Paying this debit card");
    }
}

public class StrategyImpl {
    public static void main(String[] args) {
        PaymentContext paymentContext = new PaymentContext(new CreditCard());
        paymentContext.executePayment();
        paymentContext.setPaymentStrategy(new DebitCard());
        paymentContext.executePayment();

    }
}
