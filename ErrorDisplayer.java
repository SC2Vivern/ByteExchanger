package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ErrorDisplayer extends JFrame //klasa odpowiadajaca za wyswietlania okienka z bledem
{

    ErrorDisplayer(String err) // konstruktor z parametrem- tekstem do wyswietlenia
    {
        JFrame f = new JFrame(); //utworzenie nowej ramki
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                f.dispose();
            }
        });//dodanie listenera do zamykania okna

        setSize(300,100);//ustawienie rozmiaru okna

        Label errorLabel = new Label (err);//stworzenie etykiety z napisem opisujacym blad
        add(errorLabel);//dodanie etykiety

        setVisible(true);//wlaczenie widocznosci okienka
    }
}
