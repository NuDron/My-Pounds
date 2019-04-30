package mydomain.androidjsoup_01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button getBtn;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            //Tworzy wszystko co widac na ekranie telefonu.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);                                              //Result to okno w którym po nacisnieciu klawisza RESFRESH pokazuje sie wynik wszystkich metod z getWebsite()
        getBtn = (Button) findViewById(R.id.getBtn);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
    }
    public static void main(String [] args)
    {
        getWebsite();
    }
    /** Method that is set OnClickListener.
     *
     */
    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                final HashMap<String,Double> hmap = new HashMap<>();
                HashMap<String, Double> FinalResult = new HashMap<>();

                try {
                    Document doc = Jsoup.connect("http://www.kursfunta.pl/").get();             //Zczytuje stronę z danego adresu używając bibliotek JSoup.
                    String title = doc.title();                                                     // Wrzuca "tytuł" stony zczytanej do Stringu nazwanego title.
                    Elements links = new Elements();                                                //Tworzy nowy obiekt z JSoup'owymi Elementami do późniejszej iteracji w pętli (drukowania).
                   CinkciarzPoundRate ci = new CinkciarzPoundRate();
                    double CinkciarzBuyPoundRate = ci.kursfuntaKupnoCinkciarz();

                    //Pozycja dodana do mapy(hmap) z daną wyciągniętą z metody(CinkciarzBuyPoundRate).
                    hmap.put("Cinkciarz",CinkciarzBuyPoundRate);
                    //Zmyślone dane x4 aby sprawdzić czy sortowanie hashmapy działa.
                    hmap.put("Kantor Zmyslony", 4.0);
                    hmap.put("Kantor Pyskowy", 4.9);
                    hmap.put("Kantor Kasiowy", 4.8);
                    hmap.put("Kantor Polski", 4.7);

                    //Zainicjowanie HashMapSorter'a do testów
                    HashMapSorter SuperSorterr = new HashMapSorter();
                    FinalResult = SuperSorterr.sortByValue(hmap);

                    //String builder w obecnej formie jest nieużywany. Był wczesniej potrzeby do wygenerowania długiego "pliku tekstowego", który miał być wyświetlony w metodzie run() ponizej.
                    builder.append(title).append("\n");
                    for (Element link : links) {
                        builder.append("\n").append("Link : ").append(link.attr("span"))
                                .append("\n").append("Text : ").append(link.text());
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                final HashMap<String, Double> finalFinalResult = FinalResult;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Drukuję listę dla porównania przed i po sortowaniu.
                        //Wykorzystuję System.lineSeparator() do utworzenia nowej linii.
                        result.setText("Przed sortowaniem: " + System.lineSeparator() +  hmap.toString() + System.lineSeparator() + System.lineSeparator() +  "Po sortowaniu: " + "\n" + finalFinalResult.toString());
                    }
                });
            }
        }).start();                                                                                 // <<-- IMPORTANT <<--
    }
}