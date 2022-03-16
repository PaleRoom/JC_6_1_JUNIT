import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        final String savePath = "F://Games//savegames//";

        GameProgress game1 = new GameProgress(100, 50, 42, 150);
        GameProgress game2 = new GameProgress(33, 25, 71, 1500);
        GameProgress game3 = new GameProgress(75, 13, 58, 666);

        //Задание 1 Сохраняем прогресс в файл сохранения
        saveGame(game1, savePath, "save1.dat");
        saveGame(game2, savePath, "save2.dat");
        saveGame(game3, savePath, "save3.dat");

        List<String> savePathList = new ArrayList<>();
        savePathList.add(savePath + "save1.dat");
        savePathList.add(savePath + "save2.dat");
        savePathList.add(savePath + "save3.dat");

        //Задание 2 Пакуем в .Zip
         zipFiles(savePath + "Saves.zip", savePathList);

        deleteFiles(savePathList);

        //Задание 3 Распаковываем
        extractZip(savePath + "Saves.zip", savePath);

        //Загрузка и печать прогресса
        GameProgress loaded = loadGame(savePath + "Save_1.dat");
        System.out.println(loaded);

    }


    //Метод для сохранения прогресса
    public static void saveGame(GameProgress progress, String path, String saveName) {
        //открытие выходного потока для записи в файл
        try (FileOutputStream fos = new FileOutputStream(path + saveName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            //запись экземпляра класса в файл
            oos.writeObject(progress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Метод для создания zip
    public static void zipFiles(String zipPath, List<String> savePath) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath));

        for (int i = 0; i < savePath.size(); i++) {
            String filename = savePath.get(i);
            FileInputStream fis = new FileInputStream(filename);
            ZipEntry entry = new ZipEntry("Save_" + (i + 1) + ".dat");
            zout.putNextEntry(entry);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zout.write(buffer);
            zout.closeEntry();
            fis.close();
        }
        zout.close();
    }


    //Метод для удаления файлов
    public static void deleteFiles(List<String> savePath) {

        for (String save : savePath) {
            File file = new File(save);
            if (file.delete()) System.out.println("Файл " + file.getName() + " удален.");
            else System.out.println("Файл " + file.getName() + " не удален.");
        }
    }

    //Метод распаковки zip
    public static void extractZip(String zipPath, String savePath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            long size;
            while ((entry = zin.getNextEntry()) != null) {

                name = entry.getName(); // получим название файла
                size = entry.getSize();  // получим его размер в байтах
                System.out.printf("File name: %s \t File size: %d \n", name, size);

                // распаковка
                FileOutputStream fout = new FileOutputStream(savePath + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }
    }

    //Метод загрузки прогресса игры
    public static GameProgress loadGame(String saveFile) {
        GameProgress load = null;
        //откроем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(saveFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            //десериализуем объект и скастим его в класс
            load = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return load;
    }

}
