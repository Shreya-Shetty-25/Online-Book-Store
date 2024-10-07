// Name: Shreya Harish Shetty
// Enrollment No: 222002171210174
// Subject: Java-2
// Project- Online Book Store
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
abstract class Book{
        static Statement st;
        static Connection con;
    static Scanner sc=new Scanner(System.in);
    public Book()throws Exception {
}
void home() throws Exception
    {
    }
    abstract public void adminLogin() throws Exception;
    abstract public void customerLogin() throws SQLException, Exception;
}
class Admin extends Book
{
    public Admin() throws Exception 
    {
        }
        

public void adminLogin() throws Exception
    {
        int id=0;
        boolean tag1=false;
        do{
        try{
        System.out.print("Please enter your ID: ");
        id=sc.nextInt();
        sc.nextLine();
        tag1=false;
        }
        catch(InputMismatchException i){
                sc.nextLine();
                System.out.println("Enter numbers only");
                tag1=true;
                
        }
}while(tag1);
        String sql1="Select * from admin where admin_id=?;";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.setInt(1, id);
        ResultSet rs1=ps1.executeQuery();
        if(rs1.next()){
        System.out.print("Please enter your password: ");
        String password=sc.nextLine();
        if(rs1.getString("admin_password").equals(password)){
         boolean flag=false;
        System.out.println("WELCOME ");
         adminHome();
        }
        else{
        System.out.println("Incorrect password try again...");
        adminLogin();
    }
        }
        else{
             System.out.println("Id not found!");
        adminLogin();
        }
        }
    void adminHome() throws Exception{
        boolean flag=false;
        do{
        System.out.println("1.) Display whole stock \n2.) Search for book \n3.) Update stock \n4.) View Orders \n5.) Show Feedback \n6.) Back to home \n7.) Exit ");
        System.out.print("\nEnter your choice: ");
        int adminSelect=sc.nextInt();
        switch(adminSelect)
        {
            case 1: displayStock();
                    break;
            case 2: searchBook();
                    break;
            case 3: updateStock();
                    break;
            case 4: viewOrders();
            case 5: showReview();
            case 6: break;
            case 7: System.exit(0);

            default:System.out.println("Please enter a valid choice!");
                    flag=true; 
                    break;
        }
    }while(flag);
}
    private void showReview() throws Exception {
        System.out.print("Enter Book Id: ");
        int bookid=sc.nextInt();
        ResultSet r1=st.executeQuery("select * from bookdetails where book_id="+bookid);
        if(r1.next()){
            ResultSet r2=st.executeQuery("select * from review where book_id="+bookid);
            while(r2.next()){
                System.out.println("Customer Id: "+r2.getInt("cust_id")+" Rating: "+r2.getInt("rating")+" Review: "+r2.getString("feedback"));
                System.out.println();
            }
            adminHome();
        }
        else{
            System.out.println("Book Id not found");
            adminHome();
        }
    }


    private void viewOrders() throws Exception {
        System.out.println("YOUR ORDERS\n");
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("  Order Id  |  Customer Id  |  Book Id  |  Quantity  |  Order Date  |  Amount   |");
        System.out.println("---------------------------------------------------------------------------------");
        String sql="select * from orders";
        ResultSet r=st.executeQuery(sql);
        while(r.next()){
            int orderId = r.getInt(1);
        int customerId = r.getInt(2);
        int quantity = r.getInt(4);
                Date date=r.getDate(5);
        double amount = r.getDouble(6);
        int bookId=r.getInt(3);

        System.out.format("    %4d   |      %4d      |   %3d     |  %3d      |  %s   |  %.2f   |%n", orderId, customerId, bookId, quantity, date.toString(), amount);
        }
        System.out.println("----------------------------------------------------------------------------------");
        adminHome();
    }


