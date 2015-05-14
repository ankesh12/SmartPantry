package sg.edu.nus.iss.smartpantry.CustomException;

/**
 * Created by A0134493A on 14/5/2015.
 */
public class ItemNotFoundException extends Exception {

    public ItemNotFoundException(){}

    public ItemNotFoundException(String message){
        super(message);
    }
}
