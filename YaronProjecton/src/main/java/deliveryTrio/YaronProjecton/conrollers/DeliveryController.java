package deliveryTrio.YaronProjecton.conrollers;

import deliveryTrio.YaronProjecton.dataAccess.delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.services.DeliveryManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeliveryController{
    private final DeliveryManager DlvManager;
    public DeliveryController(DeliveryManager DM) {this.DlvManager = DM;}

    @RequestMapping("/ShowDeliveries")
    public String showDeliveries(HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

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
        model.addAttribute("id", 0);
        return "deliveries";
    }

    @RequestMapping("/addDelivery")
    public String addDelivery(HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        Delivery delivery = new Delivery();
        model.addAttribute("status", "add");
        model.addAttribute("delivery", delivery);
        model.addAttribute("action", "added");
        return "add-delivery";
    }

    @RequestMapping("/processDelivery")
    public String processDelivery( @RequestParam("status") String status, @Valid @ModelAttribute("delivery") Delivery delivery, BindingResult bindingResult, HttpSession session, Model model) throws Exception
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        System.out.println(delivery);
        if (bindingResult.hasErrors()) {
            model.addAttribute("status", status);
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
            model.addAttribute("note", "There are no available delivery persons in " + delivery.getDestination() +". Delivery is currently on hold.");
        }

        model.addAttribute("delivery", delivery);
        model.addAttribute("type", "delivery");
        model.addAttribute("action", (status.equals("mod") ? "modified" : "added"));
        return "success";
    }

    @RequestMapping("/modifyDelivery")
    public String modifyDelivery(@RequestParam("dlvNum") int id, HttpSession session, Model model) throws Exception
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

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
    public String reportDelivered(@RequestParam("dlvNum") int id, HttpSession session, Model model) throws Exception
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        model.addAttribute("type", "delivery");
        Delivery delivery = null;
        int delNum;
        try {
            delivery = DlvManager.getDelivery(id);
            DlvManager.delivered(id);
        } catch (NotFoundException e) {
            model.addAttribute("errorTitle", "Delivery Not Found");
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
        model.addAttribute("delivery" , delivery);
        model.addAttribute("action", "reported delivered");
        return "success";
    }
    @RequestMapping("/RefreshDeliveryAssignments")
    public String refresh(@RequestParam("id") int id, HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        DlvManager.assignForgottenDeliveries();
        if (id == 0)
            return "redirect:/ShowDeliveries";
        return "redirect:/showAllDeliveries?id="+id;
    }
}
