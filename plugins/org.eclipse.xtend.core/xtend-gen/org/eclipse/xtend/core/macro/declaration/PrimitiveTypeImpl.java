/**
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtend.core.macro.declaration;

import com.google.common.base.Objects;
import org.eclipse.xtend.core.macro.declaration.AbstractDeclarationImpl;
import org.eclipse.xtend.lib.macro.declaration.PrimitiveType;
import org.eclipse.xtend.lib.macro.declaration.PrimitiveType.Kind;
import org.eclipse.xtext.common.types.JvmPrimitiveType;

@SuppressWarnings("all")
public class PrimitiveTypeImpl extends AbstractDeclarationImpl<JvmPrimitiveType> implements PrimitiveType {
  public Kind getKind() {
    Kind _switchResult = null;
    String _name = this.getName();
    final String getName = _name;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(getName,"boolean")) {
        _matched=true;
        _switchResult = Kind.BOOLEAN;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"int")) {
        _matched=true;
        _switchResult = Kind.INT;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"char")) {
        _matched=true;
        _switchResult = Kind.CHAR;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"double")) {
        _matched=true;
        _switchResult = Kind.DOUBLE;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"long")) {
        _matched=true;
        _switchResult = Kind.LONG;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"short")) {
        _matched=true;
        _switchResult = Kind.SHORT;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"float")) {
        _matched=true;
        _switchResult = Kind.FLOAT;
      }
    }
    if (!_matched) {
      if (Objects.equal(getName,"byte")) {
        _matched=true;
        _switchResult = Kind.BYTE;
      }
    }
    return _switchResult;
  }
  
  public String getName() {
    JvmPrimitiveType _delegate = this.getDelegate();
    String _identifier = _delegate.getIdentifier();
    return _identifier;
  }
}