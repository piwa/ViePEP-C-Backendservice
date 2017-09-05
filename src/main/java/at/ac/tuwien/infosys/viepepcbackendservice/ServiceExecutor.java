
package at.ac.tuwien.infosys.viepepcbackendservice;

import at.ac.tuwien.infosys.viepepcbackendservice.database.entities.services.ServiceType;
import at.ac.tuwien.infosys.viepepcbackendservice.registry.ServiceRegistryReader;
import at.ac.tuwien.infosys.viepepcbackendservice.registry.impl.service.ServiceTypeNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martensigwart.fakeload.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ServiceExecutor {

    @Autowired
    private ServiceRegistryReader serviceRegistryReader;

    private final String queueName =  "viepepc.watchdog";

    private int task;
    private String processStepName;
    private String plainModifier;
    private String dataModifier;
    private String messageBusIp;

    @Async
    public void execute(int task, String processStepName, String plainModifier, String dataModifier, String messageBusIp) {

        try {


            ServiceType serviceType = serviceRegistryReader.findServiceType(task);

            UUID taskId = UUID.randomUUID();

            log.info("Start task with ID: " + taskId);


//            TimeUnit.MILLISECONDS.sleep(serviceType.getServiceTypeResources().getMakeSpan());

            double cores = (double) Runtime.getRuntime().availableProcessors() * 100;
            double absoluteCpuLoad = (double) serviceType.getServiceTypeResources().getCpuLoad();
            int cpuLoad = new Double(absoluteCpuLoad / cores * 100).intValue();

            log.info("cores: " + cores + ", absoluteCpuLoad: " + absoluteCpuLoad + ", cpuLoad: " + cpuLoad);

            FakeLoad fakeload = new FakeLoadBuilder()
                    .lasting(serviceType.getServiceTypeResources().getMakeSpan(), TimeUnit.MILLISECONDS)
                    .withCpu(cpuLoad)
                    .build();

            FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();
            executor.execute(fakeload);

            String result = "";

            if(!dataModifier.equals("nodata")) {
                if (serviceType.getServiceTypeResources().getDataToTransfer() == 0) {
                    result = "Hurray!";
                } else {
                    String dummytext5kb = "A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so happy, my dear friend, so absorbed in the exquisite sense of mere tranquil existence, that I neglect my talents. I should be incapable of drawing a single stroke at the present moment; and yet I feel that I never was a greater artist than now. When, while the lovely valley teems with vapour around me, and the meridian sun strikes the upper surface of the impenetrable foliage of my trees, and but a few stray gleams steal into the inner sanctuary, I throw myself down among the tall grass by the trickling stream; and, as I lie close to the earth, a thousand unknown plants are noticed by me: when I hear the buzz of the little world among the stalks, and grow familiar with the countless indescribable forms of the insects and flies, then I feel the presence of the Almighty, who formed us in his own image, and the breath of that universal love which bears and sustains us, as it floats around us in an eternity of bliss; and then, my friend, when darkness overspreads my eyes, and heaven and earth seem to dwell in my soul and absorb its power, like the form of a beloved mistress, then I often think with longing, Oh, would I could describe these conceptions, could impress upon paper all that is living so full and warm within me, that it might be the mirror of my soul, as my soul is the mirror of the infinite God! O my friend -- but it is too much for my strength -- I sink under the weight of the splendour of these visions! A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so happy, my dear friend, so absorbed in the exquisite sense of mere tranquil existence, that I neglect my talents. I should be incapable of drawing a single stroke at the present moment; and yet I feel that I never was a greater artist than now. When, while the lovely valley teems with vapour around me, and the meridian sun strikes the upper surface of the impenetrable foliage of my trees, and but a few stray gleams steal into the inner sanctuary, I throw myself down among the tall grass by the trickling stream; and, as I lie close to the earth, a thousand unknown plants are noticed by me: when I hear the buzz of the little world among the stalks, and grow familiar with the countless indescribable forms of the insects and flies, then I feel the presence of the Almighty, who formed us in his own image, and the breath of that universal love which bears and sustains us, as it floats around us in an eternity of bliss; and then, my friend, when darkness overspreads my eyes, and heaven and earth seem to dwell in my soul and absorb its power, like the form of a beloved mistress, then I often think with longing, Oh, would I could describe these conceptions, could impress upon paper all that is living so full and warm within me, that it might be the mirror of my soul, as my soul is the mirror of the infinite God! O my friend -- but it is too much for my strength -- I sink under the weight of the splendour of these visions!A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so happy, my dear friend, so absorbed in the exquisite sense of mere tranquil existence, that I neglect my talents. I should be incapable of drawing a single stroke at the present moment; and yet I feel that I never was a greater artist than now. When, while the lovely valley teems with vapour around me, and the meridian sun strikes the upper surface of the impenetrable foliage of my trees, and but a few stray gleams steal into the inner sanctuary, I throw myself down among the tall grass by the trickling stream; and, as I lie close to the earth, a thousand unknown plants are noticed by me: when I hear the buzz of the little world among the stalks, and grow familiar with the countless indescribable forms of the insects and flies, then I feel the presence of the Almighty, who formed us in his own image, and the breath of that universal love which bears and sustains us, as it floats around us in an eternity of bliss; and then, my friend, when darkness overspreads my eyes, and heaven and earth seem to dwell in my soul and absorb its power, like the form of a beloved mistress, then I often think with longing, Oh, would I could describe these conceptions,\n";
                    //200 = ~1MB
                    for (int i = 0; i < (serviceType.getServiceTypeResources().getDataToTransfer() * 200); i++) {
                        result = result + dummytext5kb;
                    }
                }
            }
            else {
                result = "done";
            }
            log.info("Finished task with ID: " + taskId);

            sendMessage(processStepName, result, messageBusIp);

        } catch (ServiceTypeNotFoundException e) {
            log.error("EXCEPTION", e);
        }
    }


    public void sendMessage(String processStepName, String result, String messageBusIp) {

        Connection connection = null;
        Channel channel = null;

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(messageBusIp);
            factory.setUsername("viepep");
            factory.setPassword("viepep");
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);


            Message msg = new Message();
            msg.setBody(result);
            msg.setProcessStepName(processStepName);
            msg.setStatus(ServiceExecutionStatus.DONE);

            ObjectMapper mapper = new ObjectMapper();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            mapper.writeValue(out, msg);

            channel.basicPublish("", queueName, null, out.toByteArray());
            System.out.println("JSON-Message sent!");



        } catch (IOException | TimeoutException e) {
            log.error("EXCEPTION", e);
        }
        finally {
            if(channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e1) {
                    log.error("EXCEPTION", e1);
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (IOException e1) {
                    log.error("EXCEPTION", e1);
                }
            }
        }

    }


}
