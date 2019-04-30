package mydomain.androidjsoup_01;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public abstract class RateChecker {
    String name = "RateChecker";
    String websiteAddress = "";
    /**
     * Getter method for class specific name. Defined in private String.
     * @return
     */
    private String getClassName()
    {
        return this.name;

    }
    /**
     * The method gets a first found Object Element from a specified website(anUrl) which satisfies a given CSS query requirement(aQuery).
     * The method uses JSoup libraries to perform its tasks.
     * @return
     * @throws IOException
     */
    String getWebsiteAddress()
    {
        return this.websiteAddress;
    }
    public Element getFirstWebsiteElement(String anUrl, String aQuery) throws IOException {
        Element ele = null;
        try
        {
            //Obtain an website in HTML&CSS form from a designated website address in method's header.
            Document doc = Jsoup.connect(anUrl).get();
            //Narrow down selection in object ele to a first item with a Tag from aQuery.
            ele = doc.select(aQuery).first();
            return ele;

        }
        catch (Exception e)
        {
            System.out.println("Error has occured in class " + this.getClassName() + " while performing method getFirstWebsiteElement(). " );
            printCharFor('_',20);
            e.printStackTrace();
        }
        return ele;
    }

    /**
     * Method to print in one line a set amount (int x) of specified char (char ch).
     * @param ch Type of char we want to print many times
     * @param x How many times we want to print specific char
     */
    void printCharFor(char ch, int x)
    {
        if(x > 0) {

            for (int i = 0; i < x;i++)
            {
                System.out.print(ch);
            }
            System.out.println(" ");
        }
    }

    /**
     * Simple method that utilize the printCharFor in more precise form.
     */
    void printDefaultStringBreak()
    {
        printCharFor('-',20);
    }

    /**
     * Method very similar to getWebsiteElement, but it return a second element from the narrowed down search with a specified Tag.
     * @param anUrl
     * @param aQuery
     * @return
     * @throws IOException
     */
    public Element getSecondWebsiteElement(String anUrl, String aQuery) throws IOException {
        Element ele = null;
        try {
            Document doc = Jsoup.connect(anUrl).get();
            ele = doc.select(aQuery).next().first();
            return ele;

        } catch (Exception e) {
            System.out.println("Error has occured in class " + this.getClassName() + " while performing method getPoundRate(). ");
            printCharFor('_', 20);
            e.printStackTrace();
        }
        return ele;

    }
    /**
     * Najpierw trzeba nawigowac po zewnetrznych folderach z CSS czyli [div.tab-pane] i wybrac wlasciwy. Dopiero potem w tym "folderze"
     * szukamy intersujacej nas liczbt(kursu walut).
     * @return
     * @throws IOException
     */
    public Element getWebsiteElements() throws IOException
    {
        Element result = null;
        String anUrl = "https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/gbp";
        String aQuery = "div.tab-pane";
        String aQuery1 = "td.cur_down";
        try{
            Document doc = Jsoup.connect(anUrl).get();
            result = doc.select(aQuery).next().select(aQuery1).first();
        }
        catch (Exception e)
        {
            System.out.println("Error has occured in class " + this.getClassName() + " while performing method getPoundRate(). " );
            this.printCharFor('-',20);
            e.printStackTrace();
        }
        return result;
    }
}

