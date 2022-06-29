package beans;

import book.Client;
import book.Currency;
import book.Manager;
import book.Results;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class ManageManagers implements IManageManagers{
    Logger logger = LoggerFactory.logger(Client.class);

    @PersistenceContext(unitName = "playAula")
    EntityManager em;

    @Transactional
    public boolean addManager(Manager manager) {
        em.merge(manager);
        logger.info("Manager added successfully !");
        return true;
    }

    @Transactional
    public boolean addClient(/*Manager manager, */Client client) {
        em.persist(client);
        logger.info("Client added successfully !");
        return true;
    }

    @Transactional
    public boolean addCurrency(Currency currency) {
        em.persist(currency);
        logger.info("Currency added successfully !");
        return true;
    }

    public List<Manager> getManagers() {

        TypedQuery<Manager> q = em.createQuery("from gestor s", Manager.class);
        List<Manager> list = q.getResultList();

        logger.debug("Listing managers");
        return list;
    }

    public List<Currency> getCurrencies() {

        TypedQuery<Currency> q = em.createQuery("from moeda m", Currency.class);
        List<Currency> list = q.getResultList();

        logger.debug("Listing managers");
        return list;
    }

    public Manager getManagerByID(int id){

        TypedQuery<Manager> q = em.createQuery("from gestor s where s.id =:id", Manager.class);
        q.setParameter("id", id);
        Manager manager = q.getSingleResult();

        return manager;
    }

    public List<Client> getClients() {

        TypedQuery<Client> q = em.createQuery("from cliente c", Client.class);
        List<Client> list = q.getResultList();

        logger.debug("Listing Clients");
        return list;
    }

    public Client getClientByID(int id) { //Para ter o balanço de um dado cliente

        TypedQuery<Client> q = em.createQuery("from cliente c where c.id =:id", Client.class);
        q.setParameter("id", id);
        Client cliente = q.getSingleResult();                               //para o balanceeee!
        logger.debug("Balance of the client with id " + id);
        return cliente;
    }

    public Results getTotals() {

        TypedQuery<Results> q = em.createQuery("from resultados r", Results.class);
        Results result = q.getSingleResult();

        logger.debug("Get Totals: " + result);
        return result;
    }
}
