import java.util.Objects;

public class Car implements Cloneable {
    String name;
    int yearOfProduce;

    public Car(String name, int yearOfProduce) {
        this.name = name;
        this.yearOfProduce = yearOfProduce;
    }

    @Override
    protected Car clone() throws CloneNotSupportedException {
        Car car = (Car)super.clone();
        car.name = this.name;
        car.yearOfProduce = this.yearOfProduce;
        return car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return yearOfProduce == car.yearOfProduce && Objects.equals(name, car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, yearOfProduce);
    }
}
class Test{
    public static void main(String[] args) throws CloneNotSupportedException {
        Car car = new Car("BMW",2020);
        Car clonedCar = car.clone();
        System.out.println(car.equals(clonedCar));
    }
}
