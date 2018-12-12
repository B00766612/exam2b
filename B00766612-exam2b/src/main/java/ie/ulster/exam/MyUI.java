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
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.shared.ui.ContentMode;
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
        layout.setSpacing(true);
        final HorizontalLayout horizontalLayout = new HorizontalLayout();

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

myGrid.setSizeFull(); // This makes the grid the width of the page
        // This makes the grid 'multi-select', adds the checkboxes for selecting to the side
        myGrid.setSelectionMode(SelectionMode.MULTI);
        MultiSelect <Bus> selected = myGrid.asMultiSelect();

// Add the grid to the list
layout.addComponent(myGrid);
//********************End Grid/ Database***********************************

//********************Start other Components******************** */
Label heading = new Label();
        heading.setContentMode(ContentMode.HTML); //HTML
        heading.setValue("<H1>Fun Bus Bookings</H1>" +"<p/>"+
        "<h3>Please enter the details below and click Book</h3>");
        
        
        Label result= new Label("Your party is not booked yet", ContentMode.HTML);
        

        Label studentNo = new Label("B00766612");

        final TextField groupName= new TextField("Name of Group");
        groupName.setMaxLength(25);
        groupName.setPlaceholder("");

        Slider people = new Slider("How many people are coming to this party", 0, 500);
        people.setValue(0.0); //starting value
        people.setWidth(people.getMax()+"px");
   
        
        ComboBox <String> accessible = new ComboBox<String>("Accessibility?");
        accessible.setItems("Yes", "No");

        Button button = new Button("BOOK");
        button.addClickListener(e -> {
        
            Boolean bookable= true;  //is it bookable?
            Boolean isAccessible = selected.getValue().stream().map(Bus::isAccessible).anyMatch(Predicate.isEqual(true));
            int arePeople = selected.getValue().stream().mapToInt(Bus::getPeople).sum();
             //check grid selection
             if(myGrid.getSelectedItems().size() == 0)
             {
                 bookable=false; //not bookable
                 result.setValue("<strong>Please select at least one Bus!</strong>");
                 return;
             }
         //check party name entried
             if(groupName.getValue().length() == 0)
             {
                 bookable=false; //not bookable
                 result.setValue("<strong>Please enter party name.</strong>");
                 return;
             }
         //check children selection
         if(!accessible.getSelectedItem().isPresent())
             {
                 bookable=false; //not bookable
                 result.setValue("<strong>Please confirm if children attending your party</strong>");
                 return;
             }
 
         //check children/alcohol
             //if ((isAlcohol.equals(yesAlco))&&(children.getValue().equals("Yes")))
             if ((accessible.getValue() == "Yes") && (isAccessible.equals(true)))
             { 
                 bookable=false; //not bookable
                result.setValue("<strong> You cannot select any Buses that are not accessible.</strong>");
                 return;
             }
        
             
         //check capacity of room
           if (arePeople<people.getValue())
             { 
                  bookable=false; //not bookable
                  result.setValue("<strong>You have selected buses with a max capacity of "+ arePeople+  " which is not enough to hold " +people.getValue().intValue() +"</strong>");
                 return;
             }
             

         //all ok
         if (bookable==true)
         {
             result.setValue("<h3>Success! The party is booked now</h3>");
             return;
         }


         layout.addComponent(new Label("Thanks " + groupName.getValue() 
                 + ", it works!"));
     });
     //************ End of other Components and calculations******* */
     //------------ Add Components to Layout ------------------------/


     horizontalLayout.addComponents(groupName, people, accessible);
     layout.addComponents(heading, horizontalLayout, button, result, myGrid, studentNo);
     setContent(layout);
     //----------------------------------------------------------------


} //try
catch (Exception e) 
{
	// This will show an error message if something went wrong
	layout.addComponent(new Label(e.getMessage()));
} //catch
        
    
    
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    } //
}