    private void updateStock() throws Exception {
        System.out.println("1.) Add Quantity\n2.) Add new book \n3.) Back \n4.) Exit");
                System.out.print("\nEnter your choice: ");
        int updateselect=sc.nextInt();
        switch(updateselect){
                case 1: addQuantity();
                break;
                case 2: addNewBook();
                break;
                case 3: adminHome();
                break;
                case 4: System.exit(0);
                break;
                default: System.out.println("Please enter a valid choice");
                updateStock();
                break;
        }
}

private void addNewBook() throws Exception {
        try{
         System.out.print("Enter Book Id: ");
        int id=sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name of the Book: ");
        String name=sc.nextLine();
        System.out.print("Enter Name of the Author: ");
        String author=sc.nextLine();
        System.out.print("Enter Book Publisher: ");
        String publisher=sc.nextLine();
        System.out.print("Enter Genre of the Book: ");
        String genre=sc.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity=sc.nextInt();
        System.out.print("Enter Price: ");
        double price=sc.nextDouble();
        sc.nextLine();
        System.out.print("Add Summary File path: ");
        String path=sc.nextLine();
        FileReader f1=new FileReader(new File(path));
        String sql="Insert into bookdetails values(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement p1=con.prepareStatement(sql);
        p1.setInt(1, id);
        p1.setString(2, name);
        p1.setString(3, author);
        p1.setString(4, publisher);
        p1.setString(5, genre);
        p1.setInt(6, quantity);
        p1.setDouble(7, price);
        p1.setClob(8, f1);
        int r=p1.executeUpdate();
        if(r>0){
                System.out.println("Book successfully added!");
        }
        else{
                System.out.println("Sorry there was error. Try again!");
        }
        updateStock();
        }
        catch(InputMismatchException e){
                System.out.println("Your input is invalid. Try again!");
                addNewBook();
        }
        catch(SQLIntegrityConstraintViolationException r){
                System.out.println("Enter different book id( as this book id already exists!)");
                
        }
       
}


private void addQuantity() throws Exception {
        boolean tag3=true;
        try{
        System.out.print("Enter the book id: ");
        int addid=sc.nextInt();
        String sql4="select book_quantity from bookdetails where book_id="+addid;
        ResultSet rs4=st.executeQuery(sql4);
       while(rs4.next()){
        System.out.println("Current Quantity:- "+rs4.getInt("book_quantity"));
        System.out.println("Update to:- ");
        int addquantity=sc.nextInt();
        String sql5="update bookdetails set book_quantity=? where book_id=?";
        PreparedStatement ps5=con.prepareStatement(sql5);
        ps5.setInt(1, addquantity);
        ps5.setInt(2, addid);
        ps5.execute();
        System.out.println("Quantity updated sucessfully!");
        tag3=false;
       }
       if(tag3){
        System.out.println("Book not found!");
       }
       updateStock();
        }
        catch(InputMismatchException i){
                sc.nextLine();
                System.out.println("Enter numbers only!");
                addQuantity();
        }
        
}


private void searchBook() throws Exception {
        try{
        System.out.print("Enter book id you want to search: ");
        int id=sc.nextInt();
        String sql3="select * from bookdetails where book_id=?;";
        PreparedStatement ps3=con.prepareStatement(sql3);
        ps3.setInt(1, id);
        ResultSet rs3=ps3.executeQuery();
        boolean tag2=true;
        while(rs3.next()){
                System.out.println("Book Id:- "+rs3.getInt("book_id")+"\nBook Title:- "+rs3.getString("book_title")+"\nBook Publisher:- "+rs3.getString("book_publisher")+"\nBook Quantity:- "+rs3.getInt("book_quantity")+"   Price:- "+rs3.getDouble("book_price"));
                tag2=false;

        }
        if(tag2){
                System.out.println("Book not found with id: " + id);
        }
        System.out.println();
        adminHome();
} 
catch(InputMismatchException e){
        sc.nextLine();
        System.out.println("Enter numbers only!");
        searchBook();
}
}

private void displayStock() throws Exception 
{
        String sql2="Select * from bookdetails";
        PreparedStatement ps2=con.prepareStatement(sql2);
        ResultSet rs2=ps2.executeQuery();
        while(rs2.next()){
                System.out.println("BookId:- "+rs2.getInt("book_id")+" Title:- "+rs2.getString("book_title")+ "\nQuantity:- "+rs2.getInt("book_quantity")+" MRP:- "+rs2.getDouble("book_price")+"\n");
        }
        adminHome();
        
}
public void customerLogin(){
      
}
}
class Customer extends Book
{
        private ShoppingCart shoppingCart;
        static int books[];
        static boolean tag=true;

