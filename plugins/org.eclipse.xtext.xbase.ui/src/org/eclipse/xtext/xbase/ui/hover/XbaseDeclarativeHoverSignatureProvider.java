/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.ui.hover;

import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.xtext.common.types.JvmAnyTypeReference;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.util.PolymorphicDispatcher;
import org.eclipse.xtext.util.PolymorphicDispatcher.ErrorHandler;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.typing.ITypeProvider;
import org.eclipse.xtext.xbase.validation.UIStrings;

import com.google.inject.Inject;

/**
 * @author Holger Schill - Initial contribution and API
 */
public class XbaseDeclarativeHoverSignatureProvider {

	@Inject
	protected HoverUiStrings hoverUiStrings;

	@Inject
	protected UIStrings uiStrings;

	@Inject
	private IJvmModelAssociations associations;

	@Inject
	private ILabelProvider labelProvider;

	@Inject
	private ITypeProvider typeProvider;

	public String getSignature(EObject object) {
		return internalGetSignature(object, false);
	}

	public String getDerivedOrSourceSignature(EObject object) {
		return internalGetSignature(object, true);
	}

	protected String internalGetSignature(EObject object, boolean typeAtEnd) {
		PolymorphicDispatcher<String> polymorphicDispatcher = new PolymorphicDispatcher<String>("_signature", 2, 2,
				Collections.singletonList(this), new ErrorHandler<String>() {
					public String handle(Object[] params, Throwable throwable) {
						return null;
					}
				});
		String result = polymorphicDispatcher.invoke(object, typeAtEnd);
		if (result != null)
			return result;
		if (object instanceof JvmIdentifiableElement) {
			return getLabel(object);
		}
		return getLabelForNonXbaseElement(object);
	}

	private String getLabelForNonXbaseElement(EObject object) {
		String label = getLabel(object);
		return object.eClass().getName() + ((label != null) ? " " + label : "");
	}

	protected String _signature(JvmGenericType clazz, boolean typeAtEnd) {
		return clazz.getSimpleName();
	}

	protected String _signature(JvmOperation jvmOperation, boolean typeAtEnd) {
		String returnTypeString = "void";
		JvmTypeReference returnType = jvmOperation.getReturnType();
		if (returnType != null) {
			if (returnType instanceof JvmAnyTypeReference) {
				returnTypeString = "Object";
			} else {
				returnTypeString = returnType.getSimpleName();
			}
		}

		String signature = jvmOperation.getSimpleName() + hoverUiStrings.parameters(jvmOperation)
				+ getThrowsDeclaration(jvmOperation);
		if (typeAtEnd)
			return signature + " : " + returnTypeString;
		return returnTypeString + " " + signature;
	}

	protected String _signature(JvmField jvmField, boolean typeAtEnd) {
		JvmTypeReference type = jvmField.getType();
		if (type != null) {
			if (typeAtEnd)
				return jvmField.getSimpleName() + " : " + type.getSimpleName();
			return type.getSimpleName() + " " + jvmField.getSimpleName();
		}
		return "";
	}

	protected String _signature(JvmConstructor contructor, boolean typeAtEnd) {
		return contructor.getSimpleName() + " " + hoverUiStrings.parameters(contructor)
				+ getThrowsDeclaration(contructor);
	}

	protected String _signature(JvmFormalParameter parameter, boolean typeAtEnd) {
		JvmTypeReference parameterType = parameter.getParameterType();
		if (parameterType != null) {
			EObject container = parameter.eContainer();
			String signature = parameter.getName() + JavaElementLabels.CONCAT_STRING + getSimpleSignature(container);
			if (typeAtEnd)
				return signature + " : " + parameterType.getSimpleName();
			return parameterType.getSimpleName() + " " + signature;
		}
		return "";
	}

	protected String getThrowsDeclaration(JvmExecutable executable) {
		String result = "";
		EList<JvmTypeReference> exceptions = executable.getExceptions();
		if (exceptions.size() > 0) {
			result += " throws ";
			Iterator<JvmTypeReference> iterator = exceptions.iterator();
			while (iterator.hasNext()) {
				JvmTypeReference next = iterator.next();
				result += next.getSimpleName();
				if (iterator.hasNext())
					result += ", ";
			}
		}
		return result;
	}

	protected String getSimpleSignature(EObject container) {
		if (container instanceof JvmOperation) {
			return getSimpleSignature((JvmOperation) container);
		} else if (container instanceof JvmConstructor) {
			return getSimpleSignature((JvmConstructor) container);
		}
		return labelProvider.getText(container);
	}

	protected String getSimpleSignature(JvmConstructor contructor) {
		return contructor.getSimpleName() + " " + uiStrings.parameters(contructor);
	}

	protected String getSimpleSignature(JvmOperation jvmOperation) {
		return jvmOperation.getSimpleName() + uiStrings.parameters(jvmOperation);
	}

	public String getImageTag(EObject object) {
		return getImageTagLink(ImageDescriptor.createFromImage(labelProvider.getImage(object)));
	}

	protected String getImageTagLink(ImageDescriptor imageDescriptor) {
		URL url = getURL(imageDescriptor);
		if (url != null)
			return "<image src='" + url.toExternalForm() + "'/>";
		return "";
	}

	protected URL getURL(ImageDescriptor descriptor) {
		return JavaPlugin.getDefault().getImagesOnFSRegistry().getImageURL(descriptor);
	}

	protected String getLabel(EObject object) {
		return labelProvider.getText(object);
	}

}