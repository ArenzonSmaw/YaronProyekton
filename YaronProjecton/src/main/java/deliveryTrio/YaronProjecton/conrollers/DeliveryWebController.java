package deliveryTrio.YaronProjecton.conrollers;

import deliveryTrio.YaronProjecton.dataAccess.delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.entities.Delivery;
import deliveryTrio.YaronProjecton.entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.exceptions.UnfinishedDutyException;
import deliveryTrio.YaronProjecton.exceptions.notFound.DeliveryNotFoundException;
import deliveryTrio.YaronProjecton.exceptions.notFound.NotFoundException;
import deliveryTrio.YaronProjecton.services.DeliveryManager;
import jakarta.servlet.http.HttpSession;
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

    @ExceptionHandler(NotFoundException.class)
    public String notFoundHandler(Model model, Exception e)
    {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", e.getMessage());

        return "error";
    }

    @ExceptionHandler(UnfinishedDutyException.class)
    public String unfinishedDutyHandler(Model model, Exception e)
    {
        model.addAttribute("errorTitle", "Unfinished Duty");
        model.addAttribute("errorMessage", e.getMessage());

        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String ExoticExceptionHandler(Model model, Exception e)
    {
        model.addAttribute("errorTitle", "Encountered an exception");
        model.addAttribute("errorMessage", e.getMessage());

        return "error";
    }

    @RequestMapping("/success")
    public String success(Model model)
    {
        return "success";
    }

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        return "main";
    }
}
