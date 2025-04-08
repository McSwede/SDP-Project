package org.grupp2.sdpproject.Utils;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PictureUtil {
    private static final int MAX_IMAGE_SIZE = 65535; // 65KB max for BLOB
    private static final int INITIAL_TARGET_WIDTH = 200;
    private static final int INITIAL_TARGET_HEIGHT = 200;

    public static byte[] handleImageUpload(Window ownerWindow) throws IOException {
        FileChooser fileChooser = createImageFileChooser();
        File selectedFile = fileChooser.showOpenDialog(ownerWindow);

        if (selectedFile != null) {
            return processAndCompressImage(selectedFile);
        }
        return null;
    }

    public static Image byteArrayToImage(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        return new Image(new ByteArrayInputStream(imageData));
    }

    private static FileChooser createImageFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        return fileChooser;
    }

    private static byte[] processAndCompressImage(File imageFile) throws IOException {
        long fileSize = Files.size(imageFile.toPath());
        if (fileSize > MAX_IMAGE_SIZE) {
            return compressImageToSizeLimit(imageFile);
        }
        return Files.readAllBytes(imageFile.toPath());
    }

    private static byte[] compressImageToSizeLimit(File imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        BufferedImage resizedImage = originalImage;

        int targetWidth = INITIAL_TARGET_WIDTH;
        int targetHeight = INITIAL_TARGET_HEIGHT;
        float quality = 0.8f;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] imageData;

        while (quality > 0.1f) {
            if (originalImage.getWidth() > targetWidth || originalImage.getHeight() > targetHeight) {
                resizedImage = resizeImage(originalImage, targetWidth, targetHeight);
            }

            outputStream.reset();
            ImageIO.write(resizedImage, "jpg", outputStream);
            imageData = outputStream.toByteArray();

            if (imageData.length <= MAX_IMAGE_SIZE) {
                return imageData;
            }

            quality -= 0.1f;
            targetWidth = (int)(targetWidth * 0.9);
            targetHeight = (int)(targetHeight * 0.9);
        }

        // Try PNG as fallback since it for some reason is smaller on random
        outputStream.reset();
        ImageIO.write(resizedImage, "png", outputStream);
        imageData = outputStream.toByteArray();

        return imageData.length <= MAX_IMAGE_SIZE ? imageData : null;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        double ratio = Math.min(
                (double) targetWidth / originalImage.getWidth(),
                (double) targetHeight / originalImage.getHeight()
        );
        int newWidth = (int) (originalImage.getWidth() * ratio);
        int newHeight = (int) (originalImage.getHeight() * ratio);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }
}