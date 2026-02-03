package deliveryTrio.YaronProjecton;

import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryPersonNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.services.DeliveryManager;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping("/ShowDeliveryPersons")
    public String showDeliverers(Model model) throws Exception
    {
        model.addAttribute("username", "User");
        model.addAttribute("title", "deliverers");
        model.addAttribute("tbData", DlvManager.getDeliveryPersons());
        return "delivery-persons";
    }

    @RequestMapping("/ShowDeliveries")
    public String showDeliveries(Model model) throws Exception
    {
        model.addAttribute("username", "User");
        model.addAttribute("title", "deliveries");

        model.addAttribute("tbData", DlvManager.getDeliveries());
        model.addAttribute("dlvNum", "");

        return "deliveries";
    }

    @RequestMapping("/addDelivery")
    public String addDelivery(Model model) throws Exception
    {
        Delivery delivery = new Delivery();
        model.addAttribute("delivery", delivery);
        model.addAttribute("action", "added");
        return "add-delivery";
    }
    @RequestMapping("/processDelivery")
    public String processDelivery(@Valid @ModelAttribute("delivery") Delivery delivery, BindingResult bindingResult, Model model) throws Exception
    {
        System.out.println(delivery);
        if (bindingResult.hasErrors()) {
            return "add-delivery";
        }
        try {
            if (delivery.getRefId() != 0)
                delivery.setRef(DlvManager.getDeliveryPerson(delivery.getRefId()));
            DlvManager.addDelivery(delivery);
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
            model.addAttribute("delivery", delivery);
            model.addAttribute("action", "updated");
            DlvManager.delivered(id);
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
        try {
            Delivery delivery = DlvManager.getDelivery(id);
            DlvManager.delivered(id);
            model.addAttribute("delivery", delivery);
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
        model.addAttribute("deliverer", delper);
        model.addAttribute("action", "added");
        return "add-delivery-person";
    }
    @RequestMapping("/processDeliveryPerson")
    public String processDeliveryPerson(@Valid @ModelAttribute("deliverer") DeliveryPerson delper, BindingResult bindingResult, Model model) throws Exception
    {
        if (bindingResult.hasErrors()) {
            return "add-delivery-person";
        }

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
    public String modifyDeliverer(@Valid @RequestAttribute("id") int id, Model model)
    {
        return "success";
    }
    @RequestMapping("/showAllDeliveries")
    public String showAllDeliveries(@Valid @RequestAttribute("id") int id, Model model)
    {
        return "success";
    }
    @RequestMapping("/fire")
    public String fireDeliverer(@Valid @RequestAttribute("id") int id, Model model)
    {
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
