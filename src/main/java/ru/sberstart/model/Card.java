package ru.sberstart.model;

import java.util.Objects;
import java.util.UUID;

public class Card {

    private Long id;
    private UUID number;
    private Account account;
    private Double balance;

    public Card() {
    }

    public Card(Long id, UUID number, Account account, Double balance) {
        this.id = id;
        this.number = number;
        this.account = account;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getNumber() {
        return number;
    }

    public void setNumber(UUID number) {
        this.number = number;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void changeBalance(Double balance) {
        this.balance += balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) && Objects.equals(number, card.number) && Objects.equals(account, card.account) && Objects.equals(balance, card.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, account, balance);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", number=" + number +
                ", account=" + account +
                ", balance=" + balance +
                '}';
    }
}
