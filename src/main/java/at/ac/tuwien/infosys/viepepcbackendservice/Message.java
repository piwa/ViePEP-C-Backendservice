package at.ac.tuwien.infosys.viepepcbackendservice;

import java.io.Serializable;

public class Message implements Serializable {

    private String processStepName;
    private ServiceExecutionStatus status;
    private String body;

    public Message() {
    }

    public Message(String processStepName, ServiceExecutionStatus status, String body) {
        this.processStepName = processStepName;
        this.status = status;
        this.body = body;
    }

    public String getProcessStepName() {
        return processStepName;
    }

    public void setProcessStepName(String processStepName) {
        this.processStepName = processStepName;
    }

    public ServiceExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceExecutionStatus status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "processStepName=" + processStepName +
                ", status=" + status +
                ", body='" + body + '\'' +
                '}';
    }
}
