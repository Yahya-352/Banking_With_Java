# ACME Bank — Java Banking CLI

A command-line banking application built in Java, demonstrating core OOP principles, file-based persistence, exception handling, and functional-style stream/lambda usage.

## Overview

ACME Bank supports two roles — **Banker** and **Customer** — and models a real banking workflow: account creation, deposits, withdrawals, transfers, card-based transaction limits, overdraft protection, and transaction history with date-based filtering.

All data is persisted across sessions using flat text files (no external database), and passwords are hashed before storage.

## Features

### Authentication
- Login with role recognition (Banker / Customer)
- Passwords hashed with SHA-256 before storage — never stored in plain text ([reference](https://www.baeldung.com/sha-256-hashing-java))
- Fraud protection: account locks for 1 minute after 3 failed login attempts

### Accounts
- Customers can hold a Checking account, a Savings account, or both
- Each account is issued one debit card at creation (chosen by the banker)

### Debit Cards
Three card tiers, each with different daily transaction limits:

| Operation | Mastercard Platinum | Mastercard Titanium | Mastercard (Standard) |
|---|---|---|---|
| Withdraw / day | $20,000 | $10,000 | $5,000 |
| Transfer / day (to others) | $40,000 | $20,000 | $10,000 |
| Transfer / day (own accounts) | $80,000 | $40,000 | $20,000 |
| Deposit / day | $100,000 | $100,000 | $100,000 |
| Deposit / day (own accounts) | $200,000 | $200,000 | $200,000 |

Daily limits are enforced by summing each account's same-day transactions and rejecting anything that would push the total over the card's limit.

### Transactions
- **Deposit** — increases balance; reactivates a deactivated account if the balance returns to non-negative
- **Withdraw** — decreases balance; triggers overdraft handling if it goes negative
- **Transfer** — between a customer's own accounts, or to another customer's account; self-transfers are blocked

### Overdraft Protection
- A $35 fee is applied the moment a withdrawal pushes the balance negative
- Withdrawals over $100 are blocked while the balance is already negative
- The account deactivates after 2 overdrafts, and reactivates once the balance returns to non-negative

### Transaction History & Filtering
- Full transaction log per account (date, type, amount, resulting balance)
- Filter by: Today, Yesterday, Last 7 Days, Last 30 Days, a specific month, or a custom date range

### Account Statement
- Combined view of an account's current state and its full transaction history

## Architecture

```
User (abstract)
 ├─ Customer   — holds a CheckingAccount and/or SavingsAccount
 └─ Banker     — creates new customers

Account (abstract, implements Transactable)
 ├─ CheckingAccount
 └─ SavingsAccount
     each holds one ICard

ICard (interface)
 ├─ MastercardPlatinum
 ├─ MastercardTitanium
 └─ Mastercard (standard)

Transaction        — immutable record of one event
Bank               — orchestrates login, accounts, transfers, and persistence
FileHandler        — reads/writes users.txt, accounts.txt, transactions.txt
PasswordUtil       — SHA-256 password hashing
```

## Data Persistence

Three flat files act as the "database":

| File | Format | Write mode |
|---|---|---|
| `users.txt` | `username\|hashedPassword\|role\|userId` | Full rewrite on every change |
| `accounts.txt` | `accountNumber\|balance\|overdraftCount\|active\|type\|customerId\|cardType` | Full rewrite on every change |
| `transactions.txt` | `accountNumber\|type\|amount\|balanceAfter\|timestamp` | Append-only — history is never overwritten |

## Running the Project

1. Compile and run `Main.java`.
2. On first run, the three data files are created empty.
3. A default Banker account is created automatically on startup (main() calls bank.addBanker(...)), so there's a banker to log in as right away — username admintest, password admintest123. You can find and edit this in main().
4. Since this call runs on every startup, if you rerun the program after the account already exists, it fails silently (caught and ignored) rather than creating a duplicate. If you want a different admin username, change it in main() before running again.
5. Log in as the Banker to create customer accounts, then log in as a Customer to bank.


## User Stories

1. As a banker, I have to be able to add a new customer so that the new customers can start banking within the app.
2. As a customer, I want to log into my account with a username and password, so that I can securely access my banking information.
3. As a customer, I want to be warned and charged a fee if my withdrawal overdraws my account, so that I understand the consequences of spending more than I have.
4. As a customer, I want my daily transaction limits to be enforced based on my card type, so that my account stays protected from excessive or fraudulent activity.
5. As a user, I want my account to lock temporarily after repeated failed login attempts, so that unauthorized users can't brute-force their way into my account.
6. As a customer, I want my password to be stored securely, so that my credentials aren't exposed if any data Leak occurs.
7. As a customer, I want to deposit money into my checking or savings account, so that I can add funds to my balance.
8. As a customer, I want to withdraw money from my checking or savings account, so that I can access my funds.
9. As a customer, I want to transfer money between my own accounts or to another customer's account, so that I can move funds where I need them.
10. As a customer, I want to view a list of my accounts with their balances and status, so that I know my current financial standing at a glance.
11. As a customer, I want to filter my transaction history by date, so that I can quickly find specific past transactions without scrolling through everything.
12. As a user, I want to log out of my session, so that I can securely end my access.



### Testing
- Unit tests (JUnit 5) covering `Account` behavior: deposits, withdrawals, overdraft fees, the $100 cap while negative, deactivation after repeated overdrafts, and reactivation