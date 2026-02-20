package deliveryTrio.YaronProjecton.conrollers;

import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.exceptions.UnfinishedDutyException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.services.DeliveryManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class DeliveryPersonController {
    private final DeliveryManager DlvManager;
    public DeliveryPersonController(DeliveryManager DM) {this.DlvManager = DM;}

    @RequestMapping("/ShowDeliveryPersons")
    public String showDeliverers(Model model, HttpSession session)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        model.addAttribute("title", "deliverers");
        try {
            model.addAttribute("tbData", DlvManager.getDeliveryPersons());
        } catch (NotFoundException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "delivery-persons";
    }

    @RequestMapping("/addDeliveryPerson")
    public String addDeliveryPerson(HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        DeliveryPerson delper = new DeliveryPerson();
        model.addAttribute("status", "add");
        model.addAttribute("deliverer", delper);
        model.addAttribute("action", "added");
        return "add-delivery-person";
    }

    @RequestMapping("/processDeliveryPerson")
    public String processDeliveryPerson(@RequestParam("status") String status, @Valid @ModelAttribute("deliverer") DeliveryPerson delper, BindingResult bindingResult, HttpSession session, Model model) throws Exception
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("status", status);
            return "add-delivery-person";
        }
        System.out.println(delper);
        if(status.equals("mod"))
        {
            int id = delper.getID();
            DlvManager.modifyDeliveryPerson(id, "name", delper.getName());
            DlvManager.modifyDeliveryPerson(id, "max capacity", ((Integer)delper.getDeliveryMaxCapacity()).toString());
            DlvManager.modifyDeliveryPerson(id, "city", delper.getCity());
        }
        else
            DlvManager.addDeliveryPerson(delper);

        model.addAttribute("deliverer", delper);
        model.addAttribute("type", "deliverer");
        model.addAttribute("action", (status.equals("mod")) ? "modified" : "added");
        return "success";
    }

    @RequestMapping("/modifyDeliveryPerson")
    public String modifyDeliverer(@RequestParam("id") int id, HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

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
    public String showDeliveriesOf(@RequestParam("id") int id, HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

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
        model.addAttribute("id", id);
        model.addAttribute("tbData", col);
        model.addAttribute("dlvNum", col.size());

        return "deliveries";
    }

    @RequestMapping("/fire")
    public String fireDeliverer(@RequestParam("id") int id, HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

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
        delper = new DeliveryPerson(name,city,0);
        model.addAttribute("type", "deliverer");
        model.addAttribute("deliverer", delper);
        model.addAttribute("action", "fired");

        return "success";
    }
}