    public Customer() throws Exception {
        shoppingCart = new ShoppingCart();
    }
    static int customerId;
    static String customerName;

public void customerLogin() throws Exception
    {
       System.out.println("1.) Login(Already a User) \n2.) Sign Up(New User)");
        System.out.print("\nEnter your choice: ");
       int choose=sc.nextInt();
       switch(choose){
        case 1: login();
        break;
        case 2: signUp();
        break;
        case 3: System.exit(0);
        break;
        default: System.out.println("Invalid choice. Try Again!");
       }
    }
    private void signUp() throws Exception {
        boolean flag=true;
        sc.nextLine();
        System.out.print("Enter your Name: ");
       customerName=sc.nextLine();
        System.out.print("Enter your Mail Id: ");
        String mail=sc.nextLine();
        String password;
        while(flag){
        if(!mail.contains("@")){
                System.out.println("Your mail id is invalid. Try again!");
                mail=sc.nextLine();
        }
        else{
                flag=false;
        }
        }
        flag=true;
        System.out.println("Set your password(only 8 characters allowed)");
        password=sc.nextLine();
        while(flag){
        if(password.length()!=8){
                System.out.println("Please enter 8 characters only!");
                password=sc.nextLine();
        }
        else{
                flag=false;
        }
        }
        String sql="Insert into customer (cust_name, cust_mail, cust_password) values(?, ?, ?)";
        PreparedStatement p1=con.prepareStatement(sql);
        p1.setString(1,customerName);
        p1.setString(2, mail);
        p1.setString(3, password);
        int r=p1.executeUpdate();
        if(r>0){
        System.out.println("You have Successfully Signed Up!\n"); 
        ResultSet rs=st.executeQuery("select last_insert_id()");
        if(rs.next()){
            customerId=rs.getInt(1);
        }
        tag=false; 
        customerMenu();     
        }
        else{
                System.out.println("Sign in Failed. Try Again!");
                ResultSet r1=st.executeQuery("select * from bookdetails where cust_mail="+mail);
        if(r1.next()){
            customerId=r1.getInt("cust_id");
            System.out.println(customerId);
            signUp();
        }
        }
}

private void login() throws Exception {
        sc.nextLine();
        boolean flag=true;
        do{
        System.out.print("Enter your Mail Id: ");
        String mail=sc.nextLine();
        System.out.print("Enter your Password: ");
        String password=sc.nextLine();
        String sql="select * from customer where cust_mail='"+mail+"' and cust_password='"+password+"'";
        ResultSet r=st.executeQuery(sql);
        if(r.next()){
            customerName=r.getString("cust_name");
            customerId=r.getInt("cust_id");
                System.out.println("You have successfully logged in!\n");
                flag=false;
        }
        else{
                System.out.println("Incorrect mail id or password. Please try again!");
        }
}while(flag);
customerMenu();
}
public void customerMenu() throws Exception{
boolean flag = true;
        do {
            System.out.println("Customer Menu:\n1.) View Books\n2.) View Bestsellers \n3.) Search Books\n4.) View Shopping Cart  \n5.) Give Feedback \n6.) Checkout\n7.) Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    viewBooks();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 2: viewBestseller();
                break;
                case 4:
                    viewShoppingCart();
                    break;
                case 5: writeReview();break;
                case 6:
                    checkout();
                    break;
                case 7:
                    System.out.println("Logged out successfully.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again!");
                    break;
            }
        } while (flag);
    }
private void viewBestseller()throws Exception {
    books=new int[3];
    int count=0;
    System.out.println("Here are out top three bestsellers!");
    String sql="Select * from orders order by quantity desc limit 3";
    ResultSet r=st.executeQuery(sql);
    while(r.next()){
     books[count]=r.getInt("book_id");
     count++;
    }
    String sql1="select * from bookdetails where book_id=? or book_id=? or book_id=?";
    PreparedStatement ps=con.prepareStatement(sql1);
    ps.setInt(1, books[0]);
    ps.setInt(2, books[1]);
    ps.setInt(3, books[2]);
    ResultSet rs1=ps.executeQuery();
    while(rs1.next()){
         System.out.print("Book Id: " + rs1.getInt("book_id"));
            System.out.println(", Title: " + rs1.getString("book_title"));
            System.out.print("Author: " + rs1.getString("book_author"));
            System.out.println(", Price: " + rs1.getDouble("book_price"));
            System.out.println();
    }
    proceedShopping();


}
private void viewBooks() throws Exception {
         String sql = "SELECT * FROM bookdetails";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.print("Book Id: " + rs.getInt("book_id"));
            System.out.println(", Title: " + rs.getString("book_title"));
            System.out.print("Author: " + rs.getString("book_author"));
            System.out.println(", Price: " + rs.getDouble("book_price"));
            System.out.println();
        }
        System.out.println();
        proceedShopping();
    }

