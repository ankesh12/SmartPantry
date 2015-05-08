package sg.edu.nus.iss.smartpantry.controller;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class ControlFactory {
    //private MainController mainController;
    private ItemController itemController;

    public ControlFactory(){
        //mainController = new MainController();
        itemController = new ItemController();
    }
    //Returns the instance of the MainController
    public MainController getMainController(){
        return (new MainController());
    }
    //Returns the instance of ItemController
    public ItemController getItemController(){
        return itemController;
    }
}
