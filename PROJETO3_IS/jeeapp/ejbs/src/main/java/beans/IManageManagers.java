package beans;

import book.Client;
import book.Currency;
import book.Manager;
import book.Results;

import java.util.List;

public interface IManageManagers {
    boolean addManager(Manager manager);
    boolean addClient(/*Manager manager,*/ Client client);
    boolean addCurrency(Currency currency);
    Manager getManagerByID(int id);
    List<Manager> getManagers();
    List<Client> getClients();
    List<Currency> getCurrencies();
    Client getClientByID(int id);
    public Results getTotals();
    }
