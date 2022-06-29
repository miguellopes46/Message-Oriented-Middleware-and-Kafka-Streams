package book;

import data.Currency;
import data.Manager;
import data.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class App {

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		Client client = ClientBuilder.newClient();
		WebTarget target;
		Response response;
		int menuOption;
		String value;
		String name, phone, address, password, mail;
		Scanner scan = new Scanner(System.in);
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(in);
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

		while (true) {
			System.out.println("_____________________________< ADMIN CONSOLE >_______________________________________");
			System.out.println("<1> ADD MANAGER");
			System.out.println("<2> ADD CLIENT");
			System.out.println("<3> ADD CURRENCY");
			System.out.println("<4> LIST MANAGERS");
			System.out.println("<5> LIST CLIENTS");
			System.out.println("<6> LIST CURRENCIES");
			System.out.println("<7> CREDITS PER CLIENT");
			System.out.println("<8> PAYMENTS PER CLIENT");
			System.out.println("<9> BALANCE OF A CLIENT");
			System.out.println("<10> SUM OF ALL CREDITS");
			System.out.println("<11> SUM OF ALL PAYMENTS");
			System.out.println("<12> SUM OF ALL BALANCES");
			System.out.println("<14> LIST OF CLIENTS WITHOUT PAYMENTS FOR THE LAST 2 MONTHS");
			System.out.println("<15> CLIENT WITH HIGHEST DEBT");
			System.out.println("<16> MANAGER WITH HIGHEST REVENUE");
			System.out.print(">");
			menuOption = scan.nextInt();
			switch (menuOption) {
				case 1:
					//RECOLHE DADOS MANAGER
					System.out.println("Nome:");
					name = reader.readLine();
					System.out.println("Telemovel:");
					phone = reader.readLine();
					System.out.println("Morada:");
					address = reader.readLine();
					System.out.println("Password:");
					password = reader.readLine();
					System.out.println("Email:");
					mail = reader.readLine();

					//ENVIA MANAGER
					target = client.target("http://localhost:8080/rest/services/myservice/addManager");
					Manager p = new Manager(name, password, phone, mail, address);
					Entity<Manager> input = Entity.entity(p, MediaType.APPLICATION_JSON);
					response = target.request().post(input);
					value = response.readEntity(String.class);
					System.out.println("RESPONSE: " + value);
					response.close();
					break;

				case 2:
					//RECEBE DADOS DO CLIENTE
					System.out.println("Nome:");
					name = reader.readLine();
					System.out.println("Telemovel:");
					phone = reader.readLine();
					System.out.println("Morada:");
					address = reader.readLine();
					System.out.println("Password:");
					password = reader.readLine();
					System.out.println("Email:");
					mail = reader.readLine();

					//RECEBE MANAGERS
					target = client.target("http://localhost:8080/rest/services/myservice/getManagers");
					response = target.request().get();
					List<Manager> personList = response.readEntity(new GenericType<List<Manager>>(){});
/*
					//IMPRIME NOMES DOS MANAGERS
					for( int i = 0; i<personList.size(); i++){
						System.out.println("<" + i + "> " + personList.get(i).getName());
					}

					//ESCOLHE MANAGER
					System.out.println("SELECT YOUR MANAGER");
					int n = scan.nextInt();
					Manager managerSelected = personList.get(n);
*/
					//ENVIA CLINTE PARA SERVER
					target = client.target("http://localhost:8080/rest/services/myservice/addClient");
					data.Client c = new data.Client(name, password, phone, mail, address/*, managerSelected*/);
					Entity<data.Client> inp = Entity.entity(c, MediaType.APPLICATION_JSON);
					response = target.request().post(inp);
					value = response.readEntity(String.class);
					System.out.println("RESPONSE: " + value);
					response.close();
					break;

				case 3:
					//RECOLHE DADOS CURRENCY
					System.out.println("Nome:");
					name = reader.readLine();
					System.out.println("Exchange Value:");
					Double exchange = scan.nextDouble();

					//ENVIA Currency
					target = client.target("http://localhost:8080/rest/services/myservice/addCurrency");
					Currency cur = new Currency(name, exchange);
					Entity<Currency> input1 = Entity.entity(cur, MediaType.APPLICATION_JSON);
					response = target.request().post(input1);
					value = response.readEntity(String.class);
					System.out.println("RESPONSE: " + value);
					response.close();
					break;

				case 4: //LIST MANAGERS
					target = client.target("http://localhost:8080/rest/services/myservice/getManagers");
					response = target.request().get();
					List<Manager> managerslist = response.readEntity(new GenericType<List<Manager>>(){});

					//IMPRIME NOME e IDS DOS MANAGERS
					for( int i = 0; i<managerslist.size(); i++){
						System.out.println("<ID: " + managerslist.get(i).getId() + "> Name:" + managerslist.get(i).getName()); // ou System.out.println(clientslist.get(i))
					}
					response.close();
					break;

				case 5: //LIST CLIENTS

					target = client.target("http://localhost:8080/rest/services/myservice/getClients");
					response = target.request().get();
					List<data.Client> clientslist = response.readEntity(new GenericType<List<data.Client>>(){});

					//IMPRIME NOME e IDS DOS CLIENTES
					for( int i = 0; i<clientslist.size(); i++){
						System.out.println("<ID: " + clientslist.get(i).getId() + "> Name:" + clientslist.get(i).getName()); // ou System.out.println(clientslist.get(i))
					}
					response.close();
					break;

				case 6:
					//RECEBE CURRENCIES
					target = client.target("http://localhost:8080/rest/services/myservice/getCurrencies");
					response = target.request().get();
					List<Currency> currencyList = response.readEntity(new GenericType<List<Currency>>(){});

					//IMPRIME NOMES DAS CURRENCIES
					for( int i = 0; i<currencyList.size(); i++){
						System.out.println("<" + i + "> " + currencyList.get(i).getName());
					}
					response.close();
					break;

				case 7: //CREDITS PER CLIENT

					target = client.target("http://localhost:8080/rest/services/myservice/getClients");
					response = target.request().get();
					List<data.Client>  clientList= response.readEntity(new GenericType<List<data.Client>>(){});

					//IMPRIME NOMES do ID e do crédito
					for( int i = 0; i<clientList.size(); i++) {
						System.out.println("<ID: " + clientList.get(i).getId() +"> Name: "+ clientList.get(i).getName() +  ", Créditos: " + clientList.get(i).getCredit() + "€");
					}
					response.close();
					break;

				case 8: //PAYMENTS PER CLIENT
					target = client.target("http://localhost:8080/rest/services/myservice/getClients");
					response = target.request().get();
					List<data.Client>  clientListP= response.readEntity(new GenericType<List<data.Client>>(){});

					//IMPRIME NOMES do id e do pagamento
					for( int i = 0; i<clientListP.size(); i++) {
						System.out.println("<ID: " + clientListP.get(i).getId() +"> Name: "+ clientListP.get(i).getName() +  ", Pagamentos: " + clientListP.get(i).getPayment() + "€");
					}
					response.close();
					break;

				case 9:
					// BALANÇO DE UM CLIENTE
					target = client.target("http://localhost:8080/rest/services/myservice/getClients");
					response = target.request().get();
					List<data.Client> clientListB= response.readEntity(new GenericType<List<data.Client>>(){});

					//IMPRIME NOMES DOS CLIENTES
					for( int i = 0; i<clientListB.size(); i++){
						System.out.println("<ID: " + clientListB.get(i).getId() + "> Name: " + clientListB.get(i).getName());
					}

					//ESCOLHE O CLIENTE
					System.out.println("SELECT THE CLIENT ID");
					int t = scan.nextInt();
					data.Client clientSelected = clientListB.get(t);

					System.out.println("<ID: " + clientSelected.getId() +"> Name: "+ clientSelected.getName() +  ", Balance: " + clientSelected.getBalance() + "€");
					response.close();
					break;

				case 10: //TOTAL SUM OF CREDITS

					target = client.target("http://localhost:8080/rest/services/myservice/getTotals");
					response = target.request().get();
					Results credits = response.readEntity(new GenericType<Results>(){});

					System.out.println("Total Credits: " + credits.getTotalCredits() + "€");
					response.close();
					break;
				case 11: //TOTAL SUM OF PAYMENTS

					target = client.target("http://localhost:8080/rest/services/myservice/getTotals");
					response = target.request().get();
					Results payments = response.readEntity(new GenericType<Results>(){});

					System.out.println("Total Payments: " + payments.getTotalPayments() + "€");
					response.close();
					break;

				case 12: //TOTAL SUM OF BALANCE

					target = client.target("http://localhost:8080/rest/services/myservice/getTotals");
					response = target.request().get();
					Results totalbalance = response.readEntity(new GenericType<Results>(){});

					System.out.println("Total Balance: " + totalbalance.getTotalBalance() + "€");
					response.close();
					break;

				case 13: //BILL (credits??) FOR EACH CLIENT IN LAST MONTH

					break;
				case 14: // LIST OF CLIENTS WITHOUT PAYMENT IN 2 MONTHS

					break;
				case 15: //PERSON WITH MORE DEBT - BIGGER NEGATIVE VALUE IN BALANCE

					break;

				case 16: //MANAGER WHO MADE HIGHEST REVENUE IN PAYMENTS

					break;
			}
		}
	}
}
