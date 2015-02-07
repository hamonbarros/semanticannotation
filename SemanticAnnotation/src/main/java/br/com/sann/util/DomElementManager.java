package br.com.sann.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
public class DomElementManager {

	public static void printElementSchema(Element element) {
		System.out.println("Nome:" + element.getName());
		System.out.println("Atributos:");
		Iterator<Attribute> attributes = element.getAttributes().iterator();
		while (attributes.hasNext()) {
			Attribute currentElement = attributes.next();
			System.out.println("     " + currentElement.getName() + "("
					+ currentElement.getValue() + ")");

		}
		System.out.println("Elementos:");
		Iterator<Element> elements = element.getChildren().iterator();
		while (elements.hasNext()) {
			Element currentElement = elements.next();
			System.out.print("     " + currentElement.getName());
			if (currentElement.getChildren().isEmpty())
				System.out.println(" (Simples)");
			else
				System.out.println(" (Composto)");

		}

	}

	public static void printElementProperties(Element element) {
		System.out.println("Nome:" + element.getName());
		System.out.println("Atributos:");
		Iterator<Attribute> attributes = element.getAttributes().iterator();
		while (attributes.hasNext()) {
			Attribute currentElement = attributes.next();
			System.out.println("     " + currentElement.getName() + "("
					+ currentElement.getValue() + ")");

		}
		System.out.println("Elementos:");
		Iterator<Element> elements = element.getChildren().iterator();
		while (elements.hasNext()) {
			Element currentElement = elements.next();
			System.out.print("     " + currentElement.getName());
			if (isSimpleElement(currentElement)) {
				System.out.println("(" + currentElement.getValue() + ")");
			} else
				printElementProperties(currentElement);

		}

	}

	public static Attribute findAtributte(Element element, String attributeName) {
		Iterator<Attribute> attributes = element.getAttributes().iterator();
		while (attributes.hasNext()) {
			Attribute currentElement = attributes.next();
			if (currentElement.getName().toLowerCase()
					.equals(attributeName.toLowerCase()))
				return currentElement;
		}
		return null;
	}

	public static Element findElement(Element element, String elementName) {
		Iterator<Element> elements = element.getChildren().iterator();
		while (elements.hasNext()) {
			Element currentElement = elements.next();
			if (currentElement.getName().toLowerCase()
					.equals(elementName.toLowerCase()))
				return currentElement;
		}
		return null;
	}

	private static boolean isSimpleElement(Element element) {
		return element.getChildren().isEmpty();
	}

	public static Element getChild(Element element, String name) {
		Iterator<Element> it = element.getChildren().iterator();
		while (it.hasNext()) {
			Element currentElement = it.next();
			if (currentElement.getName().equals(name)) {
				return currentElement;
			}
		}
		return null;
	}

	public static Collection<Element> getChildrenByName(Element element,
			String name) {
		Iterator<Element> it = element.getChildren().iterator();
		Collection<Element> result = new Vector();
		while (it.hasNext()) {
			Element currentElement = it.next();
			if (currentElement.getName().equals(name)) {
				result.add(currentElement);
			}
		}
		return result;
	}

	public static Element findElementByPath(Element rootElement, String path) {
		try {
			if (path.equals(""))
				return rootElement;
			int index = path.indexOf("/");
			if (index == -1) {
				Element result = getChild(rootElement, path);
				return result;
			}
			String currentElementName = path.substring(0, index);
			Element currentElement = getChild(rootElement, currentElementName);
			String newPath = path.substring(index + 1);
			return findElementByPath(currentElement, newPath);
		} catch (Exception e) {
			return null;

		}

	}

	public static Element findElementByAttributeValue(Element rootElement,
			String path, String attributeName, String attributeValue) {
		try {
			Iterator<Element> elements = DomElementManager
					.findElementSetByPath(rootElement, path).iterator();
			while (elements.hasNext()) {
				Element currentElement = elements.next();
				Iterator<Attribute> attributes = currentElement.getAttributes()
						.iterator();
				while (attributes.hasNext()) {
					Attribute currentAttribute = attributes.next();
					if (currentAttribute.getName().equals(attributeName))
						if (currentAttribute.getValue().equals(attributeValue))
							return currentElement;
				}
			}
			return null;
		} catch (Exception e) {
			return null;

		}

	}

	public static Collection<Element> findElementSetByAttributeValue(
			Element rootElement, String path, String attributeName,
			String attributeValue) {
		try {
			Collection result = new Vector();
			Iterator<Element> elements = DomElementManager
					.findElementSetByPath(rootElement, path).iterator();
			while (elements.hasNext()) {
				Element currentElement = elements.next();
				Iterator<Attribute> attributes = currentElement.getAttributes()
						.iterator();
				while (attributes.hasNext()) {
					Attribute currentAttribute = attributes.next();
					if (currentAttribute.getName().equals(attributeName))
						if (currentAttribute.getValue().equals(attributeValue))
							result.add(currentElement);
				}
			}
			return result;
		} catch (Exception e) {
			return null;

		}

	}

	public static Collection<Element> findElementSetByPath(Element rootElement,
			String path) {
		try {
			int index = path.indexOf("/");
			if (index == -1) {
				return getChildrenByName(rootElement, path);
			}
			String currentElementName = path.substring(0, index);
			Element currentElement = getChild(rootElement, currentElementName);
			String newPath = path.substring(index + 1);
			return findElementSetByPath(currentElement, newPath);
		} catch (Exception e) {
			return null;

		}

	}

	public static Attribute findAttributeByPath(Element rootElement,
			String path, String attributeName) {
		try {
			Element element = DomElementManager.findElementByPath(rootElement,
					path);
			Iterator<Attribute> attributes = element.getAttributes().iterator();
			while (attributes.hasNext()) {
				Attribute currentAttribute = attributes.next();
				if (currentAttribute.getName().equals(attributeName)) {
					return currentAttribute;
				}
			}

		} catch (Exception e) {
		}
		return null;

	}

}
