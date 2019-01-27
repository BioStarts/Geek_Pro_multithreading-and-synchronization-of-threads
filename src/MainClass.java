import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
    public static final int CARS_COUNT = 4;

    private static CyclicBarrier prepareStart = new CyclicBarrier(CARS_COUNT);//Барьер для того чтобы все участиники стартовали в одно время
    private static Semaphore tunCount = new Semaphore(CARS_COUNT/2);//Ограничение вместимости туннеля
    private static Lock winLock = new ReentrantLock();//замок для печати WIN только первого финишировавшего
    private static AtomicInteger aiStart = new AtomicInteger(0);// атомарная переменная для того чтобы печатать об начале гонки
    private static AtomicInteger ai = new AtomicInteger(0);// атомарная переменная для того чтобы печатать об окончании

    public static AtomicInteger getAiStart() {
        return aiStart;
    }
    public static AtomicInteger getAi() {
        return ai;
    }
    public static CyclicBarrier getPrepareStart() {
        return prepareStart;
    }
    public static Lock getWinLock() {
        return winLock;
    }
    public static Semaphore getTunCount() {
        return tunCount;
    }




    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        //System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        //System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
/*Все участники должны стартовать одновременно, несмотря на то, что на подготовку у каждого их них уходит разное время. +
В тоннель не может заехать одновременно больше половины участников (условность). +
Печатать WIN только у победителя. +
Попробуйте все это синхронизировать.
Только после того, как все завершат гонку, нужно выдать объявление об окончании.
Можете корректировать классы (в т.ч. конструктор машин) и добавлять объекты классов из пакета util.concurrent.*/