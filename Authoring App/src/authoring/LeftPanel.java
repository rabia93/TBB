package authoring;

import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import commands.PlayerCommand;
import listeners.NewQuestionListener;

/**
 * LeftPanel is the abstraction which is used to store the list of commands. It
 * provides a view which the authoring user can interact with via buttons
 * defined in the RightPanel. It acts as: a wrapper in front of a list, a mouse
 * listener to detect click events, and a UI panel for the GUI
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-03-15
 */
public class LeftPanel extends JPanel implements KeyListener {
	/** Autogenerated serial */
private static final long serialVersionUID = 2716138356085893186L;

private JScrollPane scrollPane = new JScrollPane();

public JList<PlayerCommand> commandList = new JList<>();
private DefaultListModel<PlayerCommand> listModel = new DefaultListModel<>();
int index=-1;
private GUI gui;
private boolean isEdit = false;
int selectedIndex;

ColourMapper map= new ColourMapper();
NewQuestionListener listener= new NewQuestionListener(gui,map);


private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

/**
 * Create a new left panel of the GUI.
 *
 * @param gui
 *            Reference to the overall GUI object
 * @param mapper
 *            Reference to the common colour mapper
 */
public LeftPanel(GUI gui, ColourMapper mapper) {
	// Create a basic JPanel with a grid layout
	super(new GridLayout(1, 1));

	// Set the JList to have listModel as the content
	commandList.setModel(listModel);
	commandList.setCellRenderer(new ColourCellRenderer(mapper));
	commandList.getAccessibleContext().setAccessibleName("Script Area.");
	commandList.getAccessibleContext().setAccessibleDescription(
			"This contains the script you are writing. Items you create are added here. Use up and down arrow keys to move and press spacebar to edit the selected item.");
	
	// Set the scollpane to have commandList as its content
	scrollPane.setViewportView(commandList);

	commandList.addKeyListener(this);
	
	
	// Create a border around this panel
	setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Scenario"));

	// Add the scrollpane to this panel
	add(scrollPane);

	this.gui = gui;
	setup();
	commandList.addListSelectionListener(new ListSelectionListener(){
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		gui.getRightPanel().setDelete(true);
		gui.getRightPanel().setUp(true);
		gui.getRightPanel().setDown(true);
		gui.getRightPanel().setEdit(true);

	}
	});
}




public DefaultListModel<PlayerCommand> getlistModel()
	{
		return this.listModel;
	}



/**
 * Swap two string elements in the list, given their indices
 *
 * @param a
 *            Parameter to swap
 * @param b
 *            Parameter to be swapped with
 */


private void swapElements(int a, int b) {
	// Get the element at each of the requested locations
	PlayerCommand strA = listModel.getElementAt(a);
	PlayerCommand strB = listModel.getElementAt(b);

	// Swap the elements and their indices
	listModel.set(a, strB);
	listModel.set(b, strA);
}

/**
 * Add an element to the scrollpane in the left panel
 *
 * @param newElement
 *            New element to be added
 */
public void addItem(PlayerCommand newElement) {
	if(isEdit==true)
	listModel.setElementAt(newElement, this.selectedIndex);
	
	else
	{listModel.addElement(newElement);
	}

}


/**
 * Edit an element from the list 
 *
* @param a
 *            Parameter to edit
 */
public void EditItem() {
	
	int selectedIndex = commandList.getSelectedIndex();
	PlayerCommand a = commandList.getSelectedValue();
	a.editCommand();
	listModel.setElementAt(a, selectedIndex);
	
	
}

/**
 * Move the currently selected element one spot higher in the list. If the
 * selected element is already the top element, this method will gracefully
 * do nothing.
 */
public void moveUp() {
	// Get the index of the selected element
	int selectedIndex = commandList.getSelectedIndex();

	// Do not move the top element "up"!
	if (selectedIndex == 0) {
		return;
	}

	// Swap the element with the one above it
	swapElements(selectedIndex, selectedIndex - 1);

	// Update the highlight position
	commandList.setSelectedIndex(selectedIndex - 1);
}

/**
 * Move the currently selected element one spot lower in the list. If the
 * selected element is already the bottom element, this method will
 * gracefully do nothing.
 */
public void moveDown() {
	// Get the index of the selected element
	int selectedIndex = commandList.getSelectedIndex();

	// Do not move the bottom "down"!
	if (selectedIndex == listModel.size() - 1) {
		return;
	}

	// Swap the element with the one below it
	swapElements(selectedIndex, selectedIndex + 1);

	// Update the highlight position
	commandList.setSelectedIndex(selectedIndex + 1);
}

