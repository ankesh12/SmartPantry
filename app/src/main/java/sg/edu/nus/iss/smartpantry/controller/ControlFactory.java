package sg.edu.nus.iss.smartpantry.controller;

/**
 * Created by A0134493A on 7/5/2015.
 */
public class ControlFactory {
    //private MainController mainController;
    ItemController itemController;

    public ControlFactory(){
        //mainController = new MainController();
        itemController = new ItemController();
    }

    public MainController getMainController(){
        return (new MainController());
    }
    public ItemController getItemController(){
        return itemController;
    }
}
