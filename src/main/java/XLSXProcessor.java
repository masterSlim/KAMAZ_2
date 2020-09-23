
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Класс для чтения и обработки файлов формата xlsx (Excel).
 */

public class XLSXProcessor {
    /** Поле с адресом иходного файла. */
    private String file;

    /** Поле с картой пар слов en(ключ)-ru(значение). */
    private HashMap<String, String> dictionary;

    /** Структурированый объект представляющий обрабатываемый xlsx-файл. */
    private XSSFWorkbook source;

    /**
     * Конструктор объекта.
     * @param file - адрес исходного файла xlsx.
     */
    public XLSXProcessor(String file) {
        this.file = file;
        dictionary = new HashMap<>();
    }

    /**
     * Читает файл по переданному в конструктор адресу и создаёт на его основе карту слов.
     * @return карта слов, где en(ключ)-ru(значение).
     * @throws IOException
     */
    public HashMap<String, String> getDictionary() throws IOException {
        // Открытие файла
        source = new XSSFWorkbook(file);

        // Создание итератора для страниц документа
        Iterator<Sheet> sheetIterator = source.sheetIterator();

        // Проход по каждой странице документа
        while (sheetIterator.hasNext()) {
            //Переход на следующую страницу
            Sheet sheet = sheetIterator.next();

            // Создание итератора для строк текущей страницы
            Iterator<Row> rowIterator = sheet.rowIterator();

            // Проход по каждой строке страницы
            while (rowIterator.hasNext()) {
                //Переход на следующую строку
                Row row = rowIterator.next();
                String eng = row.getCell(0).getStringCellValue();
                String rus = row.getCell(1).getStringCellValue();

                // Слово из первой ячейки записывается как ключ, из второй как значение. Более никакие ячейки
                // не обрабатываются
                dictionary.put(eng, rus);
            }
        }

        // Закрытие файла
        source.close();

        //Возврат заполненного "словаря"
        return dictionary;
    }
}
