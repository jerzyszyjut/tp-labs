package com.lab6;

import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments provided");
            return;
        }

        String dirPath = args[0];
        String outputPath = args[1];
        int threads = Integer.parseInt(args[2]);

        List<Path> files;
        Path source = Path.of(dirPath);
        try {
            Stream<Path> stream = Files.list(source);
            files = stream.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ForkJoinPool customThreadPool = new ForkJoinPool(threads);

        long time = System.currentTimeMillis();

        try {
            customThreadPool.submit(() ->
                    files.parallelStream()
                            .map(path -> {
                                try {
                                    BufferedImage image = ImageIO.read(path.toFile());
                                    return Pair.of(path.getFileName().toString(), image);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .map(pair -> {
                                BufferedImage original = pair.getRight();
                                BufferedImage transformedImage = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

                                for (int i = 0; i < original.getWidth(); i++) {
                                    for (int j = 0; j < original.getHeight(); j++) {
                                        int rgb = original.getRGB(i, j);
                                        Color color = new Color(rgb);

                                        int red = color.getBlue();
                                        int green = color.getGreen();
                                        int blue = color.getRed();

                                        Color outColor = new Color(red, green, blue);
                                        transformedImage.setRGB(i, j, outColor.getRGB());
                                    }
                                }

                                return Pair.of(pair.getLeft(), transformedImage);
                            })
                            .forEach(pair -> {
                                try {
                                    ImageIO.write(pair.getRight(), "png", Path.of(outputPath, pair.getLeft()).toFile());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000.0 + "s");

        customThreadPool.shutdown();
    }
}