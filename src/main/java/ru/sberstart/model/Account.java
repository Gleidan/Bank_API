package ru.sberstart.model;

import java.util.Objects;
import java.util.UUID;

public class Account {

    private Long id;
    private UUID number;
    private Client client;

    public Account() {
    }

    public Account(Long id, UUID number, Client client) {
        this.id = id;
        this.number = number;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(number, account.number) && Objects.equals(client, account.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, client);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number=" + number +
                ", client=" + client +
                '}';
    }
}
