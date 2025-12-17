package deliveryTrio.YaronProjecton.Services;

import deliveryTrio.YaronProjecton.Services.DataAccess.DataAccessObject;
import org.springframework.stereotype.Service;


@Service
public class DeliveryManager {
    private DataAccessObject dao;
    public DeliveryManager(DataAccessObject accessObj) {
        this.dao = accessObj;
    }


}
