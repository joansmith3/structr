package org.structr.websocket.command.dom;

import java.util.Map;
import org.structr.web.entity.dom.DOMNode;
import org.structr.websocket.StructrWebSocket;
import org.structr.websocket.command.AbstractCommand;
import org.structr.websocket.message.MessageBuilder;
import org.structr.websocket.message.WebSocketMessage;
import org.w3c.dom.DOMException;

/**
 *
 * @author Christian Morgner
 */
public class InsertBeforeDOMNodeCommand extends AbstractCommand {

	static {
		
		StructrWebSocket.addCommand(InsertBeforeDOMNodeCommand.class);
	}
	
	@Override
	public void processMessage(WebSocketMessage webSocketData) {

		Map<String, Object> nodeData = webSocketData.getNodeData();
		String parentId              = (String) nodeData.get("parentId");
		String newId                 = (String) nodeData.get("newId");
		String refId                 = (String) nodeData.get("refId");
		String pageId                = webSocketData.getPageId();
		
		if (pageId != null) {

			// check for parent ID before creating any nodes
			if (parentId == null) {
		
				getWebSocket().send(MessageBuilder.status().code(422).message("Cannot replace node without parentId").build(), true);		
				return;
			}

			// check if parent node with given ID exists
			DOMNode parentNode = getDOMNode(parentId);
			if (parentNode == null) {
		
				getWebSocket().send(MessageBuilder.status().code(404).message("Parent node not found").build(), true);		
				return;
			}

			// check for old ID before creating any nodes
			if (refId == null) {
		
				getWebSocket().send(MessageBuilder.status().code(422).message("Cannot insert node without refId").build(), true);
				return;
			}

			// check if old node with given ID exists
			DOMNode refNode = getDOMNode(refId);
			if (refNode == null) {
		
				getWebSocket().send(MessageBuilder.status().code(404).message("Reference node not found").build(), true);		
				return;
			}

			// check for new ID before creating any nodes
			if (newId == null) {
		
				getWebSocket().send(MessageBuilder.status().code(422).message("Cannot replace node without newId").build(), true);
				return;
			}

			// check if new node with given ID exists
			DOMNode newNode = getDOMNode(newId);
			if (newNode == null) {
		
				getWebSocket().send(MessageBuilder.status().code(404).message("New node not found").build(), true);		
				return;
			}
			
			try {
				parentNode.insertBefore(newNode, refNode);

			} catch (DOMException dex) {

				// send DOM exception
				getWebSocket().send(MessageBuilder.status().code(422).message(dex.getMessage()).build(), true);		
			}
			
		} else {
		
			getWebSocket().send(MessageBuilder.status().code(422).message("Cannot insert node without pageId").build(), true);		
		}
	}

	@Override
	public String getCommand() {
		return "INSERT_BEFORE_DOM_NODE";
	}

}