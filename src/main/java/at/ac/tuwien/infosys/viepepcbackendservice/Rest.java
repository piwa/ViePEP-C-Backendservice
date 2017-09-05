package at.ac.tuwien.infosys.viepepcbackendservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class Rest {

    @Autowired
    private ServiceExecutor serviceExecutor;

    @RequestMapping(value = "/{task}/{name}/{normal}/{data}/{mqIp}", method = RequestMethod.GET)
    public String invokeService(@PathVariable("task") int task, @PathVariable("name") String name, @PathVariable("normal") String plainModifier, @PathVariable("data") String dataModifier, @PathVariable("mqIp") String messageBusIp) throws IOException, InterruptedException {

        messageBusIp = messageBusIp.replace("_", ".");

        serviceExecutor.execute(task, name, plainModifier, dataModifier, messageBusIp);

        return "done";
    }


}