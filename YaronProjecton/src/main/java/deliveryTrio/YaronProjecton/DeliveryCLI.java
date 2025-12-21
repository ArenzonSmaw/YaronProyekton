package deliveryTrio.YaronProjecton;

import deliveryTrio.YaronProjecton.Entities.Delivery;
import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Exceptions.InvalidInputException;
import deliveryTrio.YaronProjecton.Services.Delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.Services.DeliveryManager;
import deliveryTrio.YaronProjecton.Services.DeliveryPerson.DeliveryPersonCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;



@SpringBootApplication
public class DeliveryCLI {

    public enum Option{
        ADD_DELIVERY_PERSON,
        ADD_DELIVERY,
        PRINT_DELIVERIES,
        PRINT_DELIVERY_PERSONS,
        SHOW_DELIVERY,
        SHOW_DELIVERY_PERSON,
        MODIFY_DELIVERY,
        MODIFY_DELIVERY_PERSON,
        FIRE_DELIVERY_PERSON,
        DELIVERED,
        STOP
    }

	public static void main(String[] args) {
        var context=SpringApplication.run(DeliveryCLI.class, args);
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

            try{
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
                        scanner.nextLine();
                        System.out.println("Enter destination city:");
                        String destinationCity = scanner.nextLine();
                        System.out.println("Enter customer ID:");
                        String customerID = scanner.nextLine();
                        if (mng.AddDelivery(weight, destinationCity, customerID)){
                            System.out.println("Successfuly added delivery.");
                        }
                        break;
                    case PRINT_DELIVERIES:
                        System.out.println("All deliveries:");
                        DeliveryCollection deliveries = mng.getDeliveries();
                        for(Delivery del : deliveries){
                            System.out.println(del.toString());
                        }
                        break;
                    case PRINT_DELIVERY_PERSONS:
                        System.out.println("All delivery persons:");
                        DeliveryPersonCollection deliveriePersons = mng.getDeliveryPersons();
                        for(DeliveryPerson delPer : deliveriePersons){
                            System.out.println(delPer.toString());
                        }
                        break;
                    case SHOW_DELIVERY:
                        System.out.println("Enter delivery number:");
                        int deliveryNum = scanner.nextInt();
                        scanner.nextLine();
                        Delivery del = mng.getDelivery(deliveryNum);
                        System.out.println(del.toString());
                        break;
                    case SHOW_DELIVERY_PERSON:
                        System.out.println("Enter delivery person ID:");
                        int deliveryPersonID = scanner.nextInt();
                        scanner.nextLine();
                        DeliveryPerson delPer = mng.getDeliveryPerson(deliveryPersonID);
                        System.out.println(delPer.toString());
                        break;
                    case MODIFY_DELIVERY:
                        System.out.println("Enter delivery number:");
                        deliveryNum = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter field name to modify:");
                        String deliveryField = scanner.nextLine();
                        System.out.println("Enter new value:");
                        String deliveryValue = scanner.nextLine();
                        mng.modifyDelivery(deliveryNum, deliveryField, deliveryValue);
                        System.out.println("Modified delivery number " + deliveryNum + ".");
                        break;
                    case MODIFY_DELIVERY_PERSON:
                        System.out.println("Enter delivery person ID:");
                        deliveryPersonID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter field name to modify:");
                        String deliveryPersonField = scanner.nextLine();
                        System.out.println("Enter new value:");
                        String deliveryPersonValue = scanner.nextLine();
                        mng.modifyDeliveryPerson(deliveryPersonID, deliveryPersonField, deliveryPersonValue);
                        System.out.println("Modified delivery person '" + deliveryPersonID + "'.");
                        break;
                    case FIRE_DELIVERY_PERSON:
                        System.out.println("Enter delivery person ID:");
                        deliveryPersonID = scanner.nextInt();
                        scanner.nextLine();
                        mng.fireDeliveryPerson(deliveryPersonID);
                        System.out.println("Fired delivery person '" + deliveryPersonID + "'.");
                        break;
                    case DELIVERED:
                        System.out.println("Enter delivery number:");
                        deliveryNum = scanner.nextInt();
                        scanner.nextLine();
                        mng.delivered(deliveryNum);
                        System.out.println("Marked delivery number " + deliveryNum + " as delivered.");
                        break;
                    case STOP:
                        running = false;
                        break;
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

            if (selected != Option.STOP){
                System.out.println("\nPress enter to continue.");
                scanner.nextLine();
            }
        }

    }

    public static Option ShowMenu() throws InvalidInputException{
        System.out.println("----------------------------------------------");
        System.out.println("Choose an option by typing a number:");
        System.out.println("1. Add a delivery person to the system.");
        System.out.println("2. Add a delivery to the system and assign a delivery person to it.");
        System.out.println("3. Print all deliveries.");
        System.out.println("4. Print all delivery persons.");
        System.out.println("5. Show a single delivery.");
        System.out.println("6. Show a single delivery person.");
        System.out.println("7. Modify a delivery.");
        System.out.println("8. Modify a delivery person.");
        System.out.println("9. Fire a delivery person and remove them from the system.");
        System.out.println("10. Report a delivery as delivered.");
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
            case "3":
                selected = Option.PRINT_DELIVERIES;
                break;
            case "4":
                selected = Option.PRINT_DELIVERY_PERSONS;
                break;
            case "5":
                selected = Option.SHOW_DELIVERY;
                break;
            case "6":
                selected = Option.SHOW_DELIVERY_PERSON;
                break;
            case "7":
                selected = Option.MODIFY_DELIVERY;
                break;
            case "8":
                selected = Option.MODIFY_DELIVERY_PERSON;
                break;
            case "9":
                selected = Option.FIRE_DELIVERY_PERSON;
                break;
            case "10":
                selected = Option.DELIVERED;
                break;
            case "0":
                selected = Option.STOP;
                break;
            default:
                throw new InvalidInputException(input);
        }
        return selected;
    }

}
