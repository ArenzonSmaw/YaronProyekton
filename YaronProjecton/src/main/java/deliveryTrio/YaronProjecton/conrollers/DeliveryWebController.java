package deliveryTrio.YaronProjecton.conrollers;

import deliveryTrio.YaronProjecton.dataAccess.delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.exceptions.UnfinishedDutyException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.services.DeliveryManager;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@ControllerAdvice
public class DeliveryWebController {
    private final DeliveryManager DlvManager;
    public DeliveryWebController(DeliveryManager mng)
    {
        this.DlvManager = mng;
    }
    /*
    NEED:   add httpsession obj
            login page -> https session
            and add httpsession obj to each mapping for attributes
     */

    @ExceptionHandler(Exception.class)
    public String notFoundHandler(Model model, Exception e)
    {
        model.addAttribute("errorTitle", e.getClass());
        model.addAttribute("errorMessage", e.getMessage());
        e.printStackTrace();

        return "error";
    }
    /*@ExceptionHandler(Exception.class)
    public String weirdExceptionHandler(Model model, Exception e)
    {
        model.addAttribute("errorTitle", e.getClass());
        model.addAttribute("errorMessage", e.getMessage());

        return "error";
    }*/

    @RequestMapping("/")
    public String homePage()
    {
        return "redirect:ShowDeliveries";
    }
    @RequestMapping("/ShowDeliveryPersons")
    public String showDeliverers(Model model)
    {
        model.addAttribute("username", "User");
        model.addAttribute("title", "deliverers");
        try {
            model.addAttribute("tbData", DlvManager.getDeliveryPersons());
        } catch (NotFoundException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "delivery-persons";
    }

    @RequestMapping("/ShowDeliveries")
    public String showDeliveries(Model model)
    {
        model.addAttribute("username", "User");
        model.addAttribute("title", "deliveries");
        model.addAttribute("header", "All Deliveries");
        DeliveryCollection deliveries;
        try {
            deliveries = DlvManager.getDeliveries();
            if (deliveries != null) {
                model.addAttribute("tbData", deliveries);
                model.addAttribute("dlvNum", deliveries.getList().size());
            }
        } catch (NotFoundException e) {
            model.addAttribute("message", "There are no deliveries yet");
        }

        return "deliveries";
    }

    @RequestMapping("/addDelivery")
    public String addDelivery(Model model) throws Exception
    {
        Delivery delivery = new Delivery();
        model.addAttribute("status", "add");
        model.addAttribute("delivery", delivery);
        model.addAttribute("action", "added");
        return "add-delivery";
    }
    @RequestMapping("/processDelivery")
    public String processDelivery(@Valid @ModelAttribute("delivery") Delivery delivery, @RequestParam("status") String status, BindingResult bindingResult, Model model) throws Exception
    {
        System.out.println(delivery);
        if (bindingResult.hasErrors()) {
            return "add-delivery";
        }
        try {
            System.out.println(status);
            if (delivery.getRefId() != 0)
                delivery.setRef(DlvManager.getDeliveryPerson(delivery.getRefId()));
            if (status.equals("mod")) {
                DlvManager.modifyDelivery(delivery.getDeliveryNo(), "destination", delivery.getDestination());
                DlvManager.modifyDelivery(delivery.getDeliveryNo(), "weight", ((Double)delivery.getWeight()).toString());
                DlvManager.modifyDelivery(delivery.getDeliveryNo(), "customerID", delivery.getCustomerID());
            }
            else {
                DlvManager.addDelivery(delivery);
                System.out.println("added");
            }
        } catch(NoAvailableDeliveryPersonException e)
        {
            System.out.println("caught");
            model.addAttribute("note", "There are no available delivery persons in " + delivery.getDestination() +". Delivery is currently on hold.");
        }
        model.addAttribute("delivery", delivery);
        model.addAttribute("type", "delivery");
        model.addAttribute("action", model.getAttribute("action"));
        return "success";
    }

    @RequestMapping("/modifyDelivery")
    public String modifyDelivery(@RequestParam("dlvNum") int id, Model model) throws Exception
    {
        try {
            Delivery delivery = DlvManager.getDelivery(id);
            model.addAttribute("status", "mod");
            model.addAttribute("delivery", delivery);
            model.addAttribute("action", "updated");
            return "add-delivery";
        } catch (DeliveryNotFoundException e)
        {
            model.addAttribute("errorTitle", "Delivery Not Found");
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @RequestMapping("/delivered")
    public String reportDelivered(@RequestParam("dlvNum") int id, Model model) throws Exception
    {
        model.addAttribute("type", "delivery");
        String dest;
        int delNum;
        try {
            Delivery delivery = DlvManager.getDelivery(id);
            dest = delivery.getDestination();
            delNum = delivery.getDeliveryNo();
            DlvManager.delivered(id);
            model.addAttribute("deliveryNo", delNum);
            model.addAttribute("destination", dest);
            model.addAttribute("action", "reported delivered");
            return "success";
        } catch (NotFoundException e) {
            model.addAttribute("errorTitle", "Delivery Not Found");
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @RequestMapping("/addDeliveryPerson")
    public String addDeliveryPerson(Model model)
    {
        DeliveryPerson delper = new DeliveryPerson();
        model.addAttribute("status", "add");
        model.addAttribute("deliverer", delper);
        model.addAttribute("action", "added");
        return "add-delivery-person";
    }
    @RequestMapping("/processDeliveryPerson")
    public String processDeliveryPerson(@Valid @ModelAttribute("deliverer") DeliveryPerson delper, @RequestParam("status") String status ,BindingResult bindingResult, Model model) throws Exception
    {
        if (bindingResult.hasErrors()) {
            return "add-delivery-person";
        }
        if(status.equals("mod"))
        {
            int id = delper.getID();
            System.out.println(id);
            DlvManager.modifyDeliveryPerson(id, "name", delper.getName());
            DlvManager.modifyDeliveryPerson(id, "max capacity", ((Integer)delper.getDeliveryMaxCapacity()).toString());
            DlvManager.modifyDeliveryPerson(id, "city", delper.getCity());
        }
        else
            DlvManager.addDeliveryPerson(delper);

        model.addAttribute("deliverer", delper);
        model.addAttribute("type", "deliverer");
        model.addAttribute("action", model.getAttribute("action"));
        return "success";
    }

    @RequestMapping("/success")
    public String success(Model model)
    {
        return "success";
    }

    @RequestMapping("/modifyDeliveryPerson")
    public String modifyDeliverer(@RequestParam("id") int id, Model model)
    {
        DeliveryPerson delper = null;
        try {
            delper = DlvManager.getDeliveryPerson(id);
        } catch(NotFoundException e) {
            model.addAttribute("errorTitle", "Not Found");
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
        model.addAttribute("status", "mod");
        model.addAttribute("deliverer", delper);
        return "add-delivery-person";
    }
    @RequestMapping("/showAllDeliveries")
    public String showAllDeliveries(@RequestParam("id") int id, Model model)
    {
        try {
            model.addAttribute("header", "Deliveries of " + DlvManager.getDeliveryPerson(id).getName());
        } catch (NotFoundException e)
        {
            model.addAttribute("errorTitle", "Not Found");
            model.addAttribute("errorMessage", "person not found");
            return "error";
        }
        ArrayList<Delivery> col = null;
        try {
            col = DlvManager.getDeliveriesOfPreson(id);
        } catch (NotFoundException e) {
            model.addAttribute("message", "delivery person has no deliveries");
            return "deliveries";
        }
        model.addAttribute("tbData", col);
        model.addAttribute("dlvNum", col.size());

        return "deliveries";
    }
    @RequestMapping("/fire")
    public String fireDeliverer(@RequestParam("id") int id, Model model)
    {
        DeliveryPerson delper;
        String name, city;
        try {
            delper = DlvManager.getDeliveryPerson(id);
            name = delper.getName();
            city = delper.getCity();
            DlvManager.fireDeliveryPerson(id);
        } catch (UnfinishedDutyException e) {
            model.addAttribute("errorTitle", "Cannot Fire Delivery Person");
            model.addAttribute("errorMessage", "Delivery person has unfinished delivery business.");
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorTitle", "Couldn't Fire Delivery Person ");
            model.addAttribute("errorMessage", "error message: " + e.getMessage());
            return "error";
        }
        model.addAttribute("type", "deliverer");
        model.addAttribute("name", name);
        model.addAttribute("city", city);
        model.addAttribute("action", "fired");
        
        return "success";
    }


    @RequestMapping("/login")
    public String loginForm(Model model)
    {
        model.addAttribute("username", "Lior");
        model.addAttribute("title", "login");

        return "login";
    }
}
