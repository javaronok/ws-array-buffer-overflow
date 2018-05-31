/*
 * Copyright (C) 2018 Red Soft Corporation.
 *
 * This file is part of Red NCore.
 *
 * Red NCore is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * Red NCore is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Red NCore; see the file COPYING.  If not, write to the
 * Red Soft Corporation, 117186, Russia, Moscow, Nagornaya St, 5.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */

package ru.redsoft.mvv;

import com.sun.xml.ws.util.ByteArrayBuffer;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayBufferOverflow {
  private static String TEST_1MB_NAME = "test1m";
  private static String TEST_2MB_NAME = "test2m";
  private static String TEST_1GB_NAME = "test1g";

  @Before
  public void prepareTestFile() throws IOException {
    findOrCreate(TEST_1MB_NAME, FileUtils.ONE_MB);
    findOrCreate(TEST_2MB_NAME, 2 * FileUtils.ONE_MB);
    findOrCreate(TEST_1GB_NAME, FileUtils.ONE_GB);
  }

  @After
  public void clearTestFile() throws IOException {
    clearExistsFile(TEST_1MB_NAME);
    clearExistsFile(TEST_2MB_NAME);
    clearExistsFile(TEST_1GB_NAME);
  }

  private boolean clearExistsFile(String fileName) throws IOException {
    File f = new File(fileName);
    return f.exists() && f.delete();
  }

  private File findOrCreate(String fileName, long size) throws IOException {
    File f = new File(fileName);
    if (!f.exists() || f.length() < size) {
      clearExistsFile(fileName);
      new StubFileGenerator(fileName).write(size);
    }
    return f;
  }

  private void readFile(String file) throws IOException {
    ByteArrayBuffer buffer = new ByteArrayBuffer();
    try (InputStream is = new FileInputStream(file)) {
      buffer.write(is);
    }
    buffer.close();
  }

  @Test
  public void test1m() throws IOException {
    readFile(TEST_1MB_NAME);
  }

  @Test
  public void test2m() throws IOException {
    readFile(TEST_2MB_NAME);
  }

  @Test
  public void test1g() throws IOException {
    try {
      readFile(TEST_1GB_NAME);
    } catch (IllegalStateException e) {
      Assert.assertNotNull(e);
    }
  }
}
