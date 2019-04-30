package mydomain.androidjsoup_01;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CinkciarzPoundRate extends RateChecker {
    private String name = "Cinkciarz Kurs Funta";
    private String websiteAddress = "https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/gbp";
    public static void main(String[] args) {
         test();
    }

    /** Main contructor for class CinkciarzPoundRate.
     *   Uses the superclass contructor (called super()).
     */
    public CinkciarzPoundRate()
    {
        super();
    }

    /**
     * The method further specified for obtaining information about
     * Pound Sterling --> Polish Zloty ratio
     * from Cinkiarz.pl website.
     * @return finalResult
     * @throws IOException
     */
    public double kursfuntaKupnoCinkciarz() {
        Element result = null;
        double finalResult = -1;
        try {
            Document doc = Jsoup.connect(websiteAddress).get();
            Element firstQuery = doc.select("div#for-1").first();
            Element firstElement = firstQuery.select("td.cur_down").first();
            result = firstElement;
        }
        catch (Exception e)
        {
            System.out.println("Error has occured in class " + this.getClassName() + " while performing method getPoundRate(). " );
            this.printCharFor('-',20);
            e.printStackTrace();
        }

        String aText = result.ownText();
        String aText1 = aText.replace(',','.');
        finalResult = Double.parseDouble(aText1);
        return finalResult;
    }
    /**
     * The method further specified for obtaining information about
     * Polish Zloty --> Pound Sterling ratio
     * from Cinkiarz.pl website.
     * @return finalResult
     * @throws IOException
     */
    public double kursfuntaSprzedazCinkciarz() {
        Element result = null;
        double finalResult = -1;
        try {
            Document doc = Jsoup.connect(websiteAddress).get();
            Element firstQuery = doc.select("div#for-1").first();
            Element secondElement = firstQuery.select("td.cur_down").next().first();
            result = secondElement;
        }
        catch (Exception e)
        {
            System.out.println("Error has occured in class " + this.getClassName() + " while performing method getPoundRate(). " );
            this.printCharFor('-',20);
            e.printStackTrace();
        }
        String aText = result.ownText();
        String aText1 = aText.replace(',','.');
        finalResult = Double.parseDouble(aText1);
        return finalResult;
    }
    /**
     * Getter method for class specific name. Defined in private String.
     * @return
     */
    private String getClassName()
    {
        return this.name;

    }

    /**
     * Method serves to locate a specific data on website and return it as an String value without any additional unecessery data(mostly TAGs).
     * It uses JSoup library to navigate through HTML&CSS tags to narrow down search and the by using first() or next() to cycle through
     * a single values in a 'branch' of specific code. To get rid of the TAGging use ownText().
     * @return
     * @throws IOException
     */
    String specificElement() throws IOException {
        String result = "";
        //Zczytuje całą stronę w formie HTML&CSS do JSoup'owego dokumentu.
        Document doc = Jsoup.connect("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/gbp").get();

        //Wybiera pierwsze elementy (i tylko te) z gałęzi TAGu [div] z id "for-1"(kurs dla jednego funta).
        Element firstQuery = doc.select("div#for-1").first();

        //Wybiera pierwszy element z [firstQuery] z TAGiem [td] o id "cur_down" (i tylko ten element).
        Element firstElement = firstQuery.select("td.cur_down").first();
        Element secondElement = firstQuery.select("td.cur_down").next().first();

        //Wyciąga sam tekst(informacje) bez oTAGowania HTML czy CSS.
       result = secondElement.ownText();
       return result;                                                                               //Returns String value with ',' instead of typical '.' in number like 4,7654.
    }


    /**
     * Method created to test how the main methods of the class performs.
     */
    private static void test() {
        mydomain.androidjsoup_01.CinkciarzPoundRate ci = new mydomain.androidjsoup_01.CinkciarzPoundRate();
        System.out.println(ci.kursfuntaKupnoCinkciarz() + "(kupno)");                           // 1) Kurs kupna
        System.out.println(ci.kursfuntaSprzedazCinkciarz() + "(sprzedaz)");                     // 2) Kurs sprzedazy
    }
}

