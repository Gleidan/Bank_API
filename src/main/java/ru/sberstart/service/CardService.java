package ru.sberstart.service;

import ru.sberstart.model.Card;

import java.util.List;

public interface CardService {

    public Double checkBalance(Long id);

    public boolean changeBalance(Long id, Double sum);

    public List<Card> getAllClientCard(Long client_id);

    public List<Card> getAllCardOnAccount(Long account_id);

    public boolean createNewCard(Long account_id);
}
