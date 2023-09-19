package custom_interfaces;


/**
 * By implementing Punishable interface we make sure that the Object can be used with our custom defined
 * Punishment method that also Serialize the vehicle once punished,
 * but also are able to retrieve info if the vehicle is deserialized.
 */
public interface Punishable {
	 public void setReasonOfPunishment(String reasonOfPunishment);
	 public String getReasonOfPunishment();
}
