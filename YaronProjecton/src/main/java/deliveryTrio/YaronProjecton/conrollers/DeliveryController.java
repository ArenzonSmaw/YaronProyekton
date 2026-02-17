/*package deliveryTrio.YaronProjecton.conrollers;

import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeliveryController extends DeliveryWebController{

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
}*/
