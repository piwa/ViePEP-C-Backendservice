package at.ac.tuwien.infosys.viepepcbackendservice.registry;

import at.ac.tuwien.infosys.viepepcbackendservice.database.entities.services.ServiceType;
import at.ac.tuwien.infosys.viepepcbackendservice.registry.impl.service.ServiceTypeNotFoundException;

/**
 * Created by philippwaibel on 18/10/2016.
 */
public interface ServiceRegistryReader {
    ServiceType findServiceType(int serviceTypeNumber) throws ServiceTypeNotFoundException;

    ServiceType findServiceType(String serviceTypeName) throws ServiceTypeNotFoundException;

    int getServiceTypeAmount();
}
