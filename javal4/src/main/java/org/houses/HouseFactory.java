package org.houses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.houses.utils.ConcreteHouse;

public class HouseFactory {

    private static final String ALGORITHM = "AES"; 
    private static final String KEY_FILE = "secretKey.key"; 
    private static HouseCollection houseCollection = new HouseList();
    private static HouseCollection houseCollection2 = new HouseList();
    static SecretKey key;
    
    public static void saveKeyToFile(SecretKey secretKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(secretKey.getEncoded());
        }
    }

    // Загрузка ключа из файла
    public static SecretKey loadKeyFromFile() throws IOException {
        byte[] keyBytes = new byte[32]; 
        FileInputStream fis = new FileInputStream(KEY_FILE);
        fis.read(keyBytes);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
    

    public static void main(String[] args) throws IOException {
        try {
            key = loadKeyFromFile();
            System.out.println("Ключ загружен из файла.");
        } catch (IOException e) {
            System.out.println("Ключ не найден, генерируем новый.");    
            key = EncryptionUtils.generateKey();
            System.out.println("Новый ключ сгенерирован и сохранен.");
        }
        System.out.println(key);
        Scanner scanner = new Scanner(System.in);
        String txtFilename = "houses.txt";
        String xmlFilename = "houses.xml";
        String jsonFilename = "houses.json";
        String encryptedFilename = "encrypted.txt";

        Set<House> houseSet = new HashSet<>();
        houseSet.addAll(FileManager.readFromTxt(txtFilename));
        houseSet.addAll(FileManager.readFromXML(xmlFilename));
        houseSet.addAll(FileManager.readFromJSON(jsonFilename));
        houseCollection.getAllHouses().addAll(houseSet);

        while (true) {
            System.out.println("1. Добавить дом");
            System.out.println("2. Показать все дома");
            System.out.println("3. Сохранить и зашифровать данные");
            System.out.println("4. Прочитать и расшифровать данные");
            System.out.println("5. Сохранить данные и создать архив (ZIP)");
            System.out.println("6. Удалить дом");
            System.out.println("7. Обновить дом");
            System.out.println("8. Сортировать по цене");
            System.out.println("9. Сортировать по площади");
            System.out.println("10. Сохранить дома в файл");
            System.out.println("11. Прочитать дома из файла");
            System.out.println("12. Выход");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addHouse(scanner);
                    break;
                case 2:
                    printAllHouses();
                    break;
                case 3:
                    saveAndEncryptData();
                    break;
                case 4:
                    readAndDecryptData();
                    break;
                case 5:
                    saveDataAndCreateZip();
                    break;
                case 6:
                    removeHouse(scanner);
                    break;
                case 7:
                    updateHouse(scanner);
                    break;
                case 8:
                    sortHousesByPrice();
                    break;
                case 9:
                    sortHousesByArea();
                    break;
                case 10:
                    saveToFile(txtFilename, xmlFilename, jsonFilename);
                    break;
                case 11:
                    readFromFile(txtFilename, xmlFilename, jsonFilename);
                    break;
                case 12:
                    System.out.println("Выход из программы.");
                    saveKeyToFile(key);
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, попробуйте снова.");
            }
        }
    }

    // Добавление нового дома
    private static void addHouse(Scanner scanner) {
        System.out.println("Введите id дома:");
        int id = scanner.nextInt();
        System.out.println("Введите тип дома:");
        String type = scanner.next();
        System.out.println("Введите площадь дома:");
        int area = scanner.nextInt();
        System.out.println("Введите количество этажей:");
        int floors = scanner.nextInt();
        System.out.println("Введите цену дома:");
        double price = scanner.nextDouble();

        House newHouse = new ConcreteHouse(id, type, area, floors, price);
        houseCollection.addHouse(newHouse);
    }

    // Удаление дома
    private static void removeHouse(Scanner scanner) {
        System.out.println("Введите id дома для удаления:");
        int id = scanner.nextInt();
        houseCollection.removeHouse(id);
    }

    // Обновление дома
    private static void updateHouse(Scanner scanner) {
        System.out.println("Введите id дома для обновления:");
        int id = scanner.nextInt();
        House house = houseCollection.getHouseById(id);

        if (house != null) {
            System.out.println("Введите новый тип дома:");
            String type = scanner.next();
            System.out.println("Введите новую площадь дома:");
            int area = scanner.nextInt();
            System.out.println("Введите новое количество этажей:");
            int floors = scanner.nextInt();
            System.out.println("Введите новую цену дома:");
            double price = scanner.nextDouble();

            house.setType(type);
            house.setArea(area);
            house.setFloors(floors);
            house.setPrice(price);
        } else {
            System.out.println("Дом с таким id не найден.");
        }
    }

    // Показать все дома
    private static void printAllHouses() {
        List<House> houses = houseCollection.getAllHouses();
        if (houses.isEmpty()) {
            System.out.println("Дома отсутствуют.");
        } else {
            for (House house : houses) {
                System.out.println(house);
            }
        }
    }
    private static void printAllHouses2() {
        List<House> houses = houseCollection2.getAllHouses();
        if (houses.isEmpty()) {
            System.out.println("Дома отсутствуют.");
        } else {
            for (House house : houses) {
                System.out.println(house);
            }
        }
    }

    // Сортировка по цене
    private static void sortHousesByPrice() {
        List<House> houses = houseCollection.getAllHouses();
        houses.sort(Comparator.comparingDouble(House::getPrice));
        System.out.println("Дома отсортированы по цене:");
        printAllHouses();
    }

    // Сортировка по площади
    private static void sortHousesByArea() {
        List<House> houses = houseCollection.getAllHouses();
        houses.sort(Comparator.comparingInt(House::getArea));
        System.out.println("Дома отсортированы по площади:");
        printAllHouses();
    }

    // Сохранение и шифрование данных
    private static void saveAndEncryptData() {
            FileManager.writeEncryptedToTxt("encrypted.txt", houseCollection.getAllHouses(), key);
            System.out.println("Данные успешно зашифрованы и сохранены.");
    }

    // Чтение и расшифровка данных
    private static void readAndDecryptData() {
            List<House> decryptedHouses = FileManager.readDecryptedFromTxt("encrypted.txt", key);
            System.out.println("Дешифрованные данные:");
            houseCollection2.getAllHouses().addAll(decryptedHouses);
            printAllHouses2();
    }

    // Сохранение данных и создание ZIP архива
    private static void saveDataAndCreateZip() {
            FileManager.saveDataWithEncryptionAndZip(houseCollection.getAllHouses());
    }

    // Сохранение данных в файлы
    private static void saveToFile(String txtFilename, String xmlFilename, String jsonFilename) {
        List<House> houses = houseCollection.getAllHouses();
        FileManager.writeToTxt(txtFilename, houses);
        FileManager.writeToXML(xmlFilename, houses);
        FileManager.writeToJSON(jsonFilename, houses);
        System.out.println("Данные успешно сохранены в файлы.");
    }

    // Чтение данных из файлов
    private static void readFromFile(String txtFilename, String xmlFilename, String jsonFilename) {
        Set<House> houseSet = new HashSet<>();
        houseSet.addAll(FileManager.readFromTxt(txtFilename));
        houseSet.addAll(FileManager.readFromXML(xmlFilename));
        houseSet.addAll(FileManager.readFromJSON(jsonFilename));
        houseCollection.getAllHouses().clear();
        houseCollection.getAllHouses().addAll(houseSet);
        System.out.println("Данные успешно прочитаны из файлов.");
        printAllHouses();
    }
}
