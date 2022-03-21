import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainTest {
    GameProgress game1 = new GameProgress(100, 50, 42, 150);
    final String savePath = "src//test//";
    final String testPath = "src//test//Test";

    @Test //Проверка на правильую запись в файл
    public void SaveGame_Test(){
        System.out.println("Тест на запись в файл");
        GameProgress expected = game1;
        Main.saveGame(game1,testPath,"10.test");
        GameProgress result = Main.loadGame(testPath + "10.test");//метод тестируется независимо
        List<String> savePathList1 = new ArrayList<>();
        savePathList1.add(testPath + "10.test");
        Main.deleteFiles(savePathList1); //данный метод также тестируется независимо

        Assertions.assertEquals(expected.toString(),result.toString(),"SaveGame_Test failed");
        System.out.println();
            }

    @Test //Проверка на правильное чтение из файла
    public void loadGame_Test(){
        GameProgress expected = game1;
        GameProgress result = Main.loadGame(savePath + "Save_1.dat");
        System.out.println("Тест на чтение из файла");
        System.out.println(expected);
        System.out.println(result);

        Assertions.assertEquals(expected.toString(),result.toString(),"LoadGame_Test failed ");
        System.out.println();
    }


    @Test//Проверка метода удаления файлов
    public void deleteFiles_Test() throws IOException {
        File dir = new File (testPath);
        if (dir.mkdir()) System.out.println("Каталог создан");

        Files.createFile(Path.of(testPath+"1.temp"));
        Files.createFile(Path.of(testPath+"2.temp"));
        Files.createFile(Path.of(testPath+"3.temp"));

        List<String> savePathList = new ArrayList<>();
        savePathList.add(testPath + "1.temp");
        savePathList.add(testPath + "2.temp");
        savePathList.add(testPath + "3.temp");

        System.out.println("Тест на удаление файла");
        Main.deleteFiles(savePathList);
        File dirTest = new File(testPath);

        Assertions.assertTrue(isDirectoryEmpty(dirTest), "Delete_Test 2 failed ");
        System.out.println();

    }




    public boolean isDirectoryEmpty(File directory) {
        String[] files = directory.list();
        assert files != null;
        return files.length == 0;
    }
}
