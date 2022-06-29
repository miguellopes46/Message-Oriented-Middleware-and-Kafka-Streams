package data;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import beans.IManageManagers;
import book.Currency;
import book.Manager;
import java.util.List;


@RequestScoped
@Path("/myservice")

public class MyService {

    @EJB
    private IManageManagers manageManagers;

    @POST
    @Path("/addManager")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response method1(Manager manager) {
        manageManagers.addManager(manager);
        return Response.status(Response.Status.OK).entity("MANAGER ADICIONADO COM SUCESSO!").build();
    }

    @GET
    @Path("/getManagers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method2() {
        List<Manager> managerList = manageManagers.getManagers();
        return Response.ok().entity(managerList).build();
    }

    @POST
    @Path("/addClient")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response method3(book.Client client) {
        //Manager man = manageManagers.getManagerByID(client.getManager().getId());
        manageManagers.addClient(/*man,*/ client);
        return Response.status(Response.Status.OK).entity("CLIENTE ADICIONADO COM SUCESSO!").build();
    }

    @POST
    @Path("/addCurrency")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response method4(Currency currency) {
        manageManagers.addCurrency(currency);
        return Response.status(Response.Status.OK).entity("MOEDA ADICIONADA COM SUCESSO!").build();
    }

    @GET
    @Path("/getClients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method5() {
        List<book.Client> clientList = manageManagers.getClients();
        return Response.ok().entity(clientList).build();
    }

    @GET
    @Path("/getCurrencies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method6() {
        List<Currency> currencyList = manageManagers.getCurrencies();
        return Response.ok().entity(currencyList).build();
    }

    @GET
    @Path("/getClientByID")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method7(int id) {
        book.Client client = manageManagers.getClientByID(id);
        return Response.ok().entity(client).build();
    }

    @GET
    @Path("/getTotals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method8() {
        book.Results resultado = manageManagers.getTotals();
        return Response.ok().entity(resultado).build();
    }
}