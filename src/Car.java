

public class Car implements Runnable{
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            MainClass.getAi().incrementAndGet();//увеличиваем атомарную переменную на 1, считая количесвто готовых к старту
            if (MainClass.getAi().get() == CARS_COUNT){//печатаем что гонка закончилась когда количество готовых к старту равно количеству участников
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            }
            MainClass.getPrepareStart().await();//ждем пока в Мэйне не запустятся 4 потока
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        if (MainClass.getWinLock().tryLock()) { // первый финишировавший захватывает замок и печатет WIN, при этом
            // не отдавая доступ(без unlock() ) к нему слудующим участникам.
            System.out.println(this.name + " WIN");
        }

        MainClass.getAi().decrementAndGet();//уменьшаем атомарную переменную на 1, уменьшая количество активных участинков
        if (MainClass.getAi().get() == 0){//печатаем что гонка закончилась когда количество активных участников равно 0 (когда все финишировали)
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        }

    }
}
