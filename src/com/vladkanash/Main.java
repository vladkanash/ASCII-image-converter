package com.vladkanash;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static AlphabetHolder alphabetHolder;
    private static int outputHeight;
    private static int outputWidth;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide path to image file");
            return;
        }

        final String alphabet = args.length <= 1 ? Constants.DEFAULT_ALPHABET : args[1];
        final String imagePath = args[0];
        BufferedImage image;

        alphabetHolder = new AlphabetHolder(alphabet);

        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        outputHeight = (int)(image.getHeight() * Constants.HEIGHT_SCALE * Constants.SIZE_SCALE);
        outputWidth = (int)(image.getWidth() * Constants.SIZE_SCALE);

        final byte[] pixelData = getImageData(image);
        final char[] output = processPixels(pixelData);
        writeFile(output);
    }

    private static byte[] getImageData(BufferedImage image) {
        final int height = image.getHeight();
        final int width = image.getWidth();
        final BufferedImage rescaledImage =
                new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_BYTE_GRAY);
        final Graphics rescaledGraphics = rescaledImage.getGraphics();
        rescaledGraphics.drawImage(image, 0, 0, outputWidth, outputHeight,  null);
        rescaledGraphics.dispose();

        final WritableRaster raster = rescaledImage.getRaster();
        return ((DataBufferByte) raster.getDataBuffer()).getData();
    }

    private static char[] processPixels(byte[] pixelData) {
        int pixelCount = pixelData.length;
        final int[] intPixelData = new int[pixelCount];
        for (int i = 0; i < pixelCount; i++) {
            intPixelData[i] = pixelData[i] & 0xff;
        }

        final int step = alphabetHolder.getStep();
        final int size = alphabetHolder.getSize();
        final String alphabet = alphabetHolder.getAlphabet();
        final int[] processedData = Arrays.stream(intPixelData)
                .map(e -> (int) Math.floor(e / step))
                .map(e -> e >= size ? size - 1 : e)
                .map(alphabet::charAt)
                .toArray();

        final char[] output = new char[pixelCount];
        for (int i = 0; i < pixelCount; i++) {
            output[i] = (char) processedData[i];
        }
        return output;
    }

    private static void writeFile(char[] output) {
        int offset = 0;
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File("output.txt")))) {
            for (int i = 0; i < outputHeight; i++) {
                out.write(output, offset, outputWidth);
                offset += outputWidth;
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
