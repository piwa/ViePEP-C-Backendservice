package at.ac.tuwien.infosys.viepepcbackendservice.database.entities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by philippwaibel on 19/10/2016.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name="ServiceTypeResources")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTypeResources {

    private Long id;

    @XmlElement
    private int cpuLoad;
    @XmlElement
    private int memory;
    @XmlElement
    private long makeSpan;
    @XmlElement
    private int dataToTransfer;
    @XmlElement
    private long bootTime;
    @XmlElement
    private int internPort;

}
