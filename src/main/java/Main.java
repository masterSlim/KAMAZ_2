import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Главный класс приложения.
 */
public class Main {
    /**
     * Запускает приложение. Никакх входных параметров не требуется.
     * @param args
     */
    public static void main(String[] args){
        /** Объект, отвечающий за обработку файлов XLSX*/
        XLSXProcessor xlsxProcessor = null;

        /** Объект, отвечающий за обработку файлов XML*/
        XMLProcessor xmlProcessor = null;

        /** Карта хранящая пары слов en(ключ)-ru(значение). Заполняется объектом xlsxProcessor*/
        HashMap<String, String> dictionary = null;

        /** Поле с адресом файла-словаря xlsx*/
        String xlsxFile = "E:\\Java\\JavaProjects\\KAMAZ 2\\src\\main\\resources\\ViewerMessages.xlsx";

        /** Поле с адресом заполняемого файла xml*/
        String xmlFile = "E:\\Java\\JavaProjects\\KAMAZ 2\\src\\main\\resources\\ViewerMessages.xml";

        /** Поле с адресом сохранения заполненного файла xml*/
        String xmlFileFilled = xmlFile.substring(0, xmlFile.length()-4) + "_filled.xml";


        try {
            // Создание объекта для работы с xlsx
            xlsxProcessor = new XLSXProcessor(xlsxFile);
            // Получение карты-словаря и присвоение его dictionary
            dictionary = xlsxProcessor.getDictionary();
        } catch (IOException e){
            System.err.println("Невозможно прочитать файл Excel:\n");
            System.err.println(e.getMessage());
        }
        try {
            // Создание объекта для работы с xml
            xmlProcessor = new XMLProcessor(xmlFile, dictionary);
            // Чтение файла
            xmlProcessor.read();
            // Заполнение из соответствующими словами карты-словаря dictionary
            xmlProcessor.process();
        } catch (IOException | SAXException | ParserConfigurationException e){
            System.err.println("Невозможно прочитать файл XML:\n");
            System.err.println(e.getMessage());
        }
        try {
            // Сохранение файла под новым названием
            xmlProcessor.write(xmlFileFilled);
        } catch (TransformerException e){
            System.err.println("Невозможно записать файл " + xmlFileFilled + ":\n");
            System.err.println(e.getMessage());
        }

        System.out.println("Файл успешно заполнен и сохранён под именем " + xmlFileFilled);
    }
}
