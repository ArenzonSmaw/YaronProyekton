package deliveryTrio.YaronProjecton.conrollers;

import deliveryTrio.YaronProjecton.entities.User;
import deliveryTrio.YaronProjecton.services.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SecurityController {

	private final SecurityService security;

	public SecurityController(SecurityService security) {
		this.security = security;
	}

    @GetMapping("/")
    public String startPoint(){return "redirect:/login";}

	@GetMapping("/login")
	public String showLoginForm(@ModelAttribute("user") User user) {
		return "login-page";
	}

	@PostMapping("/processLogin")
	public String processLogin(@ModelAttribute("user") User user,
							   HttpServletRequest request,
							   RedirectAttributes redirectAttributes) {

		if (security.login(user)) {
			// protection against Session Fixation attck
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate();
			}

			HttpSession newSession = request.getSession(true);
			newSession.setAttribute("user", user);

			return "redirect:/ShowDeliveries";
		}

		redirectAttributes.addFlashAttribute("message", "Username or password incorrect");
		return "redirect:/login";
	}

	@GetMapping("/showMainScreen")
	public String showMainScreen() {
		return "main-screen";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login";
	}
}