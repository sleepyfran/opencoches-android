package io.spaceisstrange.opencoches.api.parser

import io.spaceisstrange.opencoches.api.model.elements.Element
import io.spaceisstrange.opencoches.api.model.elements.ImageElement
import io.spaceisstrange.opencoches.api.model.elements.TextElement
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/*
 * Hecho con <3 por Fran González (@spaceisstrange)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

class PostParser {
    companion object {
        /**
         * Toma los elementos de un post en HTML y lo parsea para obtener los elementos a mostrar
         * en la interfaz con la información ya incluida
         */
        fun parse(postNodes: MutableList<Node>, isOp: Boolean): MutableList<Element> {
            val postElements: MutableList<Element> = mutableListOf()

            // Eliminamos los nodos que no nos interesan
            val cleanNodes: List<Node>

            if (isOp) {
                cleanNodes = postNodes.slice(12..postNodes.size - 1)
            } else {
                cleanNodes = postNodes.slice(4..postNodes.size - 1)
            }

            // Iteramos sobre los nodos obteniendo el elemento asociado
            var previousElement: Element? = null

            for (node in cleanNodes) {
                if (isTextNode(node)) {
                    val nodeText = getTextFromNode(node)

                    // Comprobamos si podemos añadirlo al elemento anterior
                    if (previousElement is TextElement) {
                        previousElement.appendText(nodeText)
                    } else {
                        // Sino lo añadimos a la lista de nodos y lo guardamos
                        val newTextElement = TextElement(nodeText)
                        postElements.add(newTextElement)
                        previousElement = newTextElement
                    }
                } else if (isImageNode(node)) {
                    val nodeImageSource = getImageSourceFromNode(node as org.jsoup.nodes.Element)
                    val newImageElement = ImageElement(nodeImageSource)
                    postElements.add(newImageElement)
                    previousElement = newImageElement
                } // TODO: Añadir el resto de casos
            }

            return postElements
        }

        /**
         * Evalúa si un nodo es o no un nodo con text
         */
        fun isTextNode(node: Node): Boolean {
            return node is TextNode ||
                    (node as org.jsoup.nodes.Element).tag().toString() == "br" ||
                    node.hasText()
        }

        /**
         * Evalúa si un nodo contiene o no un elemento de imagen
         */
        fun isImageNode(node: Node): Boolean {
            val images = (node as org.jsoup.nodes.Element).select("img")
            return images.size > 0
        }

        /**
         * Obtiene el texto o código HTML de un nodo de texto
         */
        fun getTextFromNode(textNode: Node): String {
            if (textNode is TextNode) {
                return textNode.text()
            } else {
                if ((textNode as org.jsoup.nodes.Element).tag().toString() == "br") {
                    return "<br>"
                }

                return (textNode).html()
            }
        }

        /**
         * Obtiene la dirección de una imagen
         */
        fun getImageSourceFromNode(imageNode: org.jsoup.nodes.Element): String {
            return ""
        }
    }
}