/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.processor;

import com.google.common.base.VerifyException;
import com.skydoves.methodscope.MethodScope;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@SuppressWarnings("WeakerAccess")
public class MethodScopeAnnotatedClass {

    public final TypeElement annotatedElement;
    public final String packageName;
    public final String clazzName;
    public final List<AnnotationMirror> scopeAnnotationList;

    public MethodScopeAnnotatedClass(TypeElement annotatedElement, Elements elementUtils) throws VerifyException {
        PackageElement packageElement = elementUtils.getPackageOf(annotatedElement);
        this.packageName = packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
        this.annotatedElement = annotatedElement;
        this.clazzName = annotatedElement.getSimpleName().toString();
        this.scopeAnnotationList = new ArrayList<>();

        annotatedElement.getAnnotationMirrors()
              .forEach(annotation -> {
                  scopeAnnotationList.add(annotation);
                  annotation.getAnnotationType().asElement().getAnnotationMirrors()
                        .stream()
                        .filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals(MethodScope.class.getSimpleName()))
                        .forEach(annotationMirror ->
                            scopeAnnotationList.add(annotation));
              });
    }
}
