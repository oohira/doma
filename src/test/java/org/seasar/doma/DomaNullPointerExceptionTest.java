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
package org.seasar.doma;

import junit.framework.TestCase;

/** @author taedium */
public class DomaNullPointerExceptionTest extends TestCase {

  public void test() throws Exception {
    DomaNullPointerException e = new DomaNullPointerException("aaa");
    assertEquals("aaa", e.getParameterName());
  }

  public void testGenerics() throws Exception {
    hoge(new A());
    hoge(new B());
    hoge(new C());
  }

  private <T> T hoge(T t) {
    return t;
  }

  private A hoge(A a) {
    return a;
  }

  static class A {}

  static class B extends A {}

  static class C {}
}
