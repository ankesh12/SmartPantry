package sg.edu.nus.iss.smartpantry.controller;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class ControlFactory {

    private ItemController itemController = new ItemController();
    private MainController mainController = new MainController();

    //singleton object creation
    private static ControlFactory controlFactory = new ControlFactory();

    public static ControlFactory getInstance(){

        return controlFactory;
    }

    //Returns the instance of the MainController
    public MainController getMainController(){
        return mainController;
    }

    //Returns the instance of ItemController
    public ItemController getItemController(){
        return itemController;
    }


}
