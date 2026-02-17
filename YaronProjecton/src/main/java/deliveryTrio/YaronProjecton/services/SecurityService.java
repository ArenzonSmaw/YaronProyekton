package deliveryTrio.YaronProjecton.services;

import deliveryTrio.YaronProjecton.entities.User;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public boolean login(User user){
        return "hatul".equals(user.getPassword());
    }
}
