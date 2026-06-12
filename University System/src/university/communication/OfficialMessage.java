package university.communication;
import university.users.Employee;

public class OfficialMessage extends Message{
    private String header;
    private String digitalSignature;
    public OfficialMessage(String header, String text, Employee sender, Employee receiver){
        super(text, sender, receiver);
        this.header = header;
        this.digitalSignature = "SIG-" + sender.getId() + "-" + System.currentTimeMillis();
    }
    public String getHeader() { 
    	return header; }
    public String getDigitalSignature() { 
    	return digitalSignature; }

    @Override
    public String toString() {
        return "OfficialMessage{header=" + header + ", from=" + getSender().getName() + ", to=" + getReceiver().getName() + ", signature=" + digitalSignature + "}";
    }
}
