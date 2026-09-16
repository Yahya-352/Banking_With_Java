public class Mastercard implements ICard{

    @Override
    public double getWithdrawLimit() {
        return 5000;
    }

    @Override
    public double getTransferLimit() {
        return 10000;
    }

    @Override
    public double getOwnTransferLimit() {
        return 20000;
    }

    @Override
    public double getDepositLimit() {
        return 100000;
    }

    @Override
    public double getOwnDepositLimit() {
        return 200000;
    }

    @Override
    public String getCardType() {
        return "Standard";
    }
}
