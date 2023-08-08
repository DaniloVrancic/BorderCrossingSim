package passengers;

/**
 * Identification class serves as a placeholder for the information that 
 * every passenger will carry with him.
 * Should be initialized and populated with information only 
 * inside the 'Passenger' class.
 *
 * 
 */
public class Identification {
    private String fullName;
    private String gender;
    private String passportNumber;
    private String nationality;

    // Constants for gender
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";

    public Identification() {
        fullName = "";
        gender = "";
        passportNumber = "";
        nationality = "";
    }

    public Identification(String fullName, String gender, String passportNumber, String nationality) {
        this.fullName = fullName;
        this.gender = gender;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("----------\tIDENTIFICATION\t----------\n");
        sb.append("Name: " + this.fullName + "\n");
        sb.append("Gender: " + this.gender + "\n");
        sb.append("Passport Number: " + this.passportNumber + "\n");
        sb.append("Nationality: " + this.nationality + "\n");
        sb.append("----------------------------------------");

        return sb.toString();
    }
}
