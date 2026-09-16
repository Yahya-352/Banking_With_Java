public interface ICard {
    double getWithdrawLimit();
    double getTransferLimit();
    double getOwnTransferLimit();
    double getDepositLimit();
    double getOwnDepositLimit();
    String getCardType();
}