private void addToShoppingCart() throws SQLException {
        System.out.print("Enter Book Id: ");
        int bookId=sc.nextInt();
        String sql = "SELECT * FROM bookdetails WHERE book_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int quantityInStock = rs.getInt("book_quantity");
                if (quantityInStock > 0) {
                    // Check if the book is already in the cart
                    List<BookItem> cartItems = shoppingCart.getItems();
                    boolean found = false;
                    for (BookItem item : cartItems) {
                        if (item.getBookId() == bookId) {
                            item.increaseQuantity(1); // Increment the quantity if already in cart
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        // If not found in the cart, add it as a new item
                        String title = rs.getString("book_title");
                        double price = rs.getDouble("book_price");
                        BookItem newItem = new BookItem(bookId, title, price, 1);
                        shoppingCart.addItem(newItem);
                    }
                    System.out.println("Book added to the shopping cart!");
                } else {
                    System.out.println("Sorry, this book is out of stock!");
                }
            } else {
                System.out.println("Book not found with id: " + bookId);
            }
        
}

private void viewBookDetails() throws Exception {
        System.out.print("Enter the book iD to view details: ");
        int bookId = sc.nextInt();

        String sql = "SELECT * FROM bookdetails WHERE book_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Book Details:");
            System.out.println("Title: " + rs.getString("book_title"));
            System.out.println("Genre: " + rs.getString("book_genre"));
            System.out.println("Price: " + rs.getDouble("book_price"));
            System.out.println("Summary:- ");
            System.out.println(rs.getString("book_summary"));
            proceedShopping();
        }
        else
         {
            System.out.println("Book not found with ID: " + bookId);
            customerMenu();
        }
}
private void proceedShopping() throws Exception{
        System.out.println("Options:");
        System.out.println("1.) View Details of a Book");
        System.out.println("2.) Add a Book to the Shopping Cart");
        System.out.println("3.) Back to Customer Menu");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                viewBookDetails();
                break;
            case 2:
                addToShoppingCart();
                break;
            case 3:
                customerMenu();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
private void viewShoppingCart() throws Exception {
    List<BookItem> cartItems = shoppingCart.getItems();
    System.out.println("Shopping Cart: ");
    System.out.println("-----------------------------------");
    if(cartItems.size()>0){
        for (BookItem item : cartItems) {
        System.out.println("Book ID: " + item.getBookId());
        System.out.println("Title: " + item.getTitle());
        System.out.println("Price: " + item.getPrice());
        System.out.println("Quantity: " + item.getQuantity());
        System.out.println("Subtotal: " + item.getSubtotal());
        System.out.println("-----------------------------------");
    }
}
    else{
        System.out.println("Empty!");
    }
    }
        

private void searchBooks() throws Exception {
        sc.nextLine();
        boolean flag=false;
        System.out.print("Enter your Search(book title, genre, author):");
        String search=sc.nextLine();
        String sql = "SELECT * FROM bookdetails WHERE book_title Like '%"+search+"%' or book_author Like '%"+search+"%' or book_genre LIKE '%"+search+"%'";
        System.out.println(search);
        ResultSet rs=st.executeQuery(sql);
        while(rs.next()){
             System.out.print("Book Id: " + rs.getInt("book_id"));
            System.out.println(", Title: " + rs.getString("book_title"));
            System.out.print("Author: " + rs.getString("book_author"));
            System.out.println(", Price: " + rs.getDouble("book_price"));
            System.out.println();
            flag=true;
        }
        if(!flag){
                System.out.println("No books found matching your search!");
        }
        else{
                proceedShopping();
        }
}
private void writeReview() throws Exception{
    int check[]=new int[10];
    if(tag){
        int counter=1;
        String sql="Select * from orders where cust_id="+customerId;
        ResultSet rs=st.executeQuery(sql);
        boolean go=rs.next();
        if(go==false){
            System.out.println("Sorry you have no order history");
            customerMenu();
        }
        else{
        String sql2="Select * from bookdetails where book_id in(select book_id from orders where cust_id="+customerId+")"  ;
        ResultSet rs2=st.executeQuery(sql2);
        System.out.println("Your previously purchased books are");
        while(rs2.next()){
            System.out.println("Book Id: "+rs2.getInt("book_id")+"  Book Title: "+rs2.getString("book_title"));
            check[counter-1]=rs2.getInt("book_id");
            counter++;
        }
        }
        System.out.print("\nSelect book id to review: ");
        int select=sc.nextInt();
        for(int checker: check){
            if(checker==select){
                 inputReview(select);
            }
            else{
                tag=true;
            }
        }
        
        if(tag){
            System.out.println("Book id not found. Try Again!");
            writeReview();
        }
    }
    else{
        System.out.println("Sorry you cannot review yet!");
    }
}

private void inputReview(int select) throws Exception {
    try{
        String replica="";
    System.out.println("Please rate your book first");
    System.out.println("**1 - 2( Not satisfied)\n**3( Average)\n**4 - 5( Must buy)");
    int rate=sc.nextInt();
    if(rate>5&&rate<=0){
        System.out.println("Rating out of input range. Try Again!");
        inputReview(select);
    }
    sc.nextLine();
    System.out.println("Write your feedback here( max 20 words!)\n(Press enter to skip)");
    String feedback=sc.nextLine();
    String check[]=feedback.split(" ");
    String checker[]=new String[check.length];
    
    for(int i=0;i<checker.length&&i<check.length;i++){
        checker[i]=check[i];
    }
    for(String input: checker){
        if(feedback.length()==0){
            replica="";
            break;   
    }
        replica=replica+input+" ";
    }
    String sql="Insert into review values (?, ?, ?, ?)";
    PreparedStatement ps=con.prepareStatement(sql);
    ps.setInt(2, select);
    ps.setInt(1, customerId);
    ps.setInt(3, rate);
    ps.setString(4, replica);
    int rateupdate=ps.executeUpdate();
    if(rateupdate>0){
        System.out.println("Review submitted successfully");
    }
    else{
        System.out.println("There was an error in submitting review. Try Again!");
    }
    customerMenu();
    }
    catch(InputMismatchException e){
        System.out.println("Enter numbers only!");
    }
}
private void checkout() throws Exception {
        viewShoppingCart();
        List<BookItem> cartItems = shoppingCart.getItems();
            BufferedWriter bw=new BufferedWriter(new FileWriter("Bill.txt"));
            bw.write("**********************************YOUR BILL**********************************");
            bw.write("\nCustomer Name: "+customerName);
           // bw.write("\nCustomer Id: "+customerId);
           long millis=System.currentTimeMillis();
           java.sql.Date date=new java.sql.Date(millis);
        for(BookItem item : cartItems){
            bw.write("\nBook ID: " + item.getBookId()+"\nTitle: " + item.getTitle()+"\nPrice: " + item.getPrice()+"\nQuantity: " + item.getQuantity());
                    String sql2="Insert into orders(cust_id, book_id, quantity, order_date, amount) values (?,?,?,?, ? )";
                    PreparedStatement ps2=con.prepareStatement(sql2);
                    ps2.setInt(1, customerId);
                    ps2.setInt(2, item.getBookId());
                    ps2.setInt(3, item.getQuantity());
                  ps2.setDate(4, date);
                    ps2.setDouble(5, (item.getPrice()*item.getQuantity()));
                    ps2.executeUpdate();
        }
        double totalCost = shoppingCart.getTotalCost();
        if(totalCost>0){
        System.out.println("Total Cost: " + totalCost);
        System.out.print("Enter your payment amount: ");
        double paymentAmount = sc.nextDouble();

        if (paymentAmount == totalCost) {
            // Process the payment (you can implement this part)
            // Assuming payment is successful, update the database and clear the cart
            processPayment(totalCost);
            shoppingCart.getItems().clear();
            System.out.println("Payment successful. Thank you for your purchase!");
            bw.write("\nTotal Amount: "+totalCost);
            bw.write("\n**************************THANKS FOR SHOPPING FROM BOOKSCAPE**************************");
            bw.close();
            System.exit(0);
        } 
        else {
            System.out.println("Insufficient payment. Transaction canceled!");
        }
        }
        else{
            System.out.println("Your Shopping Cart is Empty!");
            customerMenu();
        }
       
    }
    private void processPayment(double amount) throws InterruptedException{
        Thread.sleep(2500);
    System.out.println("Processing payment of " + amount + "...");
}
public void adminLogin() throws Exception {
}
}
class ShoppingCart {
    private List<BookItem> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addItem(BookItem item) {
        items.add(item);
    }

