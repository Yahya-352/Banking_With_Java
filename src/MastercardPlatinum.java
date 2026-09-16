public class MastercardPlatinum implements ICard{
    @Override
    public double getWithdrawLimit() {
        return 20000;
    }

    @Override
    public double getTransferLimit() {
        return 40000;
    }

    @Override
    public double getOwnTransferLimit() {
        return 80000;
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
        return "PLATINUM";
    }
}