/**
 * Remove the currently selected element from the list completely.
 */
public void deleteItem() {
	// Get the index of the selected element
	int[] selectedIndex = commandList.getSelectedIndices();
	
	
/*	if(!(commandList.isSelectionEmpty()))
	{
		gui.getRightPanel().setDelete(true);
	}
	gui.getRightPanel().setDelete(true);*/
	
	// Remove that position from the listModel
	
	
	  int index = commandList.getSelectedIndices().length - 1;

       while (commandList.getSelectedIndices().length != 0) {
           this.listModel.removeElementAt(commandList.getSelectedIndices()[index--]);
       }
	
	/*for(int i=0; i< selectedIndex.length; i++ )
	{
	System.out.println(selectedIndex[i]);
	listModel.remove(selectedIndex[i]);
	}*/
}

/**
 * Remove all elements from the list
 */
public void clearAll() {
	listModel.removeAllElements();
}

/**
 * Returns a boolean to see if the list is empty or not
 *
 * @return a true or false value
 */
public boolean elementCheck() {
	return listModel.isEmpty();
}

/**
 * Re-set the statuses of each of the relevant buttons.
 */
public void recalculateButtonStatus() {
	// Get the index of the selected element
	int selectedIndex = commandList.getSelectedIndex();

	gui.getRightPanel().setUp(true);
	gui.getRightPanel().setDown(true);
	gui.getRightPanel().setDelete(true);

	if (selectedIndex == 0) {
		gui.getRightPanel().setUp(false);
	}
	if (selectedIndex == listModel.size() - 1) {
		gui.getRightPanel().setDown(false);
	}
	if (listModel.size() == 0 || selectedIndex == -1) {
		gui.getRightPanel().setUp(false);
		gui.getRightPanel().setDown(false);
		gui.getRightPanel().setDelete(false);
	}
}

private void setup() {
	  KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK );
	  actionMap.put(key1, new AbstractAction("action1") {
		private static final long serialVersionUID = 1L;
		@Override
	    public void actionPerformed(ActionEvent e) {
			moveUp();
		}
	  });
	  
	  KeyStroke key2 = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK);
	  actionMap.put(key2, new AbstractAction("action2") {
		private static final long serialVersionUID = 1L;
		@Override
	    public void actionPerformed(ActionEvent e) {
			moveDown();
		}
	  });
	  
	  KeyStroke key3 = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
	  actionMap.put(key3, new AbstractAction("action3") {
		private static final long serialVersionUID = 1L;
		@Override
	    public void actionPerformed(ActionEvent e) {
			moveDown();
		}
	  });
	  // add more actions..

	  KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	  kfm.addKeyEventDispatcher( new KeyEventDispatcher() {

	    @Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
	      KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
	      if ( actionMap.containsKey(keyStroke) ) {
	        final Action a = actionMap.get(keyStroke);
	        final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null );
	        SwingUtilities.invokeLater( new Runnable() {
	          @Override
	          public void run() {
	            a.actionPerformed(ae);
	          }
	        } ); 
	        return true;
	      }
	      return false;
	    }
	  });
	}

/**
 * Returns the full ordered list of commands stored at this point. Returns a
 * defensive copy, not the original reference, so changes to the returned
 * object will not be seen in the UI.
 *
 * @return A copy of the current list, in the same order that they are
 *         stored and shown in the panel
 */
public List<PlayerCommand> getList() {
	List<PlayerCommand> result = new ArrayList<>();
	for (Object o : listModel.toArray()) {
		if (o instanceof PlayerCommand) {
			result.add((PlayerCommand) o);
		}
	}
	
	return result;
}

@Override
public void keyPressed(KeyEvent e) {
	
	recalculateButtonStatus();
	
	PlayerCommand command = null;
	int key = e.getKeyCode();
	
	if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
		command = this.commandList.getModel().getElementAt(index);
		
		// Show the Add Item dialog
		Object answer;
		answer = JOptionPane.showInputDialog(gui, command.getEditLabel(), "Edit Item Details",
				JOptionPane.PLAIN_MESSAGE, null, null, command.getCurrentValue());

		if (answer == null) {
			return;
		}

		if (!((String) answer).isEmpty()) {
			command.setCurrentValue((String) answer);
		}
		
		System.out.println(index);

	} else if (key == KeyEvent.VK_UP) {
		if(index <= 0)
			index = 0;
		else
			index--;
		System.out.println(index);
	} else if (key == KeyEvent.VK_DOWN) {
		if(index >= commandList.getModel().getSize()-1)
			index = commandList.getModel().getSize()-1;
		else
			index++;
		System.out.println(index);
	}
	

}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
		
	}


}
