# KAMAZ_2
Автор Алексеенко Иван
+7(937)290 26 97
master.slim@rambler.ru

Задание:
"Необходимо написать программу, которая соотнесёт перевод фраз (словарь в Excel документе 'ViewerMessages.xlsx') к английским фразам XML-документа ('ViewerMessages.xml')
Пример:
<message id="1">
  <variant language="en_US>Customer</variant>
  <variant language="ru_RU">Клиент</variant>
</message>"

Решение написано на Java 8. через Maven для работы с XLSX подключена удалённая библиотека Apache POI. 
Для запуска приложения в методе main() класса Main поменяйте значения полей xlsxFile и xmlFile на свои.
Код полностью откомментирован.
