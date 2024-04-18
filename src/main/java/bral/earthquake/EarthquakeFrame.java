package bral.earthquake;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import bral.earthquake.json.Feature;
import bral.earthquake.json.FeatureCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.function.Function;

public class EarthquakeFrame extends JFrame {

    private JList<String> jlist = new JList<>();
    private EarthquakeService service;
    private FeatureCollection featureCollection;

    public EarthquakeFrame() {

        setTitle("EarthquakeFrame");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());



        service = new EarthquakeServiceFactory().getService();

        // panel for radio buttons:
        JPanel radioPanel = new JPanel();

        // group together
        ButtonGroup radioGroup = new ButtonGroup();

        JRadioButton oneHourButton = new JRadioButton("One Hour");
        radioGroup.add(oneHourButton);
        radioPanel.add(oneHourButton);
        JRadioButton thirtyDaysButton = new JRadioButton("30 Days");
        radioGroup.add(thirtyDaysButton);
        radioPanel.add(thirtyDaysButton);

        add(radioPanel, BorderLayout.NORTH);

        // action listeners:
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (oneHourButton.isSelected()) {
                    Disposable disposable = service.oneHour()
                            // tells Rx to request the data on a background Thread
                            .subscribeOn(Schedulers.io())
                            // tells Rx to handle the response on Swing's main Thread
                            .observeOn(SwingSchedulers.edt())
                            //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                            .subscribe(
                                    (response) -> handleResponse(response),
                                    Throwable::printStackTrace);

                } else if (thirtyDaysButton.isSelected()) {
                    Disposable disposable = service.significantMonth()
                            // tells Rx to request the data on a background Thread
                            .subscribeOn(Schedulers.io())
                            // tells Rx to handle the response on Swing's main Thread
                            .observeOn(SwingSchedulers.edt())
                            //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                            .subscribe(
                                    (response) -> handleResponse(response),
                                    Throwable::printStackTrace);
                }
            }
        };

        oneHourButton.addActionListener(radioListener);
        thirtyDaysButton.addActionListener(radioListener);


        // selection listener
        jlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = jlist.getSelectedIndex();
                    Feature selectedFeature = featureCollection.features[selectedIndex];
                    double longitude = selectedFeature.geometry.coordinates[0];
                    double latitude = selectedFeature.geometry.coordinates[1];
                        String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                    try {
                        Desktop.getDesktop().browse(new URI(googleMapsUrl));
                    } catch (IOException | URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        add(jlist, BorderLayout.CENTER);
    }


    private void handleResponse(FeatureCollection response) {
        featureCollection = response;
        String[] listData = new String[response.features.length];
        for (int i = 0; i < response.features.length; i++) {
            Feature feature = response.features[i];
            listData[i] = feature.properties.mag + " " + feature.properties.place;
        }
        jlist.setListData(listData);
    }



    public static void main(String[] args) {
        new EarthquakeFrame().setVisible(true);
    }

}