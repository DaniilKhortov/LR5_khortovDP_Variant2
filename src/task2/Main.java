package task2;



import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {

        String[] statusList = {"Booked", "Available"};
        CompletableFuture<CopyOnWriteArrayList> generateTicket1 = CompletableFuture.supplyAsync(() -> {
            CopyOnWriteArrayList ticketList = new CopyOnWriteArrayList();
            Random kaiserRandom = new Random();
            for(int i=0; i<40; i++){
                Ticket t1 = new Ticket("Ticket-"+ticketList.size()+"-1", statusList[kaiserRandom.nextInt(statusList.length)],kaiserRandom.nextInt(5)*1000+999);
                ticketList.add(t1);
            }
        return ticketList;
        });
        CompletableFuture<CopyOnWriteArrayList> generateTicket2 = CompletableFuture.supplyAsync(() -> {
            CopyOnWriteArrayList ticketList = new CopyOnWriteArrayList();
            Random kaiserRandom = new Random();
            for(int i=0; i<40; i++){
                Ticket t1 = new Ticket("Ticket-"+ticketList.size()+"-2", statusList[kaiserRandom.nextInt(statusList.length)],kaiserRandom.nextInt(5)*1000+999);
                ticketList.add(t1);
            }
            return ticketList;
        });
        CompletableFuture<CopyOnWriteArrayList> generateTicket3 = CompletableFuture.supplyAsync(() -> {
            CopyOnWriteArrayList ticketList = new CopyOnWriteArrayList();
            Random kaiserRandom = new Random();
            for(int i=0; i<40; i++){
                Ticket t1 = new Ticket("Ticket-"+ticketList.size()+"-3", statusList[kaiserRandom.nextInt(statusList.length)],kaiserRandom.nextInt(5)*1000+999);
                ticketList.add(t1);
            }
            return ticketList;
        });

        CompletableFuture<CopyOnWriteArrayList<Ticket>> combinedTickets = generateTicket1
                .thenCombine(generateTicket2, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                })
                .thenCombine(generateTicket3, (list1, list3) -> {
                    list1.addAll(list3);
                    return list1;
                });

        combinedTickets.thenApply(ticketList -> {
            System.out.println("Combined Tickets:");
            ticketList.forEach(System.out::println);

            System.out.println("-----------------------------------");
            System.out.println("Find available Places? [y/n]");
            Scanner input = new Scanner(System.in);

            boolean continueVar = true;
            while (continueVar == true){
                String option = input.nextLine();
                if (option.equals("y")){
                    continueVar = false;

                }else if (option.equals("n")){
                    System.exit(0);
                }else{
                    System.out.println("Undefined option...");
                }
            }
            return ticketList;

        }).join();

        CompletableFuture<CopyOnWriteArrayList> deleteBooked1 = combinedTickets.thenApply(ticketList-> {
            CopyOnWriteArrayList ticketListCopy = new CopyOnWriteArrayList<>();
            for(int i=0; i<ticketList.size()/2; i++){
                Ticket ticket = ticketList.get(i);
                if ("Available".equals(ticket.status)) {
                    ticketListCopy.add(ticket);
                }
            }

            return ticketListCopy;


        });
        CompletableFuture<CopyOnWriteArrayList> deleteBooked2 = combinedTickets.thenApply(ticketList-> {
            CopyOnWriteArrayList ticketListCopy2 = new CopyOnWriteArrayList<>();
            for(int i=ticketList.size()/2; i<ticketList.size(); i++){
                Ticket ticket = ticketList.get(i);
                if ("Available".equals(ticket.status)) {
                    ticketListCopy2.add(ticket);
                }
            }

            return ticketListCopy2;


        });

        CompletableFuture<CopyOnWriteArrayList<Ticket>> combinedTickets2 = deleteBooked1
                .thenCombine(deleteBooked2, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });

        combinedTickets2.thenApply(ticketList -> {
            System.out.println("Available Tickets:");
            ticketList.forEach(System.out::println);

            System.out.println("-----------------------------------");
            System.out.println("Find best price? [y/n]");
            Scanner input = new Scanner(System.in);

            boolean continueVar = true;
            while (continueVar) {
                String option = input.nextLine();
                if (option.equals("y")) {
                    continueVar = false;
                } else if (option.equals("n")) {
                    System.exit(0);
                } else {
                    System.out.println("Undefined option...");
                }
            }
            return ticketList;

        }).join();

        CompletableFuture<Ticket> findMin1 = combinedTickets2.thenApply(ticketList-> {
            CopyOnWriteArrayList ticketListCopy = new CopyOnWriteArrayList<>();
            Ticket ticketTest = new Ticket("Test", "Booked", 9999999);
            for (int i = 0; i < ticketList.size() / 2; i++) {
                Ticket ticket = ticketList.get(i);

                if (ticket.price < ticketTest.price) {
                    ticketTest = ticket;
                }

            }

            return ticketTest;
        });
        CompletableFuture<Ticket> findMin2 = combinedTickets2.thenApply(ticketList-> {
            CopyOnWriteArrayList ticketListCopy = new CopyOnWriteArrayList<>();
            Ticket ticketTest = new Ticket("Test", "Booked", 9999999);
            for (int i = ticketList.size()/2; i < ticketList.size(); i++) {
                Ticket ticket = ticketList.get(i);

                if (ticket.price < ticketTest.price) {
                    ticketTest = ticket;
                }

            }

            return ticketTest;
        });
        CompletableFuture<Void> generateLine1 = CompletableFuture.runAsync(()->{
            System.out.println("--------------------------------------------------------------------------");
        });
        CompletableFuture<Void> generateLine2 = CompletableFuture.runAsync(()->{
            System.out.println("--------------------------------------------------------------------------");
        });

        CompletableFuture<Object> bestPrice = CompletableFuture.anyOf(findMin1, findMin2);

        CompletableFuture<Object> bestPriceText = bestPrice.thenCompose(result ->
                CompletableFuture.supplyAsync(() -> "Recommended Ticket: "+ result ));


        bestPriceText.thenAccept(result -> {
               System.out.println(result);

           }

           ).join();

        CompletableFuture<Void> text = CompletableFuture.allOf(generateLine1, generateLine2);





    }
}

