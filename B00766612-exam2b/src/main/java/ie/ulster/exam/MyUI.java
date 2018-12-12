package ie.ulster.exam;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        String connectionString = 
        "jdbc:sqlserver://b00766612-exam2b.database.windows.net:1433;" +
        "database=B00766612-exam2b;"+
        "user=examserver@b00766612-exam2b;"+
        "password=Clouddev1;"+
        "encrypt=true;"+
        "trustServerCertificate=false;" +
        "hostNameInCertificate=*.database.windows.net;"+
        "loginTimeout=30;";

// Create the connection object
Connection connection = null;  
try 
{
	// Connect with JDBC driver to a database
    connection = DriverManager.getConnection(connectionString);
    // Add a label to the web app with the message and name of the database we connected to 
    layout.addComponent(new Label("Connected to database: " + connection.getCatalog()));
	 
ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM BusDetails;");
// Convert the resultset that comes back into a List 
List<Bus> buses = new ArrayList<Bus>();
// While there are more records in the resultset
while(rs.next())
{   
	// Add a new Customer instantiated with the fields from the record (that we want, we might not want all the fields, note how I skip the id)
	buses.add(new Bus(rs.getString("destination"), 
				rs.getInt("maxPeople"), 
				rs.getString("feature"), 
                rs.getBoolean("accessible")));

} 
// Add my component, grid is templated with Customer
Grid<Bus> myGrid = new Grid<>();
// Set the items (List)
myGrid.setItems(buses);
// Configure the order and the caption of the grid
myGrid.addColumn(Bus::getDestination).setCaption("Destination");
myGrid.addColumn(Bus::getPeople).setCaption("MaxPeople");
myGrid.addColumn(Bus::getFeature).setCaption("Feature");
myGrid.addColumn(Bus::isAccessible).setCaption("Accessible");

// Add the grid to the list
layout.addComponent(myGrid);

} //try
catch (Exception e) 
{
	// This will show an error message if something went wrong
	layout.addComponent(new Label(e.getMessage()));
}
        
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });
        
        layout.addComponents(name, button);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
