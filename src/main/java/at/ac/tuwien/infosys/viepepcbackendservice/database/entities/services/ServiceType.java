package at.ac.tuwien.infosys.viepepcbackendservice.database.entities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philippwaibel on 18/10/2016.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name="ServiceType")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceType {

    private Long id;

    @XmlElement
    private String name;
    @XmlElement
    private boolean onlyInternal;
    @XmlElement(name = "serviceTypeResources")

    private ServiceTypeResources serviceTypeResources;


    private Map<DateTime, ServiceTypeResources> monitoredServiceTypeResources = new HashMap<>();

    public void addMonitoredServiceTypeResourceInformation(ServiceTypeResources serviceTypeResources) {
        monitoredServiceTypeResources.put(new DateTime(), serviceTypeResources);
    }

    public static ServiceType fromValue(String serviceType) {
        ServiceType serviceType1 = new ServiceType();
        serviceType1.name = serviceType;
        return serviceType1;
    }

    @Override
    public String toString() {
        return "ServiceType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", onlyInternal=" + onlyInternal +
//                ", serviceTypeResources=" + serviceTypeResources +
//                ", monitoredServiceTypeResources=" + monitoredServiceTypeResources +
                '}';
    }
}
