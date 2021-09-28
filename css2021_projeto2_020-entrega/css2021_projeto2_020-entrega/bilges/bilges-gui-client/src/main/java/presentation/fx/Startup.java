package presentation.fx;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


import facade.services.IEventServiceRemote;
import facade.services.IVenueServiceRemote;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.fx.inputcontroller.CreateEventController;
import presentation.fx.model.CreateEventModel;

public class Startup extends Application {
    
	private static IEventServiceRemote eventService;
	private static IVenueServiceRemote venueService;

	@Override 
    public void start(Stage stage) throws IOException {
    
		// This line to resolve keys against Bundle.properties
		// ResourceBundle i18nBundle = ResourceBundle.getBundle("i18n.Bundle", new Locale("en", "UK"));
        ResourceBundle i18nBundle = ResourceBundle.getBundle("i18n.Bundle", new Locale("pt", "PT"));
        
        
		
//      FXMLLoader createCustomerLoader = new FXMLLoader(getClass().getResource("/fxml/newCustomer.fxml"), i18nBundle);
//    	Parent root = createCustomerLoader.load();
//    	NewCustomerController newCustomerController = createCustomerLoader.getController();  
        
//    	NewCustomerModel newCustomerModel = new NewCustomerModel(customerService);
//    	newCustomerController.setModel(newCustomerModel);
//    	newCustomerController.setCustomerService(customerService);
//    	newCustomerController.setI18NBundle(i18nBundle);
    	
        
    	FXMLLoader createEventLoader = new FXMLLoader(getClass().getResource("/fxml/bilgesStage.fxml"), i18nBundle);
    	Parent root = createEventLoader.load();
    	CreateEventController CreateEventController = createEventLoader.getController();  
    	
    	CreateEventModel createEventMode = new CreateEventModel(eventService, venueService);
    	CreateEventController.setModel(createEventMode);
    	CreateEventController.setI18NBundle(i18nBundle);
    	CreateEventController.setEventService(eventService);
    	CreateEventController.setVenueService(venueService);
    	

    	
        Scene scene = new Scene(root, 600, 400);
       
        stage.setTitle(i18nBundle.getString("application.title"));
        stage.setScene(scene);
        stage.show();
    }
	
	public static void startGUI(IEventServiceRemote eventService, IVenueServiceRemote venueService) {
		Startup.eventService = eventService;
		Startup.venueService = venueService;
        launch();
	}
}
