package deliveryTrio.YaronProjecton;

import deliveryTrio.YaronProjecton.Entities.Delivery;
import deliveryTrio.YaronProjecton.Entities.DeliveryPerson;
import deliveryTrio.YaronProjecton.Exceptions.NoAvailableDeliveryPersonException;
import deliveryTrio.YaronProjecton.Services.DataAccess.DataAccessObject;
import deliveryTrio.YaronProjecton.Services.DataAccess.IDAO;
import deliveryTrio.YaronProjecton.Services.Delivery.DeliveryCollection;
import deliveryTrio.YaronProjecton.Services.DeliveryPerson.DeliveryPersonCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class YaronProjectonApplication {

	public static void main(String[] args) {
        var context=SpringApplication.run(YaronProjectonApplication.class, args);
        DataAccessObject dao=context.getBean("dataAccessObject", DataAccessObject.class);

        DeliveryManager mng = context.getBean("manager", DeliveryManager.class);

        mng.AddDeliveryPerson("Lior","BTY");
        try {
            mng.AddDelivery(12, "BTY", "1");
            mng.AddDelivery(10, "TLV", "5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
