package ru.redsoft.mvv;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Random;

public class StubFileGenerator {
  private static final int CHUNK_SIZE = 1024*1024;

  private final Path file;

  public StubFileGenerator(String fileName) {
    this.file = Paths.get(fileName);
  }

  public long write(long byteCount) throws IOException {
    long count = 0;
    Random rnd = new Random();
    byte[] bytes = new byte[CHUNK_SIZE];
    try (SeekableByteChannel channel = Files.newByteChannel(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
      while (byteCount > count) {
        rnd.nextBytes(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        channel.write(buffer);
        count += CHUNK_SIZE;
      }
    }

    return Files.size(file);
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      printUsage();
      System.exit(1);
    }

    String file = args[0];
    Long count = Long.valueOf(args[1]);

    long time = new Date().getTime();

    long size = new StubFileGenerator(file).write(count * FileUtils.ONE_MB);
    System.out.println(size);
    System.out.println("Time: " + (new Date().getTime() - time) + " ms");
  }

  public static void printUsage() {
    System.out.println("Enter file path and size (Mb)");
  }
}
