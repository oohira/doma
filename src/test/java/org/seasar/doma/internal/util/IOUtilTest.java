/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;

/** @author taedium */
public class IOUtilTest extends TestCase {

  public void test() throws Exception {
    IOUtil.close(
        new Closeable() {

          @Override
          public void close() throws IOException {
            throw new IOException();
          }
        });
  }

  public void testEndWith_true() throws Exception {
    File file = new File("/fuga/META-INF/piyo/HogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertTrue(IOUtil.endsWith(file, pathname));
  }

  public void testEndWith_false() throws Exception {
    File file = new File("/fuga/META-INF/piyo/hogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertFalse(IOUtil.endsWith(file, pathname));
  }
}
