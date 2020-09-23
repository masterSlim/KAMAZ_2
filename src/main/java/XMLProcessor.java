import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NodeList;

import java.util.HashMap;

/**
 * Класс для чтения, обработки и сохранения файлов формата xml
 */

public class XMLProcessor {
    /** Поле с адресом иходного файла. */
    String file;

    /** Поле с картой пар слов en(ключ)-ru(значение). */
    private HashMap<String, String> dictionary;

    //Вспомогательные объекты библиотеки javax
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;

    /** Структурированый объект представляющий обрабатываемый xml-файл. */
    private Document doc;

    /**
     * Конструктор объекта.
     * @param file - адрес исходного файла xml.
     * @param dictionary - словарь с парами слов en-ru для перевода.
     */
    public XMLProcessor(String file, HashMap dictionary) {
        this.file = file;
        this.dictionary = dictionary;
    }

    /**
     * Читает файл по переданному в конструктор адресу и создаёт на его основе структурированный объект Document.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void read() throws ParserConfigurationException, IOException, SAXException {
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(file);

    }

    /**
     * Обрабатывает документ из объекта doc. Соответствующим словам (ключ) из элемента variant с аттрибутом language = en_EN
     * из карты dictionary подставляется перевод (значение) в следующий за ним тег variant если: 1) он имеет аттрибут
     * language=ru_RU и 2) оба эти элемента относятся к одному и тому же родительскому узлу (message). По мере обработки
     * обновляется исходный объект doc.
     */

    public void process(){
        String en = "";
        String ru = "";

        // Получение списка всех узлов под тегом variant
        NodeList variants = doc.getElementsByTagName("variant");

        // Проход по каждому элементу списка
        for (int i = 0; i < variants.getLength(); i++) {

        // Проеверка на соответствие условиям
            if ("en_US".equals(variants.item(i).getAttributes().getNamedItem("language").getTextContent())
                    && "ru_RU".equals(variants.item(i + 1).getAttributes().getNamedItem("language").getTextContent())
                    && variants.item(i).getParentNode().isEqualNode(variants.item(i + 1).getParentNode())) {
                en = variants.item(i).getTextContent();

                // Если проверка успешна и для слова есть перевод, то он вставляется в следующий за текущим эелемент
                // и он пропускается в цикле
                if (dictionary.containsKey(en)) {
                    ru = dictionary.get(en);
                    variants.item(i + 1).setTextContent(ru);
                    i++;
                }

                // Иначе выводится сообщение. Программа продолжает работу
                else {
                    System.out.println("Для слова " + en + " соответствия в файле-словаре не найдено.");
                }
            }
        }
    }

    /**
     * Запись файла хранящегося в поле doc.
     *
     * @param newFile — имя сохраненяемого файла.
     * @throws TransformerException
     */

    public void write(String newFile) throws TransformerException {
        // Создание объектов требуемых для сохранения файла
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource docDom = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(newFile));

        // Сохранение документа в конечный файл
        transformer.transform(docDom, result);
    }
}

