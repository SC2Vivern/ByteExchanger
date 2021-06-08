package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FolderBrowser extends JFrame
{


    public static String path = null;//string przechowujacy sciezke wybranego folderu


    FolderBrowser()//konstruktor
    {
        JFrame f = new JFrame();//tworzymy nowa ramke
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                f.dispose();
            }
        });//listener odpowiadajacy za zamykanie okna

        setLayout(new BorderLayout());//ustawienie odpowiedniego layoutu
        JFileChooser jc = new JFileChooser();//stworzenie nowego eksploratora plikow
        jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//eksplorator widzi tylko foldery, nie widzi plikow

        JButton wybor = new JButton("wybierz folder");//dodanie guzika do wyboru folderu
        wybor.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

                try{
                path = jc.getSelectedFile().getAbsolutePath();}//przekazanie sciezki wybranego folderu do zmiennej path
                catch(Exception err){System.out.println(err);}
                f.dispose();
            }
        });//dodanie listenera do guzika wyboru folderu



        //ustawienia polozenia guzika i eksploratora oraz wielkosci okna
        f.add(jc, BorderLayout.CENTER);
        f.add(wybor, BorderLayout.SOUTH);
        f.setSize(600, 400);
        f.setVisible(true);


    }


}
