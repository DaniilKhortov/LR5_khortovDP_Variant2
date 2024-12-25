package task2;

public class Ticket {
    String name;
    String status;
    double price;

    Ticket(String name, String status, double price) {
        this.name = name;
        this.status = status;
        this.price = price;

    }
    @Override
    public String toString() {
        return "TICKET: '" + name + '\'' +"| STATUS: '" + status + '\'' +"| PRICE: $" + price;
    }

}