    public void removeItem(BookItem item) {
        items.remove(item);
    }

    public double getTotalCost() {
        double totalCost = 0.0;
        for (BookItem item : items) {
            totalCost += item.getSubtotal();
        }
        return totalCost;
    }

    public List<BookItem> getItems() {
        return items;
    }
}

class BookItem {
    private int bookId;
    private String title;
    private double price;
    private int quantity;

    public BookItem(int bookId, String title, double price, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int amount) {
        quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        quantity -= amount;
    }

    public double getSubtotal() {
        return price * quantity;
    }
}

class RunBookStore{
        public static void main(String[] args)throws Exception {
         String dburl="JDBC:mysql://localhost/onlinebookstore";
        String dbuser="root";
        String dbpass="";
        String Driver="com.mysql.cj.jdbc.Driver";
       Book.con=DriverManager.getConnection(dburl, dbuser, dbpass);
        Book.st=Book.con.createStatement();
        Scanner sc=new Scanner(System.in);
        Book admin=new Admin();
        Book cust=new Customer();
        do{
        System.out.println("WELCOME TO BOOKSCAPE");
        System.out.println("1.) Admin Login \n2.) Customer Login \n3.) Exit");
        System.out.print("Enter your choice: ");
        try{
        int selectHome=sc.nextInt();
        switch(selectHome){
            case 1: admin.adminLogin();
                    break;
            case 2: cust.customerLogin();
                    break;
            case 3: System.exit(0);
            default: System.out.println("Please enter a valid choice!");
                    break;
        }
}
catch(InputMismatchException e){
sc.nextLine();
System.out.println("Enter numbers only!");
}
}while(true);
}
}