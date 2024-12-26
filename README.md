# LR5_Async_Khortov

# Завдання
<p><b>Варіант-2</b></p>
<ul>
<li>1. Створіть кілька асинхронних завдань, які виконуються паралельно.
Виведіть результат першого завершеного успішно завдання.</li>

<li>2. Користувач хоче забронювати квиток на літак, перевірити
наявність місць, ціни, а потім здійснити оплату. Потрібно перевірити
наявність місць, знайти найкращу ціну і завершити бронювання.</li>
</ul>

# Загальний опис рішення
<p><b>Проект складається з частин:</b></p>
<ul>
  <li>1. Генератор ідей (Завдання 1)</li>
  <li>2. Додаток для бронювання квитків (Завдання 2)</li>
</ul>

# Опис ініціалізації ідей (Завдання 1)
<p>Ідеї генеруються у 3 потоки. Найперша придумана ідея виводиться у консоль за допомогою anyOf. Структура потоку:<\n></p>


        CompletableFuture<ArrayResult> generateArray1 = CompletableFuture.supplyAsync(() -> {

            String result = null;
            try {
                result = kaiserSheiseIdeeGenerator();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new ArrayResult(result, Thread.currentThread().getName());

        });

        
       
        
   Для генерації ідеї використовується такий метод:
   

       static String kaiserSheiseIdeeGenerator() throws InterruptedException {
        Random kaiserRandom = new Random();
        String[] idea = {"Числа фібоначі", "Сесія за три дні"};
        sleep(kaiserRandom.nextInt(9999));
        String providedIdea = idea[kaiserRandom.nextInt(idea.length)];
        return providedIdea;
      }  

  
# Опис генерації квитків (Завдання 2)
Квитки генеруються у три потоки, які обєднуються в combinedTickets. Структура потоку:

        CompletableFuture<CopyOnWriteArrayList> generateTicket3 = CompletableFuture.supplyAsync(() -> {
            CopyOnWriteArrayList ticketList = new CopyOnWriteArrayList();
            Random kaiserRandom = new Random();
            for(int i=0; i<40; i++){
                Ticket t1 = new Ticket("Ticket-"+ticketList.size()+"-3", statusList[kaiserRandom.nextInt(statusList.length)],kaiserRandom.nextInt(5)*1000+999);
                ticketList.add(t1);
            }
            return ticketList;
        });


Процес обєднання потоків:


        CompletableFuture<CopyOnWriteArrayList<Ticket>> combinedTickets = generateTicket1
                .thenCombine(generateTicket2, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                })
                .thenCombine(generateTicket3, (list1, list3) -> {
                    list1.addAll(list3);
                    return list1;
                });
            
# Опис знаходження вільних місць (Завдання 2)
Після підтвердження дії користувачем, у два потоки перебирається колекція квитків, які потім обєднуються у нову колекцію. Структура потоку:

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
    
Процес обєднання потоків:


        CompletableFuture<CopyOnWriteArrayList<Ticket>> combinedTickets2 = deleteBooked1
                .thenCombine(deleteBooked2, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });

   

# Знаходження найкращої ціни квитка (Завдання 2)  
Після підтвердження дії користувачем, програма у 2 потоки перебирає масив вакантних квитків. За допомогою anyOf береться за відповідь результат потоку, що першим впорався з завданням:


    CompletableFuture<Object> bestPrice = CompletableFuture.anyOf(findMin1, findMin2);

