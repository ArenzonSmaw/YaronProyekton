package deliveryTrio.YaronProjecton;

import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Exceptions.InvalidInputException;
import deliveryTrio.YaronProjecton.Services.DataAccess.DataAccessObject;
import deliveryTrio.YaronProjecton.Services.DeliveryManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;



@SpringBootApplication
public class YaronProjectonApplication {

    public enum Option{
        ADD_DELIVERY_PERSON,
        ADD_DELIVERY,
        STOP
    }

	public static void main(String[] args) {
        var context=SpringApplication.run(YaronProjectonApplication.class, args);
        DeliveryManager mng = context.getBean("manager", DeliveryManager.class);
        Scanner scanner = new Scanner(System.in);

        Boolean running = true;

        while (running){
            Option selected = null;
            try{
                selected = ShowMenu();
            } catch (InvalidInputException e){
                System.out.println(e.getMessage());
                continue;
            }

            switch (selected){
                case ADD_DELIVERY_PERSON:
                    System.out.println("Enter name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter city:");
                    String city = scanner.nextLine();
                    if (mng.AddDeliveryPerson(name, city)){
                        System.out.println("Successfully added the delivery person '" + name + "'.");
                    }
                    break;
                case ADD_DELIVERY:
                    System.out.println("Enter delivery weight:");
                    double weight = scanner.nextDouble();
                    System.out.println("Enter destination city:");
                    String destinationCity = scanner.nextLine();
                    System.out.println("Enter customer ID:");
                    String customerID = scanner.nextLine();
//                    if (mng.AddDelivery(weight, destinationCity, customerID)){
//
//                    }

                    break;
                case STOP:
                    running = false;
                    break;
            }
        }

    }

    public static Option ShowMenu() throws InvalidInputException{
        System.out.println("----------------------------------------------");
        System.out.println("Choose an option by typing a number:");
        System.out.println("1. Add a delivery person to the system.");
        System.out.println("2. Add a delivery to the system and assign a delivery person to it.");
        System.out.println("0. Stop the system.");

        return ReceiveChoice();
    }

    public static Option ReceiveChoice() throws InvalidInputException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Option selected = null;
        switch (input){
            case "1":
                selected = Option.ADD_DELIVERY_PERSON;
                break;
            case "2":
                selected = Option.ADD_DELIVERY;
                break;
            case "0":
                selected = Option.STOP;
            default:
                throw new InvalidInputException(input);
        }
        return selected;
    }

}
