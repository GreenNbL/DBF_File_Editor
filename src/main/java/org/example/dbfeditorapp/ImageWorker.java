package org.example.dbfeditorapp;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageWorker {

    private static Image getFileImage(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return null; // Если файл не существует, возвращаем null
        }
        // Получаем системный вид файловой системы
        FileSystemView fsv = FileSystemView.getFileSystemView();
        // Получаем иконку файла
        return iconToImage(fsv.getSystemIcon(file));
    }
    private static Image iconToImage(Icon icon) {
        // Create a BufferedImage to draw the icon
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // Paint the icon onto the BufferedImage
        Graphics g = bufferedImage.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        System.out.println("Image is " + icon);
        // Convert BufferedImage to JavaFX Image
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    public static void setImage(String filePath, StackPane dropArea)
    {
        Image image = getFileImage(filePath);
        ImageView imageView = new ImageView(image);
       // imageView.setFitWidth(64);  // Устанавливаем ширину
        //imageView.setFitHeight(64);  // Устанавливаем высоту
        imageView.setFitWidth(image.getWidth() * 2);
        imageView.setFitHeight(image.getHeight() * 2);
        imageView.setPreserveRatio(true); // Сохраняем пропорции
        Label caption = new Label(getNameFile(filePath));
        caption.setTranslateY(30);
        dropArea.getChildren().clear();
        dropArea.getChildren().addAll(imageView,caption);
        //dropArea.getChildren().get(0).setVisible(false);
    }
    private static String getNameFile(String filePath) {
        return new File(filePath).getName();
    }



}
