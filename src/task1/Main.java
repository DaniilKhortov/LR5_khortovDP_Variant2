package task1;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) {
        CompletableFuture<ArrayResult> generateArray1 = CompletableFuture.supplyAsync(() -> {

            String result = null;
            try {
                result = kaiserSheiseIdeeGenerator();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new ArrayResult(result, Thread.currentThread().getName());

        });

        CompletableFuture<ArrayResult> generateArray2 = CompletableFuture.supplyAsync(() -> {


            String result = null;
            try {
                result = kaiserSheiseIdeeGenerator();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new ArrayResult(result, Thread.currentThread().getName());
        });

        CompletableFuture<ArrayResult> generateArray3 = CompletableFuture.supplyAsync(() -> {


            String result = null;
            try {
                result = kaiserSheiseIdeeGenerator();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new ArrayResult(result, Thread.currentThread().getName());
        });

        CompletableFuture<Object> firstArray = CompletableFuture.anyOf(generateArray1, generateArray2, generateArray3);
        firstArray.thenAccept(result ->{
            ArrayResult taskResult = (ArrayResult) result;
            System.out.println("Тюлень з геніальною ідеєю: " + taskResult.threadName);
            System.out.println("Ідея: " + taskResult.idea);
        }).join();
    }


    static String kaiserSheiseIdeeGenerator() throws InterruptedException {
        Random kaiserRandom = new Random();
        String[] idea = {"Числа фібоначі", "Сесія за три дні", "Відправити тюлення в космос", "Відправити тюлення в космос на катапульті", "Відправити тюлення в космос гарматою", "Зробити це завдання більш асинхронним", "Запустити програму ще раз", "Нецензурний компілятор", "Польський компілятор", "Купити блок живлення під час Фердюнкельн", "Ідей немає", "Зробити тест програми", "Зіграти в лоторею Картоплі", "Сам думай" };
        sleep(kaiserRandom.nextInt(9999));
        String providedIdea = idea[kaiserRandom.nextInt(idea.length)];

        return providedIdea;
    }



}

