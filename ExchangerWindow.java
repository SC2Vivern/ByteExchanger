package com.company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.FileOutputStream;



public class ExchangerWindow extends JFrame  {


    public static TextField extension; //pole tekstowe przyjmujace rozszerzenie
    public static TextField byteIn; //pole tekstowe przyjmujace ciag bitow szukanych
    public static TextField byteOut; //pole tekstowe przyjmujace ciag bitow na wymiane
    public static String path;
    FolderBrowser fb; //zainicjowanie obiektu FolderBrowser





    ExchangerWindow() //konstruktor
    {
        JFrame f = new JFrame();//nowa ramka
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//sprawia, ze aplikacja zamknie sie po zamknieciu okienka
        //super.setTitle("ByteExchanger");
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {


                f.dispose();
            }
        });//listener do zamkniecia okna


        f.setLayout(new GridLayout(5,2));//ustawienie layoutu


        extension= new TextField();//stworzenie pola tesktowego
        byteIn= new TextField();//stworzenie pola tesktowego
        byteOut= new TextField();//stworzenie pola tesktowego
        Label folderLabel= new Label("Wybierz folder");//stworzenie etykiety
        Label extLabel= new Label("Wpisz wybrane rozszerzenie (bez kropki np. 'txt')");//stworzenie etykiety
        Label inLabel= new Label("Ciąg bajtów szukanych (wartości od -128 do 127, bajty oddzielamy spacją)");//stworzenie etykiety
        Label outLabel= new Label("Ciąg bajtów na podmianę (wartości od -128 do 127, bajty oddzielamy spacją)");//stworzenie etykiety

        Button b = new Button("Podmień");//stworzenie guzika do podmiany ciagow
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                String s1, s2, s3;//zmienne przyjmujace ciagi danych wpisanych przez uzytkownika
                s1=null;
                s2=null;
                s3=null;
                String []split2 = null; //tablica stringow dzielaca ciag bajtow szukanych na pojedyncze bajty
                String []split3 = null; //tablica stringow dzielaca ciag bajtow na wymiane na pojedyncze bajty
                byte []b2 = null; //tablica bajtow szukanych
                byte []b3 = null; //tablica bajtow na wymiane


                //przypisywanie wartosci pol tekstowych do zmiennych
                s1 = extension.getText();
                s2 = byteIn.getText();
                s3 = byteOut.getText();


              /*  try{
                System.out.println(fb.path);

                File root = new File(fb.path);}
                catch(Exception errx){ErrorDisplayer errdisp = new ErrorDisplayer("BŁĄD: brak wybranej ścieżki");}*/

                try{
                split2 = s2.split(" ");//rozdzielanie wejsciowego pliku bajtow szukanych
                b2 = new byte[split2.length];//tworzenie tablicy bajtow o dlugosci podanego ciagu

                    for (int i=0; i<split2.length; i++)
                {
                    //System.out.println(split2[i]);
                    b2[i]=(byte)Integer.parseInt(split2[i]);//dla kazdego podanego bajta zamieniamy danego stringa na inta a nastepnie rzutujemy go na byte i dodajemy do tablicy bajtow b2

                }
                    //System.out.println(Arrays.toString(b2));

                }
                catch(Exception es){
                    System.out.println(es);
                    ErrorDisplayer errdisp = new ErrorDisplayer("BŁĄD: błędny ciąg bitów szukanych");//okienko wyskakujace jezeli ciag bajtow jest bledny
                }

                //analogiczne operacje dla ciagu bajtow na wymiane
                try{
                    split3 = s3.split(" ");
                    b3 = new byte[split3.length];

                    for (int i=0; i<split3.length; i++)
                    {
                        //System.out.println(split3[i]);
                        b3[i]=(byte)Integer.parseInt(split3[i]);
                    }
                    //System.out.println(Arrays.toString(b3));

                }
                catch(Exception est){
                    System.out.println(est);
                ErrorDisplayer errdisp = new ErrorDisplayer("BŁĄD: błędny ciąg bitów wymiennych");
                }


                try {
                    List<String> files = findFiles(Paths.get(fb.path), s1);//lista plikow tworzona przez przeszukania podanego folderu i jego podfolderow
                    //files.forEach(x -> System.out.println(x));
                    for(String f : files)//dla kazdego z plikow na liscie files
                    {
                        Path path = Paths.get(f);//tworzenie sciezki ze sciezki podanej stringiem
                        System.out.println(f);
                        byte[] fileContent = Files.readAllBytes(path);//zapelnienie tablicy bajtowej bajtami z pliku
                        System.out.println(Arrays.toString(fileContent));
                        System.out.println(Arrays.toString(b2));
                        //jezeli ciag bajtow w pliku jest dluzszy lub rowny szukanemu ciagowi to kontynuujemy
                        if (fileContent.length>=b2.length){
                        ArrayList<Integer>sequenceCheck = indexOf(fileContent, b2);//sprawdzamy czy sekwencja wystepuje w danym pliku
                        System.out.println(sequenceCheck.toString());
                        //System.out.println(sequenceCheck.size());
                        if (sequenceCheck.size()!=0){
                            int m= 0;//zmienna uzywana do ustawiania pozycji w nowej sekwencji do zapisu
                            byte []newSeq = new byte[fileContent.length+(sequenceCheck.size()*(b3.length-b2.length))];//okreslanie dlugosci nowej sekwencji

                            for(int i= 0; i<fileContent.length; i++)//tworzenie nowej tablicy bajtow
                            {
                                if (sequenceCheck.contains(i))
                                {
                                    for (int j=0; j<b3.length; j++)
                                    {
                                        newSeq[j+m]=b3[j];
                                    }
                                    i=i+b2.length-1;
                                    m=m+b3.length;
                                }
                                else{newSeq[m]=fileContent[i];
                                    m++;
                                }

                            }
                            System.out.println(Arrays.toString(newSeq));
                            try (FileOutputStream fos = new FileOutputStream(f)) {
                                fos.write(newSeq);//zapisywanie nowej tablicy bajtow do konkretnego pliku

                            }catch(Exception excas){System.out.println(excas);
                                ErrorDisplayer erant= new ErrorDisplayer("błąd dla: " + f);//informacja zwrotna dla uzytkownika

                            }

                        }
                        }

                    }

                    ErrorDisplayer eran= new ErrorDisplayer("Wymiana udana!");//informacja zwrotna dla uzytkownika

                }
                catch(Exception easd){System.out.println(easd);}



            }
        });

        Button folderChoice = new Button (". . .");//tworzymy guzik wywolujacy eksplorator folderow do wyboru
        folderChoice.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                fb= new FolderBrowser();

            }
        });//dodajemy listener do guzika, wywolujacy nowe okienko z eksploratorem

        f.add(folderLabel);
        f.add(folderChoice);

        f.add(extLabel);
        f.add(extension);
        f.add(inLabel);
        f.add(byteIn);
        f.add(outLabel);
        f.add(byteOut);
        f.add(b);
// dodajemy wszystkie elementy do okna
        f.setSize(900, 400);//ustawiamy wielkosc okna
        //f.pack();
        f.setVisible(true);//umozliwiamy wyswietlanie okna


    }

    public static List<String> findFiles(Path path, String fileExtension)//funkcja tworzaca liste plikow z folderu i podfolderow
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<String> result;

        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(p -> !Files.isDirectory(p))
                    // this is a path, not string,
                    // this only test if path end with a certain path
                    //.filter(p -> p.endsWith(fileExtension))
                    // convert path to string first
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith(fileExtension))
                    .collect(Collectors.toList());
        }

        return result;
    }

    public ArrayList<Integer> indexOf(byte[] outerArray, byte[] smallerArray) //funkcja sprawdzajaca czy dany ciag zawiera sie w drugim, zwraca tablice indeksow, dla ktorych znaleziono poczatek szukanej sekwencji
    {
        ArrayList<Integer> indexArray = new ArrayList<Integer>();
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found){
                indexArray.add(i);

            }
        }

        return indexArray;

    }






}